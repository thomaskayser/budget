/*
 * DesktopApplication1AboutBox.java
 */

package ch.tkayser.budget.swing.common;

import static ch.tkayser.budget.swing.widgets.UIFactory.createLabel;
import static ch.tkayser.budget.swing.widgets.UIFactory.createPasswordEditField;
import static ch.tkayser.budget.swing.widgets.UIFactory.createTextEditField;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

import ch.tkayser.budget.swing.Constants;
import ch.tkayser.budget.swing.MainApplication;
import ch.tkayser.budget.swing.service.LoginInformation;
import ch.tkayser.budget.swing.service.ServiceLocator;

import com.jgoodies.binding.PresentationModel;

public class LoginDialog extends JDialog {

    private static final long                   serialVersionUID = 1L;

    // @formatter:off
    private static final String                 AC_LOGIN         = "loginAction";

    // a resource and action map
    private ResourceMap                         resourceMap;

    private PresentationModel<LoginInformation> editModel;

    private JTextField                          txtPassword;

    // @formatter:on

    public LoginDialog(JFrame parent) {
        super(parent);

        editModel = new PresentationModel<LoginInformation>(new LoginInformation());

        // get the resource and action map
        resourceMap = Application.getInstance().getContext().getResourceMap(getClass());
        // Window Properties
        setModal(true);
        setResizable(true);

        // title
        setTitle(resourceMap.getString(Constants.DIALOG_TITEL));

        // init the content
        createDialog();

    }

    @org.jdesktop.application.Action
    public void loginAction(ActionEvent e) {
        try {
            ServiceLocator.init(editModel.getBean());
        } catch (Exception ex) {
            ExceptionDialog.showDialg(translateKey(Constants.ERROR_LOGON), ex);
            System.exit(0);
        }
        setVisible(false);
    }

    private void createDialog() {

        // create the content panel
        JPanel pnlContent = new JPanel();
        pnlContent.setLayout(new BorderLayout());

        // Panel mit Buttons erstellen
        JButton btnLogin = new JButton(getAction(AC_LOGIN));
        getRootPane().setDefaultButton(btnLogin);
        JPanel pnlButtons = new JPanel();
        GroupLayout btnLayout = new GroupLayout(pnlButtons);
        pnlButtons.setLayout(btnLayout);
        btnLayout.setAutoCreateContainerGaps(true);
        btnLayout.setAutoCreateGaps(true);
        btnLayout.setHorizontalGroup(btnLayout.createSequentialGroup().addComponent(btnLogin));
        btnLayout.setVerticalGroup(btnLayout.createParallelGroup().addComponent(btnLogin));

        // layout the content and button panel
        Container pane = getContentPane();
        GroupLayout mainLayout = new GroupLayout(pane);
        pane.setLayout(mainLayout);
        mainLayout.setAutoCreateContainerGaps(true);
        mainLayout.setAutoCreateGaps(true);
        // horizontal: paralell. Buttons rechts ausgerichtet
        mainLayout.setHorizontalGroup(mainLayout.createParallelGroup().addComponent(pnlContent)
                .addComponent(pnlButtons, Alignment.TRAILING));
        mainLayout.setVerticalGroup(mainLayout.createSequentialGroup().addComponent(pnlContent)
                .addComponent(pnlButtons));

        // Content hinzufuegen
        pnlContent.add(createContent(), BorderLayout.CENTER);

    }

    private JPanel createContent() {
        JLabel lblServer = createLabel("lbl.server");
        JLabel lblPort = createLabel("lbl.port");
        JLabel lblUser = createLabel("lbl.user");
        JLabel lblPassword = createLabel("lbl.password");

        // Fields
        JTextField txtServer = createTextEditField(editModel.getModel(LoginInformation.PROP_SERVER),
                LoginInformation.PROP_SERVER, null, 80);
        JTextField txtPort = createTextEditField(editModel.getModel(LoginInformation.PROP_PORT),
                LoginInformation.PROP_PORT, null, 80);
        JTextField txtUser = createTextEditField(editModel.getModel(LoginInformation.PROP_USERNAME),
                LoginInformation.PROP_USERNAME, null, 80);
        txtPassword = createPasswordEditField(editModel.getModel(LoginInformation.PROP_PASSWORD),
                LoginInformation.PROP_PASSWORD, null, 80);

        // layout
        JPanel pnlFields = new JPanel();
        GroupLayout fieldsLayout = new GroupLayout(pnlFields);
        pnlFields.setLayout(fieldsLayout);
        fieldsLayout.setAutoCreateContainerGaps(true);
        fieldsLayout.setAutoCreateGaps(true);

        // horizontal: labels and text fields parallel
        GroupLayout.SequentialGroup hGroup = fieldsLayout.createSequentialGroup();
        hGroup.addGroup(fieldsLayout.createParallelGroup().addComponent(lblServer).addComponent(lblPort)
                .addComponent(lblUser).addComponent(lblPassword));
        hGroup.addGroup(fieldsLayout.createParallelGroup().addComponent(txtServer).addComponent(txtPort)
                .addComponent(txtUser).addComponent(txtPassword));
        fieldsLayout.setHorizontalGroup(hGroup);

        // vertical: lbl and textfield parallel
        GroupLayout.SequentialGroup vGroup = fieldsLayout.createSequentialGroup();
        vGroup.addGroup(fieldsLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblServer)
                .addComponent(txtServer));
        vGroup.addGroup(fieldsLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblPort)
                .addComponent(txtPort));
        vGroup.addGroup(fieldsLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblUser)
                .addComponent(txtUser));
        vGroup.addGroup(fieldsLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblPassword)
                .addComponent(txtPassword));
        fieldsLayout.setVerticalGroup(vGroup);

        return pnlFields;
    }

    /**
     * get an action for the abstractBudgetDialog Class
     * 
     * @param actionName
     * @return
     */
    private Action getAction(String actionName) {
        return Application.getInstance(MainApplication.class).getContext().getActionMap(LoginDialog.class, this)
                .get(actionName);

    }

    /**
     * display the dialog
     */
    public final void doLogin() {
        // default config anzeigen
        editModel.setBean(LoginInformation.initDefaultFromConfig());

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                txtPassword.requestFocus();
            }
        });

        // inject resources
        resourceMap.injectComponents(this);

        // set position
        pack();
        setLocationRelativeTo(MainApplication.getMainApplication().getMainFrame());

        // display
        setVisible(true);

    }

    /**
     * i18n
     * 
     * @param key
     * @return
     */
    protected String translateKey(String key) {
        return Application.getInstance(MainApplication.class).getContext().getResourceMap().getString(key);
    }
}
