package de.jcup.commons.csv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import de.jcup.commons.csv.CSVModel.CSVRow;

class CSVModelTest {

    @Test
    void getRowCount_empty_model_is_0() {
        /* prepare */
        CSVModel model = new CSVModel("my-column1", "my-column2");

        /* execute + test */
        assertEquals(0, model.getRowCount());
    }
    
    @Test
    void set_value_for_non_existing_column_throws_exception() {
        /* prepare */
        CSVModel model = new CSVModel("my-column1", "my-column2");

        /* execute + test */
        assertThrows(IllegalArgumentException.class, () -> model.addRow().set("not-existing", 1));

    }

    @Test
    void toCSVString_no_argument_no_data() {
        /* prepare */
        CSVModel model = new CSVModel("my-column1", "my-column2");

        /* execute */
        String csv = model.toCSVString();

        /* test */
        String expected = """
                my-column1,my-column2
                """;

        assertEquals(expected, csv);
    }

    @Test
    void toCSVString_false_no_data() {
        /* prepare */
        CSVModel model = new CSVModel("my-column1", "my-column2");

        /* execute */
        String csv = model.toCSVString(false);

        /* test */
        String expected = """
                """;

        assertEquals(expected, csv);
    }

    @Test
    void toCSVString_no_argument_is_with_header_and_data() {
        /* prepare */
        CSVModel model = new CSVModel("my-column1", "my-column2");

        CSVRow row = model.addRow();
        row.set("my-column2", "i am inside row0-colum2");
        row.set("my-column1", "i am inside row0-colum1");

        /* execute */
        String csv = model.toCSVString();

        /* test */
        String expected = """
                my-column1,my-column2
                i am inside row0-colum1,i am inside row0-colum2
                                """;

        assertEquals(expected, csv);
    }

    @Test
    void toCSVString_multiple_rows_no_argument_is_with_header_and_data() {
        /* prepare */
        CSVModel model = new CSVModel("my-column1", "my-column2");

        model.addRow().set("my-column2", "i am inside row0-colum2").set("my-column1", "i am inside row0-colum1");
        model.addRow().set("my-column2", "i am inside row1-colum2").set("my-column1", "i am inside row1-colum1");
        model.addRow().set("my-column1", "i am inside row2-colum1").set("my-column2", "i am inside row2-colum2");

        /* execute */
        String csv = model.toCSVString();

        /* test */
        String expected = """
                my-column1,my-column2
                i am inside row0-colum1,i am inside row0-colum2
                i am inside row1-colum1,i am inside row1-colum2
                i am inside row2-colum1,i am inside row2-colum2
                                """;

        assertEquals(expected, csv);
    }

    @Test
    void toCSVString_false_is_without_header_but_only_data() {
        /* prepare */
        CSVModel model = new CSVModel("my-column1", "my-column2");

        CSVRow row = model.addRow();
        row.set("my-column2", "i am inside row0-colum2");
        row.set("my-column1", "i am inside row0-colum1");

        /* execute */
        String csv = model.toCSVString(false);

        /* test */
        String expected = """
                i am inside row0-colum1,i am inside row0-colum2
                """;

        assertEquals(expected, csv);
    }

    @Test
    void row_getCellValue_one_row() {
        /* prepare */
        CSVModel model = new CSVModel("my-column1", "my-column2");

        CSVRow row = model.addRow();
        row.set("my-column2", "i am inside row0-colum2");
        row.set("my-column1", "i am inside row0-colum1");

        /* execute + test */
        assertEquals("i am inside row0-colum1", row.getCellValue("my-column1"));

    }

    @Test
    void model_getCellValue_one_row() {
        /* prepare */
        CSVModel model = new CSVModel("my-column1", "my-column2");

        CSVRow row = model.addRow();
        row.set("my-column2", "i am inside row0-colum2");
        row.set("my-column1", "i am inside row0-colum1");

        /* execute + test */
        assertEquals("i am inside row0-colum1", model.getCellValue("my-column1", 0));

    }
    
    @Test
    void model_getCellValue_three_rows() {
        /* prepare */
        CSVModel model = new CSVModel("my-column1", "my-column2");
        
        /* @formatter:off */
        model.addRow().
          set("my-column2", "i am inside row0-colum2").
          set("my-column1", "i am inside row0-colum1");
        model.addRow().
          set("my-column2", "i am inside row1-colum2").
          set("my-column1", "i am inside row1-colum1");
        model.addRow().
          set("my-column2", "i am inside row2-colum2").
          set("my-column1", "i am inside row2-colum1");
        /* @formatter:on */
        
        /* execute + test */
        assertEquals("i am inside row0-colum1", model.getCellValue("my-column1", 0));
        assertEquals("i am inside row2-colum2", model.getCellValue("my-column2", 2));
        
    }
    @Test
    void model_rowiterator_works_as_exected() {
        /* prepare */
        CSVModel model = new CSVModel("my-column1", "my-column2");
        
        /* @formatter:off */
        model.addRow().
        set("my-column2", "i am inside row0-colum2").
        set("my-column1", "i am inside row0-colum1");
        model.addRow().
        set("my-column2", "i am inside row1-colum2").
        set("my-column1", "i am inside row1-colum1");
        model.addRow().
        set("my-column2", "i am inside row2-colum2").
        set("my-column1", "i am inside row2-colum1");
        /* @formatter:on */
        
        /* execute + test */
        assertEquals("i am inside row0-colum1", model.getCellValue("my-column1", 0));
        assertEquals("i am inside row2-colum2", model.getCellValue("my-column2", 2));
        
    }
    @Test
    void model_getRow_three_rows() {
        /* prepare */
        CSVModel model = new CSVModel("my-column1", "my-column2");
        
        /* @formatter:off */
        model.addRow().
            set("my-column2", "i am inside row0-colum2").
            set("my-column1", "i am inside row0-colum1");
        model.addRow().
            set("my-column2", "i am inside row1-colum2").
            set("my-column1", "i am inside row1-colum1");
        model.addRow().
            set("my-column2", "i am inside row2-colum2").
            set("my-column1", "i am inside row2-colum1");
        /* @formatter:on */
        
        /* execute + test */
        assertEquals("i am inside row0-colum1", model.getRow(0).getCellValue("my-column1"));
        assertEquals("i am inside row2-colum2", model.getRow(2).getCellValue("my-column2"));
        assertEquals(3, model.getRowCount());
    }

}
