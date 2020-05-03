import java.sql.*;
import java.util.List;

public class DBConnection {

    private Connection conn;

    private DBConnection(Connection conn) {
        this.conn = conn;
    }

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    public static DBConnection OpenConnection(String host, String userName, String password) throws SQLException, IllegalArgumentException, ClassNotFoundException {
        String jdbc_url;

        if (host != null && host.isEmpty() && userName != null && userName.isEmpty()) {
            throw new IllegalArgumentException("Host and username can't be empty");
        }
        if (password != null && !password.isEmpty()) {
            jdbc_url = "jdbc:mysql://" + userName + ":" + password + "@" + host;
        } else {
            jdbc_url = "jdbc:mysql://" + userName + "@" + host;
        }

        System.out.println("A connection to the database is being created...");

        Class.forName(JDBC_DRIVER);
        Connection conn = DriverManager.getConnection(jdbc_url);
        return new DBConnection(conn);
    }

    public void createTemperatureChangersDB() throws Exception {
        Statement stmt = conn.createStatement();

        System.out.println("Creating a database...");

        stmt.executeUpdate("CREATE DATABASE temperaturechanger");

        stmt.executeUpdate("CREATE TABLE temperaturechanger.temperature_changers("
                + "id INT NOT NULL  PRIMARY KEY,"
                + "name VARCHAR(45) NOT NULL,"
                + "price VARCHAR(10) NOT NULL,"
                + "color VARCHAR(20) NOT NULL,"
                + "type VARCHAR(20) not null)");

        stmt.executeUpdate("CREATE TABLE temperaturechanger.air_conditioners("
                + "id INT not null PRIMARY KEY,"
                + "refrigerantType VARCHAR(45) not null,"
                + "pipelineHeight VARCHAR(45) not null,"
                + "airflowRate VARCHAR(45) not null,"
                + "FOREIGN KEY (id) REFERENCES temperaturechanger.temperature_changers(id))");

        stmt.executeUpdate("CREATE TABLE temperaturechanger.heaters("
                + "id INT not null PRIMARY KEY,"
                + "heatedArea VARCHAR(45) not null,"
                + "modesNumber VARCHAR(45) not null,"
                + "power VARCHAR(45) not null,"
                + "FOREIGN KEY (id) REFERENCES temperaturechanger.temperature_changers(id))");
    }

    public void InsertTemperatureChangers(List<String[]> mass) throws Exception {

        for (String[] row : mass) {
            for (String a : row) {
                String[] k = a.split(";");
                PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO temperaturechanger.temperature_changers( id,name,price,color,type) VALUES(?,?,?,?,?)");
                stmt1.setString(1, k[0]);
                stmt1.setString(2, k[1]);
                stmt1.setString(3, k[2]);
                stmt1.setString(4, k[3]);
                stmt1.setString(5, k[4]);
                stmt1.executeUpdate();

                if (k[4].equals("conditioner")) {
                    PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO temperaturechanger.air_conditioners(id,refrigerantType,pipelineHeight,airflowRate) VALUES(?,?,?,?)");
                    stmt2.setString(1, k[0]);
                    stmt2.setString(2, k[5]);
                    stmt2.setString(3, k[6]);
                    stmt2.setString(4, k[7]);
                    stmt2.executeUpdate();

                } else if (k[4].equals("heater")) {
                    PreparedStatement stmt3 = conn.prepareStatement("INSERT INTO temperaturechanger.heaters(id,heatedArea,modesNumber,power) VALUES(?,?,?,?)");
                    stmt3.setString(1, k[0]);
                    stmt3.setString(2, k[5]);
                    stmt3.setString(3, k[6]);
                    stmt3.setString(4, k[7]);
                    stmt3.executeUpdate();
                }
            }
        }
    }

