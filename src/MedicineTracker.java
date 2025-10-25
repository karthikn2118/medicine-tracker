import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;

public class MedicineTracker {
    private Database database;
    private Scanner scanner;

    public MedicineTracker() {
        this.database = new Database();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║        MEDICINE STOCK & EXPIRY TRACKER     ║");
        System.out.println("╚════════════════════════════════════════════╝");

        while (true) {
            showMenu();
            int choice = getIntInput("Choose option: ");

            switch (choice) {
                case 1:
                    addMedicine();
                    break;
                case 2:
                    viewAllMedicines();
                    break;
                case 3:
                    checkExpiryAlerts();
                    break;
                case 4:
                    exitProgram();
                    return;
                default:
                    System.out.println("❌ Invalid option! Please choose 1-4.");
            }
        }
    }

    private void showMenu() {
        System.out.println("\n" + "═".repeat(50));
        System.out.println("                  MAIN MENU");
        System.out.println("═".repeat(50));
        System.out.println("1. ➕ Add New Medicine");
        System.out.println("2. 📋 View All Medicines");
        System.out.println("3. 🚨 Check Expiry Alerts");
        System.out.println("4. ❌ Exit Program");
        System.out.println("═".repeat(50));
    }

    private void addMedicine() {
        System.out.println("\n" + "─".repeat(50));
        System.out.println("              ADD NEW MEDICINE");
        System.out.println("─".repeat(50));

        System.out.print("💊 Medicine Name: ");
        String name = scanner.nextLine();

        System.out.print("🏷️  Batch Number: ");
        String batch = scanner.nextLine();

        int quantity = getIntInput("📦 Quantity: ");
        int minStock = getIntInput("📊 Minimum Stock Level: ");

        System.out.print("🏢 Supplier: ");
        String supplier = scanner.nextLine();

        System.out.print("📅 Expiry Date (YYYY-MM-DD): ");
        String expiry = scanner.nextLine();

        // Validate date format
        if (!isValidDate(expiry)) {
            System.out.println("❌ Invalid date format! Use YYYY-MM-DD");
            return;
        }

        Medicine medicine = new Medicine(name, batch, quantity, minStock, supplier, expiry);
        database.addMedicine(medicine);
    }

    private void viewAllMedicines() {
        System.out.println("\n--- ALL MEDICINES ---");
        List<Medicine> medicines = database.getAllMedicines();

        if (medicines.isEmpty()) {
            System.out.println("📭 No medicines found in inventory.");
            return;
        }

        System.out.printf("%-3s %-20s %-12s %-6s %-8s %-15s %-12s %s%n",
                "ID", "Name", "Batch", "Qty", "Min Stock", "Supplier", "Expiry", "Status");
        System.out.println("------------------------------------------------------------");

        for (Medicine med : medicines) {
            System.out.printf("%-3d %-20s %-12s %-6d %-8d %-15s %-12s %s%n",
                    med.getId(),
                    med.getName(),
                    med.getBatchNumber(),
                    med.getQuantity(),
                    med.getMinStock(),
                    med.getSupplier(),
                    med.getExpiryDate(),
                    med.getStatus());
        }

        System.out.println("------------------------------------------------------------");
        System.out.println("📊 Total medicines: " + medicines.size());

        // Show low stock warning
        int lowStockCount = 0;
        for (Medicine med : medicines) {
            if (med.getQuantity() <= med.getMinStock()) {
                lowStockCount++;
            }
        }
        if (lowStockCount > 0) {
            System.out.println("⚠️  " + lowStockCount + " medicine(s) are at or below minimum stock level!");
        }
    }

    private void checkExpiryAlerts() {
        System.out.println("\n" + "─".repeat(80));
        System.out.println("                      EXPIRY ALERTS");
        System.out.println("─".repeat(80));

        List<Medicine> alerts = database.getExpiryAlerts();

        if (alerts.isEmpty()) {
            System.out.println("✅ No expiry alerts! All medicines are within safe dates.");
            return;
        }

        System.out.println("🚨 ALERT: " + alerts.size() + " medicine(s) need attention:");
        System.out.println();

        for (Medicine med : alerts) {
            String alertType = med.getStatus().contains("❌") ? "EXPIRED" : "EXPIRING SOON";
            System.out.println("   " + med.getStatus() + " " + med.getName());
            System.out.println("      Batch: " + med.getBatchNumber() + " | Expiry: " + med.getExpiryDate() + " | " + alertType);
            System.out.println();
        }

        // Additional warning for expired medicines
        long expiredCount = 0;
        for (Medicine med : alerts) {
            if (med.getStatus().contains("❌")) {
                expiredCount++;
            }
        }
        if (expiredCount > 0) {
            System.out.println("🔴 CRITICAL: " + expiredCount + " medicine(s) have EXPIRED and should be discarded!");
        }
    }

    private void exitProgram() {
        System.out.println("\n" + "═".repeat(50));
        System.out.println("           THANK YOU FOR USING");
        System.out.println("      MEDICINE STOCK & EXPIRY TRACKER");
        System.out.println("═".repeat(50));
        database.close();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                if (value < 0) {
                    System.out.println("❌ Please enter a positive number!");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number!");
            }
        }
    }

    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String truncate(String text, int length) {
        if (text.length() <= length) return text;
        return text.substring(0, length - 3) + "...";
    }

    public static void main(String[] args) {
        MedicineTracker tracker = new MedicineTracker();
        tracker.start();
    }
}