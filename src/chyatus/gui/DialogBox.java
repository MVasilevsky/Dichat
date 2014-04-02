package chyatus.gui;

import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 *
 * @author mvas
 */
public class DialogBox {

    private static final Logger log = Logger.getLogger(DialogBox.class);

    private final String title;
    private final String question;
    private ImageIcon icon;

    public DialogBox(String title, String question) {
        this.title = title;
        this.question = question;
        try {
            this.icon = new ImageIcon(ImageIO.read(getClass().getResource("/images/icon16.png")));
        } catch (IOException ex) {
            log.warn("Can't load icon for dialog box");
        }
    }

    public DialogBox(String title, String question, ImageIcon icon) {
        this.title = title;
        this.question = question;
        this.icon = icon;
    }

    public String show() {
        return (String) JOptionPane.showInputDialog(
                null,
                question,
                title,
                JOptionPane.PLAIN_MESSAGE,
                icon,
                null,
                "");
    }

}
