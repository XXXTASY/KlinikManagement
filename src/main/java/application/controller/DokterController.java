/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.controller;

import application.model.Dokter;
import application.model.KlinikData;
import application.view.DokterView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
/*
 *
 * @author XXXTASY
 */
public class DokterController {
    private KlinikData model;
    private DokterView view;

    public DokterController(KlinikData model, DokterView view) {
        this.model = model;
        this.view = view;
        attachEventHandlers();
        updateDokterTable();
    }

    private void attachEventHandlers() {
        view.addAddButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddDokter();
            }
        });
        view.addUpdateButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleUpdateDokter();
            }
        });
        view.addDeleteButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDeleteDokter();
            }
        });
        view.addBackButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        });
        view.addClearButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.clearForm();
            }
        });
        view.addExportButtonListener(e -> handleExportDokterToCSV());
        view.addSearchButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSearchDokter();
            }
        });
        view.addResetSearchButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleResetSearchDokter();
            }
        });
    }

    private void handleExportDokterToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Data Dokter sebagai CSV");
        fileChooser.setSelectedFile(new File("data_dokter.csv"));
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
                                if (col < tableModel.getColumnCount() - 1) {
                                    writer.append(',');
                                }
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

    private void handleSearchDokter() {
        String keyword = view.getSearchKeyword();
        if (keyword.isEmpty()) {
            view.showMessage("Masukkan kata kunci pencarian.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        new SwingWorker<List<Dokter>, Void>() {
            String errorMessage = null;

            @Override
            protected List<Dokter> doInBackground() throws Exception {
                try {
                    return model.cariDokter(keyword);
                } catch (SQLException e) {
                    errorMessage = e.getMessage();
                    throw e;
                }
            }

            @Override
            protected void done() {
                try {
                    List<Dokter> hasilPencarian = get();
                    DefaultTableModel tableModel = view.getTableModel();
                    tableModel.setRowCount(0);

                    if (hasilPencarian.isEmpty()) {
                        view.showMessage("Tidak ditemukan dokter dengan kata kunci '" + keyword + "'.", "Pencarian", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                        for (Dokter d : hasilPencarian) {
                            tableModel.addRow(new Object[]{
                                d.getIdDokter(),
                                d.getNama(),
                                d.getSpesialisasi(),
                                d.getTelepon(),
                                d.getJamMulaiPraktik().format(formatter),
                                d.getJamSelesaiPraktik().format(formatter)
                            });
                        }
                    }
                } catch (Exception ex) {
                    view.showMessage("Gagal melakukan pencarian: " + (errorMessage != null ? errorMessage : ex.getMessage()), "Error Database", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } finally {
                    view.clearForm();
                }
            }
        }.execute();
    }

    private void handleResetSearchDokter() {
        updateDokterTable();
        view.getSearchField().setText("");
        view.showMessage("Pencarian direset. Menampilkan semua data dokter.", "Reset", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleAddDokter() {
        Dokter newDokter = view.getDokterFromForm();
        if (newDokter != null) {
            new SwingWorker<Boolean, Void>() {
                String errorMessage = null;
                @Override
                protected Boolean doInBackground() throws Exception {
                    try {
                        return model.tambahDokter(newDokter);
                    } catch (SQLException e) {
                        errorMessage = e.getMessage();
                        throw e;
                    }
                }
                @Override
                protected void done() {
                    try {
                        boolean success = get();
                        if (success) {
                            view.showMessage("Dokter " + newDokter.getNama() + " berhasil ditambahkan.", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                            updateDokterTable();
                            view.clearForm();
                        } else {
                            view.showMessage("ID Dokter " + newDokter.getIdDokter() + " sudah ada. Silakan gunakan ID lain.", "ID Dokter Duplikat", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        view.showMessage("Gagal menambahkan dokter: " + ex.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }.execute();
        }
    }

    private void handleUpdateDokter() {
        int selectedRow = view.getDokterTable().getSelectedRow();
        if (selectedRow == -1) {
            view.showMessage("Pilih dokter yang ingin diperbarui dari tabel.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String oldDokterId = view.getTableModel().getValueAt(selectedRow, 0).toString();
        Dokter selectedDokterOriginal = model.getDokterById(oldDokterId).orElse(null);

        if (selectedDokterOriginal == null) {
            view.showMessage("Data dokter asli tidak ditemukan. Silakan refresh tabel.", "Error Internal", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Dokter updatedDokterFromForm = view.getDokterFromForm();
        if (updatedDokterFromForm == null) {
            return;
        }

        Dokter dokterToUpdateModel = new Dokter(
                selectedDokterOriginal.getIdDokter(),
                updatedDokterFromForm.getNama(),
                updatedDokterFromForm.getSpesialisasi(),
                updatedDokterFromForm.getTelepon(),
                updatedDokterFromForm.getJamMulaiPraktik(),
                updatedDokterFromForm.getJamSelesaiPraktik()
        );

        new SwingWorker<Boolean, Void>() {
            String errorMessage = null;
            @Override
            protected Boolean doInBackground() throws Exception {
                try {
                    return model.updateDokter(selectedDokterOriginal, dokterToUpdateModel);
                } catch (SQLException e) {
                    errorMessage = e.getMessage();
                    throw e;
                }
            }
            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        view.showMessage("Data dokter " + dokterToUpdateModel.getNama() + " berhasil diperbarui!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                        updateDokterTable();
                        view.clearForm();
                    } else {
                        if (errorMessage == null) {
                            view.showMessage("Gagal memperbarui dokter. ID dokter baru mungkin duplikat.", "Error Update", JOptionPane.ERROR_MESSAGE);
                        } else {
                            if (errorMessage.contains("Duplicate entry") && errorMessage.contains("for key 'id_dokter'")) {
                                view.showMessage("Dokter dengan ID " + dokterToUpdateModel.getIdDokter() + " sudah ada. Silakan gunakan ID unik.", "Input Error", JOptionPane.ERROR_MESSAGE);
                            } else {
                                view.showMessage("Terjadi kesalahan database saat memperbarui dokter: " + errorMessage, "Error Database", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } catch (Exception ex) {
                    view.showMessage("Terjadi kesalahan tak terduga: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        }.execute();
    }

    private void handleDeleteDokter() {
        int selectedRow = view.getDokterTable().getSelectedRow();
        if (selectedRow == -1) {
            view.showMessage("Pilih dokter yang ingin dihapus dari tabel.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String dokterIdToDelete = view.getTableModel().getValueAt(selectedRow, 0).toString();
        Dokter selectedDokter = model.getDokterById(dokterIdToDelete).orElse(null);

        if (selectedDokter == null) {
            view.showMessage("Dokter tidak ditemukan untuk dihapus.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view,
                "Anda yakin ingin menghapus dokter " + selectedDokter.getNama() + "?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            new SwingWorker<Boolean, Void>() {
                String errorMessage = null;
                @Override
                protected Boolean doInBackground() throws Exception {
                    try {
                        return model.hapusDokter(selectedDokter);
                    } catch (SQLException e) {
                        errorMessage = e.getMessage();
                        throw e;
                    }
                }
                @Override
                protected void done() {
                    try {
                        boolean success = get();
                        if (success) {
                            view.showMessage("Dokter " + selectedDokter.getNama() + " berhasil dihapus.", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                            updateDokterTable();
                            view.clearForm();
                        } else {
                            view.showMessage("Gagal menghapus dokter.", "Error Hapus", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        view.showMessage("Gagal menghapus dokter: " + ex.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }.execute();
        }
    }

    private void updateDokterTable() {
        new SwingWorker<List<Dokter>, Void>() {
            String errorMessage = null;
            @Override
            protected List<Dokter> doInBackground() throws Exception {
                try {
                    model.loadDokterFromDatabase();
                    return model.getDaftarDokter();
                } catch (Exception e) {
                    errorMessage = e.getMessage();
                    throw e;
                }
            }
            @Override
            protected void done() {
                try {
                    List<Dokter> dokterList = get();
                    DefaultTableModel tableModel = view.getTableModel();
                    tableModel.setRowCount(0);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    for (Dokter d : dokterList) {
                        tableModel.addRow(new Object[]{
                            d.getIdDokter(),
                            d.getNama(),
                            d.getSpesialisasi(),
                            d.getTelepon(),
                            d.getJamMulaiPraktik().format(formatter),
                            d.getJamSelesaiPraktik().format(formatter)
                        });
                    }
                } catch (Exception ex) {
                    view.showMessage("Gagal memuat data dokter: " + (errorMessage != null ? errorMessage : ex.getMessage()), "Error Database", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        }.execute();
    }
}
