package chyatus;

import chyatus.users.User;
import chyatus.users.Users;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Server "side" of chyat
 *
 * @author mvas
 */
public class Server implements Constants, Runnable {

    /**
     * Send list of users to new user
     * 
     * @param channel 
     */
    private void sendInformation(DatagramChannel channel) {
        try {
            // Receive a packet
            ByteBuffer buffer = ByteBuffer.wrap(new byte[512]);
            InetSocketAddress clientAddress = (InetSocketAddress) channel.receive(buffer);

            // Packet received
            String message = new String(buffer.array()).trim();
            String[] parts = message.split("_");
            if ((parts.length >= 2) && (parts[0].equals(Constants.USERS_LIST))) {

                // hello new user
                Users.addUser(new User(parts[1], clientAddress.getAddress()));

                // TCP Response with users list
                try (SocketChannel tcpResponseChannel = SocketChannel.open()) {
                    // error here : Error while sending info to new user: Connection timed out: connect
                    tcpResponseChannel.connect(new InetSocketAddress(clientAddress.getAddress(), CLIENT_PORT));
                    ObjectOutputStream oos = new ObjectOutputStream(tcpResponseChannel.socket().getOutputStream());
                    oos.writeObject(Users.getAll());
                    System.out.println("Send information to: " + clientAddress.getAddress().getHostAddress());
                }
            }
        } catch (IOException e) {
            System.err.println("Error while sending info to new user: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    /**
     * Main functionality of server: listens TCP and UDP connections on the same
     * port.
     *
     * Starts new threads for handling connections
     */
    @Override
    public void run() {

        SocketAddress address = new InetSocketAddress(SERVER_PORT);

        try (DatagramChannel udpChannel = DatagramChannel.open();
                ServerSocketChannel tcpChannel = ServerSocketChannel.open()) {

            udpChannel.bind(address);
            tcpChannel.bind(address);

            udpChannel.configureBlocking(false);
            tcpChannel.configureBlocking(false);

            // selector for listening both channels
            Selector selector = Selector.open();

            udpChannel.register(selector, SelectionKey.OP_READ);
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
                    }

                    if (key.isReadable()) {
                        sendInformation((DatagramChannel) key.channel());
                    }

                    iterator.remove();
                }
            }
        } catch (IOException e) {
            System.err.println("Error while server listening: " + e.getLocalizedMessage());
        }
    }
}
