/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
/**
 *
 * @author XXXTASY
 */
public class RegisterView extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton backToLoginButton;
    private JLabel statusLabel;

    public RegisterView(JFrame parentFrame) {
        super(parentFrame, "Daftar Akun Baru", true);
        setSize(450, 400);
        setLocationRelativeTo(parentFrame);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Daftar Akun Baru");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Username Baru:"), gbc);
        usernameField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Konfirmasi Password:"), gbc);
        confirmPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(confirmPasswordField, gbc);

        registerButton = new JButton("Daftar");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(registerButton, gbc);

        backToLoginButton = new JButton("Kembali ke Login");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(backToLoginButton, gbc);

        statusLabel = new JLabel("");
        statusLabel.setForeground(Color.RED);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(statusLabel, gbc);

        add(panel);
    }

    public String getUsernameField() { return usernameField.getText(); }
    public String getPasswordField() { return new String(passwordField.getPassword()); }
    public String getConfirmPasswordField() { return new String(confirmPasswordField.getPassword()); }

    public void addRegisterListener(ActionListener listener) { registerButton.addActionListener(listener); }
    public void addBackToLoginListener(ActionListener listener) { backToLoginButton.addActionListener(listener); }

    public void setStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setForeground(isError ? Color.RED : Color.GREEN);
    }

    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }
}
