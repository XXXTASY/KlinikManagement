/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.model;

import java.time.LocalTime;
/**
 *
 * @author XXXTASY
 */
public class Dokter {
    private String idDokter;
    private String nama;
    private String spesialisasi;
    private String telepon;
    private LocalTime jamMulaiPraktik;
    private LocalTime jamSelesaiPraktik;

    public Dokter(String idDokter, String nama, String spesialisasi, String telepon,
                  LocalTime jamMulaiPraktik, LocalTime jamSelesaiPraktik) {
        this.idDokter = idDokter;
        this.nama = nama;
        this.spesialisasi = spesialisasi;
        this.telepon = telepon;
        this.jamMulaiPraktik = jamMulaiPraktik;
        this.jamSelesaiPraktik = jamSelesaiPraktik;
    }

    public String getIdDokter() { return idDokter; }
    public String getNama() { return nama; }
    public String getSpesialisasi() { return spesialisasi; }
    public String getTelepon() { return telepon; }
    public LocalTime getJamMulaiPraktik() { return jamMulaiPraktik; }
    public LocalTime getJamSelesaiPraktik() { return jamSelesaiPraktik; }

    public void setIdDokter(String idDokter) { this.idDokter = idDokter; }
    public void setNama(String nama) { this.nama = nama; }
    public void setSpesialisasi(String spesialisasi) { this.spesialisasi = spesialisasi; }
    public void setTelepon(String telepon) { this.telepon = telepon; }
    public void setJamMulaiPraktik(LocalTime jamMulaiPraktik) { this.jamMulaiPraktik = jamMulaiPraktik; }
    public void setJamSelesaiPraktik(LocalTime jamSelesaiPraktik) { this.jamSelesaiPraktik = jamSelesaiPraktik; }

    @Override
    public String toString() {
        return "Dokter{" +
               "idDokter='" + idDokter + '\'' +
               ", nama='" + nama + '\'' +
               ", spesialisasi='" + spesialisasi + '\'' +
               '}';
    }
}
