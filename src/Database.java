import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private Connection connection;

    // ⚠️ UPDATE THESE VALUES FOR YOUR MySQL SETUP ⚠️
    private final String URL = "jdbc:mysql://localhost:3306/medicine_tracker?useSSL=false&allowPublicKeyRetrieval=true";
    private final String USERNAME = "root";  // Your MySQL username
    private final String PASSWORD = "your_mysql_password";  // ⚠️ CHANGE THIS!

    public Database() {
        connect();
    }

    private void connect() {
        try {
            // Load MySQL JDBC Driver - FIXED VERSION
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish Connection
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("✅ Connected to MySQL database successfully!");

        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL Driver not found! Check JAR file.");
            System.out.println("   Make sure mysql-connector-java-8.0.33.jar is in lib folder");
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
            System.out.println("   Check: 1) MySQL service running 2) Correct password 3) Database exists");
        } catch (Exception e) {
            System.out.println("❌ Unexpected error: " + e.getMessage());
        }
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public void addMedicine(Medicine medicine) {
        if (!isConnected()) {
            System.out.println("❌ Cannot add medicine - no database connection!");
            return;
        }

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

        if (!isConnected()) {
            System.out.println("❌ No database connection!");
            return medicines;
        }

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

        if (!isConnected()) {
            return alerts;
        }

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
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error closing connection: " + e.getMessage());
        }
    }
}