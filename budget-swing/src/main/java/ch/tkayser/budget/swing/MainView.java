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

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.jdesktop.application.Application;
import org.jdesktop.application.FrameView;

import ch.tkayser.budget.swing.common.AboutDialog;

/**
 * @author tom
 * 
 */
public class MainView extends FrameView {

    private MainPanel mainPanel;

    // the main Presenter
    private final MainPresenter mainPresenter;

    // some actions
    private static final String ACTION_EXIT = "exitAction";

    private static final String ACTION_ABOUT = "aboutAction";

    /**
     * @param application
     * @param presenter
     * @param mainController
     */
    public MainView(Application application, MainPresenter presenter) {
        super(application);
        this.mainPresenter = presenter;
        initComponents();
        setMenuBar(createMenuBar());
    }

    /**
     * Menu erstellen
     * 
     * @return
     */
    private JMenuBar createMenuBar() {
        // Menu erstellen
        JMenuBar menuBar = new JMenuBar();
        
        // File Menu
        JMenu menuFile = new JMenu();
        menuFile.setName(Constants.MENU_FILE);
        menuFile.add(getAction(ACTION_EXIT));
        menuBar.add(menuFile);

        // Help Menu
        JMenu menuHelp = new JMenu();
        menuHelp.setName(Constants.MENU_HELP);
        menuHelp.add(getAction(ACTION_ABOUT));
        menuBar.add(menuHelp);
        
        // I18n setzen
        Application.getInstance().getContext().getResourceMap(getClass()).injectComponents(menuBar);

        return menuBar;

    }

    /**
     * gibt eine Action zurueck
     * 
     * @param actionName
     * @return
     */
    private Action getAction(String actionName) {
        return Application.getInstance(MainApplication.class).getContext().getActionMap(this.getClass(), this).get(actionName);
    }

    /**
     * 
     */
    private void initComponents() {
        // set init size. will be overwritten from config...
        getFrame().setPreferredSize(new Dimension(1200, 600));
        setComponent(getMainPanel());

    }

    /**
     * @return
     */
    private JComponent getMainPanel() {
        if (mainPanel == null) {
            mainPanel = new MainPanel(mainPresenter);
        }
        return mainPanel;
    }
    
    /**
     * exit action
     */
    @org.jdesktop.application.Action
    public void exitAction(ActionEvent e) {
        System.exit(0);
    }
    
    /**
     * about Dialog
     */
    @org.jdesktop.application.Action
    public void aboutAction(ActionEvent e) {
        new AboutDialog(MainApplication.getMainApplication().getMainFrame()).setVisible(true);
    }
}
