/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.controller;

import application.model.KlinikData;
import application.view.DokterView;
import application.view.MainMenuView;
import application.view.PasienView;
import application.view.LoginView;
import application.view.ObatView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author XXXTASY
 */
public class MainMenuController {
    private MainMenuView view;
    private KlinikData model;

    public MainMenuController(MainMenuView view, KlinikData model) {
        this.view = view;
        this.model = model;
        attachEventHandlers();
    }

    private void attachEventHandlers() {
        view.addManagePasienListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPasienManagement();
            }
        });

        view.addManageDokterListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDokterManagement();
            }
        });
        
        this.view.addManageObatListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showObatManagement();
            }
        });
        
        view.addLogoutListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogout();
            }
        });
    }

    private void showPasienManagement() {
        view.setVisible(false);

        PasienView pasienView = new PasienView(view.getFrame());
        new PasienController(model, pasienView);
        pasienView.setVisible(true);

        pasienView.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                view.setVisible(true);
            }
        });
    }

    private void showDokterManagement() {
        view.setVisible(false);

        DokterView dokterView = new DokterView(view.getFrame());
        new DokterController(model, dokterView);
        dokterView.setVisible(true);

        dokterView.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                view.setVisible(true);
            }
        });
    }
    
    private void showObatManagement() {
        view.setVisible(false);

        ObatView obatView = new ObatView(view.getFrame());
        new ObatController(model, obatView);
        obatView.setVisible(true);

        obatView.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                view.setVisible(true);
            }
        });
    }
    
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(view.getFrame(),
                "Anda yakin ingin logout?", "Konfirmasi Logout", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            view.dispose();
            showLoginScreen();
        }
    }

    private void showLoginScreen() {
        LoginView loginView = new LoginView();
        loginView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        new LoginController(loginView, model, loginView);
        loginView.setVisible(true);
        loginView.clearFields();
    }
}
