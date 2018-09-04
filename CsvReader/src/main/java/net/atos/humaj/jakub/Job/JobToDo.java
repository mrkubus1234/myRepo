package net.atos.humaj.jakub.Job;

import com.google.common.collect.Lists;
import net.atos.humaj.jakub.Files.*;
import net.atos.humaj.jakub.Rabbit.*;
import net.atos.humaj.jakub.Rows.RowToJsonParser;
import net.atos.humaj.jakub.Types.*;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class JobToDo {

    Properties prop;

    public JobToDo(Properties prop){
        this.prop = prop;
    }

    public void doJob(){

        List<File> fileList = getFileList();

        for(File file: fileList){
            sendCsvDataViaRabbit(file.toString());
        }
    }

    private List<File> getFileList(){

        List<File> fileList = new ArrayList<>();

        try{
            List<String> directoriesList = Files.readAllLines(Paths.get("paths"));
            for(String directory: directoriesList) {
                fileList.addAll(getFileListInDirectory(directory, isSearchForFilesInSubdirectories()));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return fileList;
    }

    private List<File> getFileListInDirectory(String path, boolean searchInSubdirectories){
        CsvFinder finder = new CsvFinder();
        return finder.getFilteredFileList(path,prop.getProperty("file.suffix"), searchInSubdirectories);
    }

    private boolean isSearchForFilesInSubdirectories(){
        boolean searchForFilesInSubdirectories;
        if(prop.getProperty("searchForFilesInSubdirectories").toLowerCase().equals("true")){
            searchForFilesInSubdirectories = true;
        } else if (prop.getProperty("searchForFilesInSubdirectories").toLowerCase().equals("false")){
            searchForFilesInSubdirectories = false;
        } else{
            throw new NumberFormatException();
        }
        return searchForFilesInSubdirectories;
    }

    private void sendCsvDataViaRabbit(String path){
        CsvReader reader = new CsvReader();
        List<String[]> csvData = reader.readCsvAllData(path, prop.getProperty("csv.file.separator"));;
        String[] headers = reader.readCsvLine(path, prop.getProperty("csv.file.separator"));
        String fileName = FilenameUtils.getBaseName(path);
        System.out.println("File name: " +  fileName + "." + prop.getProperty("file.suffix"));

        List<DataType> dataTypes = getDataTypes(csvData);

        int batchSize = -1;
        try {
            batchSize = Integer.parseInt(prop.getProperty("rabbitmq.batchsize"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        RowToJsonParser rowToJsonParser = new RowToJsonParser(headers, dataTypes);

        RabbitSender sender = new RabbitSender();
        RabbitConfiguration configuration = new RabbitConfiguration(prop);

        List<List<String[]>> listOfBatchedRows = Lists.partition(csvData, batchSize);
        for(List<String[]> list : listOfBatchedRows){
            String message = list.
                    stream().
                    map(rowToJsonParser::parseRowToJson).
                    collect(Collectors.joining("\n"));
            try {
                sender.sendDataViaRabbit(message, fileName, configuration);
                System.out.println(message + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<DataType> getDataTypes(List<String[]> csvData) throws NumberFormatException{
        TypeFinder typeFinder = new TypeFinder();
        return typeFinder.getDataTypesFromCsvData(csvData);
    }
}