/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package application;

import application.controller.LoginController;
import application.model.KlinikData;
import application.view.LoginView;

import javax.swing.*;
/**
 *
 * @author XXXTASY
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                KlinikData model = new KlinikData();
                LoginView loginView = new LoginView();
                loginView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                new LoginController(loginView, model, loginView);
                loginView.setVisible(true);
            }
        });
    }
}
