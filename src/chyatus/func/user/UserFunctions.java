package chyatus.func.user;

import static chyatus.Constants.USER_PORT;
import chyatus.commands.Message;
import chyatus.users.User;
import chyatus.users.Users;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * User functionality.
 *
 * Contains functionality for messaging, etc.
 *
 * @author M.Vasileusky
 */
public class UserFunctions {

    /**
     * Send message to single user
     * 
     * @param user user
     * @param message message
     * @throws IOException 
     */
    public static void sendMessage(User user, Message message) throws IOException {
        try (Socket socket = new Socket(user.getIp(), USER_PORT);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
            oos.writeObject(message);
        }
    }

    /**
     * Send message to all users
     * 
     * @param message message
     * @throws IOException 
     */
    public static void sendMessageToAll(Message message) throws IOException {
        for (User user : Users.getAll()) {
            sendMessage(user, message);
        }
    }

    /**
     * Asynch listener of new messages
     */
    public static void listenToNewMessages() {
        Thread thread = new Thread(new MessagesListener());
        thread.start();
    }

}
