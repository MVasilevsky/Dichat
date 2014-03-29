package chyatus.func.user;

import chyatus.Constants;
import chyatus.commands.Message;
import chyatus.users.Users;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Messages from other users listener
 *
 * @author M.Vasileusky
 */
public class MessagesListener implements Runnable {

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(Constants.USER_PORT)) {
            while (true) {
                try (Socket socket = serverSocket.accept();
                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
                    Message message = (Message) ois.readObject();
                    System.out.println("New message from " + Users.findByAddress(socket.getInetAddress()).getUsername() + ": " + message.text);
                } catch (ClassNotFoundException ex) {
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MessagesListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
