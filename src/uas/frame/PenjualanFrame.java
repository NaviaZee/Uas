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

public class PenjualanFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public PenjualanFrame() {
        setTitle("CRUD Data Penjualan");
        setSize(800, 400);
        setLayout(new BorderLayout());

        // Tabel
        String[] columnNames = {"ID Penjualan", "ID Pelanggan", "ID Mobil", "Total Biaya"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel Form
        JPanel formPanel = new JPanel(new GridLayout(5, 2));
        JTextField tfIdPelanggan = new JTextField();
        JTextField tfIdMobil = new JTextField();
        JTextField tfTotalBiaya = new JTextField();
        JButton btnAdd = new JButton("Tambah");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Hapus");

        formPanel.add(new JLabel("ID Pelanggan:"));
        formPanel.add(tfIdPelanggan);
        formPanel.add(new JLabel("ID Mobil:"));
        formPanel.add(tfIdMobil);
        formPanel.add(new JLabel("Total Biaya:"));
        formPanel.add(tfTotalBiaya);
        formPanel.add(btnAdd);
        formPanel.add(btnUpdate);
        formPanel.add(btnDelete);
        add(formPanel, BorderLayout.SOUTH);

        // Event Handlers
        btnAdd.addActionListener(e -> {
            try (Connection conn = KoneksiMySQL.getConnection()) {
                String sql = "INSERT INTO data_penjualan (idpelanggan, idmobil, totalbiaya) VALUES (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(tfIdPelanggan.getText()));
                ps.setInt(2, Integer.parseInt(tfIdMobil.getText()));
                ps.setDouble(3, Double.parseDouble(tfTotalBiaya.getText()));
                ps.executeUpdate();
                loadData();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal menambahkan data. Pastikan ID Pelanggan dan ID Mobil valid.");
            }
        });

        btnUpdate.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
                try (Connection conn = KoneksiMySQL.getConnection()) {
                    String sql = "UPDATE data_penjualan SET idpelanggan = ?, idmobil = ?, totalbiaya = ? WHERE idpenjualan = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, Integer.parseInt(tfIdPelanggan.getText()));
                    ps.setInt(2, Integer.parseInt(tfIdMobil.getText()));
                    ps.setDouble(3, Double.parseDouble(tfTotalBiaya.getText()));
                    ps.setInt(4, id);
                    ps.executeUpdate();
                    loadData();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Gagal memperbarui data. Pastikan ID Pelanggan dan ID Mobil valid.");
                }
            }
        });

        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
                try (Connection conn = KoneksiMySQL.getConnection()) {
                    String sql = "DELETE FROM data_penjualan WHERE idpenjualan = ?";
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
                    tfIdPelanggan.setText(model.getValueAt(selectedRow, 1).toString());
                    tfIdMobil.setText(model.getValueAt(selectedRow, 2).toString());
                    tfTotalBiaya.setText(model.getValueAt(selectedRow, 3).toString());
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
            ResultSet rs = stmt.executeQuery("SELECT * FROM data_penjualan");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("idpenjualan"),
                        rs.getInt("idpelanggan"),
                        rs.getInt("idmobil"),
                        rs.getDouble("totalbiaya")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

