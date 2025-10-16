package ru.netology.task3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.netology.CSV_JSON_XML_parser_task1and2.Employee;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String fileName = "new_data.json";

        String json = readString(fileName);
//        System.out.println(json);

        List<Employee> list = jsonToList(json);
        list.forEach(System.out::println);
    }

    public static String readString(String fileName){
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла: " + fileName, e);
        }
        return content.toString();
    }

    public static List<Employee> jsonToList(String json){
        List<Employee> employees = new ArrayList<>();

        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(json);

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) obj;

                String jsonString = jsonObject.toJSONString();

                Employee employee = gson.fromJson(jsonString, Employee.class);
                employees.add(employee);
            }

        } catch (ParseException e) {
            throw new RuntimeException("Ошибка при парсинге JSON", e);
        }

        return employees;
    }
}
