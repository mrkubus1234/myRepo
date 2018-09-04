package net.atos.humaj.jakub.Types;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TypeFinder {

    public List<DataType> getDataTypesFromCsvData(List<String[]> csvData){

        DataType defaultDataType = DataType.STRING;
        DataType currentDataType;

        List<DataType> firstRowDataTypes = Arrays.stream(csvData.get(0)).map(TypeFinder::findDataType).collect(Collectors.toList());

        for(String[] row : csvData){
            int i = 0;
            for (String cell : row){
                currentDataType = findDataType(cell);
                if (!(currentDataType == firstRowDataTypes.get(i))) {
                    firstRowDataTypes.set(i, defaultDataType);
                }
                i++;
            }
        }
        return firstRowDataTypes;

        /*
        ---STREAM---
        csvData.stream().forEach(row -> {
            AtomicReference<Integer> i = new AtomicReference<>(0);
            Arrays.stream(row).map(TypeFinder::findDataType).forEach(cell -> {
                if(!(cell == firstRowDataTypes.get(i.get()))){
                        firstRowDataTypes.set(i.get(), defaultDataType);
                    }
                    i.getAndSet(i.get() + 1);
            });
        });*/

       /*
       ---ITERATOR---
       for(int i = 0; i < csvData.get(0).length; i++){

            Iterator <String[]> iterator = csvData.iterator();
            String[] currentRow = iterator.next();
            while(findDataType(currentRow[i]) == findDataType(iterator.next()[i])){
                if(iterator.hasNext()) {
                    currentRow = iterator.next();
                    if(iterator.hasNext())
                        continue;
                }
                dataTypes.add(findDataType(currentRow[i]));
                break;
            }
            if ((i+1) != dataTypes.size())
                dataTypes.add(dataType);
        }
        return dataTypes;*/
    }

    private static DataType findDataType(String data){
        if (data.toLowerCase().equals("true") || data.toLowerCase().equals("false")) {
            return DataType.BOOLEAN;
        } else if (data.toLowerCase().equals("")){
            return DataType.NULL;
        } else {
            try {
                Double testDouble = Double.parseDouble(data);
                return DataType.NUMBER;
            } catch (NumberFormatException e) {
            }
        }
        return DataType.STRING;
    }
}