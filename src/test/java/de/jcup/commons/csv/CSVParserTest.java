// / SPDX-License-Identifier: MIT
package de.jcup.commons.csv;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserTest {

    private CSVParser parserToTest;

    @BeforeEach
    void beforeEach() {
        parserToTest = new CSVParser();
    }

    @Test
    void csv_3_lines_4_columns_no_header_can_be_parsed_and_has_auto_columnnames() throws Exception {
        /* prepare */
        String csv = """
                a0,b0,c0,d0
                a1,b1,c1,d1
                a2,b2,c2,d2
                """;

        /* execute */
        CSVModel result = parserToTest.parse(csv, false);
        
        /* test */
        assertEquals(3, result.getRowCount());
        assertEquals("c1", result.getCellValue("col2",1));
    }
    @Test
    void csv_4_lines_4_columns_with_header_can_be_parsed_and_has_columnnames_and_cells_are_trimmed() throws Exception {
        /* prepare */
        String csv = """
                alpha, beta, gamma, delta
                a0,    b0,   c0,    d0
                a1,    b1,   c1,    d1
                a2,    b2,   c2,    d2
                """;
        
        /* execute */
        CSVModel result = parserToTest.parse(csv, true);
        
        /* test */
        assertEquals(3, result.getRowCount());
        assertEquals("c1", result.getCellValue("gamma",1));
    }
    
    @Test
    void empty_csv_can_be_parsed_but_contains_no_columns_or_rows() throws Exception {
        /* prepare */
        String csv = """
                """;
        
        /* execute */
        CSVModel result = parserToTest.parse(csv, false);
        
        /* test */
        assertEquals(0, result.getRowCount());
        assertTrue(result.getColumnNames().isEmpty());
    }

}
