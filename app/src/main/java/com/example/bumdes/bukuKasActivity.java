package com.example.bumdes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class bukuKasActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    ArrayList<String> Tanggal, Keterangan, Qty, Harga, kode_akun, monthList, yearList;
    BukuKasAdapter bukuKasAdapter;
    RecyclerView recyclerView;
    TextView bukuKasBack;
    Spinner monthSelect, yearSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buku_kas);
        recyclerView = findViewById(R.id.rvKas);
        bukuKasBack = findViewById(R.id.bukuKasBack);

//        Validasi ArrayList
        databaseHelper = new DatabaseHelper(bukuKasActivity.this);
        Tanggal = new ArrayList<>();
        Keterangan = new ArrayList<>();
        Qty = new ArrayList<>();
        Harga = new ArrayList<>();
        kode_akun = new ArrayList<>();
        monthSelect = findViewById(R.id.monthSelect);
        yearSelect = findViewById(R.id.yearSelect);
        monthList = new ArrayList<>();
        yearList = new ArrayList<>();

        monthList.add("Month");
        monthList.add("Januari");
        monthList.add("Februari");
        monthList.add("Maret");
        monthList.add("April");
        monthList.add("Mei");
        monthList.add("Juni");
        monthList.add("Juli");
        monthList.add("Agustus");
        monthList.add("September");
        monthList.add("Oktober");
        monthList.add("November");
        monthList.add("Desember");
        storeData();
        getYearList();

        int[] cekYear = {0};
        int[] cekMonth = {0};

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                monthList);
        monthSelect.setAdapter(monthAdapter);

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                yearList);
        yearSelect.setAdapter(yearAdapter);

        monthSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cekMonth[0] = i;
                if(monthList.get(i).compareToIgnoreCase("month") != 0){
                    if(cekYear[0] != 0 && cekMonth[0] != 0){
                        recyclerView.setVisibility(View.VISIBLE);
                        Tanggal.clear();
                        Keterangan.clear();
                        Qty.clear();
                        Harga.clear();
                        kode_akun.clear();
                        storeData();
                        bukuKasAdapter = new BukuKasAdapter(bukuKasActivity.this, bukuKasActivity.this, Tanggal, Keterangan, Qty, Harga, kode_akun, yearList.get(cekYear[0]),cekMonth[0]);
                        recyclerView.setAdapter(bukuKasAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(bukuKasActivity.this));
                    }
                }else{
                    recyclerView.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        yearSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cekYear[0] = i;
                if(yearList.get(i).compareToIgnoreCase("year") != 0){
                    if(cekYear[0] != 0 && cekMonth[0] != 0){
                        recyclerView.setVisibility(View.VISIBLE);
                        Tanggal.clear();
                        Keterangan.clear();
                        Qty.clear();
                        Harga.clear();
                        kode_akun.clear();
                        storeData();
                        bukuKasAdapter = new BukuKasAdapter(bukuKasActivity.this, bukuKasActivity.this, Tanggal, Keterangan, Qty, Harga, kode_akun, yearList.get(cekYear[0]),cekMonth[0]);
                        recyclerView.setAdapter(bukuKasAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(bukuKasActivity.this));
                    }
                }else{
                    recyclerView.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bukuKasBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kasBack = new Intent(bukuKasActivity.this, MainActivity.class);
                startActivity(kasBack);
            }
        });
    }

    void storeData() {
        Cursor cursor = databaseHelper.getBukuKas();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Data Exist.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                Tanggal.add(cursor.getString(4));
                Keterangan.add(cursor.getString(0));
                Qty.add(cursor.getString(3));
                Harga.add(cursor.getString(2));
                kode_akun.add(cursor.getString(1));
            }
        }
    }

    void getYearList(){
        Cursor cursor = databaseHelper.readYearData();
        yearList.add("Year");
        if(cursor.getCount() == 0){
            yearList.add("2022");
        }else{
            while (cursor.moveToNext()){
                yearList.add((cursor.getString(0)));
            }
        }
    }
}