import au.com.bytecode.opencsv.CSVReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class Main {

    static final String USER = "root";
    static final String PASS = "";
    static final String HOST = "localhost:3307";

    @SuppressWarnings({"rawtypes", "unchecked", "resource"})
    public static void main(String[] args) throws IOException {

        List<TemperatureChanger> TemperatureChanger = List.of(
                new Heater(1, "Samsung AR9500T", "8600", "white", "conditioner", "6", "3", "500-1000 Вт"),
                new AirConditioner(3, "Bork R704", "4500", "grey", "heater", "R410A", "5", "10.9")
        );

        FileWriter csvWrite = new FileWriter("data.csv");
        for (var frame : TemperatureChanger) {
            csvWrite.append(frame.toString());
        }
        csvWrite.close();
        DBConnection conn = null;

        try {
            conn = DBConnection.OpenConnection(HOST, USER, PASS);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        try {
            CSVReader reader = new CSVReader(new FileReader("data.csv"), ',', '"', 0);
            List<String[]> allRows = reader.readAll();
            conn.createTemperatureChangersDB();
            conn.InsertTemperatureChangers(allRows);

            while (true) {
                System.out.println("Write 1 to see all temperature changers");
                System.out.println("Write 2 to see heaters");
                System.out.println("Write 3 to see air conditioners");
                System.out.println("Write 0 to exit");
                Scanner in = new Scanner(System.in);
                System.out.print("Enter the number ");
                int num = in.nextInt();
                switch (num) {
                    case 1:
                        conn.SelectAllTemperatureChangers();
                        break;
                    case 2:
                        conn.SelectAirConditioners();
                        break;
                    case 3:
                        conn.SelectHeaters();
                        break;
                    case 0:
                        conn.DropSchema();
                        System.exit(0);
                    default:
                        System.out.println("Try again or enter 0 to finish the program");
                }
            }
        } catch (Exception e) {
            System.out.println("Error while setup db: " + e.getLocalizedMessage());
            System.exit(-1);
        }
    }
}
