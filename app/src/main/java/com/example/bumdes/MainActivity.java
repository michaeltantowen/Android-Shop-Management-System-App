package com.example.bumdes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper db;
    Button menuTransaksi, menuAssets, menuBku;
    ArrayList<String> penjualan, pembelian;
    TextView tvLabaRugi, profit, lost, oneMonthShow, threeMonthShow, oneYearShow, oneMonthHide, threeMonthHide, oneYearHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuTransaksi = findViewById(R.id.menuTransaksi);
        menuAssets = findViewById(R.id.menuAssets);
        menuBku = findViewById(R.id.menuBukuKas);
        tvLabaRugi = findViewById(R.id.tvLabaRugi);
        profit = findViewById(R.id.profit);
        lost = findViewById(R.id.lost);
        oneMonthShow = findViewById(R.id.oneMonthShow);
        threeMonthShow = findViewById(R.id.threeMonthShow);
        oneYearShow = findViewById(R.id.oneYearShow);
        oneMonthHide = findViewById(R.id.oneMonthHide);
        threeMonthHide = findViewById(R.id.threeMonthHide);
        oneYearHide = findViewById(R.id.oneYearHide);

        penjualan = new ArrayList<>();
        pembelian = new ArrayList<>();
        db = new DatabaseHelper(MainActivity.this);

        readPembelian1Month();
        readPenjualan1Month();
        readPembelian3Month();
        readPenjualan3Month();
        readPembelian1Year();
        readPenjualan1Year();

        tvLabaRugi.setText(formatNumberCurrency(String.valueOf(Integer.valueOf(penjualan.get(0)) - Integer.valueOf(pembelian.get(0)))));
        if(Integer.valueOf(penjualan.get(0)) - Integer.valueOf(pembelian.get(0)) < 0){
            profit.setVisibility(View.GONE);
            lost.setVisibility(View.VISIBLE);
        }else if(Integer.valueOf(penjualan.get(0)) - Integer.valueOf(pembelian.get(0)) > 0){
            profit.setVisibility(View.VISIBLE);
            lost.setVisibility(View.GONE);
        }else{
            profit.setVisibility(View.GONE);
            lost.setVisibility(View.GONE);
        }

        oneMonthHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneMonthShow.setVisibility(View.VISIBLE);
                oneMonthHide.setVisibility(View.GONE);
                oneYearShow.setVisibility(View.GONE);
                threeMonthShow.setVisibility(View.GONE);
                threeMonthHide.setVisibility(View.VISIBLE);
                oneYearHide.setVisibility(View.VISIBLE);

                tvLabaRugi.setText(formatNumberCurrency(String.valueOf(Integer.valueOf(penjualan.get(0)) - Integer.valueOf(pembelian.get(0)))));
                if(Integer.valueOf(penjualan.get(0)) - Integer.valueOf(pembelian.get(0)) < 0){
                    profit.setVisibility(View.GONE);
                    lost.setVisibility(View.VISIBLE);
                }else if(Integer.valueOf(penjualan.get(0)) - Integer.valueOf(pembelian.get(0)) > 0){
                    profit.setVisibility(View.VISIBLE);
                    lost.setVisibility(View.GONE);
                }else{
                    profit.setVisibility(View.GONE);
                    lost.setVisibility(View.GONE);
                }
            }
        });

        threeMonthHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                threeMonthShow.setVisibility(View.VISIBLE);
                threeMonthHide.setVisibility(View.GONE);
                oneMonthShow.setVisibility(View.GONE);
                oneYearShow.setVisibility(View.GONE);
                oneYearHide.setVisibility(View.VISIBLE);
                oneMonthHide.setVisibility(View.VISIBLE);

                tvLabaRugi.setText(formatNumberCurrency(String.valueOf(Integer.valueOf(penjualan.get(1)) - Integer.valueOf(pembelian.get(1)))));
                if(Integer.valueOf(penjualan.get(1)) - Integer.valueOf(pembelian.get(1)) < 0){
                    profit.setVisibility(View.GONE);
                    lost.setVisibility(View.VISIBLE);
                }else if(Integer.valueOf(penjualan.get(1)) - Integer.valueOf(pembelian.get(1)) > 0){
                    profit.setVisibility(View.VISIBLE);
                    lost.setVisibility(View.GONE);
                }else{
                    profit.setVisibility(View.GONE);
                    lost.setVisibility(View.GONE);
                }
            }
        });

        oneYearHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneYearShow.setVisibility(View.VISIBLE);
                oneYearHide.setVisibility(View.GONE);
                threeMonthShow.setVisibility(View.GONE);
                oneMonthShow.setVisibility(View.GONE);
                oneMonthHide.setVisibility(View.VISIBLE);
                threeMonthHide.setVisibility(View.VISIBLE);

                tvLabaRugi.setText(formatNumberCurrency(String.valueOf(Integer.valueOf(penjualan.get(2)) - Integer.valueOf(pembelian.get(2)))));
                if(Integer.valueOf(penjualan.get(2)) - Integer.valueOf(pembelian.get(2)) < 0){
                    profit.setVisibility(View.GONE);
                    lost.setVisibility(View.VISIBLE);
                }else if(Integer.valueOf(penjualan.get(2)) - Integer.valueOf(pembelian.get(2)) > 0){
                    profit.setVisibility(View.VISIBLE);
                    lost.setVisibility(View.GONE);
                }else{
                    profit.setVisibility(View.GONE);
                    lost.setVisibility(View.GONE);
                }
            }
        });

        menuTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent transaksi = new Intent(MainActivity.this, TransactionActivity.class);
                startActivity(transaksi);
            }
        });
        menuAssets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent assets = new Intent(MainActivity.this, AssetsActivity.class);
                startActivity(assets);
            }
        });
        menuBku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bukuKas = new Intent(MainActivity.this, bukuKasActivity.class);
                startActivity(bukuKas);
            }
        });
    }

    void readPenjualan1Month(){
        Cursor cursor = db.readPenjualan1Month();
        if(cursor.getCount() == 0){
            penjualan.add("0");
        }else{
            while (cursor.moveToNext()){
                penjualan.add(cursor.getString(0));
            }
        }
    }
    void readPembelian1Month(){
        Cursor cursor = db.readPembelian1Month();
        if(cursor.getCount() == 0){
            pembelian.add("0");
        }else{
            while (cursor.moveToNext()){
                pembelian.add(cursor.getString(0));
            }
        }
    }
    void readPenjualan3Month(){
        Cursor cursor = db.readPenjualan3Month();
        if(cursor.getCount() == 0){
            penjualan.add("0");
        }else{
            while (cursor.moveToNext()){
                penjualan.add(cursor.getString(0));
            }
        }
    }
    void readPembelian3Month(){
        Cursor cursor = db.readPembelian3Month();
        if(cursor.getCount() == 0){
            pembelian.add("0");
        }else{
            while (cursor.moveToNext()){
                pembelian.add(cursor.getString(0));
            }
        }
    }
    void readPenjualan1Year(){
        Cursor cursor = db.readPenjualan1Year();
        if(cursor.getCount() == 0){
            penjualan.add("0");
        }else{
            while (cursor.moveToNext()){
                penjualan.add(cursor.getString(0));
            }
        }
    }
    void readPembelian1Year(){
        Cursor cursor = db.readPembelian1Year();
        if(cursor.getCount() == 0){
            pembelian.add("0");
        }else{
            while (cursor.moveToNext()){
                pembelian.add(cursor.getString(0));
            }
        }
    }
    public static String formatNumberCurrency(String number){
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(Double.parseDouble(number));
    }
}