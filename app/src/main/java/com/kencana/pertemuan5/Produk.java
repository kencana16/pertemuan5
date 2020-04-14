package com.kencana.pertemuan5;

public class Produk {
    private String kode;
    private String nama;
    private String harga;
    private Integer jumlah = 0;
    //private int img;
    private String img;
    public Produk(){}
    public Produk(String kode, String nama, String harga,String img) {
        this.kode = kode;
        this.nama = nama;
        this.harga = harga;
        this.img=img;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }
    public void setImg(String img) {
        this.img = img;
    }

    public Integer getJumlah() {
        return jumlah;
    }

    public void setJumlah(Integer jumlah) {
        this.jumlah = jumlah;
    }

    public String getImg() { return "http://192.168.43.168/mobileandroid/pertemuan5/uploads/"+img;  }
}
