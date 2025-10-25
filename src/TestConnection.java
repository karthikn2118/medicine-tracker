import java.sql.*;
public class TestConnection {
    public static void main(String[] args) {
        String[] passwords = {"", "root", "", "1234"};
        for (String pwd : passwords) {
            try {
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/medicine_tracker", "root", pwd);
                System.out.println("✅ Works with password: '" + pwd + "'");
                conn.close();
                break;
            } catch (Exception e) {
                System.out.println("❌ Failed: '" + pwd + "'");
            }
        }
    }
}