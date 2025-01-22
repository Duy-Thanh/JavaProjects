CREATE DATABASE VehicleManager;
USE VehicleManager;

-- Tạo bảng Vehicle
CREATE TABLE Vehicle (
    VehicleID VARCHAR(20) PRIMARY KEY,
    Type VARCHAR(50) NOT NULL,
    Brand VARCHAR(50) NOT NULL,
    Model VARCHAR(50),
    Year INT NOT NULL,
    Detail NVARCHAR(MAX)  -- Thay TEXT bằng NVARCHAR(MAX) để hỗ trợ Unicode tốt hơn trong MSSQL
);

-- Chèn 5 bản ghi mẫu
INSERT INTO Vehicle (VehicleID, Type, Brand, Model, Year, Detail) 
VALUES 
    ('VH001', N'Car', N'Toyota', N'Camry', 2022, N'Sedan 4 cửa màu đen'),
    ('VH002', N'Motorcycle', N'Honda', N'Wave', 2021, N'Xe máy số màu xanh'),
    ('VH003', N'Car', N'Honda', N'CR-V', 2023, N'SUV 7 chỗ màu trắng'),
    ('VH004', N'Motorcycle', N'Yamaha', N'Exciter', 2022, N'Xe côn tay màu đỏ'),
    ('VH005', N'Car', N'Ford', N'Ranger', 2023, N'Bán tải màu xám');