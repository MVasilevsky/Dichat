package chyatus;

import chyatus.commands.Command;
import chyatus.users.Users;
import chyatus.users.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Set;

/**
 *
 * @author mvas
 */
public class Client implements Constants {

    private static final int TIMEOUT = 3000;
    private final User user;

    public Client(String username) throws IOException {
        this.user = new User(username, InetAddress.getLocalHost());
    }

    /**
     * Load users list
     *
     * @throws IOException if can't get users list
     * @throws java.lang.ClassNotFoundException
     */
    public void getUsersList() throws IOException, ClassNotFoundException {
        try (DatagramSocket requestSocket = new DatagramSocket();
                ServerSocket responseSocket = new ServerSocket(CLIENT_PORT)) {

            // Send request with my username. request list
            requestSocket.setBroadcast(true);

            byte[] sendData = (Constants.USERS_LIST + "_" + user.getUsername()).getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                    Utils.getBroadcastAddress(), SERVER_PORT);
            requestSocket.send(sendPacket);
            System.out.println("Request users list from machines in network: "
                    + Utils.getBroadcastAddress().getHostAddress() + ". Waiting for " + (TIMEOUT / 1000) + " seconds");

            // Wait for answer
            responseSocket.setSoTimeout(TIMEOUT);
            try (Socket socket = responseSocket.accept();
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
                Users.addAll((Set<User>) ois.readObject());
            } catch (SocketTimeoutException e) {
                System.out.println("No users. I'm alone");
            }
        }
    }

    /**
     * Listens for incoming messages. 
     */
    public void listen() {
        
    }
    
    /**
     * For sending message to all clients
     *
     * @param message message text
     * @throws IOException
     */
    public void sendMessageToAll(String message) throws IOException {
        for (User other : Users.getAll()) {
            if (!other.equals(user)) {
                sendMessage(user, message, false);
            }
        }
    }

    /**
     * For sending message to single client
     *
     * @param addressee message adressee
     * @param message message text
     * @throws IOException
     */
    public void sendMessageToUser(User addressee, String message) throws IOException {
        sendMessage(addressee, message, true);
    }

    /**
     * Main message sender
     *
     * @param addressee adressee of message
     * @param message message text
     * @param single is message only for this adressee
     * @throws IOException
     */
    public void sendMessage(User addressee, String message, boolean single) throws IOException {
        try (Socket socket = new Socket(addressee.getIp(), CLIENT_PORT);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
            Command command = new Command(single ? Command.TYPE_SINGLE_MESSAGE : Command.TYPE_MULTI_MESSAGE, message);
            oos.writeObject(command);
        }
    }

    /**
     * Starts client work: read lines and sent them to all
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void start() throws IOException, ClassNotFoundException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                sendMessageToAll(reader.readLine());
            }
        }
    }

}
