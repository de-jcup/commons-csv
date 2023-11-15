package de.jcup.commons.csv;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.jcup.commons.csv.CSVModel.CSVRow;

class CSVModelTest {

    @Test
    void toCSVString_no_argument_no_data() {
        /* prepare */
        CSVModel model = new CSVModel("my-column1", "my-column2");
        
        /* execute */
        String csv = model.toCSVString();
        
        /* test */
        String expected ="""
                my-column1,my-column2
                """;
        
        assertEquals(expected,csv);
    }
    @Test
    void toCSVString_false_no_data() {
        /* prepare */
        CSVModel model = new CSVModel("my-column1", "my-column2");
        
        /* execute */
        String csv = model.toCSVString(false);
        
        /* test */
        String expected ="""
                """;
        
        assertEquals(expected,csv);
    }
    
    @Test
    void toCSVString_no_argument_is_with_header_and_data() {
        /* prepare */
        CSVModel model = new CSVModel("my-column1", "my-column2");
        
        CSVRow row = model.addRow();
        row.set("my-column2", "i am inside row1-colum2");
        row.set("my-column1", "i am inside row1-colum1");

        /* execute */
        String csv = model.toCSVString();
        
        /* test */
        String expected ="""
my-column1,my-column2
i am inside row1-colum1,i am inside row1-colum2
                """;
        
        assertEquals(expected,csv);
    }
    @Test
    void toCSVString_multiple_rows_no_argument_is_with_header_and_data() {
        /* prepare */
        CSVModel model = new CSVModel("my-column1", "my-column2");
        
        model.addRow().set("my-column2", "i am inside row1-colum2").set("my-column1", "i am inside row1-colum1");
        model.addRow().set("my-column2", "i am inside row2-colum2").set("my-column1", "i am inside row2-colum1");
        model.addRow().set("my-column1", "i am inside row3-colum1").set("my-column2", "i am inside row3-colum2");
        
        /* execute */
        String csv = model.toCSVString();
        
        /* test */
        String expected ="""
my-column1,my-column2
i am inside row1-colum1,i am inside row1-colum2
i am inside row2-colum1,i am inside row2-colum2
i am inside row3-colum1,i am inside row3-colum2
                """;
        
        assertEquals(expected,csv);
    }
    
    @Test
    void toCSVString_false_is_without_header_but_only_data() {
        /* prepare */
        CSVModel model = new CSVModel("my-column1", "my-column2");
        
        CSVRow row = model.addRow();
        row.set("my-column2", "i am inside row1-colum2");
        row.set("my-column1", "i am inside row1-colum1");
        
        /* execute */
        String csv = model.toCSVString(false);
        
        /* test */
        String expected ="""
                i am inside row1-colum1,i am inside row1-colum2
                """;
        
        assertEquals(expected,csv);
    }

}
