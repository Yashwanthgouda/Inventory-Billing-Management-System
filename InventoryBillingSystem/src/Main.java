package src;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Inventory & Billing System Started!");

        String url = "jdbc:mysql://localhost:3306/inventory_db";
        String user = "root";  // change if you have a different username
        String password = "your_password"; // set your MySQL password

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Connected to MySQL database!");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM products");

            while (rs.next()) {
                System.out.println(rs.getInt("id") + " - " + rs.getString("name") +
                        " - Qty: " + rs.getInt("quantity") +
                        " - ₹" + rs.getDouble("price"));
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
