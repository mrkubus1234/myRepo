package net.atos.humaj.jakub.Types;

import net.atos.humaj.jakub.Files.CsvReader;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class TypeFinderTest {

    private CsvReader reader;
    private String pathToTestFile;
    private TypeFinder typeFinder;
    private List<DataType> dataTypesExpected;
    private List<String[]> csvData;
    private List<DataType> dataTypes;

    @Before
    public void setUp(){
        reader = new CsvReader();
        typeFinder = new TypeFinder();
        dataTypesExpected = new ArrayList<>();
        dataTypesExpected.add(DataType.NUMBER);
        dataTypesExpected.add(DataType.STRING);
        dataTypesExpected.add(DataType.BOOLEAN);
    }

    @Test
    public void getDataTypesFromCsvDataTest(){
        pathToTestFile = "C:\\Java Projects\\TestFiles\\data.csv";
        csvData = reader.readCsvAllData(pathToTestFile, ",");
        dataTypes = typeFinder.getDataTypesFromCsvData(csvData);
        assertEquals(dataTypesExpected, dataTypes);
    }

    @Test
    public void getDataTypesFromWrongCsvDataTest(){
        dataTypesExpected.remove(2);
        dataTypesExpected.add(DataType.STRING);
        pathToTestFile = "C:\\Java Projects\\TestFiles\\badData.csv";
        csvData = reader.readCsvAllData(pathToTestFile, ",");
        dataTypes = typeFinder.getDataTypesFromCsvData(csvData);
        assertEquals(dataTypesExpected, dataTypes);
    }
}
