package de.jcup.commons.csv;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CSVLineSplitterTest {

    @Test
    void string_keeps_as_is() {
        /* prepare */
        CSVLineSplitter splitterToTest = new CSVLineSplitter(';');

        /* execute */
        String[] result = splitterToTest.splitLine("Hello World");

        /* test */
        assertEquals(1, result.length);
        assertEquals("Hello World", result[0]);
    }

    @Test
    void string_without_escaping_can_be_splitted() {
        /* prepare */
        CSVLineSplitter splitterToTest = new CSVLineSplitter(';');

        /* execute */
        String[] result = splitterToTest.splitLine("Hello;World");

        /* test */
        assertEquals(2, result.length);
        assertEquals("Hello", result[0]);
        assertEquals("World", result[1]);
    }

    @Test
    void string_with_escaping_can_be_splitted() {
        /* prepare */
        CSVLineSplitter splitterToTest = new CSVLineSplitter(';');

        /* execute */
        String[] result = splitterToTest.splitLine("\"Hello;World\"");

        /* test */
        assertEquals(1, result.length);
        assertEquals("Hello;World", result[0]);
    }

    @Test
    void string_with_escaping_and_normal_separation_can_be_splitted() {
        /* prepare */
        CSVLineSplitter splitterToTest = new CSVLineSplitter(';');

        /* execute */
        String[] result = splitterToTest.splitLine("\"Hello;World\";we are here");

        /* test */
        assertEquals(2, result.length);
        assertEquals("Hello;World", result[0]);
        assertEquals("we are here", result[1]);
    }

    @Test
    void string_with_escaping_double_quotes_can_be_splitted() {
        /* prepare */
        CSVLineSplitter splitterToTest = new CSVLineSplitter(';');

        /* execute */
        String csvToSplit = "\"\"Hello\"\";World;we are here";
        String[] result = splitterToTest.splitLine(csvToSplit);

        /* test */
        assertEquals(3, result.length);
        assertEquals("\"Hello\"", result[0]);
        assertEquals("World", result[1]);
        assertEquals("we are here", result[2]);
    }

}
