/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.view;

import application.model.Pasien;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Properties;
/*
 *
 * @author XXXTASY
 */
public class PasienView extends JDialog {
    private JTable pasienTable;
    private DefaultTableModel tableModel;
    private JTextField idPasienField;
    private JTextField namaField;
    private JDatePickerImpl tanggalLahirPicker;
    private JTextField alamatField;
    private JTextField teleponField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton backButton;
    private JButton clearButton;
    private JTextField searchField;
    private JButton searchButton;
    private JButton resetSearchButton;

    public PasienView(JFrame parentFrame) {
        super(parentFrame, "Manajemen Data Pasien", true);
        setSize(900, 600);
        setLocationRelativeTo(parentFrame);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel topContainerPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        Border titledBorder = BorderFactory.createTitledBorder("Form Data Pasien");
        Border emptyBorder = new EmptyBorder(10, 10, 10, 10);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(titledBorder, emptyBorder));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("ID Pasien:"), gbc);
        gbc.gridx = 1; idPasienField = new JTextField(20); inputPanel.add(idPasienField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("Nama:"), gbc);
        gbc.gridx = 1; namaField = new JTextField(20); inputPanel.add(namaField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("Tanggal Lahir:"), gbc);
        gbc.gridx = 1;
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        tanggalLahirPicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        inputPanel.add(tanggalLahirPicker, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("Alamat:"), gbc);
        gbc.gridx = 1; alamatField = new JTextField(20); inputPanel.add(alamatField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("Telepon:"), gbc);
        gbc.gridx = 1; teleponField = new JTextField(20); inputPanel.add(teleponField, gbc);
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
        searchPanel.setBorder(BorderFactory.createTitledBorder("Pencarian Pasien"));
        searchPanel.add(new JLabel("Cari (ID/Nama):"));
        searchField = new JTextField(25);
        searchPanel.add(searchField);
        searchButton = new JButton("Cari");
        searchPanel.add(searchButton);
        resetSearchButton = new JButton("Reset");
        searchPanel.add(resetSearchButton);

        topContainerPanel.add(formAndCrudPanel, BorderLayout.CENTER);
        topContainerPanel.add(searchPanel, BorderLayout.SOUTH);
        add(topContainerPanel, BorderLayout.NORTH);


        String[] columnNames = {"ID Pasien", "Nama", "Tanggal Lahir", "Alamat", "Telepon"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        pasienTable = new JTable(tableModel);
        pasienTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pasienTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(pasienTable);
        add(scrollPane, BorderLayout.CENTER);

        pasienTable.getSelectionModel().addListSelectionListener(e -> {
            System.out.println("Selection event triggered. ValueIsAdjusting: " + e.getValueIsAdjusting() + ", Selected Row: " + pasienTable.getSelectedRow());

            if (!e.getValueIsAdjusting()) {
                if (pasienTable.getSelectedRow() != -1) {
                    int selectedRow = pasienTable.getSelectedRow();
                    System.out.println("Processing selected row: " + selectedRow);

                    try {
                        idPasienField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                        namaField.setText(tableModel.getValueAt(selectedRow, 1).toString());

                        String dateString = tableModel.getValueAt(selectedRow, 2).toString();
                        System.out.println("Tanggal Lahir String from table: " + dateString);
                        try {
                            LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                            tanggalLahirPicker.getModel().setDate(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
                            tanggalLahirPicker.getModel().setSelected(true);
                            System.out.println("Tanggal Lahir parsed and set successfully: " + date);
                        } catch (Exception ex) {
                            System.err.println("Error parsing date string '" + dateString + "': " + ex.getMessage());
                            tanggalLahirPicker.getModel().setSelected(false);
                            tanggalLahirPicker.getJFormattedTextField().setText("");
                        }

                        alamatField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                        teleponField.setText(tableModel.getValueAt(selectedRow, 4).toString());

                        idPasienField.setEditable(false);
                        System.out.println("Form fields updated and ID locked.");
                    } catch (Exception ex) {
                        System.err.println("General error while filling form: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println("No row selected after adjustment, clearing form.");
                    clearForm();
                    idPasienField.setEditable(true);
                }
            }
        });
        System.out.println("PasienView UI initialized and listener added.");
    }

    public String getIdPasienField() { return idPasienField.getText(); }
    public String getNamaField() { return namaField.getText(); }
    public LocalDate getTanggalLahirPickerValue() {
        if (tanggalLahirPicker.getModel().isSelected()) {
            return LocalDate.of(tanggalLahirPicker.getModel().getYear(),
                                tanggalLahirPicker.getModel().getMonth() + 1,
                                tanggalLahirPicker.getModel().getDay());
        }
        return null;
    }
    public String getAlamatField() { return alamatField.getText(); }
    public String getTeleponField() { return teleponField.getText(); }

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

    public JTable getPasienTable() { return pasienTable; }
    public DefaultTableModel getTableModel() { return tableModel; }

    public void clearForm() {
        idPasienField.setText("");
        namaField.setText("");
        tanggalLahirPicker.getModel().setSelected(false);
        alamatField.setText("");
        teleponField.setText("");
        idPasienField.setEditable(true);
        pasienTable.clearSelection();
    }

    public Pasien getPasienFromForm() {
        String id = idPasienField.getText().trim();
        String nama = namaField.getText().trim();
        LocalDate tglLahir = getTanggalLahirPickerValue();
        String alamat = alamatField.getText().trim();
        String telepon = teleponField.getText().trim();

        if (id.isEmpty() || nama.isEmpty() || tglLahir == null || alamat.isEmpty() || telepon.isEmpty()) {
            showMessage("Semua field harus diisi.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return new Pasien(id, nama, tglLahir, alamat, telepon);
    }

    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    private class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private String datePattern = "yyyy-MM-dd";
        private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);

        @Override
        public Object stringToValue(String text) {
            try {
                return LocalDate.parse(text, dateFormatter);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public String valueToString(Object value) {
            if (value != null) {
                if (value instanceof LocalDate) {
                    return ((LocalDate) value).format(dateFormatter);
                } else if (value instanceof Date) {
                    return ((Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(dateFormatter);
                }
            }
            return "";
        }
    }
}