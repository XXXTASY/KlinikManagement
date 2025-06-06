/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.controller;

import application.model.KlinikData;
import application.model.Pasien;
import application.view.PasienView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
/*
 *
 * @author XXXTASY
 */
public class PasienController {
    private KlinikData model;
    private PasienView view;

    public PasienController(KlinikData model, PasienView view) {
        this.model = model;
        this.view = view;
        attachEventHandlers();
        updatePasienTable();
    }

    private void attachEventHandlers() {
        view.addAddButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddPasien();
            }
        });
        view.addUpdateButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleUpdatePasien();
            }
        });
        view.addDeleteButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDeletePasien();
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
        view.addSearchButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSearchPasien();
            }
        });
        view.addResetSearchButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleResetSearchPasien();
            }
        });
    }

    private void handleSearchPasien() {
        String keyword = view.getSearchKeyword();
        if (keyword.isEmpty()) {
            view.showMessage("Masukkan kata kunci pencarian.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        new SwingWorker<List<Pasien>, Void>() {
            String errorMessage = null;

            @Override
            protected List<Pasien> doInBackground() throws Exception {
                try {
                    return model.cariPasien(keyword);
                } catch (SQLException e) {
                    errorMessage = e.getMessage();
                    throw e;
                }
            }

            @Override
            protected void done() {
                try {
                    List<Pasien> hasilPencarian = get();
                    DefaultTableModel tableModel = view.getTableModel();
                    tableModel.setRowCount(0);

                    if (hasilPencarian.isEmpty()) {
                        view.showMessage("Tidak ditemukan pasien dengan kata kunci '" + keyword + "'.", "Pencarian", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        for (Pasien p : hasilPencarian) {
                            tableModel.addRow(new Object[]{
                                p.getIdPasien(),
                                p.getNama(),
                                p.getTanggalLahir().format(formatter),
                                p.getJenisKelamin(),
                                p.getAlamat(),
                                p.getTelepon()
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

    private void handleResetSearchPasien() {
        updatePasienTable();
        view.getSearchField().setText("");
        view.showMessage("Pencarian direset. Menampilkan semua data pasien.", "Reset", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void handleAddPasien() {
        Pasien newPasien = view.getPasienFromForm();
        if (newPasien != null) {
            new SwingWorker<Boolean, Void>() {
                String errorMessage = null;
                @Override
                protected Boolean doInBackground() throws Exception {
                    try { return model.tambahPasien(newPasien); }
                    catch (SQLException e) { errorMessage = e.getMessage(); throw e; }
                }
                @Override
                protected void done() {
                    try {
                        boolean success = get();
                        if (success) {
                            view.showMessage("Pasien " + newPasien.getNama() + " berhasil ditambahkan.", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                            updatePasienTable();
                            view.clearForm();
                        } else {
                            view.showMessage("ID Pasien " + newPasien.getIdPasien() + " sudah ada. Silakan gunakan ID lain.", "ID Pasien Duplikat", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        view.showMessage("Gagal menambahkan pasien: " + ex.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }.execute();
        }
    }

    private void handleUpdatePasien() {
        int selectedRow = view.getPasienTable().getSelectedRow();
        if (selectedRow == -1) {
            view.showMessage("Pilih pasien yang ingin diperbarui dari tabel.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String oldPasienId = view.getTableModel().getValueAt(selectedRow, 0).toString();
        Pasien selectedPasienOriginal = model.getPasienById(oldPasienId).orElse(null);

        if (selectedPasienOriginal == null) {
            view.showMessage("Data pasien asli tidak ditemukan. Silakan refresh tabel.", "Error Internal", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Pasien updatedPasienFromForm = view.getPasienFromForm();
        if (updatedPasienFromForm == null) { return; }

        Pasien pasienToUpdateModel = new Pasien(
            selectedPasienOriginal.getIdPasien(),
            updatedPasienFromForm.getNama(),
            updatedPasienFromForm.getTanggalLahir(),
            updatedPasienFromForm.getJenisKelamin(),
            updatedPasienFromForm.getAlamat(),
            updatedPasienFromForm.getTelepon()
        );

        new SwingWorker<Boolean, Void>() {
            String errorMessage = null;
            @Override
            protected Boolean doInBackground() throws Exception {
                try { return model.updatePasien(selectedPasienOriginal, pasienToUpdateModel); }
                catch (SQLException e) { errorMessage = e.getMessage(); throw e; }
            }
            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        view.showMessage("Data pasien " + pasienToUpdateModel.getNama() + " berhasil diperbarui!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                        updatePasienTable();
                        view.clearForm();
                    } else {
                        if (errorMessage == null) {
                            view.showMessage("Gagal memperbarui pasien. ID Pasien baru mungkin duplikat.", "Error Update", JOptionPane.ERROR_MESSAGE);
                        } else {
                            if (errorMessage.contains("Duplicate entry") && errorMessage.contains("for key 'id_pasien'")) {
                                view.showMessage("Pasien dengan ID " + pasienToUpdateModel.getIdPasien() + " sudah ada. Silakan gunakan ID unik.", "Input Error", JOptionPane.ERROR_MESSAGE);
                            } else {
                                view.showMessage("Terjadi kesalahan database saat memperbarui pasien: " + errorMessage, "Error Database", JOptionPane.ERROR_MESSAGE);
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

    private void handleDeletePasien() {
        int selectedRow = view.getPasienTable().getSelectedRow();
        if (selectedRow == -1) {
            view.showMessage("Pilih pasien yang ingin dihapus dari tabel.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String pasienIdToDelete = view.getTableModel().getValueAt(selectedRow, 0).toString();
        Pasien selectedPasien = model.getPasienById(pasienIdToDelete).orElse(null);

        if (selectedPasien == null) {
            view.showMessage("Pasien tidak ditemukan untuk dihapus.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view,
                "Anda yakin ingin menghapus pasien " + selectedPasien.getNama() + "?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            new SwingWorker<Boolean, Void>() {
                String errorMessage = null;
                @Override
                protected Boolean doInBackground() throws Exception {
                    try { return model.hapusPasien(selectedPasien); }
                    catch (SQLException e) { errorMessage = e.getMessage(); throw e; }
                }
                @Override
                protected void done() {
                    try {
                        boolean success = get();
                        if (success) {
                            view.showMessage("Pasien " + selectedPasien.getNama() + " berhasil dihapus.", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                            updatePasienTable();
                            view.clearForm();
                        } else {
                            view.showMessage("Gagal menghapus pasien.", "Error Hapus", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        view.showMessage("Gagal menghapus pasien: " + ex.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }.execute();
        }
    }

    private void updatePasienTable() {
        new SwingWorker<List<Pasien>, Void>() {
            String errorMessage = null;
            @Override
            protected List<Pasien> doInBackground() throws Exception {
                try {
                     model.loadPasienFromDatabase();
                     return model.getDaftarPasien();
                } catch (Exception e) {
                    errorMessage = e.getMessage();
                    throw e;
                }
            }

            @Override
            protected void done() {
                try {
                    List<Pasien> pasienList = get();
                    DefaultTableModel tableModel = view.getTableModel();
                    tableModel.setRowCount(0);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    for (Pasien p : pasienList) {
                        tableModel.addRow(new Object[]{
                            p.getIdPasien(),
                            p.getNama(),
                            p.getTanggalLahir().format(formatter),
                            p.getJenisKelamin(),
                            p.getAlamat(),
                            p.getTelepon()
                        });
                    }
                } catch (Exception ex) {
                    view.showMessage("Gagal memuat data pasien: " + (errorMessage != null ? errorMessage : ex.getMessage()), "Error Database", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        }.execute();
    }
}