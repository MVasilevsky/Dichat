package chyatus.func.system;

import static chyatus.Constants.*;
import chyatus.Utils;
import chyatus.users.User;
import chyatus.users.Users;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * System functions.
 *
 * Contains functionality for connection establishment etc.
 *
 * @author M.Vasileusky
 */
public class SystemFunctions {

    private static final Logger log = Logger.getLogger(SystemFunctions.class);
    private static final int TIMEOUT = 3000;

    /**
     * Send self username and load list of online users
     *
     * @param user current user
     * @throws IOException
     */
    public static void loadUsersList(User user) throws IOException {
        try (DatagramSocket requestSocket = new DatagramSocket();
                ServerSocket responseSocket = new ServerSocket(SYSTEM_PORT, 5, InetAddress.getLocalHost())) {

            // Send request with my username
            requestSocket.setBroadcast(true);
            byte[] sendData = new byte[512];
            sendData[0] = USERS_LIST_COMMAND;
            byte[] username = user.getUsername().getBytes();
            System.arraycopy(username, 0, sendData, 1, username.length);
            DatagramPacket sendPacket = new DatagramPacket(sendData,
                    sendData.length,
                    Utils.getBroadcastAddress(),
                    SYSTEM_PORT);
            requestSocket.send(sendPacket);

            log.info("Request users list from machines in network: "
                    + Utils.getBroadcastAddress().getHostAddress() + ". Waiting for " + (TIMEOUT / 1000) + " seconds");

            // Wait for answer
            responseSocket.setSoTimeout(TIMEOUT);
//            responseSocket.setReuseAddress(true);
            try (Socket socket = responseSocket.accept();
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
                Users.addAll((Set<User>) ois.readObject());
            } catch (SocketTimeoutException e) {
                log.info("No users. I'm alone");
            } catch (ClassNotFoundException e) {
                log.error(e);
            }
        }
    }

    /**
     * Asynch method listens to new users.
     */
    public static void listenToNewUsers() {
        Thread listener = new Thread(new UsersListener());
        listener.start();
    }

}
