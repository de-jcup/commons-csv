package de.jcup.commons.csv;

import de.jcup.commons.csv.CSVModel.CSVRow;
import de.jcup.commons.csv.CSVModel.LineEnding;

public class CSVParser {

    /**
     * Parses given csv string with default delimiter and line ending. Cells are auto trimmed.
     * 
     * @param csv
     * @param withHeadline when defined as <code>false</code>, the column names for
     *                     the model will be auto created with "col${columnIndex}".
     *                     For example if the first line has 3 columns, the headers
     *                     will be "col0,col1,col2"
     * @return csv model
     * @throws CSVParseException if parsing fails
     */
    public CSVModel parse(String csv, boolean withHeadline) throws CSVParseException {
        return parse(csv, withHeadline, CSVModel.DEFAULT_DELIMITER);
    }

    /**
     * Parses given csv string and default line ending. Cells are auto trimmed.
     * 
     * @param csv
     * @param withHeadline when defined as <code>false</code>, the column names for
     *                     the model will be auto created with "col${columnIndex}".
     *                     For example if the first line has 3 columns, the headers
     *                     will be "col0,col1,col2"
     * @param delimiter    delimiter to use - e.g. ',' or ';'
     * @return csv model
     * @throws CSVParseException if parsing fails
     */
    public CSVModel parse(String csv, boolean withHeadline, char delimiter) throws CSVParseException {
        return parse(csv, withHeadline, delimiter, CSVModel.DEFAULT_LINE_ENDING);
    }

    /**
     * Parses given csv string and default line ending. Cells are auto trimmed.
     * 
     * @param csv
     * @param withHeadline when defined as <code>false</code>, the column names for
     *                     the model will be auto created with "col${columnIndex}".
     *                     For example if the first line has 3 columns, the headers
     *                     will be "col0,col1,col2"
     * @param delimiter    delimiter to use - e.g. ',' or ';'
     * @param lineEnding   the line ending to use
     * @return csv model
     * @throws CSVParseException if parsing fails
     */
    public CSVModel parse(String csv, boolean withHeadline, char delimiter, LineEnding lineEnding)
            throws CSVParseException {
        if (csv == null) {
            throw new IllegalArgumentException("csv may not be null");
        }
        if (lineEnding == null) {
            throw new IllegalArgumentException("lineEnding may not be null");
        }

        String delimiterString = String.valueOf(delimiter);
        String[] lines = csv.split(lineEnding.getChars());
        CSVModel model = buildModel(withHeadline, delimiterString, lines);
        model.setDelimiter(delimiter);
        model.setLineEnding(lineEnding);
        return model;
    }

    private CSVModel buildEmptyModel() {
        return new CSVModel();
    }

    private class CSVModelBuildContext {
        private CSVModel model;
        private int currentLineNumber = 0;
        public boolean withHeadline;
        public String delimiter;
        public int firstLineColumnCount;
    }

    private CSVModel buildModel(boolean withHeadline, String delimiterString, String[] lines) throws CSVParseException {
        CSVModelBuildContext context = new CSVModelBuildContext();
        context.withHeadline = withHeadline;
        context.delimiter = delimiterString;

        for (String line : lines) {

            buildLine(context, line);

            context.currentLineNumber++;
        }
        if (context.model == null) {
            context.model = buildEmptyModel();
        }
        return context.model;
    }

    private void buildLine(CSVModelBuildContext context, String line) throws CSVParseException {
        if (line.isBlank()) {
            return;
        }
        String[] cells = line.split(context.delimiter);
        trimCells(cells);
        if (context.currentLineNumber == 0) {
            context.firstLineColumnCount = cells.length;
        } else {
            assertSameColumnSizeAsAtFirstLine(context.firstLineColumnCount, context.currentLineNumber, cells);
        }
        boolean addAsRow = handleHeaders(context, cells);
        if (addAsRow) {
            CSVRow row = context.model.addRow();
            int index = 0;
            for (String columnName : context.model.getColumnNames()) {
                row.set(columnName, cells[index]);
                index++;
            }
        }
    }

    private void trimCells(String[] cells) {
        for (int i = 0; i < cells.length; i++) {
            String value = cells[i];
            if (value != null) {
                cells[i] = value.trim();
            }
        }

    }

    private boolean handleHeaders(CSVModelBuildContext context, String[] cells) {
        boolean addAsRow = true;
        // handle model creation for first line
        if (context.currentLineNumber == 0) {
            if (context.withHeadline) {
                context.model = new CSVModel(cells);
                addAsRow = false;
            } else {
                // create and use synthetic column names
                String[] syntheticColNames = new String[cells.length];
                for (int i = 0; i < cells.length; i++) {
                    syntheticColNames[i] = "col" + i;
                }
                context.model = new CSVModel(syntheticColNames);
            }
        }
        return addAsRow;
    }

    private void assertSameColumnSizeAsAtFirstLine(int amountOfColumns, int currentLineNumber, String[] cells)
            throws CSVParseException {
        if (cells.length != amountOfColumns) {
            throw new CSVParseException("In first line we have " + amountOfColumns + ", but line: " + currentLineNumber
                    + " has: " + cells.length, currentLineNumber);
        }
    }

    public class CSVParseException extends Exception {

        private static final long serialVersionUID = 1L;
        private int line;
        private int column;

        public CSVParseException(String message) {
            this(message, 0);
        }

        public CSVParseException(String message, int line) {
            this(message, line, 0);
        }

        public CSVParseException(String message, int line, int column) {
            super(message);
            this.line = line;
            this.column = column;
        }

        public int getLine() {
            return line;
        }

        public int getColumn() {
            return column;
        }

    }
}
