package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // Create - Add new product
    public void addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO products(name, quantity, price) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, product.getName());
            pst.setInt(2, product.getQuantity());
            pst.setDouble(3, product.getPrice());
            pst.executeUpdate();
        }
    }

    // Read - Get product by ID
    public Product getProductById(int id) throws SQLException {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                    );
                }
            }
        }
        return null; // not found
    }

    // Update - Modify existing product
    public boolean updateProduct(Product product) throws SQLException {
        String sql = "UPDATE products SET name=?, quantity=?, price=? WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, product.getName());
            pst.setInt(2, product.getQuantity());
            pst.setDouble(3, product.getPrice());
            pst.setInt(4, product.getId());

            int rows = pst.executeUpdate();
            return rows > 0;
        }
    }

    // Delete - Remove product by ID
    public boolean deleteProduct(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            int rows = pst.executeUpdate();
            return rows > 0;
        }
    }

    // Get all products - List all products (for GUI)
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("quantity"),
                    rs.getDouble("price")
                );
                products.add(product);
            }
        }
        return products;
    }
}
