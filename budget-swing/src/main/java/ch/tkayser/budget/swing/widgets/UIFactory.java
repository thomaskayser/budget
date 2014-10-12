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

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.Date;

import javax.swing.ComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.TreeTableModel;

import ch.tkayser.budget.swing.formating.BudgetFormats;
import ch.tkayser.budget.swing.validation.BudgetValidator;
import ch.tkayser.budget.swing.validation.DocumentValidator;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueModel;

/**
 * Factory class fro UI Elements
 * 
 * @author tom
 * 
 */
public class UIFactory {

    // @formatter:off

    // the name of the ClientProperty for the related attributeName
    public static final String CLIENT_PROPERTY_ATTRIBUT_NAME = "ch.tkayser.budget.attributename";

    // default sizes
    private static final int   NO_PREFERRED_SIZE             = -1;
    private static final int   DEFAULT_HEIGHT                = 0;

    // @formatter:on

    /**
     * create a JFormattedTextField to dipslay a BigDecimal value
     * 
     * @return
     */
    public static JFormattedTextField createBigDecimalDisplayField(ValueModel model) {
        return createBigDecimalDisplayField(model, NO_PREFERRED_SIZE);
    }

    /**
     * create a JFormattedTextField to dipslay a BigDecimal value
     * 
     * @param model
     * @param attributeName
     * @param preferredWidth
     * @return
     */
    public static JFormattedTextField createBigDecimalDisplayField(ValueModel model, int preferredWidth) {
        return createFormattedTextField(model, null, null, preferredWidth, JFormattedTextField.RIGHT, false,
                BudgetFormats.getBigDecimalFormatterFactory());
    }

    /**
     * create a Textfield for Numbers
     * 
     * @param valueModel
     * @return
     */
    public static JFormattedTextField createBigDecimalEditField(ValueModel model, String attributeName,
            BudgetValidator<?> validator) {
        return createBigDecimalEditField(model, attributeName, validator, NO_PREFERRED_SIZE);
    }

    /**
     * create a Textfield for Numbers
     * 
     * @param valueModel
     * @return
     */
    public static JFormattedTextField createBigDecimalEditField(ValueModel model, String attributeName,
            BudgetValidator<?> validator, int preferredWidth) {
        return createBigDecimalEditField(model, attributeName, validator, preferredWidth, JTextField.RIGHT);
    }

    /**
     * create a Textfield for Numbers
     * 
     * @param valueModel
     * @return
     */
    public static JFormattedTextField createBigDecimalEditField(ValueModel model, String attributeName,
            BudgetValidator<?> validator, int preferredWidth, int alignment) {
        return createFormattedTextField(model, attributeName, validator, preferredWidth, alignment, true,
                BudgetFormats.getBigDecimalFormatterFactory());
    }

    /**
     * ComboBox erstellen
     * 
     * @param selection
     * @param renderer
     * @param preferredWidth
     * @return
     */
    public static JComboBox createComboBox(ComboBoxModel model, ListCellRenderer renderer) {
        return createComboBox(model, renderer, NO_PREFERRED_SIZE);
    }

    /**
     * ComboBox erstellen
     * 
     * @param selection
     * @param renderer
     * @param preferredWidth
     * @return
     */
    public static JComboBox createComboBox(ComboBoxModel model, ListCellRenderer renderer, int preferredWidth) {
        // ComboBox erstellen
        JComboBox cb = new JComboBox(model);
        cb.setRenderer(renderer);

        // groesse setzen
        setSize(cb, preferredWidth, DEFAULT_HEIGHT);

        return cb;
    }

    /**
     * Checkbox erstellen
     * 
     * @return
     */
    public static JCheckBox createCheckBox(String cbName) {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setName(cbName);
        return checkBox;
    }

    /**
     * ComboBox erstellen
     * 
     * @param selection
     * @param renderer
     * @param preferredWidth
     * @return
     */
    public static JComboBox createComboBox(PresentationModel<?> valueHolderModel, String attributeName,
            BudgetValidator<?> validator, SelectionInList<?> selection, ListCellRenderer renderer) {
        return createComboBox(valueHolderModel, attributeName, validator, selection, renderer, NO_PREFERRED_SIZE);
    }

