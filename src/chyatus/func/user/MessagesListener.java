package chyatus.func.user;

import chyatus.Constants;
import chyatus.commands.Message;
import chyatus.users.Users;
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

    private static final Logger log = org.apache.log4j.Logger.getLogger(MessagesListener.class);

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(Constants.USER_PORT)) {
            while (true) {
                try (Socket socket = serverSocket.accept();
                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
                    Message message = (Message) ois.readObject();
                    System.out.println(Users.findByAddress(socket.getInetAddress()).getUsername() + ": " + message.text);
                    log.info("New message from " + Users.findByAddress(socket.getInetAddress()).getUsername() + ": " + message.text);
                } catch (ClassNotFoundException ex) {
                }
            }
        } catch (IOException ex) {
            log.error("Error while listening new messages " + ex);
        }
    }

}
