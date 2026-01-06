/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package id.ac.unpas.Kelompok7_PP2_D_2025.Controller;
import id.ac.unpas.Kelompok7_PP2_D_2025.Model.Entitas.*;
import id.ac.unpas.Kelompok7_PP2_D_2025.Model.Koneksi_DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author User
 */
public class Controller {
// ============================================================
    // 1. FITUR KATEGORI (CREATE, READ, UPDATE, DELETE, SEARCH)
    // ============================================================
    
    public List<Kategori> getAllKategori() throws SQLException {
        List<Kategori> list = new ArrayList<>();
        Connection conn = Koneksi_DB.configDB();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM kategori");
        while(rs.next()) {
            list.add(new Kategori(rs.getInt("id"), rs.getString("nama")));
        }
        return list;
    }

    public void tambahKategori(Kategori k) throws SQLException {
        Connection conn = Koneksi_DB.configDB();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO kategori (nama) VALUES (?)");
        ps.setString(1, k.nama);
        ps.execute();
    }

    public void ubahKategori(Kategori k) throws SQLException {
        Connection conn = Koneksi_DB.configDB();
        PreparedStatement ps = conn.prepareStatement("UPDATE kategori SET nama=? WHERE id=?");
        ps.setString(1, k.nama);
        ps.setInt(2, k.id);
        ps.executeUpdate();
    }

    public void hapusKategori(int id) throws SQLException {
        Connection conn = Koneksi_DB.configDB();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM kategori WHERE id=?");
        ps.setInt(1, id);
        ps.execute();
    }

