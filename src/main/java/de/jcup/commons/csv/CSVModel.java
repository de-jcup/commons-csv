package de.jcup.commons.csv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A simple CSV model where rows can be added.
 * 
 * Usage:
 * 
 * <pre>
 * <code>
 * CSVModel model = new CSVModel("my-column1", "my-column2");
 * 
 * CSVRow row = model.addRow();
 * row.set("my-column2", "i am inside row1-colum2");
 * row.set("my-column1", "i am inside row1-colum1");
 *
 * String csv = model.toCSVString();
 * 
 * System.out.println(csv);
 * </code>
 * </pre>
 */
public class CSVModel {

    public enum LineEnding {

        UNIX("\n"), MAC("\n"), MAC_PRE_OSX("\r"), WINDOWS("\r\n");

        private String chars;

        private LineEnding(String chars) {
            this.chars = chars;
        }
        
        public String getChars() {
            return chars;
        }
    }

    public static final LineEnding DEFAULT_LINE_ENDING = LineEnding.UNIX;
    public static final char DEFAULT_DELIMITER = ',';

    private char delimiter = DEFAULT_DELIMITER;
    private LineEnding lineEnding = DEFAULT_LINE_ENDING;
    private List<String> columnNames = new ArrayList<>();
    private List<CSVRow> rows = new ArrayList<>();

    public CSVModel(String... columnNames) {
        this.columnNames.addAll(Arrays.asList(columnNames));
    }

    public void setLineEnding(LineEnding lineEnding) {
        if (lineEnding == null) {
            lineEnding = DEFAULT_LINE_ENDING;
        }
        this.lineEnding = lineEnding;
    }

    public LineEnding getLineEnding() {
        return lineEnding;
    }

    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
    }

    public char getDelimiter() {
        return delimiter;
    }

    /**
     * Resolves cell value
     * 
     * @param columnName the name of the column
     * @param rowIndex   row number to search. First row number is 0
     * @return value or <code>null</code>
     * @throws IllegalArgumentException  if column does not exist
     * @throws IndexOutOfBoundsException if rowIndex >= rows
     */
    public String getCellValue(String columnName, int rowIndex) {
        CSVRow row = assertRowForRowIndex(rowIndex);
        return row.getCellValue(columnName);

    }

    /**
     * Resolves row for given index
     * 
     * @param rowIndex starts with 0
     * @return row
     * @throws IndexOutOfBoundsException when row not found
     */
    public CSVRow getRow(int rowIndex) {
        return assertRowForRowIndex(rowIndex);
    }

    /**
     * Resolve column names
     * 
     * @return unmodifiable list of ordered column names
     */
    public List<String> getColumnNames() {
        return Collections.unmodifiableList(columnNames);
    }

    public CSVRow addRow() {
        CSVRow row = new CSVRow();
        rows.add(row);
        return row;
    }

    public int getRowCount() {
        return rows.size();
    }

    /**
     * Convert model to CVS string - with header
     * 
     * @return CSV string
     */
    public String toCSVString() {
        return toCSVString(true);
    }

    /**
     * Convert model to CVS string - with header or not
     * 
     * @param withHeader
     * @return CSV string
     */
    public String toCSVString(boolean withHeader) {
        StringBuilder sb = new StringBuilder();

        if (withHeader) {
            Iterator<String> it = columnNames.iterator();
            while (it.hasNext()) {
                String columnName = it.next();
                sb.append(columnName);
                if (it.hasNext()) {
                    sb.append(delimiter);
                }
            }
            sb.append(lineEnding.chars);
        }
        for (CSVRow row : rows) {
            int length = row.cells.length;
            int lastColumnWithDelimiter = length - 1;

            for (int i = 0; i < length; i++) {
                String cell = row.cells[i];
                sb.append(cell);
                if (i != lastColumnWithDelimiter) {
                    sb.append(delimiter);
                }
            }
            sb.append(lineEnding.chars);
        }

        return sb.toString();
    }

    private CSVRow assertRowForRowIndex(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= rows.size()) {
            throw new IndexOutOfBoundsException(rowIndex);
        }
        return rows.get(rowIndex);
    }

    private int assetColumnIndexForName(String columnName) {
        int index = columnNames.indexOf(columnName);
        if (index == -1) {
            throw new IllegalArgumentException(
                    "The column: " + columnName + " is not wellknown! Accepted csv columns are:" + columnNames);
        }
        return index;
    }

    public class CSVRow {
        private String[] cells;

        private CSVRow() {
            this.cells = new String[columnNames.size()];
        }

        /**
         * Resolves cell value
         * 
         * @param columnName
         * @param rowIndex
         * @return value or <code>null</code>
         * @throws IllegalArgumentException  if column does not exist
         * @throws IndexOutOfBoundsException if rowIndex >= rows
         */
        public String getCellValue(String columnName) {
            int index = assetColumnIndexForName(columnName);
            return cells[index];
        }

        /**
         * Set value for given column
         * 
         * @param columnName
         * @param cellData
         * @return row instance
         * @throws IllegalArgumentException if column does not exist
         */
        public CSVRow set(String columnName, Object cellData) {
            return set(columnName, String.valueOf(cellData));
        }

        /**
         * Set value for given column
         * 
         * @param columnName
         * @param cellData
         * @return row instance
         * @throws IllegalArgumentException if column does not exist
         */
        public CSVRow set(String columnName, double cellData) {
            return set(columnName, String.valueOf(cellData));
        }

        /**
         * Set value for given column
         * 
         * @param columnName
         * @param cellData
         * @return row instance
         * @throws IllegalArgumentException if column does not exist
         */
        public CSVRow set(String columnName, long cellData) {
            return set(columnName, String.valueOf(cellData));
        }

        /**
         * Set value for given column
         * 
         * @param columnName
         * @param cellData
         * @return row instance
         * @throws IllegalArgumentException if column does not exist
         */
        public CSVRow set(String columnName, String cellData) {
            int index = assetColumnIndexForName(columnName);
            cells[index] = cellData;
            return this;
        }

    }

}
