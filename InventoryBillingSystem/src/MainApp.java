package src;

import java.sql.SQLException;
import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        ProductDAO productDAO = new ProductDAO();

        try {
            // 1. Add a new product
            Product newProduct = new Product(0, "Pen", 100, 10.0);
            productDAO.addProduct(newProduct);
            System.out.println("Product added successfully.");

            // 2. Fetch all products and get the last added one
            List<Product> products = productDAO.getAllProducts();

            if (products.isEmpty()) {
                System.out.println("No products found.");
                return;
            }

            Product lastProduct = products.get(products.size() - 1);
            System.out.println("Fetched Last Product: " + lastProduct.getId() + " - " + lastProduct.getName());

            // 3. Update the last product
            lastProduct.setName("Updated " + lastProduct.getName());
            lastProduct.setQuantity(lastProduct.getQuantity() + 10);
            lastProduct.setPrice(lastProduct.getPrice() + 1.5);
            boolean updated = productDAO.updateProduct(lastProduct);
            if (updated) {
                System.out.println("Last product updated successfully.");
            } else {
                System.out.println("Last product update failed.");
            }

            // 4. List all products
            System.out.println("All Products:");
            for (Product prod : productDAO.getAllProducts()) {
                System.out.println(prod.getId() + ": " + prod.getName() + ", Qty: " + prod.getQuantity() + ", Price: " + prod.getPrice());
            }

            // 5. Delete the last product
            boolean deleted = productDAO.deleteProduct(lastProduct.getId());
            if (deleted) {
                System.out.println("Last product deleted successfully.");
            } else {
                System.out.println("Last product delete failed.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Database operation failed.");
        }
    }
}
