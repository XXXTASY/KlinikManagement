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
public class Obat {
    private String kodeObat;
    private String namaObat;
    private String produsen;
    private String satuan;
    private int stok;
    private double hargaBeli;
    private double hargaJual;
    private LocalDate tanggalKadaluarsa;

    public Obat(String kodeObat, String namaObat, String produsen, String satuan, 
                int stok, double hargaBeli, double hargaJual, LocalDate tanggalKadaluarsa) {
        this.kodeObat = kodeObat;
        this.namaObat = namaObat;
        this.produsen = produsen;
        this.satuan = satuan;
        this.stok = stok;
        this.hargaBeli = hargaBeli;
        this.hargaJual = hargaJual;
        this.tanggalKadaluarsa = tanggalKadaluarsa;
    }

    public String getKodeObat() { return kodeObat; }
    public String getNamaObat() { return namaObat; }
    public String getProdusen() { return produsen; }
    public String getSatuan() { return satuan; }
    public int getStok() { return stok; }
    public double getHargaBeli() { return hargaBeli; }
    public double getHargaJual() { return hargaJual; }
    public LocalDate getTanggalKadaluarsa() { return tanggalKadaluarsa; }

    public void setKodeObat(String kodeObat) { this.kodeObat = kodeObat; }
    public void setNamaObat(String namaObat) { this.namaObat = namaObat; }
    public void setProdusen(String produsen) { this.produsen = produsen; }
    public void setSatuan(String satuan) { this.satuan = satuan; }
    public void setStok(int stok) { this.stok = stok; }
    public void setHargaBeli(double hargaBeli) { this.hargaBeli = hargaBeli; }
    public void setHargaJual(double hargaJual) { this.hargaJual = hargaJual; }
    public void setTanggalKadaluarsa(LocalDate tanggalKadaluarsa) { this.tanggalKadaluarsa = tanggalKadaluarsa; }

    @Override
    public String toString() {
        return "Obat{" +
               "kodeObat='" + kodeObat + '\'' +
               ", namaObat='" + namaObat + '\'' +
               ", stok=" + stok +
               '}';
    }
}