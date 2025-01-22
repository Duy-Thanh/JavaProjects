package GPT;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableStringConverter;
import javax.swing.table.DefaultTableModel;

public class GUI_VehicleManager extends JFrame {
    private JTextField txtVehicleID, txtType, txtModel, txtYear, txtDetail;
    private JComboBox<String> cbBrand;
    private JTable tblVehicle;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnDelete;
    private XLVehicle xlVehicle;
    private JLabel lblTitle;
    private JPanel headerPanel;
    private JPanel statusPanel;
    private JLabel lblStatus;
    private JLabel lblCount;
    private JTextField txtSearch;
    private JButton btnSearch, btnClear, btnRefresh;

    public GUI_VehicleManager() {
        xlVehicle = new XLVehicle();
        setTitle("Vehicle Management System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        getContentPane().setBackground(new Color(240, 240, 240));
        
        headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        lblTitle = new JLabel("Vehicle Management System");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        searchPanel.setBackground(new Color(240, 240, 240));
        
        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtSearch = new JTextField(25);
        styleTextField(txtSearch);
        
        btnClear = new JButton("Xóa form");
        btnRefresh = new JButton("Làm mới");
        
        styleButton(btnClear);
        styleButton(btnRefresh);
        
        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnClear);
        searchPanel.add(btnRefresh);

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { searchVehicles(); }
            public void removeUpdate(DocumentEvent e) { searchVehicles(); }
            public void insertUpdate(DocumentEvent e) { searchVehicles(); }
        });

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 240, 240));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(createFancyBorder("Vehicle Information"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.weightx = 1.0;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 12);
        
        JLabel[] labels = {
            new JLabel("Vehicle ID:"),
            new JLabel("Type:"),
            new JLabel("Brand:"),
            new JLabel("Model:"),
            new JLabel("Year:"),
            new JLabel("Detail:")
        };
        
        for (JLabel label : labels) {
            label.setFont(labelFont);
            label.setForeground(new Color(60, 60, 60));
        }

        txtVehicleID = styleTextField(new JTextField(20));
        txtType = styleTextField(new JTextField(20));
        txtModel = styleTextField(new JTextField(20));
        txtYear = styleTextField(new JTextField(20));
        txtDetail = styleTextField(new JTextField(20));
        
        cbBrand = new JComboBox<>(new String[]{"Honda", "Toyota", "Ford", "Yamaha", "Kia"});
        styleComboBox(cbBrand);

        addComponent(inputPanel, labels[0], 0, 0, gbc);
        addComponent(inputPanel, txtVehicleID, 1, 0, gbc);
        addComponent(inputPanel, labels[1], 0, 1, gbc);
        addComponent(inputPanel, txtType, 1, 1, gbc);
        addComponent(inputPanel, labels[2], 0, 2, gbc);
        addComponent(inputPanel, cbBrand, 1, 2, gbc);
        addComponent(inputPanel, labels[3], 0, 3, gbc);
        addComponent(inputPanel, txtModel, 1, 3, gbc);
        addComponent(inputPanel, labels[4], 0, 4, gbc);
        addComponent(inputPanel, txtYear, 1, 4, gbc);
        addComponent(inputPanel, labels[5], 0, 5, gbc);
        addComponent(inputPanel, txtDetail, 1, 5, gbc);

        // Panel cho các nút thao tác
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        actionPanel.setBackground(Color.WHITE);

        btnAdd = new JButton("Thêm phương tiện");
        btnDelete = new JButton("Xóa phương tiện");
        
        // Style cho các nút
        styleActionButton(btnAdd);
        styleActionButton(btnDelete);
        
        actionPanel.add(btnAdd);
        actionPanel.add(btnDelete);

        // Thêm actionPanel vào cuối inputPanel
        gbc.gridx = 0;
        gbc.gridy = 7; // sau các trường nhập liệu
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(actionPanel, gbc);

        String[] columns = {"VehicleID", "Type", "Brand", "Model", "Year", "Detail"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblVehicle = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblVehicle);
        scrollPane.setPreferredSize(new Dimension(1150, 300));
        
        tblVehicle.setRowHeight(30);
        tblVehicle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblVehicle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblVehicle.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        JTableHeader header = tblVehicle.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(new Color(60, 60, 60));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        
        tblVehicle.getColumnModel().getColumn(0).setPreferredWidth(100);
        tblVehicle.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblVehicle.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblVehicle.getColumnModel().getColumn(3).setPreferredWidth(150);
        tblVehicle.getColumnModel().getColumn(4).setPreferredWidth(80);
        tblVehicle.getColumnModel().getColumn(5).setPreferredWidth(300);

        tblVehicle.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 250));
                }
                return c;
            }
        });

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(),
            "Vehicle List",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        tablePanel.add(scrollPane);

        mainPanel.add(tablePanel, BorderLayout.SOUTH);

        tblVehicle.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tblVehicle.getSelectedRow();
                if (selectedRow >= 0) {
                    String vehicleId = tableModel.getValueAt(selectedRow, 0).toString();
                    String type = tableModel.getValueAt(selectedRow, 1).toString();
                    String brand = tableModel.getValueAt(selectedRow, 2).toString();
                    String model = tableModel.getValueAt(selectedRow, 3).toString();
                    String year = tableModel.getValueAt(selectedRow, 4).toString();
                    String detail = tableModel.getValueAt(selectedRow, 5).toString();

                    txtVehicleID.setText(vehicleId);
                    txtType.setText(type);
                    cbBrand.setSelectedItem(brand);
                    txtModel.setText(model);
                    txtYear.setText(year);
                    txtDetail.setText(detail);
                }
            }
        });

        statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(new Color(245, 245, 245));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        lblStatus = new JLabel("Ready");
        lblCount = new JLabel("Total vehicles: 0");
        statusPanel.add(lblStatus, BorderLayout.WEST);
        statusPanel.add(lblCount, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        loadData();

        btnClear.addActionListener(e -> {
            clearForm();
            txtSearch.setText("");
            tblVehicle.setRowSorter(null);
            loadData();
        });
        
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            tblVehicle.setRowSorter(null);
            loadData();
            updateStatus("Data refreshed");
        });

        btnAdd.addActionListener(e -> {
            try {
                if (txtVehicleID.getText().trim().isEmpty()) {
                    showError("Vehicle ID không được để trống!");
                    txtVehicleID.requestFocus();
                    return;
                }

                if (txtType.getText().trim().isEmpty()) {
                    showError("Type không được để trống!");
                    txtType.requestFocus();
                    return;
                }

                if (txtModel.getText().trim().isEmpty()) {
                    showError("Model không được để trống!");
                    txtModel.requestFocus();
                    return;
                }

                int year;
                try {
                    year = Integer.parseInt(txtYear.getText().trim());
                    if (year < 1900 || year > 2024) {
                        showError("Năm phải từ 1900 đến 2024!");
                        txtYear.requestFocus();
                        return;
                    }
                } catch (NumberFormatException ex) {
                    showError("Year phải là số!");
                    txtYear.requestFocus();
                    return;
                }

                Vehicle vehicle = new Vehicle(
                    txtVehicleID.getText().trim(),
                    txtType.getText().trim(),
                    cbBrand.getSelectedItem().toString(),
                    txtModel.getText().trim(),
                    year,
                    txtDetail.getText().trim()
                );
                
                int result = xlVehicle.insertVehicle(vehicle);
                if (result > 0) {
                    showInfo("Thêm phương tiện thành công!");
                    loadData();
                    clearForm();
                }
            } catch (Exception ex) {
                if (ex.getMessage().contains("PRIMARY KEY")) {
                    showError("Vehicle ID đã tồn tại!");
                    txtVehicleID.requestFocus();
                } else {
                    showError("Lỗi: " + ex.getMessage());
                }
            }
        });

        btnDelete.addActionListener(e -> {
            String brand = cbBrand.getSelectedItem().toString();
            
            // Xác nhận xóa
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa tất cả phương tiện của hãng " + brand + "?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    int result = xlVehicle.deleteByBrand(brand);
                    if (result > 0) {
                        showInfo("Đã xóa " + result + " xe của hãng " + brand);
                        loadData();
                        clearForm();
                    } else {
                        showInfo("Không có xe nào của hãng " + brand + " để xóa!");
                    }
                } catch (Exception ex) {
                    showError("Lỗi khi xóa: " + ex.getMessage());
                }
            }
        });

        pack();
    }

    private void addComponent(JPanel panel, JComponent comp, int x, int y, GridBagConstraints gbc) {
        gbc.gridx = x;
        gbc.gridy = y;
        if (comp instanceof JLabel) {
            gbc.weightx = 0.2;
            gbc.anchor = GridBagConstraints.EAST;
        } else {
            gbc.weightx = 0.8;
            gbc.anchor = GridBagConstraints.WEST;
        }
        panel.add(comp, gbc);
    }

    private JTextField styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(400, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(2, 10, 2, 10)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return field;
    }

    private void styleComboBox(JComboBox<String> box) {
        box.setPreferredSize(new Dimension(400, 35));
        box.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        box.setBackground(Color.WHITE);
        ((JComponent) box.getRenderer()).setOpaque(true);
    }

    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(200, 45));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(41, 128, 185));
        button.setBorder(new EmptyBorder(5, 15, 5, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(52, 152, 219));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(41, 128, 185));
            }
        });
    }

    private void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.getTableHeader().setForeground(new Color(60, 60, 60));
        table.setSelectionBackground(new Color(232, 241, 249));
        table.setSelectionForeground(new Color(60, 60, 60));
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setFocusable(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void loadData() {
        try {
            Connection conn = xlVehicle.getCon();
            if (conn != null) {
                Statement stmt = conn.createStatement();
                String sql = "SELECT * FROM Vehicle";
                ResultSet rs = stmt.executeQuery(sql);

                tableModel.setRowCount(0);

                while (rs.next()) {
                    Object[] row = {
                        rs.getString("VehicleID"),
                        rs.getString("Type"),
                        rs.getString("Brand"),
                        rs.getString("Model"),
                        rs.getInt("Year"),
                        rs.getString("Detail")
                    };
                    tableModel.addRow(row);
                }

                rs.close();
                stmt.close();
                conn.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi load dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
        updateStatus("Data loaded successfully");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Lỗi",
            JOptionPane.ERROR_MESSAGE
        );
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Thông báo",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void clearForm() {
        txtVehicleID.setText("");
        txtType.setText("");
        txtModel.setText("");
        txtYear.setText("");
        txtDetail.setText("");
        cbBrand.setSelectedIndex(0);
        txtVehicleID.requestFocus();
        updateStatus("Form cleared");
    }

    private Border createFancyBorder(String title) {
        Border line = BorderFactory.createLineBorder(new Color(200, 200, 200));
        Border empty = BorderFactory.createEmptyBorder(5, 10, 5, 10);
        Border compound = BorderFactory.createCompoundBorder(line, empty);
        TitledBorder titled = BorderFactory.createTitledBorder(
            compound, title, TitledBorder.LEFT, TitledBorder.TOP);
        titled.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        return titled;
    }

    private void displayVehicleDetails(int row) {
        txtVehicleID.setText(tableModel.getValueAt(row, 0).toString());
        txtType.setText(tableModel.getValueAt(row, 1).toString());
        cbBrand.setSelectedItem(tableModel.getValueAt(row, 2).toString());
        txtModel.setText(tableModel.getValueAt(row, 3).toString());
        txtYear.setText(tableModel.getValueAt(row, 4).toString());
        txtDetail.setText(tableModel.getValueAt(row, 5).toString());
    }

    private void searchVehicles() {
        String searchText = txtSearch.getText().toLowerCase().trim();
        
        if (searchText.isEmpty()) {
            tblVehicle.setRowSorter(null);
            updateStatus("Showing all vehicles");
            return;
        }

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        
        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                for (int i = 0; i < entry.getValueCount(); i++) {
                    if (entry.getStringValue(i).toLowerCase().contains(searchText)) {
                        return true;
                    }
                }
                return false;
            }
        });
        
        tblVehicle.setRowSorter(sorter);
        updateStatus("Found " + tblVehicle.getRowCount() + " vehicles");
    }

    private void updateStatus(String message) {
        lblStatus.setText(message);
        lblCount.setText("Total vehicles: " + 
            (tblVehicle.getRowSorter() == null ? 
                tableModel.getRowCount() : 
                tblVehicle.getRowCount())
        );
    }

    private void styleActionButton(JButton button) {
        button.setPreferredSize(new Dimension(150, 35));
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        
        if (button == btnAdd) {
            button.setBackground(new Color(41, 128, 185)); // Màu xanh cho nút Thêm
        } else {
            button.setBackground(new Color(231, 76, 60)); // Màu đỏ cho nút Xóa
        }
        
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hiệu ứng hover
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (button == btnAdd) {
                    button.setBackground(new Color(52, 152, 219)); // Màu xanh nhạt
                } else {
                    button.setBackground(new Color(242, 96, 80)); // Màu đỏ nhạt
                }
            }
            public void mouseExited(MouseEvent e) {
                if (button == btnAdd) {
                    button.setBackground(new Color(41, 128, 185)); // Màu xanh gốc
                } else {
                    button.setBackground(new Color(231, 76, 60)); // Màu đỏ gốc
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GUI_VehicleManager().setVisible(true);
        });
    }
} 