    /**
     * ComboBox erstellen
     * 
     * @param selection
     * @param renderer
     * @param preferredWidth
     * @return
     */
    public static JComboBox createComboBox(PresentationModel<?> valueHolderModel, String attributeName,
            BudgetValidator<?> validator, SelectionInList<?> selection, ListCellRenderer renderer, int preferredWidth) {
        // ComboBox erstellen
        JComboBox cb = BasicComponentFactory.createComboBox(selection, renderer);

        // groesse setzen
        setSize(cb, preferredWidth, DEFAULT_HEIGHT);

        // validation decorator
        if (validator != null && attributeName != null && valueHolderModel != null) {

            // final kopien der parameter
            final SelectionInList<?> finalSelection = selection;
            final PresentationModel<?> finalValueHolderModel = valueHolderModel;
            final String finalAttributeName = attributeName;

            // valueHolder auf selection setzen
            finalSelection.setSelectionHolder(valueHolderModel.getModel(attributeName));

            // listener auf valueHolder Model, damit bei aenderungen des beans
            // im PresentationModel der ValueHolder der Selection angepasst
            // werden kann
            valueHolderModel.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals(PresentationModel.PROPERTYNAME_AFTER_BEAN)) {
                        finalSelection.setSelectionHolder(finalValueHolderModel.getModel(finalAttributeName));
                    }
                }
            });

            // attribute name in client properties stellen
            cb.putClientProperty(CLIENT_PROPERTY_ATTRIBUT_NAME, attributeName);
            // decorator
            new ValidationDecoration(cb, validator);
        }

        return cb;
    }

    /**
     * ComboBox erstellen
     * 
     * @param selection
     * @param renderer
     * @param preferredWidth
     * @return
     */
    public static JComboBox createComboBox(SelectionInList<?> selection, ListCellRenderer renderer) {
        return createComboBox(selection, renderer, NO_PREFERRED_SIZE);
    }

    /**
     * ComboBox erstellen
     * 
     * @param selection
     * @param renderer
     * @param preferredWidth
     * @return
     */
    public static JComboBox createComboBox(SelectionInList<?> selection, ListCellRenderer renderer, int preferredWidth) {
        return createComboBox(null, null, null, selection, renderer, preferredWidth);
    }

    /**
     * create a Textfield for Dates
     * 
     * @param valueModel
     * @return
     */
    public static JFormattedTextField createDateEditField() {
        return createDateEditField(NO_PREFERRED_SIZE);
    }

    /**
     * create a Textfield for Dates
     * 
     * @param valueModel
     * @return
     */
    public static JFormattedTextField createDateEditField(int preferredWidth) {
        JFormattedTextField textField = new JFormattedTextField(BudgetFormats.getDateFormatterFactory());
        setSize(textField, preferredWidth, DEFAULT_HEIGHT);
        return textField;
    }

    /**
     * create a Textfield for Dates
     * 
     * @param valueModel
     * @return
     */
    public static JFormattedTextField createDateEditField(ValueModel model, String attributeName,
            BudgetValidator<?> validator) {
        return createDateEditField(model, attributeName, validator, NO_PREFERRED_SIZE);
    }

    /**
     * create a Textfield for Dates
     * 
     * @param valueModel
     * @return
     */
    public static JFormattedTextField createDateEditField(ValueModel model, String attributeName,
            BudgetValidator<?> validator, int preferredWidth) {
        return createFormattedTextField(model, attributeName, validator, preferredWidth, JTextField.LEFT, true,
                BudgetFormats.getDateFormatterFactory());
    }

    /**
     * Spinner fuer Datum erstellen
     * 
     * @param model
     * @return
     */
    public static JSpinner createDateSpinner(SpinnerModel model) {
        JSpinner spinner = createSpinner(model);
        spinner.setEditor(new JSpinner.DateEditor(spinner, BudgetFormats.DATE_FORMAT_STRING));
        return spinner;
    }

    /**
     * JTextField erstellen
     * 
     * @param valueModel
     * @param preferredWidth
     * @param alignment
     * @param enabled
     * @param budgetFormater
     * @return
     */
    public static JFormattedTextField createFormattedTextField(ValueModel model, String attributeName,
            BudgetValidator<?> validator, int preferredWidth, int alignment, boolean enabled,
            AbstractFormatterFactory formatterFactory) {

        // FormattedTextField fuer das Modell erstellen
        final JFormattedTextField field = BasicComponentFactory.createFormattedTextField(model, formatterFactory);

        // set value only when valid
        field.setFocusLostBehavior(JFormattedTextField.COMMIT);

        // groesse / ausrichtung
        setSizeAndAlignment(field, preferredWidth, DEFAULT_HEIGHT, alignment);

        // enabled/disable
        field.setEnabled(enabled);

        // Validierungslistener und decorator fuers format nur wenn das Feld
        // editierbar ist
        // und ein validator da ist
        if (validator != null || enabled == true) {
            // attribute name in client properties stellen
            field.putClientProperty(CLIENT_PROPERTY_ATTRIBUT_NAME, attributeName);
            // decorator
            new ValidationDecoration(field, validator);
            // document Listener
            field.getDocument().addDocumentListener(new DocumentValidator(validator, field));
        }

        // feld zurueckgeben
        return field;
    }

    /**
     * create a sortable JTable with default Renderers. only single selection
     * 
     * @return
     */
    public static JTable createJTable(TableModel model) {
        return createJTable(model, true, true);
    }

    /**
     * create a JTable with default Renderers
     * 
     * @return
     */
    public static JTable createJTable(TableModel model, boolean sortable, boolean singleSelection) {
        // create table
        JTable table = new JTable();
        table.setModel(model);

        // selection
        if (singleSelection) {
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        } else {
            table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        }

        // sorting
        if (sortable) {
            table.setRowSorter(new TableRowSorter<TableModel>(model));
        }

        // set default renderers
        setDefaultTableRenderer(table);

        return table;

    }

    /**
     * JTreeTable erstellen
     *
     * @param model
     * @return
     */
    public static JXTreeTable createJTreeTable(TreeTableModel model) {
        // JTree erstellen
        JXTreeTable treeTable = new JXTreeTable(model);

        // layout
        treeTable.setShowHorizontalLines(true);
        treeTable.setShowVerticalLines(true);

        // default renderers setzen
        setDefaultTableRenderer(treeTable);

        return treeTable;
    }

    /**
     * label erstellen
     * 
     * @param labelName
     * @return
     */
    public static JLabel createLabel(String labelName) {
        return createLabel(labelName, "");
    }

    /**
     * label mit name und text erstellen
     * 
     * @param labelName
     * @return
     */
    public static JLabel createLabel(String labelName, String labelText) {
        JLabel lbl = new JLabel(labelText);
        lbl.setName(labelName);
        return lbl;
    }

    /**
     * spinner erstellen
     * 
     * @param model
     * @return
     */
    private static JSpinner createSpinner(SpinnerModel model) {
        JSpinner spn = new JSpinner(model);
        return spn;
    }

    /**
     * create a Textfield for displaying texts
     * 
     * @param valueModel
     * @return
     */
    public static JTextField createTextDisplayField() {
        return createTextDisplayField(NO_PREFERRED_SIZE);
    }

    /**
     * create a Textfield for displaying texts
     * 
     * @param valueModel
     * @return
     */
    public static JTextField createTextDisplayField(int preferredWidth) {
        return createTextField(preferredWidth, JTextField.LEFT, false);
    }

    /**
     * create a Textfield for displaying texts
     * 
     * @param valueModel
     * @return
     */
    public static JTextField createTextDisplayField(int preferredWidth, int alignment, boolean enabled) {
        return createTextField(preferredWidth, alignment, enabled);
    }

    /**
     * create a Textfield for texts
     * 
     * @param valueModel
     * @return
     */
    public static JTextField createTextEditField(ValueModel valueModel, String attributeName,
            BudgetValidator<?> validator) {
        return createTextEditField(valueModel, attributeName, validator, NO_PREFERRED_SIZE);
    }

    /**
     * create a Textfield for Numbers
     * 
     * @param valueModel
     * @return
     */
    public static JTextField createTextEditField(ValueModel valueModel, String attributeName,
            BudgetValidator<?> validator, int preferredWidth) {
        return createTextField(valueModel, attributeName, validator, preferredWidth, JTextField.LEFT, true);
    }

    /**
     * create a Textfield for displaying texts
     * 
     * @param valueModel
     * @return
     */
    public static JTextField createTextField(int preferredWidth, int alignment, boolean enabled) {
        // JTextField erstellens
        JTextField field = new JTextField();
        field.setEnabled(enabled);

        // groesse setzen
        setSizeAndAlignment(field, preferredWidth, DEFAULT_HEIGHT, alignment);

        return field;
    }

    /**
     * @param valueModel
     * @param attributeName
     * @param preferredSize
     * @param left
     * @return
     */
    private static JTextField createTextField(ValueModel valueModel, String attributeName,
            BudgetValidator<?> validator, int preferredWidth, int alignment, boolean enabled) {
        // JTextField erstellens
        JTextField field = BasicComponentFactory.createTextField(valueModel);
        field.setEnabled(enabled);

        // groesse setzen
        setSizeAndAlignment(field, preferredWidth, DEFAULT_HEIGHT, alignment);

        // Validierungslistener und decorator fuers format nur wenn das Feld
        // editierbar ist
        // und ein validator da ist
        if (validator != null && enabled == true) {
            // attribute name in client properties stellen
            field.putClientProperty(CLIENT_PROPERTY_ATTRIBUT_NAME, attributeName);
            // decorator
            new ValidationDecoration(field, validator);
        }

        return field;
    }

    /**
     * create a Textfield for texts
     * 
     * @param valueModel
     * @return
     */
    public static JPasswordField createPasswordEditField(ValueModel valueModel, String attributeName,
            BudgetValidator<?> validator, int preferredWith) {
        return createPasswordField(valueModel, attributeName, validator, preferredWith, JTextField.LEFT, true);
    }

    /**
     * @param valueModel
     * @param attributeName
     * @param preferredSize
     * @param left
     * @return
     */
    private static JPasswordField createPasswordField(ValueModel valueModel, String attributeName,
            BudgetValidator<?> validator, int preferredWidth, int alignment, boolean enabled) {
        // JTextField erstellens
        JPasswordField field = BasicComponentFactory.createPasswordField(valueModel);
        field.setEnabled(enabled);

        // groesse setzen
        setSizeAndAlignment(field, preferredWidth, DEFAULT_HEIGHT, alignment);

        // Validierungslistener und decorator fuers format nur wenn das Feld
        // editierbar ist
        // und ein validator da ist
        if (validator != null && enabled == true) {
            // attribute name in client properties stellen
            field.putClientProperty(CLIENT_PROPERTY_ATTRIBUT_NAME, attributeName);
            // decorator
            new ValidationDecoration(field, validator);
        }

        return field;
    }

    /**
     * Default CellRenderers erstellen
     *
     * @param table
     */
    private static void setDefaultTableRenderer(JTable table) {
        table.setDefaultRenderer(BigDecimal.class, new FormatedTableCellRenderer(
                BudgetFormats.BIGDECIMAL_DISPLAY_FORMAT, true));
        table.setDefaultRenderer(Date.class, new FormatedTableCellRenderer(BudgetFormats.DATE_FORMAT));

    }

    /**
     * preferred Size setzen
     * 
     * @param component
     * @param prefererredWidht
     * @param preferredHeight
     */
    private static void setSize(JComponent component, int preferredWidth, int preferredHeight) {
        // preferred Size
        if (preferredWidth != NO_PREFERRED_SIZE && preferredHeight != NO_PREFERRED_SIZE) {
            component.setPreferredSize(new Dimension(preferredWidth, preferredHeight));
        }
    }

    /**
     * @param field
     * @param preferredWidth
     * @param defaultHeight
     * @param alignment
     */
    private static void setSizeAndAlignment(JTextField field, int preferredWidth, int preferredHeight, int alignment) {
        // preferred Size
        setSize(field, preferredWidth, preferredHeight);

        // alignment
        field.setHorizontalAlignment(alignment);

    }

}
