/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.controller;

import application.model.KlinikData;
import application.view.RegisterView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
/**
 *
 * @author XXXTASY
 */
public class RegisterController {
    private RegisterView view;
    private KlinikData model;
    private JFrame parentFrame;

    public RegisterController(RegisterView view, KlinikData model, JFrame parentFrame) {
        this.view = view;
        this.model = model;
        this.parentFrame = parentFrame;
        attachEventHandlers();
    }

    private void attachEventHandlers() {
        view.addRegisterListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegister();
            }
        });
        view.addBackToLoginListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        });
    }

    private void handleRegister() {
        String username = view.getUsernameField();
        String password = view.getPasswordField();
        String confirmPassword = view.getConfirmPasswordField();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            view.setStatus("Semua field harus diisi.", true);
            return;
        }
        if (!password.equals(confirmPassword)) {
            view.setStatus("Password dan konfirmasi password tidak cocok.", true);
            return;
        }
        if (password.length() < 6) {
            view.setStatus("Password minimal 6 karakter.", true);
            return;
        }

        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return model.registerUser(username, password);
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        view.showMessage("Akun '" + username + "' berhasil dibuat. Silakan login.", "Registrasi Berhasil", JOptionPane.INFORMATION_MESSAGE);
                        view.dispose();
                    } else {
                        // Pesan sudah ditangani di model jika username duplikat
                    }
                } catch (Exception ex) {
                    view.showMessage("Gagal mendaftar akun: " + ex.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        }.execute();
    }
}
