package ch.tkayser.budget.swing;

import static ch.tkayser.budget.swing.Constants.*;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

import ch.tkayser.budget.swing.account.AccountView;
import ch.tkayser.budget.swing.balance.BalanceView;
import ch.tkayser.budget.swing.budgetmonitor.BudgetMonitorListView;
import ch.tkayser.budget.swing.transaction.TransactionView;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {

    //@formatter:off

    // main components
    private JSplitPane         jMainSplitPane;
    private JSplitPane         jTopSplitPane;
    private JTabbedPane        jpanelTopLeft;
    private JPanel             jpanelTopRight;
    private JPanel             jPanelBottom;
    
    // the resourceMap
    private ResourceMap         rMap;

    // the main Presenter
    private final MainPresenter mainPresenter;
    //@formatter:on

    public MainPanel(MainPresenter mainPresenter) {
        super();
        this.mainPresenter = mainPresenter;
        // get the resource Map
        rMap = Application.getInstance().getContext().getResourceMap(getClass());
        // init components
        initialize();
    }

    /**
     * @return
     */
    private Component getBottomPanel() {
        if (jPanelBottom == null) {
            jPanelBottom = new BalanceView(mainPresenter.getBalancePresenter());
        }
        return jPanelBottom;
    }

    /**
     * MainSplit Pane with a top and bottom Component
     * 
     * @return
     */
    private Component getMainSplitPane() {
        if (jMainSplitPane == null) {
            jMainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            jMainSplitPane.setName("mainPanel_splitTopBottom");
            jMainSplitPane.setDividerLocation(300);
            jMainSplitPane.setContinuousLayout(true);
            jMainSplitPane.setTopComponent(getTopSplitPane());
            jMainSplitPane.setBottomComponent(getBottomPanel());
        }
        return jMainSplitPane;
    }

    /**
     * @return
     */
    private Component getTopLeftPanel() {
        if (jpanelTopLeft == null) {
            jpanelTopLeft = new JTabbedPane();
            jpanelTopLeft.setName("mainPanel_topleft_tabs");
            jpanelTopLeft.addTab(rMap.getString(LBL_ACCOUNTS), rMap.getIcon(ICON_ACCOUNT),
                    new AccountView(mainPresenter.getAccountPresenter()));
            jpanelTopLeft.addTab(rMap.getString(LBL_MONITOR), rMap.getIcon(ICON_BUDGET_MONITOR),
                    new BudgetMonitorListView(mainPresenter.getBudgetMonitorPresenter()));
        }
        return jpanelTopLeft;
    }

    /**
     * @return
     */
    private Component getTopRightPanel() {
        if (jpanelTopRight == null) {
            jpanelTopRight = new TransactionView(mainPresenter.getTransactionPresenter());
        }
        return jpanelTopRight;
    }

    /**
     * @return
     */
    private Component getTopSplitPane() {
        if (jTopSplitPane == null) {
            jTopSplitPane = new JSplitPane();
            jTopSplitPane.setName("mainPanel_splitLeftRight");
            jTopSplitPane.setDividerLocation(450);
            jTopSplitPane.setContinuousLayout(true);
            jTopSplitPane.setDividerSize(10);
            jTopSplitPane.setBorder(null);
            jTopSplitPane.setLeftComponent(getTopLeftPanel());
            jTopSplitPane.setRightComponent(getTopRightPanel());
        }
        return jTopSplitPane;
    }

    private void initialize() {
        setLayout(new BorderLayout());
        this.add(getMainSplitPane(), BorderLayout.CENTER);
    }


}
