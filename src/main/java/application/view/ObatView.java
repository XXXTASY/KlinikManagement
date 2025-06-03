/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.view;

import application.model.Obat;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Properties;
/**
 *
 * @author XXXTASY
 */
public class ObatView extends JDialog {
    private JTable obatTable;
    private DefaultTableModel tableModel;
    private JTextField kodeObatField;
    private JTextField namaObatField;
    private JTextField produsenField;
    private JTextField satuanField;
    private JTextField stokField;
    private JTextField hargaBeliField;
    private JTextField hargaJualField;
    private JDatePickerImpl tanggalKadaluarsaPicker;

    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton backButton;
    
    private Obat selectedObat = null;


    public ObatView(JFrame parentFrame) {
        super(parentFrame, "Manajemen Data Obat", true);
        setSize(950, 650);
        setLocationRelativeTo(parentFrame);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Data Obat"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Kode Obat:"), gbc);
        gbc.gridx = 1; kodeObatField = new JTextField(15); formPanel.add(kodeObatField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Nama Obat:"), gbc);
        gbc.gridx = 1; namaObatField = new JTextField(15); formPanel.add(namaObatField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Produsen:"), gbc);
        gbc.gridx = 1; produsenField = new JTextField(15); formPanel.add(produsenField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Satuan:"), gbc);
        gbc.gridx = 1; satuanField = new JTextField(15); formPanel.add(satuanField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Stok:"), gbc);
        gbc.gridx = 1; stokField = new JTextField(15); formPanel.add(stokField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Harga Beli:"), gbc);
        gbc.gridx = 1; hargaBeliField = new JTextField(15); formPanel.add(hargaBeliField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Harga Jual:"), gbc);
        gbc.gridx = 1; hargaJualField = new JTextField(15); formPanel.add(hargaJualField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Tgl Kadaluarsa:"), gbc);
        UtilDateModel dateModel = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
        tanggalKadaluarsaPicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        gbc.gridx = 1; formPanel.add(tanggalKadaluarsaPicker, gbc);
        row++;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addButton = new JButton("Tambah");
        updateButton = new JButton("Perbarui");
        deleteButton = new JButton("Hapus");
        clearButton = new JButton("Clear Form");
        backButton = new JButton("Kembali ke Menu Utama");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"Kode Obat", "Nama Obat", "Produsen", "Satuan", "Stok", "Harga Beli", "Harga Jual", "Tgl Kadaluarsa"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        obatTable = new JTable(tableModel);
        obatTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        obatTable.getTableHeader().setReorderingAllowed(false);

        obatTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && obatTable.getSelectedRow() != -1) {
                int selectedRow = obatTable.getSelectedRow();
                kodeObatField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                namaObatField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                produsenField.setText(tableModel.getValueAt(selectedRow, 2) != null ? tableModel.getValueAt(selectedRow, 2).toString() : "");
                satuanField.setText(tableModel.getValueAt(selectedRow, 3) != null ? tableModel.getValueAt(selectedRow, 3).toString() : "");
                stokField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                hargaBeliField.setText(tableModel.getValueAt(selectedRow, 5).toString());
                hargaJualField.setText(tableModel.getValueAt(selectedRow, 6).toString());

                Object tglValue = tableModel.getValueAt(selectedRow, 7);
                if (tglValue != null) {
                    LocalDate date = LocalDate.parse(tglValue.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    tanggalKadaluarsaPicker.getModel().setDate(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
                    tanggalKadaluarsaPicker.getModel().setSelected(true);
                } else {
                    tanggalKadaluarsaPicker.getModel().setSelected(false);
                }
                kodeObatField.setEditable(false);
            }
        });
        add(new JScrollPane(obatTable), BorderLayout.CENTER);
    }

    public String getKodeObat() { return kodeObatField.getText().trim(); }
    public String getNamaObat() { return namaObatField.getText().trim(); }
    public String getProdusen() { return produsenField.getText().trim(); }
    public String getSatuan() { return satuanField.getText().trim(); }
    public String getStok() { return stokField.getText().trim(); }
    public String getHargaBeli() { return hargaBeliField.getText().trim(); }
    public String getHargaJual() { return hargaJualField.getText().trim(); }
    public LocalDate getTanggalKadaluarsa() {
        if (tanggalKadaluarsaPicker.getModel().isSelected()) {
            Date selectedDate = (Date) tanggalKadaluarsaPicker.getModel().getValue();
            return selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return null;
    }
    
    public Obat getSelectedObatFromTable() {
        int selectedRow = obatTable.getSelectedRow();
        if (selectedRow == -1) return null;

        String kode = tableModel.getValueAt(selectedRow, 0).toString();
        String nama = tableModel.getValueAt(selectedRow, 1).toString();
        String produsen = tableModel.getValueAt(selectedRow, 2) != null ? tableModel.getValueAt(selectedRow, 2).toString() : "";
        String satuan = tableModel.getValueAt(selectedRow, 3) != null ? tableModel.getValueAt(selectedRow, 3).toString() : "";
        int stok = Integer.parseInt(tableModel.getValueAt(selectedRow, 4).toString());
        double hargaBeli = Double.parseDouble(tableModel.getValueAt(selectedRow, 5).toString());
        double hargaJual = Double.parseDouble(tableModel.getValueAt(selectedRow, 6).toString());
        LocalDate tglKadaluarsa = null;
        if (tableModel.getValueAt(selectedRow, 7) != null) {
             tglKadaluarsa = LocalDate.parse(tableModel.getValueAt(selectedRow, 7).toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return new Obat(kode, nama, produsen, satuan, stok, hargaBeli, hargaJual, tglKadaluarsa);
    }


    public void addAddButtonListener(ActionListener listener) { addButton.addActionListener(listener); }
    public void addUpdateButtonListener(ActionListener listener) { updateButton.addActionListener(listener); }
    public void addDeleteButtonListener(ActionListener listener) { deleteButton.addActionListener(listener); }
    public void addClearButtonListener(ActionListener listener) { clearButton.addActionListener(listener); }
    public void addBackButtonListener(ActionListener listener) { backButton.addActionListener(listener); }

    public void clearForm() {
        kodeObatField.setText("");
        namaObatField.setText("");
        produsenField.setText("");
        satuanField.setText("");
        stokField.setText("");
        hargaBeliField.setText("");
        hargaJualField.setText("");
        tanggalKadaluarsaPicker.getModel().setSelected(false);
        obatTable.clearSelection();
        kodeObatField.setEditable(true);
        selectedObat = null;
    }

    public void displayObatData(List<Obat> obatList) {
        tableModel.setRowCount(0);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (Obat obat : obatList) {
            tableModel.addRow(new Object[]{
                obat.getKodeObat(),
                obat.getNamaObat(),
                obat.getProdusen(),
                obat.getSatuan(),
                obat.getStok(),
                obat.getHargaBeli(),
                obat.getHargaJual(),
                obat.getTanggalKadaluarsa() != null ? obat.getTanggalKadaluarsa().format(dateFormatter) : null
            });
        }
    }

    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    private static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final String datePattern = "yyyy-MM-dd";
        private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);

        @Override
        public Object stringToValue(String text) {
            try {
                return Date.from(LocalDate.parse(text, dateFormatter).atStartOfDay(ZoneId.systemDefault()).toInstant());
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public String valueToString(Object value) {
            if (value != null) {
                if (value instanceof Date) {
                    return ((Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(dateFormatter);
                }
            }
            return "";
        }
    }
}