package net.atos.humaj.jakub.Rows;


import net.atos.humaj.jakub.Types.DataType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class RowToJsonParserTest {

    private List<DataType> dataTypes;
    private RowToJsonParser parser;

    @Before
    public void setUp(){
        dataTypes = new ArrayList<>();
        dataTypes.add(DataType.NUMBER);
        dataTypes.add(DataType.STRING);
        dataTypes.add(DataType.BOOLEAN);
    }
    @Test
    public void parseRowToJsonTest(){
        String[] firstRow = {"id", "name", "isOK"};
        String[] row = {"3", "Jan", "true"};
        parser = new RowToJsonParser(firstRow, dataTypes);
        String rowJson = parser.parseRowToJson(row);
        String json = "{ \"id\":3,\"name\":\"Jan\",\"isOK\":true }";
        assertEquals(json, rowJson);
    }
}
