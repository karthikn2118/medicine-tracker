import java.util.List;
import java.util.Scanner;

public class MedicineTracker {
    private Database database;
    private Scanner scanner;

    public MedicineTracker() {
        this.database = new Database();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("🏥 WELCOME TO MEDICINE STOCK & EXPIRY TRACKER 🏥");

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
                    System.out.println("👋 Thank you for using Medicine Tracker. Goodbye!");
                    database.close();
                    return;
                default:
                    System.out.println("❌ Invalid option! Please choose 1-4.");
            }
        }
    }

    private void showMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. ➕ Add Medicine");
        System.out.println("2. 📋 View All Medicines");
        System.out.println("3. 🚨 Check Expiry Alerts");
        System.out.println("4. ❌ Exit");
    }

    private void addMedicine() {
        System.out.println("\n--- ADD NEW MEDICINE ---");

        System.out.print("Medicine Name: ");
        String name = scanner.nextLine();

        System.out.print("Batch Number: ");
        String batch = scanner.nextLine();

        int quantity = getIntInput("Quantity: ");
        int minStock = getIntInput("Minimum Stock Level: ");

        System.out.print("Supplier: ");
        String supplier = scanner.nextLine();

        System.out.print("Expiry Date (YYYY-MM-DD): ");
        String expiry = scanner.nextLine();

        Medicine medicine = new Medicine(name, batch, quantity, minStock, supplier, expiry);
        database.addMedicine(medicine);
    }

    private void viewAllMedicines() {
        System.out.println("\n--- ALL MEDICINES ---");
        List<Medicine> medicines = database.getAllMedicines();

        if (medicines.isEmpty()) {
            System.out.println("No medicines found in database.");
            return;
        }

        System.out.printf("%-3s %-20s %-12s %-6s %-8s %-15s %-12s %s%n",
                "ID", "Name", "Batch", "Qty", "Min Stock", "Supplier", "Expiry", "Status");
        System.out.println("-------------------------------------------------------------------------------------------");

        for (Medicine med : medicines) {
            System.out.printf("%-3d %-20s %-12s %-6d %-8d %-15s %-12s %s%n",
                    med.getId(),
                    truncate(med.getName(), 18),
                    med.getBatchNumber(),
                    med.getQuantity(),
                    med.getMinStock(),
                    truncate(med.getSupplier(), 13),
                    med.getExpiryDate(),
                    med.getStatus());
        }
    }

    private void checkExpiryAlerts() {
        System.out.println("\n--- EXPIRY ALERTS ---");
        List<Medicine> alerts = database.getExpiryAlerts();

        if (alerts.isEmpty()) {
            System.out.println("✅ No alerts! All medicines are OK.");
            return;
        }

        for (Medicine med : alerts) {
            System.out.println(med.getStatus() + " " + med.getName() +
                    " (Batch: " + med.getBatchNumber() +
                    ", Expiry: " + med.getExpiryDate() + ")");
        }
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number!");
            }
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