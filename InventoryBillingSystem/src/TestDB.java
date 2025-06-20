package src;
import java.sql.SQLException;

public class TestDB {
    public static void main(String[] args) {
        try {
            if (DBConnection.getConnection() != null) {
                System.out.println("? Connected to MySQL!");
            } else {
                System.out.println("? Failed to connect.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("? Failed to connect.");
        }
    }
}
