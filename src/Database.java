import java.util.ArrayList;
import java.util.List;

public class Database {
    private ArrayList<Medicine> medicines;
    private int nextId = 1;

    public Database() {
        medicines = new ArrayList<>();
        addSampleData();
        System.out.println("✅ Medicine Tracker Ready (Local Storage)");
        System.out.println("💾 No MySQL required - Using local memory storage");
    }

    private void addSampleData() {
        // Use the 6-parameter constructor (without ID and status - they will be auto-generated)
        medicines.add(new Medicine("Paracetamol 500mg", "BATCH001", 100, 20, "Pharma Co", "2024-12-31"));
        medicines.add(new Medicine("Amoxicillin 250mg", "BATCH002", 15, 25, "Med Supplies", "2024-01-15"));
        medicines.add(new Medicine("Vitamin C 100mg", "BATCH003", 30, 10, "Health Pharma", "2024-03-20"));
        medicines.add(new Medicine("Aspirin 75mg", "BATCH004", 45, 15, "Cardio Meds", "2024-11-30"));

        System.out.println("📊 Loaded " + medicines.size() + " sample medicines");
    }

    public void addMedicine(Medicine medicine) {
        // Use the 6-parameter constructor (ID will be auto-assigned)
        Medicine newMedicine = new Medicine(
                medicine.getName(),
                medicine.getBatchNumber(),
                medicine.getQuantity(),
                medicine.getMinStock(),
                medicine.getSupplier(),
                medicine.getExpiryDate());

        medicines.add(newMedicine);
        System.out.println("✅ Medicine added successfully!");
    }

    public List<Medicine> getAllMedicines() {
        return new ArrayList<>(medicines);
    }

    public List<Medicine> getExpiryAlerts() {
        List<Medicine> alerts = new ArrayList<>();
        for (Medicine med : medicines) {
            if (med.getStatus().contains("❌") || med.getStatus().contains("⚠️")) {
                alerts.add(med);
            }
        }
        return alerts;
    }

    public void close() {
        System.out.println("💾 Data stored in memory");
        System.out.println("📊 Total medicines in inventory: " + medicines.size());
    }
}