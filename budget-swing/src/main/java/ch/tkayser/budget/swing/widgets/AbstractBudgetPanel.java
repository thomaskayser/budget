/*
 * Software is written by:
 *   Thomas Kayser
 *   tomra@gmx.net
 *   Switzerland
 *
 * Copyright (c) 2011
 * 
 */
package ch.tkayser.budget.swing.widgets;

import java.awt.BorderLayout;
import java.awt.Component;
import java.text.Format;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.GroupLayout.Alignment;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

import ch.tkayser.budget.swing.MainApplication;
import ch.tkayser.budget.swing.formating.BudgetFormats;

/**
 * @author tom
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractBudgetPanel extends JPanel {

    // the resource Map
    protected ResourceMap resourceMap;

    // UI Elements
    private JToolBar jToolBar;

    private JPanel mainPanel;

    public AbstractBudgetPanel() {
        super();

        // get the resource Map
        resourceMap = Application.getInstance().getContext().getResourceMap(getClass());

        // initialize the panel
        initializePanel();

    }

    /**
     * add a separator to the toolBar
     */
    protected void addSeperatorToToolbar() {
        jToolBar.addSeparator();
    }


    /**
     * add an Action to the toolbar
     * 
     * @param action
     */
    protected void addToToolbar(String... actionNames) {
        for (String actionName : actionNames) {
            jToolBar.add(getAction(actionName));
        }
    }


    /**
     * helper method to get an action from this view
     * 
     * @param controller
     * @param actionName
     * @return
     */
    protected Action getAction(String actionName) {
        return Application.getInstance(MainApplication.class).getContext().getActionMap(this.getClass(), this).get(actionName);
    }

    /**
     * get the number formater
     *
     * @return
     */
    protected Format getDateFormat() {
        return BudgetFormats.DATE_FORMAT;
    }

    /**
     * get the number formater
     *
     * @return
     */
    protected Format getNumberFormat() {
        return BudgetFormats.BIGDECIMAL_DISPLAY_FORMAT;
    }

    /**
     * 
     */
    private final void initializePanel() {
        // Toolbar und Main Panel erstellen und layouten
        jToolBar = new JToolBar();
        jToolBar.setFloatable(false);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(jToolBar).addComponent(mainPanel));
        layout.setHorizontalGroup(layout.createParallelGroup().addComponent(jToolBar).addComponent(mainPanel, Alignment.LEADING));

    }
    

    /**
     * set the content of the view
     * 
     * @param c
     */
    protected void setViewContent(Component c) {
        // set the component as the views content
        mainPanel.add(c, BorderLayout.CENTER);
        // inject resources
        resourceMap.injectComponents(this);
    }
}
