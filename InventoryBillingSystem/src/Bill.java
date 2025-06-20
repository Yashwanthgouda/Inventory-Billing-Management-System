package src;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Bill {
    private int id;
    private List<BillItem> items = new ArrayList<>();
    private double totalAmount;
    private LocalDateTime date; // ✅ Add this

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<BillItem> getItems() {
        return items;
    }

    public void addItem(BillItem item) {
        items.add(item);
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    // ✅ Getter and Setter for date
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
