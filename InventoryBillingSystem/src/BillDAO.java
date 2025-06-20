package src;

import java.sql.*;
import java.util.*;

public class BillDAO {
    public int createBill(Bill bill) throws SQLException {
        String insertBillSQL = "INSERT INTO bills (total_amount, date) VALUES (?, ?)";
        String insertItemSQL = "INSERT INTO bill_items (bill_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

        int billId = 0;
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement billStmt = conn.prepareStatement(insertBillSQL, Statement.RETURN_GENERATED_KEYS)) {
                billStmt.setDouble(1, bill.getTotalAmount());
                billStmt.setTimestamp(2, Timestamp.valueOf(bill.getDate()));
                billStmt.executeUpdate();

                ResultSet rs = billStmt.getGeneratedKeys();
                if (rs.next()) {
                    billId = rs.getInt(1);
                }
            }

            try (PreparedStatement itemStmt = conn.prepareStatement(insertItemSQL)) {
                for (BillItem item : bill.getItems()) {
                    itemStmt.setInt(1, billId);
                    itemStmt.setInt(2, item.getProduct().getId());
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.setDouble(4, item.getPrice());
                    itemStmt.addBatch();
                }
                itemStmt.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return billId;
    }
}
