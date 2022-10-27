package com.example.bumdes;

public class ListTransaction {
    private String tanggal, kode_akun, harga, quantity, itemName;

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getKode_akun() {
        return kode_akun;
    }

    public void setKode_akun(String kode_akun) {
        this.kode_akun = kode_akun;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public ListTransaction(String tanggal, String kode_akun, String harga, String quantity, String itemName) {
        this.tanggal = tanggal;
        this.kode_akun = kode_akun;
        this.harga = harga;
        this.quantity = quantity;
        this.itemName = itemName;
    }
}
