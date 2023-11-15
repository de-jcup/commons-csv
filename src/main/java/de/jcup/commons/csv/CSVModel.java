package de.jcup.commons.csv;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
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
    public static final String DEFAULT_LINE_ENDING = "\n";
    public static final String DEFAULT_DELIMITER = ",";

    private String delimiter = DEFAULT_DELIMITER;
    private String lineEnding = DEFAULT_LINE_ENDING;
    private List<String> columnNames = new ArrayList<>();
    private List<CSVRow> rows = new ArrayList<>();
    
    public void setLineEnding(String lineEnding) {
        this.lineEnding = lineEnding;
    }
    
    public String getLineEnding() {
        return lineEnding;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public CSVModel(String... columnNames) {
        this.columnNames.addAll(Arrays.asList(columnNames));
    }

    public class CSVRow {
        private String[] cells;

        private CSVRow() {
            this.cells = new String[columnNames.size()];
        }

        public CSVRow set(String columnId, Object cellData) {
            return set(columnId, String.valueOf(cellData));
        }
        
        public CSVRow set(String columnId, double cellData) {
            return set(columnId, String.valueOf(cellData));
        }
        
        public CSVRow set(String columnId, long cellData) {
            return set(columnId, String.valueOf(cellData));
        }

        public CSVRow set(String columnId, String cellData) {
            int index = columnNames.indexOf(columnId);
            if (index == -1) {
                throw new IllegalArgumentException(
                        "The column: " + columnId + " is not wellknown! Accepted csv columns are:" + columnNames);
            }
            cells[index] = cellData;
            return this;
        }

    }

    public CSVRow addRow() {
        CSVRow row = new CSVRow();
        rows .add(row);
        return row;
    }

    public String toCSVString() {
        return toCSVString(true);
    }
    
    public String toCSVString(boolean withHeader) {
        StringBuilder sb = new StringBuilder();
        
        if (withHeader) {
            Iterator<String> it = columnNames.iterator();
            while (it.hasNext()) {
                String columnName = it.next();
                sb.append(columnName);
                if(it.hasNext()) {
                    sb.append(delimiter);
                }
            }
            sb.append(lineEnding);
        }
        for (CSVRow row: rows) {
            int length = row.cells.length;
            int lastColumnWithDelimiter = length-1;
            
            for (int i=0;i<length;i++) {
                String cell = row.cells[i];
                sb.append(cell);
                if (i!=lastColumnWithDelimiter) {
                    sb.append(delimiter);
                }
            }
            sb.append(lineEnding);
        }
        
        return sb.toString();
    }
}
