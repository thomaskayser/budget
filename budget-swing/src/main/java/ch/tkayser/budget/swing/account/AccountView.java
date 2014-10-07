/*
 * Software is written by:
 *   Thomas Kayser
 *   tomra@gmx.net
 *   Switzerland
 *
 * Copyright (c) 2011
 * 
 */
package ch.tkayser.budget.swing.account;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;

import org.jdesktop.application.Action;

import ch.tkayser.budget.dto.AccountDTO;
import ch.tkayser.budget.swing.widgets.AbstractBudgetPanel;

/**
 * @author tom
 * 
 */
@SuppressWarnings("serial")
public class AccountView extends AbstractBudgetPanel {

    // @formatter:off
    // Actions
    public static final String AC_NEW_ACCOUNT    = "newAccountAction";
    public static final String AC_DELETE_ACCOUNT = "deleteAccountAction";
    // @formatter:on

    // the presenter
    private final AccountPresenter presenter;

   
    // tree with accounts
    private JTree accountTree;

    public AccountView(AccountPresenter presenter) {
        super();
        this.presenter = presenter;
        createViewContent();
    }

    /**
     * 
     */
    private void createViewContent() {
        // new/delete actions
        addToToolbar(AccountView.AC_NEW_ACCOUNT, AccountView.AC_DELETE_ACCOUNT);

        accountTree = new JTree(presenter.getAccountTreeModel());
        accountTree.setCellRenderer(new AccountTreeCellRenderer());
        accountTree.setRootVisible(false);
        accountTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        accountTree.setToggleClickCount(3);

        JScrollPane accountPane = new JScrollPane(accountTree);

        // listener fuer seleciton
        accountTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    presenter.edit(getSelectedAccount());
                }
            }
        });

        // layouten
        JPanel pnlMain = new JPanel();
        GroupLayout layout = new GroupLayout(pnlMain);
        pnlMain.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(accountPane));
        layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(accountPane));
        
        // set as content
        setViewContent(pnlMain);
    }

    /**
     * Action to delete a monitor
     */
    @Action
    public void deleteAccountAction(ActionEvent event) {
        presenter.delete(getSelectedAccount());
    }

    /**
     * Action to add a new account
     */
    @Action
    public void newAccountAction(ActionEvent e) {
        presenter.create();
    }

    /**
     * gibt das selektierte Account zurueck
     * 
     * @return
     */
    private AccountDTO getSelectedAccount() {
        return (AccountDTO)accountTree.getLastSelectedPathComponent();
    }

}
