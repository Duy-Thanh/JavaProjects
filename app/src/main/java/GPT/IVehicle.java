package GPT;

import java.sql.Connection;
import java.util.ArrayList;

public interface IVehicle {
    // Kết nối đến cơ sở dữ liệu
    public Connection getCon();
    
    // Xoá phương tiện theo hãng xe
    public int deleteByBrand(String brand);
    
    // Thêm thông tin phương tiện
    public int insertVehicle(Vehicle vehicle);
}