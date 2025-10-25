import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private Connection connection;

    // ⚠️ CHANGE THIS PASSWORD TO YOUR MYSQL PASSWORD ⚠️
    private final String URL = "jdbc:mysql://localhost:3306/medicine_tracker";
    private final String USERNAME = "root";
    private final String PASSWORD = "your_mysql_password_here"; // CHANGE THIS!

    public Database() {
        connect();
    }

    private void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("✅ Connected to MySQL database successfully!");
        } catch (Exception e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
        }
    }

    public void addMedicine(Medicine medicine) {
        String sql = "INSERT INTO medicines (name, batch_number, quantity, min_stock, supplier, expiry_date, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, medicine.getName());
            pstmt.setString(2, medicine.getBatchNumber());
            pstmt.setInt(3, medicine.getQuantity());
            pstmt.setInt(4, medicine.getMinStock());
            pstmt.setString(5, medicine.getSupplier());
            pstmt.setString(6, medicine.getExpiryDate());
            pstmt.setString(7, medicine.getStatus());

            pstmt.executeUpdate();
            System.out.println("✅ Medicine added successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error adding medicine: " + e.getMessage());
        }
    }

    public List<Medicine> getAllMedicines() {
        List<Medicine> medicines = new ArrayList<>();
        String sql = "SELECT * FROM medicines ORDER BY expiry_date";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Medicine medicine = new Medicine(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("batch_number"),
                        rs.getInt("quantity"),
                        rs.getInt("min_stock"),
                        rs.getString("supplier"),
                        rs.getString("expiry_date"),
                        rs.getString("status")
                );
                medicines.add(medicine);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching medicines: " + e.getMessage());
        }
        return medicines;
    }

    public List<Medicine> getExpiryAlerts() {
        List<Medicine> alerts = new ArrayList<>();
        String sql = "SELECT * FROM medicines WHERE status IN ('❌ EXPIRED', '⚠️ EXPIRING')";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Medicine medicine = new Medicine(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("batch_number"),
                        rs.getInt("quantity"),
                        rs.getInt("min_stock"),
                        rs.getString("supplier"),
                        rs.getString("expiry_date"),
                        rs.getString("status")
                );
                alerts.add(medicine);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching alerts: " + e.getMessage());
        }
        return alerts;
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("✅ Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error closing connection: " + e.getMessage());
        }
    }
}