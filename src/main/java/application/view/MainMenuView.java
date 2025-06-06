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
public class MainMenuView extends JFrame {
    private JButton managePasienButton;
    private JButton manageDokterButton;
    private JButton manageObatButton;
    private JButton logoutButton;

    public MainMenuView() {
        setTitle("Menu Utama Klinik");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("Sistem Manajemen Klinik");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, gbc);

        managePasienButton = new JButton("Manajemen Pasien");
        managePasienButton.setPreferredSize(new Dimension(200, 40));
        panel.add(managePasienButton, gbc);

        manageDokterButton = new JButton("Manajemen Dokter");
        manageDokterButton.setPreferredSize(new Dimension(200, 40));
        panel.add(manageDokterButton, gbc);
        
        manageObatButton = new JButton("Manajemen Obat");
        manageObatButton.setPreferredSize(new Dimension(200, 40));
        panel.add(manageObatButton, gbc);
        
        logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(200, 40));
        gbc.insets = new Insets(25, 0, 0, 0);
        panel.add(logoutButton, gbc);

        add(panel);
    }

    public void addManagePasienListener(ActionListener listener) {
        managePasienButton.addActionListener(listener);
    }

    public void addManageDokterListener(ActionListener listener) {
        manageDokterButton.addActionListener(listener);
    }
    
    public void addManageObatListener(ActionListener listener) {
        manageObatButton.addActionListener(listener);
    }
    
    public void addLogoutListener(ActionListener listener) {
        logoutButton.addActionListener(listener);
    }

    public JFrame getFrame() {
        return this;
    }
}
