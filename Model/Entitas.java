/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package id.ac.unpas.Kelompok7_PP2_D_2025.Model;

/**
 *
 * @author User
 */
public class Entitas {
      public static class Kategori {
        public int id;
        public String nama;
        public Kategori(int id, String n) { 
            this.id = id; 
            this.nama = n; 
        }
    }
    
    public static class Kontak {
        public int id;
        public String nama, tipe, telepon;
        public Kontak(int id, String n, String t, String tel) {
            this.id = id; 
            this.nama = n; 
            this.tipe = t; 
            this.telepon = tel;
        }
    }
    
    public static class Transaksi {
        public int id;
        public String tanggal, keterangan, tipe, namaKat;
        public double jumlah;
        public int idKat;
        public Transaksi(int id, String tgl, String ket, double jml, String tipe, int idKat, String namaKat) {
            this.id = id; 
            this.tanggal = tgl; 
            this.keterangan = ket;
            this.jumlah = jml; 
            this.tipe = tipe; 
            this.idKat = idKat;
            this.namaKat = namaKat;
        }
    }
}
