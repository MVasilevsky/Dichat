package chyatus.mvc.controller;

import chyatus.Chat;
import chyatus.commands.Message;
import chyatus.func.user.UserFunctions;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * MVC Controller
 *
 * @author mvas
 */
public class MainController {

    private final static Logger log = Logger.getLogger(MainController.class);

    public void sendMessage(String text) {
        Message message = new Message(Message.PLAIN_MESSAGE, text, Chat.users.getMyself());
        try {
            UserFunctions.sendMessageToAll(message);
            Chat.messages.addMessage(message);
        } catch (IOException ex) {
            log.error("Can't send message", ex);
        }
    }
}
