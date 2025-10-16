package ru.netology.CSV_JSON_XML_parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

        String fileName = "data.csv";

        List<Employee> list = parseCSV(columnMapping, fileName);

//      list.forEach(System.out::println);
        String json = listToJson(list);
//      System.out.println(json);
        writeString(json, "data.json");

        String fileNameXml = "data.xml";

        List<Employee> listX = parseXML(fileNameXml);
        listX.forEach(System.out::println);

        String json2 = listToJson(listX);
        writeString(json, "data2.json");
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {

        List<Employee> list;

        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy =
                    new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            list = csv.parse();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static String listToJson(List list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);

        return json;
    }

    public static void writeString(String json, String fileName) {

        try (FileWriter writer = new FileWriter(fileName)) {

            writer.write(json);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Employee> parseXML(String fileName) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileName));
            doc.getDocumentElement().normalize();

            NodeList employeeNodes = doc.getElementsByTagName("employee");
            List<Employee> employees = new ArrayList<>();

            for (int i = 0; i < employeeNodes.getLength(); i++) {
                Node node = employeeNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element employeeElement = (Element) node;


                    String idStr = employeeElement.getElementsByTagName("id").item(0).getTextContent();
                    String firstName = employeeElement.getElementsByTagName("firstName").item(0).getTextContent();
                    String lastName = employeeElement.getElementsByTagName("lastName").item(0).getTextContent();
                    String country = employeeElement.getElementsByTagName("country").item(0).getTextContent();
                    String ageStr = employeeElement.getElementsByTagName("age").item(0).getTextContent();

                    int id = Integer.parseInt(idStr);
                    int age = Integer.parseInt(ageStr);

                    employees.add(new Employee(id, firstName, lastName, country, age));
                }
            }
            return employees;

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при парсинге XML: " + fileName, e);
        }
    }
}
