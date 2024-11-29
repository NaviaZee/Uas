/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uas;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PelangganFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public PelangganFrame() {
        setTitle("CRUD Data Pelanggan");
        setSize(600, 400);
        setLayout(new BorderLayout());

        // Tabel
        String[] columnNames = {"ID Pelanggan", "Nama", "NIK", "No Telp", "Alamat"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel Form
        JPanel formPanel = new JPanel(new GridLayout(6, 2));
        JTextField tfNama = new JTextField();
        JTextField tfNik = new JTextField();
        JTextField tfNotelp = new JTextField();
        JTextField tfAlamat = new JTextField();
        JButton btnAdd = new JButton("Tambah");
        JButton btnDelete = new JButton("Hapus");

        formPanel.add(new JLabel("Nama:"));
        formPanel.add(tfNama);
        formPanel.add(new JLabel("NIK:"));
        formPanel.add(tfNik);
        formPanel.add(new JLabel("No Telp:"));
        formPanel.add(tfNotelp);
        formPanel.add(new JLabel("Alamat:"));
        formPanel.add(tfAlamat);
        formPanel.add(btnAdd);
        formPanel.add(btnDelete);
        add(formPanel, BorderLayout.SOUTH);

        // Event Handlers
        btnAdd.addActionListener(e -> {
            try (Connection conn = KoneksiMySQL.getConnection()) {
                String sql = "INSERT INTO data_pelanggan (nama, nik, notelp, alamat) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, tfNama.getText());
                ps.setString(2, tfNik.getText());
                ps.setString(3, tfNotelp.getText());
                ps.setString(4, tfAlamat.getText());
                ps.executeUpdate();
                loadData();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
                try (Connection conn = KoneksiMySQL.getConnection()) {
                    String sql = "DELETE FROM data_pelanggan WHERE idpelanggan = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    loadData();
                } catch (SQLException ex) {
                    ex.printStackTrace();
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
            ResultSet rs = stmt.executeQuery("SELECT * FROM data_pelanggan");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("idpelanggan"),
                        rs.getString("nama"),
                        rs.getString("nik"),
                        rs.getString("notelp"),
                        rs.getString("alamat")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
