package chyatus;

import chyatus.func.system.SystemFunctions;
import chyatus.func.user.UserFunctions;
import chyatus.gui.DialogBox;
import chyatus.gui.MainFrame;
import chyatus.mvc.controller.MainController;
import chyatus.state.Messages;
import chyatus.state.Users;
import java.io.IOException;
import java.net.InetAddress;
import org.apache.log4j.Logger;

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

    private static final Logger logger = Logger.getLogger(Chat.class);

    // mvc (classes Users and Messages represent model)
    public static final Users users = new Users();
    public static final Messages messages = new Messages();
    public static final MainController controller = new MainController();
    public static final MainFrame frame = new MainFrame(controller);
    
    public static final Object usersLoaded = new Object();

    public static void main(String[] args) {
        try {
            new Chat().start();
        } catch (IOException e) {
            logger.error(e);
        }
    }

    /**
     * Start chatus
     */
    private void start() throws IOException {
        logger.info("Application started");

        // gui
        users.addObserver(frame);
        messages.addObserver(frame);
        showFrame();

        // logic
        String username = new DialogBox("Chyatus", I18nStrings.getString("enterusername")).show();
        User me = new User(username, InetAddress.getLocalHost());
        users.setMyself(me);

        SystemFunctions.listenToNewUsers();
        SystemFunctions.loadUsersList(me);
        UserFunctions.listenToNewMessages();
    }

    /**
     * True way of showing swing frame
     */
    private static void showFrame() {
        frame.pack();
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(true);
            }
        });
    }

}
