/*
 * $HeadURL$
 * Copyright(c) ISC-EJPD - Alle Rechte vorbehalten
 *
 * Letzter Commit
 *   $LastChangedDate: 2011-04-10 15:06:01 +0200 (So, 10 Apr 2011) $
 *   $Author: tom $
 *   $Revision: 2088 $ 
 */
package ch.tkayser.budget.swing.widgets;

import java.awt.Component;
import java.text.Format;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * TODO: Bitte durch Klassenbeschreibung ersetzen
 */
public class FormatedTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;

    // the format
    private final Format      format;

    // right aligned Cell
    private boolean           rightAligned;

    public FormatedTableCellRenderer(Format format) {
        this(format, false);
    }

    public FormatedTableCellRenderer(Format format, boolean rightAligned) {
        super();
        this.format = format;
        this.rightAligned = rightAligned;
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see
     * javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(
     * javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)}
     * 
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param column
     * @return
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        // set formatted text
        if (value != null) {                       
            lbl.setText(format.format(value));
            // rechtsbuendig?
            if (rightAligned) {
                
                // column Rendere rechtsbuendig setzen
                lbl.setHorizontalAlignment(RIGHT);

                // rechtsbuendiger Renderer fuer den Header mit dem korrekten L&F
                DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
                cellRenderer.setHorizontalAlignment(RIGHT);
                JTableHeader header = table.getTableHeader();
                cellRenderer.setForeground(header.getForeground());
                cellRenderer.setBackground(header.getBackground());
                cellRenderer.setFont(header.getFont());
                cellRenderer.setBorder(header.getBorder());
                table.getColumnModel().getColumn(column).setHeaderRenderer(cellRenderer);
            }
        }
        return lbl;

    }

}
