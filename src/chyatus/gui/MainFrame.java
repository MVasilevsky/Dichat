package chyatus.gui;

import chyatus.mvc.Controller;
import chyatus.mvc.Model;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.log4j.Logger;

/**
 *
 * @author mvas
 */
public class MainFrame extends JFrame implements Observer {

    private Controller controller;

    private static final Logger log = Logger.getLogger(MainFrame.class);
    private JButton button = new JButton("test");

    public MainFrame(Controller controller) {

        this.controller = controller;
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            log.error("Can't apply look'n'feel");
        }

        initComponents();

        setSize(400, 300);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            setIconImage(ImageIO.read(getClass().getResource("/images/icon16.png")));
        } catch (IOException ex) {
            log.warn("Can't load icon");
        }
    }

    private void initComponents() {
        button.setSize(100, 30);
        add(button);
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.test();
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        new DialogBox(((Model) o).getUser().getUsername(), "asdasd").show();
    }

}
