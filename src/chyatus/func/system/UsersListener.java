package chyatus.func.system;

import chyatus.Constants;
import chyatus.users.User;
import chyatus.users.Users;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author Max
 */
public class UsersListener implements Runnable {

    /**
     * Check all ip's and decide, should I answer or not (only one user should
     * answer)
     *
     * @return verdict
     */
    private boolean ifShouldIAnswer() {
        try {
            int myHashCode = InetAddress.getLocalHost().hashCode();
            for (User user : Users.getAll()) {
                if (user.getIp().hashCode() > myHashCode) {
                    System.out.println("I shouldn't answer");
                    return false;
                }
            }
            System.out.println("I should answer");
            return true;
        } catch (UnknownHostException ex) {
            System.out.println("I shouldn't answer");
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
                byte[] request = packet.getData();
                if (request[0] == Constants.USERS_LIST_COMMAND) {

                    // new user info
                    byte[] byteUsername = new byte[buf.length - 1];
                    System.arraycopy(request, 1, byteUsername, 0, buf.length - 1);
                    String username = new String(byteUsername).trim();
                    InetAddress userAddress = packet.getAddress();

                    // send response (if needed)
                    if (ifShouldIAnswer()) {
                        try (Socket tcpSocket = new Socket(userAddress, Constants.SYSTEM_PORT);
                                ObjectOutputStream oos = new ObjectOutputStream(tcpSocket.getOutputStream())) {
                            oos.writeObject(Users.getAll());
                            System.out.println("Send information to " + username + " from " + userAddress.getHostAddress());
                        }
                    }

                    // save new user
                    Users.addUser(new User(username, userAddress));
                }
            }

        } catch (SocketException ex) {
            System.err.println("Can't create socket for listening new connections: " + ex.getLocalizedMessage());
        } catch (IOException ex) {
            System.err.println("Error while listening new connections: " + ex.getLocalizedMessage());
        }

    }

}