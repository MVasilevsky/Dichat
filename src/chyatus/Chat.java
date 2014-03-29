package chyatus;

import chyatus.commands.Message;
import chyatus.func.system.SystemFunctions;
import chyatus.func.user.UserFunctions;
import chyatus.users.Users;
import chyatus.users.User;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;

/**
 * Distributed chat.
 *
 * 1. Request username, then send it to all users and obtain list of all users
 * from one of them. 2. Listen for later requests from new clients 3. Listen for
 * incoming messages 4. Start main cycle of requesting and sending message to
 * all users
 *
 * @author M.Vasileusky
 */
public class Chat {

    private static User user;

    public static void main(String[] args) throws Exception {

        System.out.println("Your username: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String username = reader.readLine();
        user = new User(username, InetAddress.getLocalHost());
        Users.addUser(user);

        SystemFunctions.loadUsersList(user);

        System.out.println("Online users: ");
        for (User u : Users.getAll()) {
            System.out.println(u.getUsername() + " " + u.getIp().getHostAddress());
        }

        SystemFunctions.listenToNewUsers();

        UserFunctions.listenToNewMessages();

        while (true) {
            UserFunctions.sendMessageToAll(new Message(Message.PLAIN_MESSAGE, reader.readLine()));
        }

    }

}
