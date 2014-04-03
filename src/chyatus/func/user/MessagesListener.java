package chyatus.func.user;

import chyatus.Chat;
import chyatus.Constants;
import chyatus.commands.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Logger;

/**
 * Messages from other users listener
 *
 * @author M.Vasileusky
 */
public class MessagesListener implements Runnable {

    private static final Logger log = Logger.getLogger(MessagesListener.class);

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(Constants.USER_PORT)) {
            while (true) {
                try (Socket socket = serverSocket.accept();
                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
                    Message message = (Message) ois.readObject();
                    System.out.println(Chat.users.findByAddress(socket.getInetAddress()).getUsername() + ": " + message.getText());
                    log.info("New message from " + Chat.users.findByAddress(socket.getInetAddress()).getUsername() + ": " + message.getText());
                } catch (ClassNotFoundException ex) {
                }
            }
        } catch (IOException ex) {
            log.error("Error while listening new messages " + ex);
        }
    }

}
