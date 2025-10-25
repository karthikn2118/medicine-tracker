import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Medicine {
    private int id;
    private String name;
    private String batchNumber;
    private int quantity;
    private int minStock;
    private String supplier;
    private String expiryDate;
    private String status;

    // Constructor for new medicines (auto-generates status)
    public Medicine(String name, String batchNumber, int quantity, int minStock, String supplier, String expiryDate) {
        this.name = name;
        this.batchNumber = batchNumber;
        this.quantity = quantity;
        this.minStock = minStock;
        this.supplier = supplier;
        this.expiryDate = expiryDate;
        this.status = calculateStatus();
    }

    // Constructor for medicines from database (includes ID and status)
    public Medicine(int id, String name, String batchNumber, int quantity, int minStock, String supplier, String expiryDate, String status) {
        this.id = id;
        this.name = name;
        this.batchNumber = batchNumber;
        this.quantity = quantity;
        this.minStock = minStock;
        this.supplier = supplier;
        this.expiryDate = expiryDate;
        this.status = status;
    }

    // Calculate expiry status based on current date
    private String calculateStatus() {
        try {
            LocalDate expiry = LocalDate.parse(expiryDate);
            LocalDate today = LocalDate.now();
            long daysUntilExpiry = ChronoUnit.DAYS.between(today, expiry);

            if (daysUntilExpiry < 0) {
                return "❌ EXPIRED";
            } else if (daysUntilExpiry <= 30) {
                return "⚠️ EXPIRING";
            } else {
                return "✅ OK";
            }
        } catch (Exception e) {
            return "❓ UNKNOWN";
        }
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getBatchNumber() { return batchNumber; }
    public int getQuantity() { return quantity; }
    public int getMinStock() { return minStock; }
    public String getSupplier() { return supplier; }
    public String getExpiryDate() { return expiryDate; }
    public String getStatus() { return status; }

    // toString for debugging
    @Override
    public String toString() {
        return String.format("Medicine{id=%d, name='%s', batch='%s', qty=%d, min=%d, supplier='%s', expiry=%s, status=%s}",
                id, name, batchNumber, quantity, minStock, supplier, expiryDate, status);
    }
}