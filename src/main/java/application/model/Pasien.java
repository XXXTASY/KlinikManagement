/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.model;

import java.time.LocalDate;
/**
 *
 * @author XXXTASY
 */
public class Pasien {
    private String idPasien;
    private String nama;
    private LocalDate tanggalLahir;
    private String alamat;
    private String telepon;

    public Pasien(String idPasien, String nama, LocalDate tanggalLahir, String alamat, String telepon) {
        this.idPasien = idPasien;
        this.nama = nama;
        this.tanggalLahir = tanggalLahir;
        this.alamat = alamat;
        this.telepon = telepon;
    }

    public String getIdPasien() { return idPasien; }
    public String getNama() { return nama; }
    public LocalDate getTanggalLahir() { return tanggalLahir; }
    public String getAlamat() { return alamat; }
    public String getTelepon() { return telepon; }

    public void setIdPasien(String idPasien) { this.idPasien = idPasien; }
    public void setNama(String nama) { this.nama = nama; }
    public void setTanggalLahir(LocalDate tanggalLahir) { this.tanggalLahir = tanggalLahir; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    public void setTelepon(String telepon) { this.telepon = telepon; }

    @Override
    public String toString() {
        return "Pasien{" +
               "idPasien='" + idPasien + '\'' +
               ", nama='" + nama + '\'' +
               '}';
    }
}
