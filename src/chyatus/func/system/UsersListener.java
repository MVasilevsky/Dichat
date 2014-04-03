package chyatus.func.system;

import chyatus.Chat;
import chyatus.Constants;
import chyatus.User;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Max
 */
public class UsersListener implements Runnable {

    private static final Logger log = Logger.getLogger(UsersListener.class);

    /**
     * Check all ip's and decide, should I answer or not (only one user should
     * answer)
     *
     * @return verdict
     */
    private boolean ifShouldIAnswer() {
        try {
            int myHashCode = InetAddress.getLocalHost().hashCode();
            for (User user : Chat.users.getAll()) {
                if (user.getIp().hashCode() > myHashCode) {
                    log.info("Request received, but I shouldn't answer");
                    return false;
                }
            }
            System.out.println("Request received, I should answer");
            return true;
        } catch (UnknownHostException ex) {
            log.error(ex);
            return false;
        }
    }

    @Override
    public void run() {

        try (DatagramSocket socket = new DatagramSocket(Constants.SYSTEM_PORT)) {

            byte[] buf = new byte[512];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            // infinite listening
            for (;;) {
                socket.receive(packet);

                if (packet.getAddress().equals(InetAddress.getLocalHost())) {
                    continue;
                }

                synchronized (Chat.usersLoaded) {
                    try {
                        Chat.usersLoaded.wait();
                    } catch (InterruptedException ex) {
                        java.util.logging.Logger.getLogger(UsersListener.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                byte[] request = packet.getData();
                InetAddress userAddress = packet.getAddress();

                switch (request[0]) {
                    case (Constants.USERS_LIST_COMMAND):
                        // new user info
                        byte[] byteUsername = new byte[buf.length - 1];
                        System.arraycopy(request, 1, byteUsername, 0, buf.length - 1);
                        String username = new String(byteUsername).trim();

                        // send response (if needed)
                        if (ifShouldIAnswer()) {
                            log.info("Sending information to " + username + " from " + userAddress.getHostAddress());
                            try (Socket tcpSocket = new Socket(userAddress, Constants.SYSTEM_PORT);
                                    ObjectOutputStream oos = new ObjectOutputStream(tcpSocket.getOutputStream())) {
                                oos.writeObject(Chat.users.getAll());
                            }
                            log.info("Information sent");
                        }

                        // save new user
                        Chat.users.addUser(new User(username, userAddress));
                        break;
                    case (Constants.BYE_COMMAND):
                        Chat.users.removeUser(Chat.users.findByAddress(userAddress));
                        break;
                }
            }

        } catch (SocketException ex) {
            log.error(ex);
        } catch (IOException ex) {
            log.error("Error while listening new connections: " + ex.getLocalizedMessage());
        }

    }

}