    public void SelectAllTemperatureChangers() throws SQLException {
        Statement stmt = conn.createStatement();
        StringBuilder sb = new StringBuilder();

        ResultSet rs = stmt.executeQuery("SELECT * FROM ((temperaturechanger.temperature_changers" +
                " LEFT JOIN temperaturechanger.air_conditioners ON temperaturechanger.temperature_changers.id =  temperaturechanger.air_conditioners.id)" +
                " LEFT JOIN temperaturechanger.heaters ON  temperaturechanger.temperature_changers.id =  temperaturechanger.heaters.id ) ORDER BY temperaturechanger.temperature_changers.id");

        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String price = rs.getString("price");
            String color = rs.getString("color");
            String type = rs.getString("type");

            String refrigerantType = rs.getString("refrigerantType");
            String pipelineHeight = rs.getString("pipelineHeight");
            String airflowRate = rs.getString("airflowRate");

            String heatedArea = rs.getString("heatedArea");
            String modesNumber = rs.getString("modesNumber");
            String power = rs.getString("power");

            if (type.equals("digital")) {
                sb.append("id: ").append(id)
                        .append("; name: ").append(name)
                        .append("; price: ").append(price)
                        .append("; color: ").append(color)
                        .append("; type: ").append(type)
                        .append("; refrigerantType: ").append(refrigerantType)
                        .append("; pipelineHeight: ").append(pipelineHeight)
                        .append("; airflowRate: ").append(airflowRate)
                        .append("\n");
            } else {
                sb.append("id: ").append(id)
                        .append("; name: ").append(name)
                        .append("; price: ").append(price)
                        .append("; color: ").append(color)
                        .append("; type: ").append(type)
                        .append("; heatedArea: ").append(heatedArea)
                        .append("; modesNumber: ").append(modesNumber)
                        .append("; power: ").append(power)
                        .append("\n");
            }
        }

        stmt.close();
        System.out.println(sb.toString());
    }

    public void SelectAirConditioners() throws SQLException {
        Statement stmt = conn.createStatement();
        StringBuilder sb = new StringBuilder();
        ResultSet rs = stmt.executeQuery("SELECT * FROM temperaturechanger.temperature_changers  INNER JOIN temperaturechanger.air_conditioners ON temperaturechanger.temperature_changers.id =  temperaturechanger.air_conditioners.id");

        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String price = rs.getString("price");
            String color = rs.getString("color");
            String type = rs.getString("type");

            String refrigerantType = rs.getString("refrigerantType");
            String pipelineHeight = rs.getString("pipelineHeight");
            String airflowRate = rs.getString("airflowRate");

            sb.append("id: ").append(id)
                    .append("; name: ").append(name)
                    .append("; price: ").append(price)
                    .append("; color: ").append(color)
                    .append("; type: ").append(type)
                    .append("; refrigerantType: ").append(refrigerantType)
                    .append("; pipelineHeight: ").append(pipelineHeight)
                    .append("; airflowRate: ").append(airflowRate)
                    .append("\n");
        }

        stmt.close();
        System.out.println("" +
                "" +
                "");
        System.out.println(sb.toString());
    }

    public void SelectHeaters() throws SQLException {
        Statement stmt = conn.createStatement();
        StringBuilder sb = new StringBuilder();
        ResultSet rs = stmt.executeQuery("SELECT * FROM temperaturechanger.temperature_changers INNER JOIN temperaturechanger.heaters ON  temperaturechanger.temperature_changers.id =  temperaturechanger.heaters.id ");

        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String price = rs.getString("price");
            String color = rs.getString("color");
            String type = rs.getString("type");

            String heatedArea = rs.getString("heatedArea");
            String modesNumber = rs.getString("modesNumber");
            String power = rs.getString("power");

            sb.append("id: ").append(id)
                    .append("; name: ").append(name)
                    .append("; price: ").append(price)
                    .append("; color: ").append(color)
                    .append("; type: ").append(type)
                    .append("; heatedArea: ").append(heatedArea)
                    .append("; modesNumber: ").append(modesNumber)
                    .append("; power: ").append(power)
                    .append("\n");
        }

        stmt.close();
        System.out.println("" +
                "" +
                "");
        System.out.println(sb.toString());
    }

    public void DropSchema() throws SQLException {
        Statement stmt = conn.createStatement();

        stmt.executeUpdate("drop schema temperaturechanger");
        System.out.println("The database has been deleted");
    }
}