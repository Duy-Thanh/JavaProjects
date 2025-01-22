package GPT;

public class Vehicle {
    private String vehicleID;
    private String type;
    private String brand;
    private String model;
    private int year;
    private String detail;

    // Constructor
    public Vehicle(String vehicleID, String type, String brand, String model, int year, String detail) {
        this.vehicleID = vehicleID;
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.detail = detail;
    }

    // Constructor không tham số
    public Vehicle() {
        this.vehicleID = "";
        this.type = "";
        this.brand = "";
        this.model = "";
        this.year = 0;
        this.detail = "";
    }

    // Getters and Setters
    public String getVehicleID() { return vehicleID; }
    public void setVehicleID(String vehicleID) { this.vehicleID = vehicleID; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }

    // Override phương thức toString()
    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleID='" + vehicleID + '\'' +
                ", type='" + type + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", detail='" + detail + '\'' +
                '}';
    }
}
