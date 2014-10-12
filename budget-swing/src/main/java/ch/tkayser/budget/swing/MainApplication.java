/*
 * Software is written by:
 *   Thomas Kayser
 *   tomra@gmx.net
 *   Switzerland
 *
 * Copyright (c) 2011
 * 
 */
package ch.tkayser.budget.swing;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import ch.tkayser.budget.exception.BudgetException;
import ch.tkayser.budget.service.BudgetService;
import ch.tkayser.budget.swing.common.LoginDialog;
import ch.tkayser.budget.swing.service.ServiceLocator;
import ch.tkayser.budget.util.BudgetMockService;

/**
 * @author tom
 * 
 */
public class MainApplication extends SingleFrameApplication {

    private static BudgetService mockService = new BudgetMockService();

    /**
     * convenience Method to get the MainApplication instance
     */
    public static MainApplication getMainApplication() {
        return Application.getInstance(MainApplication.class);
    }

    /**
     * @return the service
     */
    public static BudgetService getService() {
        // mock services
        if (System.getProperty(Constants.PROP_USE_MOCK_SERVICE) != null) {
            return mockService;
        }
        return ServiceLocator.getService();
    }

    public static void main(String[] args) throws BudgetException {

        launch(MainApplication.class, args);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jdesktop.application.Application#startup()
     */
    @Override
    protected void startup() {

        new LoginDialog(MainApplication.getMainApplication().getMainFrame()).doLogin();

        MainPresenter presenter = new MainPresenter();
        MainView mainView = new MainView(this, presenter);

        // // set Look and Feel
        // try {
        // GUIPreferences.getInstance();
        // UIManager.setLookAndFeel(GUIPreferences.getInstance().getLookAndFeelName());
        // SwingUtilities.updateComponentTreeUI(mainView.getFrame());
        // } catch (Exception e) {
        // JOptionPane.showMessageDialog(mainView.getFrame(),
        // "Error loading prefs: "+e.getMessage());
        // }
        show(mainView);
    }
}
