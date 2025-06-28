/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hrmanagementsystemgui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class HRManagementSystemGUI extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hr";
    private static final String USER = "root";
    private static final String PASS = "Voldemort@123";

    private JTextField nameField;
    private JTextField positionField;
    private JTextField salaryField;
    private JTextArea displayArea;

    public HRManagementSystemGUI() {
        setTitle("HR Management System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Name:"), gbc);

        nameField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(nameField, gbc);

        
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Position:"), gbc);

        positionField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(positionField, gbc);

        
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Salary:"), gbc);

        salaryField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(salaryField, gbc);

        
        JButton addButton = new JButton("Add Employee");
        addButton.setPreferredSize(new Dimension(150, 30));
        addButton.addActionListener(new AddEmployeeAction());
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(addButton, gbc);

        JButton updateButton = new JButton("Update Employee");
        updateButton.setPreferredSize(new Dimension(150, 30));
        updateButton.addActionListener(new UpdateEmployeeAction());
        gbc.gridx = 1;
        inputPanel.add(updateButton, gbc);

        JButton deleteButton = new JButton("Delete Employee");
        deleteButton.setPreferredSize(new Dimension(150, 30));
        deleteButton.addActionListener(new DeleteEmployeeAction());
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(deleteButton, gbc);

        JButton listButton = new JButton("List Employees");
        listButton.setPreferredSize(new Dimension(150, 30));
        listButton.addActionListener(new ListEmployeesAction());
        gbc.gridx = 1;
        inputPanel.add(listButton, gbc);

        
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private class AddEmployeeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            String position = positionField.getText();
            double salary;
            try {
                salary = Double.parseDouble(salaryField.getText());
                addEmployee(name, position, salary);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(HRManagementSystemGUI.this, "Please enter a valid salary.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class UpdateEmployeeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            String position = positionField.getText();
            double salary;
            try {
                salary = Double.parseDouble(salaryField.getText());
               
                updateEmployee(1, name, position, salary); 
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(HRManagementSystemGUI.this, "Please enter a valid salary.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class DeleteEmployeeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            deleteEmployee(1);
        }
    }

    private class ListEmployeesAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            listEmployees();
        }
    }

    public void addEmployee(String name, String position, double salary) {
        String sql = "INSERT INTO employees (name, position, salary) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, position);
            pstmt.setDouble(3, salary);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Employee added: " + name);
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listEmployees() {
        String sql = "SELECT * FROM employees";
        StringBuilder result = new StringBuilder();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                result.append("ID: ").append(rs.getInt("id"))
                      .append(", Name: ").append(rs.getString("name"))
                      .append(", Position: ").append(rs.getString("position"))
                      .append(", Salary: ").append(rs.getDouble("salary"))
                      .append("\n");
            }
            displayArea.setText(result.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEmployee(int id, String name, String position, double salary) {
        String sql = "UPDATE employees SET name = ?, position = ?, salary = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, position);
            pstmt.setDouble(3, salary);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
             JOptionPane.showMessageDialog(this, "Employee updated: " + name);
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEmployee(int id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Employee deleted with ID: " + id);
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        nameField.setText("");
        positionField.setText("");
        salaryField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HRManagementSystemGUI gui = new HRManagementSystemGUI();
            gui.setVisible(true);
        });
    }
}
            
        
