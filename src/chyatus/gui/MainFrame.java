package chyatus.gui;

import chyatus.User;
import chyatus.func.system.SystemFunctions;
import chyatus.mvc.controller.MainController;
import chyatus.state.ApplicationState;
import chyatus.state.Messages;
import chyatus.state.Users;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.log4j.Logger;

/**
 * M in MVC
 *
 * @author mvas
 */
public class MainFrame extends javax.swing.JFrame implements Observer {

    private MainController controller;
    private static final Logger log = Logger.getLogger(MainFrame.class);

    /**
     * Creates new form MainFrame
     *
     * @param controller C in MVC
     */
    public MainFrame(MainController controller) {
        super("Chatus");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showOptionDialog(null,
                        "Are You Sure to Close this Application?",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == JOptionPane.YES_OPTION) {
                    SystemFunctions.bye();
                    System.exit(0);
                }
            }
        });

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

        try {
            setIconImage(ImageIO.read(getClass().getResource("/images/icon16.png")));
        } catch (IOException ex) {
            log.warn("Can't load icon");
        }

        this.controller = controller;
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        usersList = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        usersList.setModel(new DefaultListModel());
        jScrollPane1.setViewportView(usersList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(609, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList usersList;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable o, Object arg) {
        Integer mode = (Integer) arg;
        DefaultListModel model;
        switch (mode) {
            case ApplicationState.MESSAGES_UPDATED:
                Messages m = (Messages) o;
//                model = (DefaultListModel) messagesList.getModel();
//                model.clear();
//                for (Message message : m.getMessages()) {
//                    model.addElement(message.text);
//                }
                break;
            case ApplicationState.USERS_UPDATED:
                Users u = (Users) o;
                model = (DefaultListModel) usersList.getModel();
                model.clear();
                for (User user : u.getAll()) {
                    model.addElement(user.getUsername());
                }
                break;
        }
    }

}
