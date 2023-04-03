import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        String fileNameJson = "data.json";

        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(fileNameJson, json);
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName){
        List<Employee> employees = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            employees = csv.parse();
        }catch (Exception e){
            e.printStackTrace();
        }
        return employees;
    }

    public static String listToJson(List<Employee> list){
        Type listType = new TypeToken<List<Employee>>(){}.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static void writeString(String fileName, String json){
        try (FileWriter file = new FileWriter(fileName)){
            file.write(json);
            file.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
