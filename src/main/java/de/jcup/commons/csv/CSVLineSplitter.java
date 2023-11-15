package de.jcup.commons.csv;

import static de.jcup.commons.csv.CSVConstants.ESCAPE_CHAR;
import static de.jcup.commons.csv.CSVConstants.ESCAPE_CHAR_AS_STRING;

import java.util.ArrayList;
import java.util.List;

public class CSVLineSplitter {

    private char delimiter;

    CSVLineSplitter(char delimiter) {
        this.delimiter = delimiter;
    }

    String[] splitLine(String line) {
        List<String> cellList = new ArrayList<>(line.length() / 2);
        char[] charArray = line.toCharArray();

        ParseState state = ParseState.NORMAL;

        StringBuilder sb = new StringBuilder();

        char charBefore='-'; 
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (c == ESCAPE_CHAR) {
                if (ParseState.NORMAL.equals(state)) {
                    
                    state = ParseState.STRING_ESCAPING_ACTIVE;
                    
                } else if (ParseState.STRING_ESCAPING_ACTIVE.equals(state)) {
                    if (charBefore==ESCAPE_CHAR) {
                        sb.append(ESCAPE_CHAR_AS_STRING);
                    }
                    state = ParseState.NORMAL;
                }
            } else if (c == delimiter) {
                if (ParseState.STRING_ESCAPING_ACTIVE.equals(state)) {
                    sb.append(c);
                } else {
                    /* end detected */
                    String string = sb.toString();
                    cellList.add(string);
                    sb = new StringBuilder();
                    state = ParseState.NORMAL;
                }
            } else {
                sb.append(c);
            }
            charBefore=c;
        }
        if (!sb.isEmpty()) {
            String string = sb.toString();
            cellList.add(string);
        }

        return cellList.toArray(new String[cellList.size()]);
    }

    private enum ParseState {
        STRING_ESCAPING_ACTIVE,

        NORMAL,

    }

}
