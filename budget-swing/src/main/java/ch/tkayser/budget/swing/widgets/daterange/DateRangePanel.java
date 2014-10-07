package ch.tkayser.budget.swing.widgets.daterange;

import static ch.tkayser.budget.swing.widgets.UIFactory.createComboBox;
import static ch.tkayser.budget.swing.widgets.UIFactory.createDateEditField;
import static ch.tkayser.budget.swing.widgets.UIFactory.createLabel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.tkayser.budget.swing.formating.BudgetFormats;

/**
 * Panel zum anzeigen eines Datumsbereiches
 * 
 * @author tom
 * 
 */
public class DateRangePanel extends JPanel {

    private static final long   serialVersionUID = 1L;

    // the date ranges
    private JFormattedTextField dateFrom;
    private JFormattedTextField dateTo;

    // combobox with the dateranges
    private JComboBox           cbRanges;

    public DateRangePanel() {
        super();
        initFields();
    }

    /**
     * initialize the layout
     * 
     * @param parent
     */
    private void initFields() {

        // JComboBox with Ranges
        JLabel lblName = createLabel("lbl.daterange");
        cbRanges = createComboBox(new DefaultComboBoxModel(DateRange.values()), new DateRangeCellRenderer());
        cbRanges.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // set the date from / to
                DateRange range = (DateRange) cbRanges.getSelectedItem();
                dateFrom.setText(BudgetFormats.DATE_FORMAT.format(range.getDateFrom()));
                dateTo.setText(BudgetFormats.DATE_FORMAT.format(range.getDateTo()));
            }
        });

        // date from / to
        JLabel lblFrom = createLabel("lbl.datefrom");
        dateFrom = createDateEditField(40);
        JLabel lblTo = createLabel("lbl.dateto");
        dateTo = createDateEditField(40);

        // set layout
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(false);
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(lblName)
                .addComponent(cbRanges).addComponent(lblFrom).addComponent(dateFrom).addComponent(lblTo)
                .addComponent(dateTo));
        layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(lblName).addComponent(cbRanges)
                .addComponent(lblFrom).addComponent(dateFrom).addComponent(lblTo).addComponent(dateTo));

        // select the year to the previous month entry
        cbRanges.setSelectedItem(DateRange.YEAR_TO_PREV_MONTH);

    }

    /**
     * get the from date
     * 
     * @return
     */
    public Date getDateFrom() {
        return getDate(dateFrom);
    }

    /**
     * get the to date
     * 
     * @return
     */
    public Date getDateTo() {
        return getDate(dateTo);
    }

    /**
     * convert a text field to a date
     * 
     * @param dateTextField
     * @return
     */
    private Date getDate(JFormattedTextField field) {
        try {
            return BudgetFormats.DATE_FORMAT.parse(field.getText());
        } catch (ParseException e) {
            // dates should be validated.
            e.printStackTrace();
            return null;
        }
    }

}
