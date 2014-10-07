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

import javax.ejb.EJB;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import ch.tkayser.budget.exception.BudgetException;
import ch.tkayser.budget.service.BudgetService;
import ch.tkayser.budget.swing.prefs.GUIPreferences;
import ch.tkayser.budget.util.BudgetMockService;

/**
 * @author tom
 * 
 */
public class MainApplication extends SingleFrameApplication {

    @EJB
    private static BudgetService service;

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
        return service;
    }
    
    public static void main(String[] args) throws BudgetException {
        // mock services
        if (System.getProperty(Constants.PROP_USE_MOCK_SERVICE) != null) {
            service = new BudgetMockService();            
        }
        launch(MainApplication.class, args);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jdesktop.application.Application#startup()
     */
    @Override
    protected void startup() {
        MainPresenter presenter = new MainPresenter();
        MainView mainView = new MainView(this, presenter);
//        // set Look and Feel
//        try {
//            GUIPreferences.getInstance();
//            UIManager.setLookAndFeel(GUIPreferences.getInstance().getLookAndFeelName());
//            SwingUtilities.updateComponentTreeUI(mainView.getFrame());
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(mainView.getFrame(), "Error loading prefs: "+e.getMessage());
//        }
        show(mainView);
    }
}
