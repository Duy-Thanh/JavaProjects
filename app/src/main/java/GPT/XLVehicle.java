package GPT;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class XLVehicle implements IVehicle {
    private Connection conn = null;
    
    @Override
    public Connection getCon() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://DESKTOP-BCLRDMB:1433;databaseName=VehicleManager;encrypt=true;trustServerCertificate=true";
            String user = "sa";
            String password = "Lolvaruslol2022@!@@"; // Thay đổi password thật
            
            // Kiểm tra kết nối trước khi trả về
            conn = DriverManager.getConnection(url, user, password);
            if (conn == null) {
                throw new SQLException("Không thể tạo kết nối!");
            }
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối: " + e.getMessage());
            return null;
        }
    }

    @Override
    public int deleteByBrand(String brand) {
        String sql = "DELETE FROM Vehicle WHERE Brand = ?";
        try (Connection conn = getCon();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, brand);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int insertVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO Vehicle (VehicleID, Type, Brand, Model, Year, Detail) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getCon();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, vehicle.getVehicleID());
            pstmt.setString(2, vehicle.getType());
            pstmt.setString(3, vehicle.getBrand());
            pstmt.setString(4, vehicle.getModel());
            pstmt.setInt(5, vehicle.getYear());
            pstmt.setString(6, vehicle.getDetail());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
} 