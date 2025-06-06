/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.view;

import application.model.Dokter;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
/*
 *
 * @author XXXTASY
 */
public class DokterView extends JDialog {
    private JTable dokterTable;
    private DefaultTableModel tableModel;
    private JTextField idDokterField;
    private JTextField namaField;
    private JTextField spesialisasiField;
    private JTextField teleponField;
    private JTextField jamMulaiField;
    private JTextField jamSelesaiField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton backButton;
    private JButton clearButton;
    private JTextField searchField;
    private JButton searchButton;
    private JButton resetSearchButton;

    public DokterView(JFrame parentFrame) {
        super(parentFrame, "Manajemen Data Dokter", true);
        setSize(900, 600);
        setLocationRelativeTo(parentFrame);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel topContainerPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        Border titledBorder = BorderFactory.createTitledBorder("Form Data Dokter");
        Border emptyBorder = new EmptyBorder(10, 10, 10, 10);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(titledBorder, emptyBorder));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("ID Dokter:"), gbc);
        gbc.gridx = 1; idDokterField = new JTextField(20); inputPanel.add(idDokterField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("Nama:"), gbc);
        gbc.gridx = 1; namaField = new JTextField(20); inputPanel.add(namaField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("Spesialisasi:"), gbc);
        gbc.gridx = 1; spesialisasiField = new JTextField(20); inputPanel.add(spesialisasiField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("Telepon:"), gbc);
        gbc.gridx = 1; teleponField = new JTextField(20); inputPanel.add(teleponField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("Jam Mulai (HH:MM):"), gbc);
        gbc.gridx = 1; jamMulaiField = new JTextField(10); inputPanel.add(jamMulaiField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("Jam Selesai (HH:MM):"), gbc);
        gbc.gridx = 1; jamSelesaiField = new JTextField(10); inputPanel.add(jamSelesaiField, gbc);
        row++;

        JPanel crudButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = new JButton("Tambah");
        updateButton = new JButton("Perbarui");
        deleteButton = new JButton("Hapus");
        clearButton = new JButton("Clear Form");
        backButton = new JButton("Kembali ke Menu Utama");

        crudButtonPanel.add(addButton);
        crudButtonPanel.add(updateButton);
        crudButtonPanel.add(deleteButton);
        crudButtonPanel.add(clearButton);
        crudButtonPanel.add(backButton);

        JPanel formAndCrudPanel = new JPanel(new BorderLayout());
        formAndCrudPanel.add(inputPanel, BorderLayout.CENTER);
        formAndCrudPanel.add(crudButtonPanel, BorderLayout.SOUTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Pencarian Dokter"));
        searchPanel.add(new JLabel("Cari (ID/Nama/Spesialisasi):"));
        searchField = new JTextField(25);
        searchPanel.add(searchField);
        searchButton = new JButton("Cari");
        searchPanel.add(searchButton);
        resetSearchButton = new JButton("Reset");
        searchPanel.add(resetSearchButton);

        topContainerPanel.add(formAndCrudPanel, BorderLayout.CENTER);
        topContainerPanel.add(searchPanel, BorderLayout.SOUTH);
        add(topContainerPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID Dokter", "Nama", "Spesialisasi", "Telepon", "Jam Mulai", "Jam Selesai"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dokterTable = new JTable(tableModel);
        dokterTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dokterTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(dokterTable);
        add(scrollPane, BorderLayout.CENTER);

        dokterTable.getSelectionModel().addListSelectionListener(e -> {
            System.out.println("Selection event triggered. ValueIsAdjusting: " + e.getValueIsAdjusting() + ", Selected Row: " + dokterTable.getSelectedRow());

            if (!e.getValueIsAdjusting()) {
                if (dokterTable.getSelectedRow() != -1) {
                    int selectedRow = dokterTable.getSelectedRow();
                    System.out.println("Processing selected row: " + selectedRow);
                    try {
                        idDokterField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                        namaField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                        spesialisasiField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                        teleponField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                        jamMulaiField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                        jamSelesaiField.setText(tableModel.getValueAt(selectedRow, 5).toString());
                        idDokterField.setEditable(false);
                        System.out.println("Form fields updated and ID locked.");
                    } catch (Exception ex) {
                        System.err.println("General error while filling form: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println("No row selected after adjustment, clearing form.");
                    clearForm();
                    idDokterField.setEditable(true);
                }
            }
        });
        System.out.println("DokterView UI initialized and listener added.");
    }

    public String getIdDokterField() { return idDokterField.getText(); }
    public String getNamaField() { return namaField.getText(); }
    public String getSpesialisasiField() { return spesialisasiField.getText(); }
    public String getTeleponField() { return teleponField.getText(); }
    public String getJamMulaiField() { return jamMulaiField.getText(); }
    public String getJamSelesaiField() { return jamSelesaiField.getText(); }

    public void addAddButtonListener(ActionListener listener) { addButton.addActionListener(listener); }
    public void addUpdateButtonListener(ActionListener listener) { updateButton.addActionListener(listener); }
    public void addDeleteButtonListener(ActionListener listener) { deleteButton.addActionListener(listener); }
    public void addBackButtonListener(ActionListener listener) { backButton.addActionListener(listener); }
    public void addClearButtonListener(ActionListener listener) { clearButton.addActionListener(listener); }

    public String getSearchKeyword() {
        return searchField.getText().trim();
    }
    
    public JTextField getSearchField() {
        return searchField;
    }

    public void addSearchButtonListener(ActionListener listener) {
        searchButton.addActionListener(listener);
    }

    public void addResetSearchButtonListener(ActionListener listener) {
        resetSearchButton.addActionListener(listener);
    }

    public JTable getDokterTable() { return dokterTable; }
    public DefaultTableModel getTableModel() { return tableModel; }

    public void clearForm() {
        idDokterField.setText("");
        namaField.setText("");
        spesialisasiField.setText("");
        teleponField.setText("");
        jamMulaiField.setText("");
        jamSelesaiField.setText("");
        idDokterField.setEditable(true);
        dokterTable.clearSelection();
    }

    public Dokter getDokterFromForm() {
        try {
            String id = idDokterField.getText().trim();
            String nama = namaField.getText().trim();
            String spesialisasi = spesialisasiField.getText().trim();
            String telepon = teleponField.getText().trim();
            LocalTime jamMulai = LocalTime.parse(jamMulaiField.getText().trim(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime jamSelesai = LocalTime.parse(jamSelesaiField.getText().trim(), DateTimeFormatter.ofPattern("HH:mm"));

            if (id.isEmpty() || nama.isEmpty() || spesialisasi.isEmpty() || telepon.isEmpty() || jamMulaiField.getText().isEmpty() || jamSelesaiField.getText().isEmpty()) {
                showMessage("Semua field harus diisi.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            if (jamMulai.isAfter(jamSelesai)) {
                showMessage("Jam MULAI praktik TIDAK BOLEH SETELAH jam SELESAI praktik dan/atau jam SELESAI praktik TIDAK BOLEH SEBELUM jam MULAI praktik", "Input Error", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            return new Dokter(id, nama, spesialisasi, telepon, jamMulai, jamSelesai);
        } catch (DateTimeParseException e) {
            showMessage("Format waktu harus HH:MM (contoh: 08:00).", "Format Waktu Salah", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (Exception e) {
            showMessage("Terjadi kesalahan pada input data: " + e.getMessage(), "Error Validasi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
}