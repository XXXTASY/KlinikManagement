/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.model;

import application.db.Connector;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author XXXTASY
 */
public class KlinikData {
    private List<Pasien> daftarPasien;
    private List<Dokter> daftarDokter;
    private List<Obat> daftarObat;

    public KlinikData() {
        daftarPasien = new ArrayList<>();
        daftarDokter = new ArrayList<>();
        daftarObat = new ArrayList<>();
        loadPasienFromDatabase();
        loadDokterFromDatabase();
        loadObatFromDatabase();
    }

    public boolean authenticateUser(String username, String password) {
        String sql = "SELECT password FROM users WHERE username = ?";
        try (Connection conn = Connector.openConnect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password");
                return BCrypt.checkpw(password, storedHash);
            }
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
        }
        return false;
    }

    public boolean registerUser(String username, String password) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM users WHERE username = ?";
        String insertSql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection conn = Connector.openConnect()) {
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return false;
                }
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                insertStmt.setString(1, username);
                insertStmt.setString(2, hashedPassword);
                insertStmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
            throw e;
        }
    }

    public List<Pasien> getDaftarPasien() {
        return new ArrayList<>(daftarPasien);
    }

    public void loadPasienFromDatabase() {
        daftarPasien.clear();
        String sql = "SELECT id_pasien, nama, tanggal_lahir, alamat, telepon FROM pasien";
        try (Connection conn = Connector.openConnect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String idPasien = rs.getString("id_pasien");
                String nama = rs.getString("nama");
                LocalDate tanggalLahir = rs.getDate("tanggal_lahir").toLocalDate();
                String alamat = rs.getString("alamat");
                String telepon = rs.getString("telepon");
                daftarPasien.add(new Pasien(idPasien, nama, tanggalLahir, alamat, telepon));
            }
        } catch (SQLException e) {
            System.err.println("Error loading pasien data from database: " + e.getMessage());
        }
    }

    public List<Pasien> cariPasien(String keyword) throws SQLException {
        List<Pasien> hasilPencarian = new ArrayList<>();
        String sql = "SELECT id_pasien, nama, tanggal_lahir, alamat, telepon FROM pasien " +
                     "WHERE id_pasien LIKE ? OR nama LIKE ?";
        try (Connection conn = Connector.openConnect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String idPasien = rs.getString("id_pasien");
                String nama = rs.getString("nama");
                LocalDate tanggalLahir = rs.getDate("tanggal_lahir").toLocalDate();
                String alamat = rs.getString("alamat");
                String telepon = rs.getString("telepon");
                hasilPencarian.add(new Pasien(idPasien, nama, tanggalLahir, alamat, telepon));
            }
        }
        return hasilPencarian;
    }

    public boolean tambahPasien(Pasien pasien) throws SQLException {
        if (getPasienById(pasien.getIdPasien()).isPresent()) {
            return false;
        }
        String sql = "INSERT INTO pasien (id_pasien, nama, tanggal_lahir, alamat, telepon) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Connector.openConnect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pasien.getIdPasien());
            pstmt.setString(2, pasien.getNama());
            pstmt.setDate(3, Date.valueOf(pasien.getTanggalLahir()));
            pstmt.setString(4, pasien.getAlamat());
            pstmt.setString(5, pasien.getTelepon());
            pstmt.executeUpdate();
            loadPasienFromDatabase();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding pasien: " + e.getMessage());
            throw e;
        }
    }

    public Optional<Pasien> getPasienById(String id) {
        return daftarPasien.stream().filter(p -> p.getIdPasien().equals(id)).findFirst();
    }

    public boolean updatePasien(Pasien pasienLama, Pasien pasienBaru) throws SQLException {
        if (!pasienLama.getIdPasien().equals(pasienBaru.getIdPasien()) && getPasienById(pasienBaru.getIdPasien()).isPresent()) {
            return false;
        }
        String sql = "UPDATE pasien SET id_pasien = ?, nama = ?, tanggal_lahir = ?, alamat = ?, telepon = ? WHERE id_pasien = ?";
        try (Connection conn = Connector.openConnect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pasienBaru.getIdPasien());
            pstmt.setString(2, pasienBaru.getNama());
            pstmt.setDate(3, Date.valueOf(pasienBaru.getTanggalLahir()));
            pstmt.setString(4, pasienBaru.getAlamat());
            pstmt.setString(5, pasienBaru.getTelepon());
            pstmt.setString(6, pasienLama.getIdPasien());
            pstmt.executeUpdate();
            loadPasienFromDatabase();
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating pasien: " + e.getMessage());
            throw e;
        }
    }

    public boolean hapusPasien(Pasien pasien) throws SQLException {
        String sql = "DELETE FROM pasien WHERE id_pasien = ?";
        try (Connection conn = Connector.openConnect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pasien.getIdPasien());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                loadPasienFromDatabase();
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error deleting pasien: " + e.getMessage());
            throw e;
        }
    }

    public List<Dokter> getDaftarDokter() {
        return new ArrayList<>(daftarDokter);
    }

    public void loadDokterFromDatabase() {
        daftarDokter.clear();
        String sql = "SELECT id_dokter, nama, spesialisasi, telepon, jam_mulai_praktik, jam_selesai_praktik FROM dokter";
        try (Connection conn = Connector.openConnect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String idDokter = rs.getString("id_dokter");
                String nama = rs.getString("nama");
                String spesialisasi = rs.getString("spesialisasi");
                String telepon = rs.getString("telepon");
                LocalTime jamMulai = rs.getTime("jam_mulai_praktik").toLocalTime();
                LocalTime jamSelesai = rs.getTime("jam_selesai_praktik").toLocalTime();
                daftarDokter.add(new Dokter(idDokter, nama, spesialisasi, telepon, jamMulai, jamSelesai));
            }
        } catch (SQLException e) {
            System.err.println("Error loading dokter data from database: " + e.getMessage());
        }
    }

    public List<Dokter> cariDokter(String keyword) throws SQLException {
        List<Dokter> hasilPencarian = new ArrayList<>();
        String sql = "SELECT id_dokter, nama, spesialisasi, telepon, jam_mulai_praktik, jam_selesai_praktik FROM dokter " +
                     "WHERE id_dokter LIKE ? OR nama LIKE ? OR spesialisasi LIKE ?";
        try (Connection conn = Connector.openConnect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String idDokter = rs.getString("id_dokter");
                String nama = rs.getString("nama");
                String spesialisasi = rs.getString("spesialisasi");
                String telepon = rs.getString("telepon");
                LocalTime jamMulai = rs.getTime("jam_mulai_praktik").toLocalTime();
                LocalTime jamSelesai = rs.getTime("jam_selesai_praktik").toLocalTime();
                hasilPencarian.add(new Dokter(idDokter, nama, spesialisasi, telepon, jamMulai, jamSelesai));
            }
        }
        return hasilPencarian;
    }

    public boolean tambahDokter(Dokter dokter) throws SQLException {
        if (getDokterById(dokter.getIdDokter()).isPresent()) {
            return false;
        }
        String sql = "INSERT INTO dokter (id_dokter, nama, spesialisasi, telepon, jam_mulai_praktik, jam_selesai_praktik) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Connector.openConnect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dokter.getIdDokter());
            pstmt.setString(2, dokter.getNama());
            pstmt.setString(3, dokter.getSpesialisasi());
            pstmt.setString(4, dokter.getTelepon());
            pstmt.setTime(5, Time.valueOf(dokter.getJamMulaiPraktik()));
            pstmt.setTime(6, Time.valueOf(dokter.getJamSelesaiPraktik()));
            pstmt.executeUpdate();
            loadDokterFromDatabase();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding dokter: " + e.getMessage());
            throw e;
        }
    }

    public Optional<Dokter> getDokterById(String id) {
        return daftarDokter.stream().filter(d -> d.getIdDokter().equals(id)).findFirst();
    }

    public boolean updateDokter(Dokter dokterLama, Dokter dokterBaru) throws SQLException {
        if (!dokterLama.getIdDokter().equals(dokterBaru.getIdDokter()) && getDokterById(dokterBaru.getIdDokter()).isPresent()) {
            return false;
        }
        String sql = "UPDATE dokter SET id_dokter = ?, nama = ?, spesialisasi = ?, telepon = ?, jam_mulai_praktik = ?, jam_selesai_praktik = ? WHERE id_dokter = ?";
        try (Connection conn = Connector.openConnect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dokterBaru.getIdDokter());
            pstmt.setString(2, dokterBaru.getNama());
            pstmt.setString(3, dokterBaru.getSpesialisasi());
            pstmt.setString(4, dokterBaru.getTelepon());
            pstmt.setTime(5, Time.valueOf(dokterBaru.getJamMulaiPraktik()));
            pstmt.setTime(6, Time.valueOf(dokterBaru.getJamSelesaiPraktik()));
            pstmt.setString(7, dokterLama.getIdDokter());
            pstmt.executeUpdate();
            loadDokterFromDatabase();
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating dokter: " + e.getMessage());
            throw e;
        }
    }

    public boolean hapusDokter(Dokter dokter) throws SQLException {
        String sql = "DELETE FROM dokter WHERE id_dokter = ?";
        try (Connection conn = Connector.openConnect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dokter.getIdDokter());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                loadDokterFromDatabase();
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error deleting dokter: " + e.getMessage());
            throw e;
        }
    }
    
    public List<Obat> getDaftarObat() {
        return new ArrayList<>(daftarObat);
    }
    
    public void loadObatFromDatabase() {
        daftarObat.clear();
        String sql = "SELECT kode_obat, nama_obat, produsen, satuan, stok, harga_beli, harga_jual, tanggal_kadaluarsa FROM obat";
        try (Connection conn = Connector.openConnect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String kodeObat = rs.getString("kode_obat");
                String namaObat = rs.getString("nama_obat");
                String produsen = rs.getString("produsen");
                String satuan = rs.getString("satuan");
                int stok = rs.getInt("stok");
                double hargaBeli = rs.getDouble("harga_beli");
                double hargaJual = rs.getDouble("harga_jual");
                LocalDate tanggalKadaluarsa = rs.getDate("tanggal_kadaluarsa") != null ? rs.getDate("tanggal_kadaluarsa").toLocalDate() : null;
                daftarObat.add(new Obat(kodeObat, namaObat, produsen, satuan, stok, hargaBeli, hargaJual, tanggalKadaluarsa));
            }
        } catch (SQLException e) {
            System.err.println("Error loading obat data from database: " + e.getMessage());
        }
    }

    public boolean tambahObat(Obat obat) throws SQLException {
        if (getObatByKode(obat.getKodeObat()).isPresent()) {
            System.err.println("Error: Kode Obat " + obat.getKodeObat() + " sudah ada.");
            return false; // Kode obat sudah ada
        }
        String sql = "INSERT INTO obat (kode_obat, nama_obat, produsen, satuan, stok, harga_beli, harga_jual, tanggal_kadaluarsa) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Connector.openConnect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, obat.getKodeObat());
            pstmt.setString(2, obat.getNamaObat());
            pstmt.setString(3, obat.getProdusen());
            pstmt.setString(4, obat.getSatuan());
            pstmt.setInt(5, obat.getStok());
            pstmt.setDouble(6, obat.getHargaBeli());
            pstmt.setDouble(7, obat.getHargaJual());
            if (obat.getTanggalKadaluarsa() != null) {
                pstmt.setDate(8, Date.valueOf(obat.getTanggalKadaluarsa()));
            } else {
                pstmt.setNull(8, java.sql.Types.DATE);
            }
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                loadObatFromDatabase(); // Muat ulang semua data
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error adding obat: " + e.getMessage());
            throw e;
        }
    }

    public Optional<Obat> getObatByKode(String kodeObat) {
        return daftarObat.stream().filter(o -> o.getKodeObat().equalsIgnoreCase(kodeObat)).findFirst();
    }

    public boolean updateObat(Obat obatLama, Obat obatBaru) throws SQLException {
        // Jika kode obat diubah dan kode baru sudah ada (dan bukan milik obat lama), tolak.
        if (!obatLama.getKodeObat().equalsIgnoreCase(obatBaru.getKodeObat()) && getObatByKode(obatBaru.getKodeObat()).isPresent()) {
             System.err.println("Error: Kode Obat baru " + obatBaru.getKodeObat() + " sudah digunakan oleh obat lain.");
             return false;
        }

        String sql = "UPDATE obat SET kode_obat = ?, nama_obat = ?, produsen = ?, satuan = ?, stok = ?, harga_beli = ?, harga_jual = ?, tanggal_kadaluarsa = ? WHERE kode_obat = ?";
        try (Connection conn = Connector.openConnect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, obatBaru.getKodeObat());
            pstmt.setString(2, obatBaru.getNamaObat());
            pstmt.setString(3, obatBaru.getProdusen());
            pstmt.setString(4, obatBaru.getSatuan());
            pstmt.setInt(5, obatBaru.getStok());
            pstmt.setDouble(6, obatBaru.getHargaBeli());
            pstmt.setDouble(7, obatBaru.getHargaJual());
            if (obatBaru.getTanggalKadaluarsa() != null) {
                pstmt.setDate(8, Date.valueOf(obatBaru.getTanggalKadaluarsa()));
            } else {
                pstmt.setNull(8, java.sql.Types.DATE);
            }
            pstmt.setString(9, obatLama.getKodeObat()); // WHERE clause menggunakan kode_obat lama

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                loadObatFromDatabase();
                return true;
            }
            return false; // Bisa jadi obatLama.getKodeObat() tidak ditemukan
        } catch (SQLException e) {
            System.err.println("Error updating obat: " + e.getMessage());
            throw e;
        }
    }

    public boolean hapusObat(Obat obat) throws SQLException {
        String sql = "DELETE FROM obat WHERE kode_obat = ?";
        try (Connection conn = Connector.openConnect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, obat.getKodeObat());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                loadObatFromDatabase();
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error deleting obat: " + e.getMessage());
            throw e;
        }
    }
}
