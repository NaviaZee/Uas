/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uas;

/**
 *
 * @author navia
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MobilFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public MobilFrame() {
        setTitle("CRUD Data Mobil");
        setSize(600, 400);
        setLayout(new BorderLayout());

        // Tabel
        String[] columnNames = {"ID Mobil", "Merk", "Tahun", "Harga"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel Form
        JPanel formPanel = new JPanel(new GridLayout(5, 2));
        JTextField tfMerk = new JTextField();
        JTextField tfTahun = new JTextField();
        JTextField tfHarga = new JTextField();
        JButton btnAdd = new JButton("Tambah");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Hapus");

        formPanel.add(new JLabel("Merk:"));
        formPanel.add(tfMerk);
        formPanel.add(new JLabel("Tahun:"));
        formPanel.add(tfTahun);
        formPanel.add(new JLabel("Harga:"));
        formPanel.add(tfHarga);
        formPanel.add(btnAdd);
        formPanel.add(btnUpdate);
        formPanel.add(btnDelete);
        add(formPanel, BorderLayout.SOUTH);

        // Event Handlers
        btnAdd.addActionListener(e -> {
            try (Connection conn = KoneksiMySQL.getConnection()) {
                String sql = "INSERT INTO data_mobil (merk, tahun, harga) VALUES (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, tfMerk.getText());
                ps.setInt(2, Integer.parseInt(tfTahun.getText()));
                ps.setDouble(3, Double.parseDouble(tfHarga.getText()));
                ps.executeUpdate();
                loadData();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        btnUpdate.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
                try (Connection conn = KoneksiMySQL.getConnection()) {
                    String sql = "UPDATE data_mobil SET merk = ?, tahun = ?, harga = ? WHERE idmobil = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, tfMerk.getText());
                    ps.setInt(2, Integer.parseInt(tfTahun.getText()));
                    ps.setDouble(3, Double.parseDouble(tfHarga.getText()));
                    ps.setInt(4, id);
                    ps.executeUpdate();
                    loadData();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
                try (Connection conn = KoneksiMySQL.getConnection()) {
                    String sql = "DELETE FROM data_mobil WHERE idmobil = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    loadData();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    tfMerk.setText(model.getValueAt(selectedRow, 1).toString());
                    tfTahun.setText(model.getValueAt(selectedRow, 2).toString());
                    tfHarga.setText(model.getValueAt(selectedRow, 3).toString());
                }
            }
        });

        loadData();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void loadData() {
        model.setRowCount(0);
        try (Connection conn = KoneksiMySQL.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM data_mobil");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("idmobil"),
                        rs.getString("merk"),
                        rs.getInt("tahun"),
                        rs.getDouble("harga")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

