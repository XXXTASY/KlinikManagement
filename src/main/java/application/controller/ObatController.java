/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.controller;

import application.model.KlinikData;
import application.model.Obat;
import application.view.ObatView;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author XXXTASY
 */
public class ObatController {
    private KlinikData model;
    private ObatView view;

    public ObatController(KlinikData model, ObatView view) {
        this.model = model;
        this.view = view;
        attachEventHandlers();
        loadAndDisplayObat();
    }

    private void attachEventHandlers() {
        view.addAddButtonListener(e -> handleAddObat());
        view.addUpdateButtonListener(e -> handleUpdateObat());
        view.addDeleteButtonListener(e -> handleDeleteObat());
        view.addClearButtonListener(e -> view.clearForm());
        view.addExportButtonListener(e -> handleExportObatToCSV());
        view.addBackButtonListener(e -> view.dispose());
        view.addSearchButtonListener(e -> handleSearchObat());
        view.addResetSearchButtonListener(e -> handleResetSearchObat());
    }
    
    private void handleExportObatToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Data Obat sebagai CSV");
        fileChooser.setSelectedFile(new File("data_obat.csv"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files (*.csv)", "csv"));

        int userSelection = fileChooser.showSaveDialog(view);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            final File fileToSave;
            if (!selectedFile.getAbsolutePath().endsWith(".csv")) {
                fileToSave = new File(selectedFile.getAbsolutePath() + ".csv");
            } else {
                fileToSave = selectedFile;
            }

            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    try (FileWriter writer = new FileWriter(fileToSave)) {
                        DefaultTableModel tableModel = view.getTableModel();
                        for (int i = 0; i < tableModel.getColumnCount(); i++) {
                            writer.append(tableModel.getColumnName(i)).append(i == tableModel.getColumnCount() - 1 ? "" : ",");
                        }
                        writer.append('\n');

                        for (int row = 0; row < tableModel.getRowCount(); row++) {
                            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                                Object obj = tableModel.getValueAt(row, col);
                                String value = (obj == null) ? "" : obj.toString();
                                writer.append('"').append(value.replace("\"", "\"\"")).append('"');
                                if (col < tableModel.getColumnCount() - 1) writer.append(',');
                            }
                            writer.append('\n');
                        }
                    } catch (IOException e) {
                        throw new IOException("Gagal menulis file: " + e.getMessage(), e);
                    }
                    return null;
                }
                @Override
                protected void done() {
                    try {
                        get();
                        view.showMessage("Data berhasil diekspor ke:\n" + fileToSave.getAbsolutePath(), "Ekspor Berhasil", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception e) {
                        view.showMessage("Terjadi kesalahan saat mengekspor data:\n" + e.getCause().getMessage(), "Error Ekspor", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.execute();
        }
    }

    private void loadAndDisplayObat() {
        new SwingWorker<List<Obat>, Void>() {
            @Override
            protected List<Obat> doInBackground() throws Exception {
                model.loadObatFromDatabase();
                return model.getDaftarObat();
            }

            @Override
            protected void done() {
                try {
                    List<Obat> obatList = get();
                    view.displayObatData(obatList);
                } catch (Exception e) {
                    view.showMessage("Gagal memuat data obat: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        }.execute();
    }
    
    private void handleSearchObat() {
        String keyword = view.getSearchKeyword();
        if (keyword.isEmpty()) {
            view.showMessage("Silakan masukkan kata kunci pencarian.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        new SwingWorker<List<Obat>, Void>() {
            @Override
            protected List<Obat> doInBackground() throws Exception {
                return model.cariObat(keyword);
            }

            @Override
            protected void done() {
                try {
                    List<Obat> searchResult = get();
                    if (searchResult.isEmpty()) {
                        view.showMessage("Data obat dengan kata kunci '" + keyword + "' tidak ditemukan.", "Hasil Pencarian", JOptionPane.INFORMATION_MESSAGE);
                    }
                    view.displayObatData(searchResult);
                } catch (Exception e) {
                    view.showMessage("Gagal melakukan pencarian obat: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        }.execute();
    }
    
    private void handleResetSearchObat() {
        view.getSearchField().setText("");
        loadAndDisplayObat();
        view.showMessage("Pencarian direset. Menampilkan semua data obat.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleAddObat() {
        String kodeObat = view.getKodeObat();
        String namaObat = view.getNamaObat();
        String produsen = view.getProdusen();
        String satuan = view.getSatuan();
        LocalDate tglKadaluarsa = view.getTanggalKadaluarsa();

        if (kodeObat.isEmpty() || namaObat.isEmpty()) {
            view.showMessage("Kode Obat dan Nama Obat tidak boleh kosong.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int stok;
        double hargaBeli, hargaJual;
        try {
            stok = Integer.parseInt(view.getStok());
            hargaBeli = Double.parseDouble(view.getHargaBeli());
            hargaJual = Double.parseDouble(view.getHargaJual());
        } catch (NumberFormatException e) {
            view.showMessage("Stok, Harga Beli, dan Harga Jual harus berupa angka.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Obat newObat = new Obat(kodeObat, namaObat, produsen, satuan, stok, hargaBeli, hargaJual, tglKadaluarsa);

        new SwingWorker<Boolean, Void>() {
            String errorMessage = null;
            @Override
            protected Boolean doInBackground() throws Exception {
                try {
                    return model.tambahObat(newObat);
                } catch (SQLException ex) {
                    errorMessage = ex.getMessage();
                    if (ex.getErrorCode() == 1062 || ex.getMessage().toLowerCase().contains("duplicate entry")) {
                         errorMessage = "Kode Obat '" + newObat.getKodeObat() + "' sudah ada.";
                    }
                    return false;
                }
            }
            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        view.showMessage("Obat berhasil ditambahkan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                        loadAndDisplayObat();
                        view.clearForm();
                    } else {
                         view.showMessage("Gagal menambahkan obat: " + (errorMessage != null ? errorMessage : "Kode obat mungkin sudah ada atau terjadi kesalahan lain."), "Error Tambah", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    view.showMessage("Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        }.execute();
    }

    private void handleUpdateObat() {
        Obat selectedObatOriginal = view.getSelectedObatFromTable();
        if (selectedObatOriginal == null) {
            view.showMessage("Pilih obat yang akan diperbarui dari tabel.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String newKodeObat = view.getKodeObat();
        String namaObat = view.getNamaObat();
        String produsen = view.getProdusen();
        String satuan = view.getSatuan();
        LocalDate tglKadaluarsa = view.getTanggalKadaluarsa();


        if (newKodeObat.isEmpty() || namaObat.isEmpty()) {
            view.showMessage("Kode Obat dan Nama Obat tidak boleh kosong.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int stok;
        double hargaBeli, hargaJual;
        try {
            stok = Integer.parseInt(view.getStok());
            hargaBeli = Double.parseDouble(view.getHargaBeli());
            hargaJual = Double.parseDouble(view.getHargaJual());
        } catch (NumberFormatException e) {
            view.showMessage("Stok, Harga Beli, dan Harga Jual harus berupa angka.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Obat updatedObat = new Obat(newKodeObat, namaObat, produsen, satuan, stok, hargaBeli, hargaJual, tglKadaluarsa);
        
        new SwingWorker<Boolean, Void>() {
            String errorMessage = null;
            @Override
            protected Boolean doInBackground() throws Exception {
                try {
                    return model.updateObat(selectedObatOriginal, updatedObat);
                } catch (SQLException ex) {
                    errorMessage = ex.getMessage();
                     if (ex.getErrorCode() == 1062 || ex.getMessage().toLowerCase().contains("duplicate entry")) {
                         errorMessage = "Kode Obat baru '" + updatedObat.getKodeObat() + "' sudah digunakan oleh obat lain.";
                    }
                    return false;
                }
            }
            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        view.showMessage("Data obat berhasil diperbarui.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                        loadAndDisplayObat();
                        view.clearForm();
                    } else {
                        view.showMessage("Gagal memperbarui obat: " + (errorMessage != null ? errorMessage : "Kode obat mungkin tidak ditemukan atau terjadi kesalahan lain."), "Error Update", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    view.showMessage("Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        }.execute();
    }

    private void handleDeleteObat() {
        Obat selectedObat = view.getSelectedObatFromTable();
        if (selectedObat == null) {
            view.showMessage("Pilih obat yang akan dihapus dari tabel.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view,
                "Anda yakin ingin menghapus obat '" + selectedObat.getNamaObat() + "'?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    try {
                        return model.hapusObat(selectedObat);
                    } catch (SQLException ex) {
                        return false;
                    }
                }
                @Override
                protected void done() {
                    try {
                        boolean success = get();
                        if (success) {
                            view.showMessage("Obat berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                            loadAndDisplayObat();
                            view.clearForm();
                        } else {
                            view.showMessage("Gagal menghapus obat.", "Error Hapus", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                       view.showMessage("Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                       ex.printStackTrace();
                    }
                }
            }.execute();
        }
    }
}
