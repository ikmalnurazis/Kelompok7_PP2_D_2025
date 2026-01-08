/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package id.ac.unpas.Kelompok7_PP2_D_2025.View;

import id.ac.unpas.Kelompok7_PP2_D_2025.Controlller.Controller;
import id.ac.unpas.Kelompok7_PP2_D_2025.Model.Entitas.*;
import id.ac.unpas.Kelompok7_PP2_D_2025.Model.Koneksi_DB;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author Acer
 */
public class mainApp extends JFrame {
    private Controller control = new Controller();
    private JTabbedPane tabs = new JTabbedPane();
    
    // Komponen Transaksi
    private JTextField tIdTrx = new JTextField(), tTanggal = new JTextField(), 
                       tKeterangan = new JTextField(), tJumlah = new JTextField(), tCariTrx = new JTextField(15);
    private JComboBox<String> cbTipeTrx = new JComboBox<>(new String[]{"Pemasukan", "Pengeluaran"});
    private JComboBox<String> cbKategoriTrx = new JComboBox<>();
    private JTable tblTrx = new JTable();
    private DefaultTableModel modTrx = new DefaultTableModel(
        new Object[]{"ID", "Tanggal", "Keterangan", "Jumlah", "Tipe", "Kategori"}, 0);
    
    // Komponen Laporan
    private JLabel lblPemasukan = new JLabel("Rp 0");
    private JLabel lblPengeluaran = new JLabel("Rp 0");
    private JLabel lblSaldo = new JLabel("Rp 0");

