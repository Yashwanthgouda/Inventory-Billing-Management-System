package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.List;

public class BillingGUI extends JFrame {
    private JComboBox<Product> productComboBox;
    private JTextField quantityField;
    private JButton addButton, generateBillButton;
    private JTable billTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;

    private ProductDAO productDAO = new ProductDAO();
    private Bill currentBill = new Bill();
    private BillDAO billDAO = new BillDAO();

    public BillingGUI() {
        setTitle("Billing System");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        // Top panel for product selection
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        topPanel.add(new JLabel("Select Product:"));
        productComboBox = new JComboBox<>();
        topPanel.add(productComboBox);

        topPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField(5);
        topPanel.add(quantityField);

        addButton = new JButton("Add to Bill");
        topPanel.add(addButton);

        add(topPanel, BorderLayout.NORTH);

        // Table for showing bill items
        String[] columns = {"Product", "Quantity", "Price", "Total"};
        tableModel = new DefaultTableModel(columns, 0);
        billTable = new JTable(tableModel);
        add(new JScrollPane(billTable), BorderLayout.CENTER);

        // Bottom panel for total and generate button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalLabel = new JLabel("Total: 0.00");
        bottomPanel.add(totalLabel);

        generateBillButton = new JButton("Generate Bill");
        bottomPanel.add(generateBillButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Load products into combo box with exception handling
        loadProducts();

        // Button action listeners
        addButton.addActionListener(e -> addToBill());
        generateBillButton.addActionListener(e -> generateBill());
    }

    private void loadProducts() {
        try {
            List<Product> products = productDAO.getAllProducts();
            DefaultComboBoxModel<Product> model = new DefaultComboBoxModel<>();
            for (Product p : products) {
                model.addElement(p);
            }
            productComboBox.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addToBill() {
        Product selectedProduct = (Product) productComboBox.getSelectedItem();
        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(this, "Please select a product.");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid quantity (positive integer).");
            return;
        }

        double price = selectedProduct.getPrice();
        double total = price * quantity;

        // Add to table
        tableModel.addRow(new Object[]{selectedProduct.getName(), quantity, price, total});

        // Add to current bill
        BillItem item = new BillItem(selectedProduct, quantity, price);
        currentBill.addItem(item);

        // Update total amount label
        currentBill.setTotalAmount(currentBill.getTotalAmount() + total);
        totalLabel.setText(String.format("Total: %.2f", currentBill.getTotalAmount()));

        quantityField.setText("");
    }

    private void generateBill() {
    try {
        if (currentBill.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No items in the bill to generate.");
            return;
        }

        currentBill.setDate(LocalDateTime.now());

        int billId = billDAO.createBill(currentBill);
        JOptionPane.showMessageDialog(this, "Bill generated with ID: " + billId);

        currentBill = new Bill();
        tableModel.setRowCount(0);
        totalLabel.setText("Total: 0.00");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error generating bill: " + e.getMessage());
        e.printStackTrace();
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BillingGUI().setVisible(true);
        });
    }
}
