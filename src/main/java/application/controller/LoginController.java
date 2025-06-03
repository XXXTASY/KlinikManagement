/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.controller;

import application.model.KlinikData;
import application.view.LoginView;
import application.view.MainMenuView;
import application.view.RegisterView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author XXXTASY
 */
public class LoginController {
    private LoginView view;
    private KlinikData model;
    private JFrame mainFrame;

    public LoginController(LoginView view, KlinikData model, JFrame mainFrame) {
        this.view = view;
        this.model = model;
        this.mainFrame = mainFrame;
        attachEventHandlers();
    }

    private void attachEventHandlers() {
        view.addLoginListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        view.addRegisterListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegisterView();
            }
        });
    }

    private void handleLogin() {
        String username = view.getUsername();
        String password = view.getPassword();

        if (username.isEmpty() || password.isEmpty()) {
            view.setStatus("Username dan password tidak boleh kosong.", true);
            return;
        }

        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return model.authenticateUser(username, password);
            }

            @Override
            protected void done() {
                try {
                    boolean authenticated = get();
                    if (authenticated) {
                        view.setStatus("Login Berhasil!", false);
                        view.dispose();
                        showMainMenu();
                    } else {
                        view.setStatus("Username atau password salah.", true);
                        view.clearFields();
                    }
                } catch (Exception ex) {
                    view.setStatus("Terjadi kesalahan saat login: " + ex.getMessage(), true);
                    ex.printStackTrace();
                }
            }
        }.execute();
    }

    private void showRegisterView() {
        view.setVisible(false);

        RegisterView registerView = new RegisterView(mainFrame);
        RegisterController registerController = new RegisterController(registerView, model, mainFrame);
        registerView.setVisible(true);

        registerView.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                view.setVisible(true);
                view.clearFields();
            }
        });
    }

    private void showMainMenu() {
        MainMenuView mainMenuView = new MainMenuView();
        mainMenuView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        new MainMenuController(mainMenuView, model);
        mainMenuView.setVisible(true);
    }
}