    public mainApp() {
        setTitle("SimpleAcc - Aplikasi Pencatatan Keuangan UMKM");
        setSize(1000, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabs.addTab("Transaksi", panelTransaksi());
        tabs.addTab("Laporan Keuangan", panelLaporan());
        add(tabs);

        loadTransaksi("");
        loadKategoriComboBox();
        updateLaporan();
    }

    // PANEL TRANSAKSI
    
    private JPanel panelTransaksi() {
        JPanel main = new JPanel(new BorderLayout());

        JPanel pCari = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton bCari = new JButton("Cari");
        pCari.add(new JLabel("Cari Transaksi:")); 
        pCari.add(tCariTrx); 
        pCari.add(bCari);

        JPanel pForm = new JPanel(new GridLayout(6, 2, 5, 5));
        pForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tIdTrx.setEditable(false);
        pForm.add(new JLabel("ID:")); pForm.add(tIdTrx);
        pForm.add(new JLabel("Tanggal (YYYY-MM-DD):")); pForm.add(tTanggal);
        pForm.add(new JLabel("Keterangan:")); pForm.add(tKeterangan);
        pForm.add(new JLabel("Jumlah (Rp):")); pForm.add(tJumlah);
        pForm.add(new JLabel("Tipe:")); pForm.add(cbTipeTrx);
        pForm.add(new JLabel("Kategori:")); pForm.add(cbKategoriTrx);

        JPanel pBtn = new JPanel();
        JButton bSim = new JButton("Simpan"), bUpd = new JButton("Update"), 
                bHap = new JButton("Hapus"), bPdf = new JButton("Export PDF"), bClr = new JButton("Clear");
        pBtn.add(bSim); pBtn.add(bUpd); pBtn.add(bHap); pBtn.add(bPdf); pBtn.add(bClr);

        JPanel pNorth = new JPanel(new BorderLayout());
        pNorth.add(pCari, BorderLayout.NORTH);
        pNorth.add(pForm, BorderLayout.CENTER);
        pNorth.add(pBtn, BorderLayout.SOUTH);

        tblTrx.setModel(modTrx);
        main.add(pNorth, BorderLayout.NORTH);
        main.add(new JScrollPane(tblTrx), BorderLayout.CENTER);

        bSim.addActionListener(e -> {
            if (!validateTransaksiInput()) return;
            try {
                String katItem = cbKategoriTrx.getSelectedItem().toString();
                int idKat = Integer.parseInt(katItem.split(" - ")[0]);
                
                control.tambahTransaksi(new Transaksi(0, tTanggal.getText().trim(), 
                    tKeterangan.getText().trim(), Double.parseDouble(tJumlah.getText().trim()),
                    cbTipeTrx.getSelectedItem().toString(), idKat, ""));
                JOptionPane.showMessageDialog(this, "Transaksi Berhasil Disimpan");
                loadTransaksi(""); 
                updateLaporan();
                clearTransaksi();
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
            }
        });

        bUpd.addActionListener(e -> {
            if (!validateTransaksiInput()) return;
            try {
                int id = Integer.parseInt(tIdTrx.getText().trim());
                String katItem = cbKategoriTrx.getSelectedItem().toString();
                int idKat = Integer.parseInt(katItem.split(" - ")[0]);
                
                control.ubahTransaksi(new Transaksi(id, tTanggal.getText().trim(), 
                    tKeterangan.getText().trim(), Double.parseDouble(tJumlah.getText().trim()),
                    cbTipeTrx.getSelectedItem().toString(), idKat, ""));
                JOptionPane.showMessageDialog(this, "Transaksi Berhasil Diupdate");
                loadTransaksi(""); 
                updateLaporan();
                clearTransaksi();
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
            }
        });

        bHap.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus transaksi ini?", 
                "Hapus", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION) {
                try {
                    int id = Integer.parseInt(tIdTrx.getText().trim());
                    control.hapusTransaksi(id);
                    JOptionPane.showMessageDialog(this, "Transaksi Berhasil Dihapus");
                    loadTransaksi(""); 
                    updateLaporan();
                    clearTransaksi();
                } catch (Exception ex) { 
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
                }
            }
        });

        bCari.addActionListener(e -> loadTransaksi(tCariTrx.getText()));
        
        bPdf.addActionListener(e -> {
            if (control.exportPDF(tblTrx, "Data_Transaksi_SimpleAcc.pdf")) {
                JOptionPane.showMessageDialog(this, 
                    "Data berhasil di-export ke PDF!\nLokasi: Downloads/Data_Transaksi_SimpleAcc.pdf");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal export PDF!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        bClr.addActionListener(e -> clearTransaksi());

        tblTrx.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = tblTrx.getSelectedRow();
                tIdTrx.setText(modTrx.getValueAt(r, 0).toString());
                tTanggal.setText(modTrx.getValueAt(r, 1).toString());
                tKeterangan.setText(modTrx.getValueAt(r, 2).toString());
                tJumlah.setText(modTrx.getValueAt(r, 3).toString());
                cbTipeTrx.setSelectedItem(modTrx.getValueAt(r, 4).toString());
            }
        });

        return main;
    }

    // PANEL LAPORAN KEUANGAN
    
    private JPanel panelLaporan() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel pInfo = new JPanel(new GridLayout(4, 2, 10, 15));
        pInfo.setBorder(BorderFactory.createTitledBorder("Ringkasan Keuangan"));
        
        Font labelFont = new Font("Arial", Font.BOLD, 16);
        Font valueFont = new Font("Arial", Font.PLAIN, 18);
        
        JLabel lbl1 = new JLabel("Total Pemasukan:");
        lbl1.setFont(labelFont);
        lblPemasukan.setFont(valueFont);
        lblPemasukan.setForeground(new Color(39, 174, 96));
        
        JLabel lbl2 = new JLabel("Total Pengeluaran:");
        lbl2.setFont(labelFont);
        lblPengeluaran.setFont(valueFont);
        lblPengeluaran.setForeground(new Color(231, 76, 60));
        
        JLabel lbl3 = new JLabel("Saldo:");
        lbl3.setFont(labelFont);
        lblSaldo.setFont(new Font("Arial", Font.BOLD, 20));
        lblSaldo.setForeground(new Color(52, 152, 219));
        
        pInfo.add(lbl1); pInfo.add(lblPemasukan);
        pInfo.add(lbl2); pInfo.add(lblPengeluaran);
        pInfo.add(lbl3); pInfo.add(lblSaldo);
        
        JButton bRefresh = new JButton("Refresh Laporan");
        bRefresh.addActionListener(e -> updateLaporan());
        pInfo.add(new JLabel("")); 
        pInfo.add(bRefresh);
        
        main.add(pInfo, BorderLayout.NORTH);
        
        return main;
    }

    // HELPER METHODS
    
    private void loadKategori(String key) {
        modKat.setRowCount(0);
        try {
            List<Kategori> list = key.isEmpty() ? control.getAllKategori() : control.cariKategori(key);
            for(Kategori k : list) modKat.addRow(new Object[]{k.id, k.nama});
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadKontak(String key) {
        modKon.setRowCount(0);
        try {
            List<Kontak> list = key.isEmpty() ? control.getAllKontak() : control.cariKontak(key);
            for(Kontak k : list) modKon.addRow(new Object[]{k.id, k.nama, k.tipe, k.telepon});
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadTransaksi(String key) {
        modTrx.setRowCount(0);
        try {
            List<Transaksi> list = key.isEmpty() ? control.getAllTransaksi() : control.cariTransaksi(key);
            NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            for(Transaksi t : list) {
                modTrx.addRow(new Object[]{t.id, t.tanggal, t.keterangan, 
                    nf.format(t.jumlah), t.tipe, t.namaKat});
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private void loadKategoriComboBox() {
        cbKategoriTrx.removeAllItems();
        try {
            List<Kategori> list = control.getAllKategori();
            for(Kategori k : list) {
                cbKategoriTrx.addItem(k.id + " - " + k.nama);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private void updateLaporan() {
        try {
            Map<String, Double> laporan = control.getLaporanKeuangan();
            NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            
            lblPemasukan.setText(nf.format(laporan.getOrDefault("pemasukan", 0.0)));
            lblPengeluaran.setText(nf.format(laporan.getOrDefault("pengeluaran", 0.0)));
            lblSaldo.setText(nf.format(laporan.getOrDefault("saldo", 0.0)));
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void clearKategori() {
        tIdKat.setText(""); 
        tNamaKat.setText(""); 
        tCariKat.setText("");
    }

    private void clearKontak() {
        tIdKon.setText(""); 
        tNamaKon.setText(""); 
        tTelepon.setText(""); 
        tCariKon.setText("");
        cbTipeKontak.setSelectedIndex(0);
    }

    private void clearTransaksi() {
        tIdTrx.setText(""); 
        tTanggal.setText(""); 
        tKeterangan.setText(""); 
        tJumlah.setText(""); 
        tCariTrx.setText("");
        cbTipeTrx.setSelectedIndex(0);
    }
}
