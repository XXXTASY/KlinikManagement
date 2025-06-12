/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
/**
 *
 * @author XXXTASY
 */
public class MainMenuView extends JFrame {
    private JPanel managePasienButton;
    private JPanel manageDokterButton;
    private JPanel manageObatButton;
    private JButton logoutButton;

    public MainMenuView() {
        setTitle("Menu Utama Klinik");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        JLabel titleLabel = new JLabel("Sistem Manajemen Klinik");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel menuGridPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        menuGridPanel.setOpaque(false);

        managePasienButton = createMenuPanel("Manajemen Pasien", "/icons/patient.png");
        manageDokterButton = createMenuPanel("Manajemen Dokter", "/icons/doctor.png");
        manageObatButton = createMenuPanel("Manajemen Obat", "/icons/medicine.png");

        menuGridPanel.add(managePasienButton);
        menuGridPanel.add(manageDokterButton);
        menuGridPanel.add(manageObatButton);

        mainPanel.add(menuGridPanel, BorderLayout.CENTER);

        JPanel logoutFlowPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        logoutButton = new JButton("Logout");
        try {
            ImageIcon logoutIcon = new ImageIcon(getClass().getResource("/icons/logout.png"));
            Image scaledImg = logoutIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            logoutButton.setIcon(new ImageIcon(scaledImg));
        } catch (Exception e) {
            System.err.println("Icon logout.png tidak ditemukan.");
        }
        
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setBackground(Color.WHITE);

        logoutFlowPanel.add(logoutButton);
        mainPanel.add(logoutFlowPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createMenuPanel(String text, String imagePath) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            Image scaledImage = icon.getImage().getScaledInstance(84, 84, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            iconLabel.setText("Icon not found");
            System.err.println("Icon " + imagePath + " tidak ditemukan.");
        }
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        panel.add(iconLabel, BorderLayout.CENTER);
        panel.add(textLabel, BorderLayout.SOUTH);

        Border defaultBorder = panel.getBorder();
        Border hoverBorder = BorderFactory.createLineBorder(new Color(100, 100, 100), 1);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBorder(hoverBorder);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBorder(defaultBorder);
            }
        });

        return panel;
    }

    private void addPanelClickListener(JPanel panel, ActionListener listener) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, "clicked"));
            }
        });
    }

    public void addManagePasienListener(ActionListener listener) {
        addPanelClickListener(managePasienButton, listener);
    }

    public void addManageDokterListener(ActionListener listener) {
        addPanelClickListener(manageDokterButton, listener);
    }

    public void addManageObatListener(ActionListener listener) {
        addPanelClickListener(manageObatButton, listener);
    }

    public void addLogoutListener(ActionListener listener) {
        logoutButton.addActionListener(listener);
    }

    public JFrame getFrame() {
        return this;
    }
}
