package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class ProductGUI extends JFrame {
    private JTextField txtId, txtName, txtQuantity, txtPrice;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private JButton btnUpdateLast, btnDeleteLast;
    private JTable table;
    private DefaultTableModel tableModel;
    private ProductDAO productDAO;

    public ProductGUI() {
        productDAO = new ProductDAO();
        setTitle("Inventory Management System - Products");
        setSize(750, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        formPanel.add(new JLabel("Product ID:"));
        txtId = new JTextField();
        txtId.setEnabled(false);  // ID is auto-generated
        formPanel.add(txtId);

        formPanel.add(new JLabel("Name:"));
        txtName = new JTextField();
        formPanel.add(txtName);

        formPanel.add(new JLabel("Quantity:"));
        txtQuantity = new JTextField();
        formPanel.add(txtQuantity);

        formPanel.add(new JLabel("Price:"));
        txtPrice = new JTextField();
        formPanel.add(txtPrice);

        // Buttons
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");
        btnUpdateLast = new JButton("Update Last Product");
        btnDeleteLast = new JButton("Delete Last Product");

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);
        btnPanel.add(btnUpdateLast);
        btnPanel.add(btnDeleteLast);

        formPanel.add(new JLabel()); // Empty cell
        formPanel.add(btnPanel);

        add(formPanel, BorderLayout.NORTH);

        // Table to show products
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Quantity", "Price"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Load products initially
        loadProducts();

        // Button actions
        btnAdd.addActionListener(e -> addProduct());
        btnUpdate.addActionListener(e -> updateProduct());
        btnDelete.addActionListener(e -> deleteProduct());
        btnClear.addActionListener(e -> clearForm());

        btnUpdateLast.addActionListener(e -> updateLastProduct());
        btnDeleteLast.addActionListener(e -> deleteLastProduct());

        // Table row click
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(tableModel.getValueAt(row, 0).toString());
                    txtName.setText(tableModel.getValueAt(row, 1).toString());
                    txtQuantity.setText(tableModel.getValueAt(row, 2).toString());
                    txtPrice.setText(tableModel.getValueAt(row, 3).toString());
                }
            }
        });
    }

    private void loadProducts() {
        try {
            List<Product> products = productDAO.getAllProducts();
            tableModel.setRowCount(0);  // Clear existing rows
            for (Product p : products) {
                tableModel.addRow(new Object[]{p.getId(), p.getName(), p.getQuantity(), p.getPrice()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + e.getMessage());
        }
    }

    private void addProduct() {
        try {
            String name = txtName.getText();
            int quantity = Integer.parseInt(txtQuantity.getText());
            double price = Double.parseDouble(txtPrice.getText());

            Product p = new Product(0, name, quantity, price);
            productDAO.addProduct(p);
            JOptionPane.showMessageDialog(this, "Product added.");
            loadProducts();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding product: " + e.getMessage());
        }
    }

    private void updateProduct() {
    try {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a product to update.");
            return;
        }

        String name = txtName.getText().trim();
        String quantityStr = txtQuantity.getText().trim();
        String priceStr = txtPrice.getText().trim();

        if (name.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        int id = Integer.parseInt(txtId.getText());
        int quantity = Integer.parseInt(quantityStr);
        double price = Double.parseDouble(priceStr);

        Product p = new Product(id, name, quantity, price);
        boolean updated = productDAO.updateProduct(p);
        if (updated) {
            JOptionPane.showMessageDialog(this, "Product updated.");
            loadProducts();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Update failed.");
        }
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Invalid number format for quantity or price.");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error updating product: " + e.getMessage());
    }
}


    private void deleteProduct() {
    try {
        String idStr = txtId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.");
            return;
        }

        int id = Integer.parseInt(idStr);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this product?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = productDAO.deleteProduct(id);
            if (deleted) {
                JOptionPane.showMessageDialog(this, "Product deleted.");
                loadProducts();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Delete failed.");
            }
        }
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Invalid ID format.");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error deleting product: " + e.getMessage());
    }
}


    private void updateLastProduct() {
        try {
            List<Product> products = productDAO.getAllProducts();
            if (!products.isEmpty()) {
                Product last = products.get(products.size() - 1);
                last.setName("Updated " + last.getName());
                last.setQuantity(last.getQuantity() + 10);
                last.setPrice(last.getPrice() + 1.5);

                boolean updated = productDAO.updateProduct(last);
                if (updated) {
                    JOptionPane.showMessageDialog(this, "✅ Last product updated: " + last.getName());
                    loadProducts();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Update failed.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "⚠ No products found.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error updating last product: " + ex.getMessage());
        }
    }

    private void deleteLastProduct() {
        try {
            List<Product> products = productDAO.getAllProducts();
            if (!products.isEmpty()) {
                Product last = products.get(products.size() - 1);
                boolean deleted = productDAO.deleteProduct(last.getId());
                if (deleted) {
                    JOptionPane.showMessageDialog(this, "🗑 Last product deleted: " + last.getName());
                    loadProducts();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Delete failed.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "⚠ No products to delete.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error deleting last product: " + ex.getMessage());
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtQuantity.setText("");
        txtPrice.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ProductGUI().setVisible(true);
        });
    }
}
