package net.atos.humaj.jakub.Files;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

    public String[] readCsvLine(String path, String separator){

        String[] headers = {};

        try{
            FileReader file = new FileReader(path);

            CSVReader csvReader = new CSVReaderBuilder(file)
                    .withCSVParser(new CSVParserBuilder().withSeparator(separator.charAt(0)).build())
                    .withSkipLines(0)
                    .build();
            return csvReader.readNext();
        } catch (IOException e){
            e.printStackTrace();
        }
        return headers;
    }

    public List<String[]> readCsvAllData(String path, String separator){

        List<String[]> allData = new ArrayList<>();

        try {
            FileReader filereader = new FileReader(path);

            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withCSVParser(new CSVParserBuilder().withSeparator(separator.charAt(0)).build())
                    .withSkipLines(1)
                    .build();
            return csvReader.readAll();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return allData;
    }
}