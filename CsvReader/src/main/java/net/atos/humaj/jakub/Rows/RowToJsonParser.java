package net.atos.humaj.jakub.Rows;

import net.atos.humaj.jakub.Types.DataType;

import java.util.List;

public class RowToJsonParser {

    private String[] firstRow;
    private List<DataType> dataTypes;

    public RowToJsonParser(String[] firstRow, List<DataType> dataTypes){
        this.firstRow = firstRow;
        this.dataTypes = dataTypes;
    }

   public String parseRowToJson(String[] row){

       StringBuilder json = new StringBuilder();

       if(firstRow.length != row.length)
           return "Wrong data in a row";
       else {
           json.append("{ ");
           for (int i = 0; i < row.length; i++) {
               json.append("\"").append(firstRow[i]).append("\":");
               switch(dataTypes.get(i)){
                   case STRING:
                       json.append("\"").append(row[i]).append("\"");
                       break;
                   case NULL:
                       json.append("null");
                       break;
                   default:
                       json.append(row[i]);
               }
               if(i < row.length-1)
                json.append(",");
           }
           json.append(" }");
       }

        return json.toString();
    }
}