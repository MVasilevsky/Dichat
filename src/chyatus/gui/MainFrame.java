package chyatus.gui;

import chyatus.User;
import chyatus.commands.Message;
import chyatus.func.system.SystemFunctions;
import chyatus.mvc.controller.MainController;
import chyatus.state.ApplicationState;
import chyatus.state.Messages;
import chyatus.state.Users;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JViewport;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.apache.log4j.Logger;

/**
 * M in MVC
 *
 * @author mvas
 */
public class MainFrame extends javax.swing.JFrame implements Observer {

    private MainController controller;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
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
                if ("Windows".equals(info.getName())) {
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

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        usersList = new javax.swing.JList();
        jSplitPane3 = new javax.swing.JSplitPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        messageArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jSplitPane1.setDividerLocation(150);

        usersList.setBackground(new java.awt.Color(253, 251, 247));
        usersList.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        usersList.setMinimumSize(new java.awt.Dimension(80, 80));
        usersList.setModel(new DefaultListModel());
        jScrollPane1.setViewportView(usersList);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jSplitPane3.setDividerLocation(490);
        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jTable2.setBackground(new java.awt.Color(253, 251, 247));
        jTable2.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable2.setFillsViewportHeight(true);
        jTable2.setGridColor(new java.awt.Color(255, 255, 255));
        jTable2.setRowHeight(28);
        jTable2.setShowHorizontalLines(false);
        jTable2.setShowVerticalLines(false);
        jTable2.setTableHeader(null);
        jScrollPane4.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setMinWidth(150);
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(150);
            jTable2.getColumnModel().getColumn(0).setMaxWidth(150);
            jTable2.getColumnModel().getColumn(2).setMinWidth(150);
            jTable2.getColumnModel().getColumn(2).setPreferredWidth(150);
            jTable2.getColumnModel().getColumn(2).setMaxWidth(150);
        }

        jSplitPane3.setTopComponent(jScrollPane4);

        messageArea.setBackground(new java.awt.Color(253, 251, 247));
        messageArea.setColumns(20);
        messageArea.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        messageArea.setLineWrap(true);
        messageArea.setRows(5);
        messageArea.setWrapStyleWord(true);
        messageArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sendMessage(evt);
            }
        });
        jScrollPane5.setViewportView(messageArea);

        jSplitPane3.setRightComponent(jScrollPane5);

        jSplitPane1.setRightComponent(jSplitPane3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 980, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sendMessage(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sendMessage
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!evt.isControlDown()) {
                controller.sendMessage(messageArea.getText());
                messageArea.setText("");
            } else {
                messageArea.append("\n");
            }
        }
    }//GEN-LAST:event_sendMessage

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextArea messageArea;
    private javax.swing.JList usersList;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable o, Object arg) {
        Integer mode = (Integer) arg;
        switch (mode) {
            case ApplicationState.MESSAGES_UPDATED:
                Messages m = (Messages) o;
                DefaultTableModel tableModel = (DefaultTableModel) jTable2.getModel();
                tableModel.setRowCount(0);
                for (Message message : m.getAll()) {
                    String[] row = {message.getSender().getUsername(), message.getText(), sdf.format(message.getDate())};
                    tableModel.addRow(row);
                }

                // scroll down
                JViewport viewport = (JViewport) jTable2.getParent();
                Rectangle rect = jTable2.getCellRect(jTable2.getRowCount() - 1, 0, true);
                viewport.scrollRectToVisible(rect);

                break;
            case ApplicationState.USERS_UPDATED:
                Users u = (Users) o;
                DefaultListModel model = (DefaultListModel) usersList.getModel();
                model.clear();
                for (User user : u.getAll()) {
                    model.addElement(user.getUsername());
                }
                break;
        }
    }

}