    public List<Kategori> cariKategori(String key) throws SQLException {
        List<Kategori> list = new ArrayList<>();
        Connection conn = Koneksi_DB.configDB();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM kategori WHERE nama LIKE ?");
        ps.setString(1, "%" + key + "%");
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            list.add(new Kategori(rs.getInt("id"), rs.getString("nama")));
        }
        return list;
    }
    
    // ============================================================
    // 2. FITUR KONTAK (CREATE, READ, UPDATE, DELETE, SEARCH)
    // ============================================================

    public List<Kontak> getAllKontak() throws SQLException {
        List<Kontak> list = new ArrayList<>();
        Connection conn = Koneksi_DB.configDB();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM kontak");
        while(rs.next()) {
            list.add(new Kontak(rs.getInt("id"), rs.getString("nama"), 
                    rs.getString("tipe"), rs.getString("telepon")));
        }
        return list;
    }

    public void tambahKontak(Kontak k) throws SQLException {
        Connection conn = Koneksi_DB.configDB();
        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO kontak (nama, tipe, telepon) VALUES (?,?,?)");
        ps.setString(1, k.nama);
        ps.setString(2, k.tipe);
        ps.setString(3, k.telepon);
        ps.execute();
    }

    public void ubahKontak(Kontak k) throws SQLException {
        Connection conn = Koneksi_DB.configDB();
        PreparedStatement ps = conn.prepareStatement(
            "UPDATE kontak SET nama=?, tipe=?, telepon=? WHERE id=?");
        ps.setString(1, k.nama);
        ps.setString(2, k.tipe);
        ps.setString(3, k.telepon);
        ps.setInt(4, k.id);
        ps.executeUpdate();
    }

    public void hapusKontak(int id) throws SQLException {
        Connection conn = Koneksi_DB.configDB();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM kontak WHERE id=?");
        ps.setInt(1, id);
        ps.execute();
    }

    public List<Kontak> cariKontak(String key) throws SQLException {
        List<Kontak> list = new ArrayList<>();
        Connection conn = Koneksi_DB.configDB();
        PreparedStatement ps = conn.prepareStatement(
            "SELECT * FROM kontak WHERE nama LIKE ? OR telepon LIKE ?");
        ps.setString(1, "%" + key + "%");
        ps.setString(2, "%" + key + "%");
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            list.add(new Kontak(rs.getInt("id"), rs.getString("nama"), 
                    rs.getString("tipe"), rs.getString("telepon")));
        }
        return list;
    }
    
    // ============================================================
    // 3. FITUR TRANSAKSI (CREATE, READ, UPDATE, DELETE, SEARCH)
    // ============================================================

    public List<Transaksi> getAllTransaksi() throws SQLException {
        List<Transaksi> list = new ArrayList<>();
        Connection conn = Koneksi_DB.configDB();
        String sql = "SELECT t.*, k.nama as nama_kategori FROM transaksi t " +
                     "LEFT JOIN kategori k ON t.id_kategori = k.id";
        ResultSet rs = conn.createStatement().executeQuery(sql);
        while(rs.next()) {
            list.add(new Transaksi(
                rs.getInt("id"),
                rs.getString("tanggal"),
                rs.getString("keterangan"),
                rs.getDouble("jumlah"),
                rs.getString("tipe"),
                rs.getInt("id_kategori"),
                rs.getString("nama_kategori")
            ));
        }
        return list;
    }

    public void tambahTransaksi(Transaksi t) throws SQLException {
        Connection conn = Koneksi_DB.configDB();
        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO transaksi (tanggal, keterangan, jumlah, tipe, id_kategori) VALUES (?,?,?,?,?)");
        ps.setString(1, t.tanggal);
        ps.setString(2, t.keterangan);
        ps.setDouble(3, t.jumlah);
        ps.setString(4, t.tipe);
        ps.setInt(5, t.idKat);
        ps.execute();
    }

    public void ubahTransaksi(Transaksi t) throws SQLException {
        Connection conn = Koneksi_DB.configDB();
        PreparedStatement ps = conn.prepareStatement(
            "UPDATE transaksi SET tanggal=?, keterangan=?, jumlah=?, tipe=?, id_kategori=? WHERE id=?");
        ps.setString(1, t.tanggal);
        ps.setString(2, t.keterangan);
        ps.setDouble(3, t.jumlah);
        ps.setString(4, t.tipe);
        ps.setInt(5, t.idKat);
        ps.setInt(6, t.id);
        ps.executeUpdate();
    }

    public void hapusTransaksi(int id) throws SQLException {
        Connection conn = Koneksi_DB.configDB();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM transaksi WHERE id=?");
        ps.setInt(1, id);
        ps.execute();
    }

    public List<Transaksi> cariTransaksi(String key) throws SQLException {
        List<Transaksi> list = new ArrayList<>();
        Connection conn = Koneksi_DB.configDB();
        String sql = "SELECT t.*, k.nama as nama_kategori FROM transaksi t " +
                     "LEFT JOIN kategori k ON t.id_kategori = k.id " +
                     "WHERE t.keterangan LIKE ? OR t.tanggal LIKE ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + key + "%");
        ps.setString(2, "%" + key + "%");
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            list.add(new Transaksi(
                rs.getInt("id"),
                rs.getString("tanggal"),
                rs.getString("keterangan"),
                rs.getDouble("jumlah"),
                rs.getString("tipe"),
                rs.getInt("id_kategori"),
                rs.getString("nama_kategori")
            ));
        }
        return list;
    }
    
    // ============================================================
    // FITUR LAPORAN KEUANGAN
    // ============================================================
    
    public Map<String, Double> getLaporanKeuangan() throws SQLException {
        Map<String, Double> laporan = new HashMap<>();
        Connection conn = Koneksi_DB.configDB();
        
        // Hitung total pemasukan
        ResultSet rsPemasukan = conn.createStatement().executeQuery(
            "SELECT SUM(jumlah) as total FROM transaksi WHERE tipe='Pemasukan'");
        if(rsPemasukan.next()) {
            laporan.put("pemasukan", rsPemasukan.getDouble("total"));
        }
        
        // Hitung total pengeluaran
        ResultSet rsPengeluaran = conn.createStatement().executeQuery(
            "SELECT SUM(jumlah) as total FROM transaksi WHERE tipe='Pengeluaran'");
        if(rsPengeluaran.next()) {
            laporan.put("pengeluaran", rsPengeluaran.getDouble("total"));
        }
        
        // Hitung saldo
        double pemasukan = laporan.getOrDefault("pemasukan", 0.0);
        double pengeluaran = laporan.getOrDefault("pengeluaran", 0.0);
        laporan.put("saldo", pemasukan - pengeluaran);
        
        return laporan;
    }
    
    // ============================================================
    // FITUR EXPORT PDF
    // ============================================================
    
    public boolean exportPDF(JTable table, String filename) {
        try {
            Document doc = new Document(PageSize.A4.rotate());
            String userHome = System.getProperty("user.home");
            String filePath = userHome + File.separator + "Downloads" + File.separator + filename;
            
            PdfWriter.getInstance(doc, new FileOutputStream(filePath));
            doc.open();
            
            // Judul
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("LAPORAN KEUANGAN - SimpleAcc UMKM", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10);
            doc.add(title);
            
            // Tanggal export
            Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
            Paragraph date = new Paragraph("Tanggal Export: " + sdf.format(new Date()), dateFont);
            date.setAlignment(Element.ALIGN_CENTER);
            date.setSpacingAfter(20);
            doc.add(date);
            
            // Tabel PDF
            PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
            pdfTable.setWidthPercentage(100);
            
            // Header
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            for (int i = 0; i < table.getColumnCount(); i++) {
                PdfPCell cell = new PdfPCell(new Phrase(table.getColumnName(i), headerFont));
                cell.setBackgroundColor(new BaseColor(41, 128, 185));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(8);
                pdfTable.addCell(cell);
            }
            
            // Data
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            for (int row = 0; row < table.getRowCount(); row++) {
                for (int col = 0; col < table.getColumnCount(); col++) {
                    Object value = table.getValueAt(row, col);
                    PdfPCell cell = new PdfPCell(new Phrase(value != null ? value.toString() : "", dataFont));
                    cell.setPadding(5);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    
                    if (row % 2 == 0) {
                        cell.setBackgroundColor(new BaseColor(236, 240, 241));
                    }
                    pdfTable.addCell(cell);
                }
            }
            
            doc.add(pdfTable);
            
            // Footer
            Paragraph footer = new Paragraph("\n\nTotal Data: " + table.getRowCount() + " record(s)", dateFont);
            footer.setAlignment(Element.ALIGN_RIGHT);
            doc.add(footer);
            
            doc.close();
            System.out.println("PDF berhasil dibuat di: " + filePath);
            return true;
            
        } catch (Exception e) {
            System.err.println("Error saat membuat PDF: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}

