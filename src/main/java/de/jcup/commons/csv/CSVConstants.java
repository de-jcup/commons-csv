package de.jcup.commons.csv;

public class CSVConstants {
    public static final char DEFAULT_DELIMITER = ';';
    
    public static final char ESCAPE_CHAR = '"';
    
    public static final String ESCAPE_CHAR_AS_STRING = String.valueOf(ESCAPE_CHAR);
    
    /**
     * An enumeration for line endings
     */
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
}
