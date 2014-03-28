package chyatus;

import chyatus.commands.Message;
import chyatus.users.Users;
import chyatus.users.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author mvas
 */
public class Client implements Constants {

    private static final int TIMEOUT = 3000;
    private final User user;

    public Client() throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Your username: ");
            String username = reader.readLine();
            this.user = new User(username, InetAddress.getLocalHost());
            Users.addUser(new User(username, InetAddress.getLocalHost()));
        } finally {
            // reader.close();
        }
        hello();
    }

    class MessagesListener implements Runnable {

        /**
         * Listens for incoming messages.
         */
        private void listen() throws IOException, InterruptedException {
            SocketAddress address = new InetSocketAddress(CLIENT_PORT);

            ServerSocketChannel tcpChannel = ServerSocketChannel.open();
            tcpChannel.bind(address);
            tcpChannel.configureBlocking(false);

            // selector for listening channel
            Selector selector = Selector.open();
            tcpChannel.register(selector, SelectionKey.OP_ACCEPT);

            // infinite loop
            while (true) {
                int ready = selector.select();
                if (ready == 0) {
                    continue;
                }

                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                SelectionKey key;

                while (iterator.hasNext()) {

                    key = iterator.next();

                    if (key.isAcceptable()) {
                        try (SocketChannel channel = tcpChannel.accept();
                                ObjectInputStream ois = new ObjectInputStream(channel.socket().getInputStream())) {
                            Message command = (Message) ois.readObject();
                            switch (command.type) {
                                case Message.TYPE_MULTI_MESSAGE:
                                case Message.TYPE_SINGLE_MESSAGE:
                                    System.out.println("New message: " + command.text);
                            }
                        } catch (ClassNotFoundException e) {
                            System.out.println("Class not found. wut?");
                        }
                    }

                    iterator.remove();
                }
            }
        }

        @Override
        public void run() {
            try {
                listen();
            } catch (IOException | InterruptedException ex) {
                System.err.println("Error while listening incoming messages: " + ex.getLocalizedMessage());
            }
        }

    }

    /**
     * Send self name and load users list from another user
     *
     * @throws IOException if can't get users list
     * @throws java.lang.ClassNotFoundException
     */
    private void hello() throws IOException {
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
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found. wut");
            }
        }
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
            Message command = new Message(single ? Message.TYPE_SINGLE_MESSAGE : Message.TYPE_MULTI_MESSAGE, message);
            oos.writeObject(command);
        }
    }

    /**
     * Starts client work: read lines and sent them to all
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void messaging() throws IOException, ClassNotFoundException {
        System.out.println("Messaging");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                sendMessageToAll(reader.readLine());
            }
        } finally {
            // reader.close();
        }
    }

    public void listenForMessages() {
        Thread messagesListener = new Thread(new MessagesListener());
        messagesListener.start();
    }

}
