/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;

/**  DmzTableModel Class
 */
public class DmzTableModel extends AbstractTableModel {

    private static final int i2 = 2;

    private static final int i5 = 5;

    private int NUM_COLUMNS = i2;

    private final int START_NUM_ROWS = i5;

    private int nextEmptyRow = 0;

    private int numRows = 0;

    private ArrayList aColNames = new ArrayList();

    private ArrayList colTitles = new ArrayList();

    private ArrayList aColData = new ArrayList();

    private String pkCol = "";

    private int iPkCol = -1;

    /** DmzTableModel Constructor
     * @param aColumnTitles Columns titles
     * @param aColumnNames Columns data sources
     * @param pkCol ordinal primary key col
     */
    public DmzTableModel(ArrayList aColumnTitles, ArrayList aColumnNames, String pkCol) {
        NUM_COLUMNS = aColumnNames.size();
        setAllColumnNames(aColumnNames);
        setAllColumnTitles(aColumnTitles);
        setPkCol(pkCol);
    }

    /** Populate the model from and external source
     * @param rs recordset to populate with
     */
    public void initRemote(ResultSet rs) {
        ArrayList aDataRow = new ArrayList();
        final int iCount = aColNames.size();
        clear();
        try {
            while (rs.next()) {
                aDataRow = new ArrayList();
                for (int i = 0; i < iCount; i++) {
                    aDataRow.add(rs.getString((String) aColNames.get(i)));
                }
                addRecord(aDataRow);
            }
        } catch (SQLException e) {
            System.out.println("dmzTableModel.initRemote(): " + e);
        }
    }

    /** Set all column titles
     * @param aaColNames array of coumns titles
     */
    private void setAllColumnTitles(ArrayList paramColTitles) {
        colTitles.addAll(aColNames);
        for (int i = 0; i < paramColTitles.size(); i++) {
            colTitles.set(i, paramColTitles.get(i));
        }
    }

    private void setPkCol(String pk) {
        for (int idx = 0; idx < aColNames.size(); idx++) {
            if (pk.compareToIgnoreCase((String) aColNames.get(idx)) == 0) {
                iPkCol = idx;
            }
        }
    }

    /** Set all column name
     * @param aaColNames array of coumns names
     */
    public void setAllColumnNames(ArrayList aaColNames) {
        aColNames.addAll(aaColNames);
        NUM_COLUMNS = aColNames.size();
    }

    /** Get the title of a column
     * @param column Ordinal column position
     * @return column title
     */
    public String getColumnName(int column) {
        String r = "invalid-column position";
        if (column >= 0 && column <= NUM_COLUMNS) {
            r = (String) colTitles.get(column);
        } else {
            System.out.println(" NUM_COLUMNS : " + NUM_COLUMNS);
            System.out.println(" aColNames.size() : " + aColNames.size());
            System.out.println(" getColumnName( int ) : " + column);
        }
        return r;
    }

    /** get the column count
     * @return the number of columns
     */
    public int getColumnCount() {
        return NUM_COLUMNS;
    }

    /** get the row count
     * @return the number of rows
     */
    public int getRowCount() {
        if (numRows < START_NUM_ROWS) {
            return START_NUM_ROWS;
        } else {
            return numRows;
        }
    }

    /** get the column/row value
     * @param row row to examine
     * @param column to select
     * @return object at row/column
     */
    public Object getValueAt(int row, int column) {
        try {
            TableRecord p = (TableRecord) aColData.get(row);
            return p.getColumnValue(column);
        } catch (Exception e) {
        }
        return "";
    }

    /** Add a record to the table
     * @param aRecord record to add as an array
     */
    public void addRecord(ArrayList aRecord) {
        String pId = "";
        TableRecord r = new TableRecord();
        boolean found = false;
        boolean addedRow = false;
        int index = 0;
        int i = 0;
        if (iPkCol > -1) {
            while (!found && (i < nextEmptyRow)) {
                r = (TableRecord) aColData.get(i);
                if (pId == r.getColumnValue(iPkCol)) {
                    found = true;
                    index = i;
                } else {
                    i++;
                }
            }
            if (found) {
                r.update(aRecord);
            } else {
                if (numRows <= nextEmptyRow) {
                    numRows++;
                    addedRow = true;
                }
                index = nextEmptyRow;
                aColData.add(index, new TableRecord(aRecord));
            }
            nextEmptyRow++;
            if (addedRow) {
                fireTableRowsInserted(index, index);
            } else {
                fireTableRowsUpdated(index, index);
            }
        }
    }

    /** clear all data from the table
     */
    public void clear() {
        final int oldNumRows = numRows;
        numRows = START_NUM_ROWS;
        aColData.clear();
        nextEmptyRow = 0;
        if (oldNumRows > START_NUM_ROWS) {
            fireTableRowsDeleted(START_NUM_ROWS, oldNumRows - 1);
        }
        fireTableRowsUpdated(0, START_NUM_ROWS - 1);
    }

    /** local diagnostic */
    private void printDiags(boolean found, int index, int i) {
        System.out.print("entry:");
        System.out.print("  aColData.size():" + aColData.size());
        System.out.print("  nextEmptyRow:" + nextEmptyRow);
        System.out.print("  found:" + found);
        System.out.print("  numRows:" + numRows);
        System.out.print("  index:" + index);
        System.out.println("  i:" + i);
    }

    /** TableRecord inner class */
    class TableRecord {

        private String id;

        private ArrayList aData = new ArrayList();

        /** TableRecord inner class constructor
         * @param aDataRow array of data to make row with
         */
        public TableRecord(ArrayList aDataRow) {
            aData.addAll(aDataRow);
        }

        /** TableRecord inner class default constructor
         */
        public TableRecord() {
            aData.clear();
        }

        /** Update the current record
         * @param aRecord array of data to add as row
         */
        public void update(ArrayList aRecord) {
            aData.clear();
            aData.addAll(aRecord);
        }

        /** Set the value of a row/column field
         * @param s Value to set
         * @param i Ordinal Column
         */
        public void setColumnValue(String s, int i) {
            aData.set(i, s);
        }

        /** Get the value of a column field
         * @param i Ordinal column
         * @return String value of columns
         */
        public String getColumnValue(int i) {
            String s = (String) aData.get(i);
            return s;
        }
    }
}
