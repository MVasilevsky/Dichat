package chyatus;

import chyatus.users.Users;
import chyatus.users.User;

/**
 * Distributed chat.
 *
 * 1. Create client, which will request username, then send it to all users and
 * obtain list of all users from one of them. 2. Listen for later requests from
 * new clients 3. Listen for incoming messages 4. Start main cycle of requesting
 * and sending message to all users
 *
 * @author M.Vasileusky
 */
public class Chat {

    private static Client client;
    private static Server server;

    public static void main(String[] args) throws Exception {

        // send self name and obtain all users
        client = new Client();
        server = new Server();

        System.out.println("users: ");
        for (User user : Users.getAll()) {
            System.out.println(user.getUsername());
        }

        // new clients listening thread
        // remove if
        if (Users.getAll().size() == 1) {
            Thread serverThread = new Thread(server);
            serverThread.start();
        }

        // messages listening thread
        client.listenForMessages();

        // main cycle: read line and send to all clients
        client.messaging();

    }

}
