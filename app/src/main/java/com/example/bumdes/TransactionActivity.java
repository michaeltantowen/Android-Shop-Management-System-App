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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TransactionActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton tambahTransaksi;
    DatabaseHelper db;
    ParentTransactionAdapter penjualanAdapter;
    ArrayList<String> tanggalParent, monthList, yearList;
    ArrayList<ListTransaction> listTransactions = new ArrayList<>();
    TextView transactionBack;
    Spinner monthSelect, yearSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        recyclerView = findViewById(R.id.rvParentTransaksi);
        tambahTransaksi = findViewById(R.id.tambahTransaksi);
        transactionBack = findViewById(R.id.transactionBack);
        monthSelect = findViewById(R.id.monthSelect);
        yearSelect = findViewById(R.id.yearSelect);
        monthList = new ArrayList<>();
        yearList = new ArrayList<>();

        transactionBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(TransactionActivity.this, MainActivity.class);
                startActivity(back);
            }
        });

        tambahTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tambahTransaksi = new Intent(TransactionActivity.this, AddTransactionActivity.class);
                startActivity(tambahTransaksi);
            }
        });

        db = new DatabaseHelper(TransactionActivity.this);
        tanggalParent = new ArrayList<>();

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
                        listTransactions.clear();
                        for (int groupInt = 0; groupInt < tanggalParent.size(); groupInt++) {
                            readAllData(tanggalParent.get(groupInt));
                            penjualanAdapter = new ParentTransactionAdapter(TransactionActivity.this, TransactionActivity.this, tanggalParent, listTransactions, yearList.get(cekYear[0]),cekMonth[0]);
                            recyclerView.setAdapter(penjualanAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(TransactionActivity.this));
                        }
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
                        listTransactions.clear();
                        for (int groupInt = 0; groupInt < tanggalParent.size(); groupInt++) {
//                      Toast.makeText(this, listTransactions.get(0).getItemName(), Toast.LENGTH_SHORT).show();
                            readAllData(tanggalParent.get(groupInt));
                            penjualanAdapter = new ParentTransactionAdapter(TransactionActivity.this, TransactionActivity.this, tanggalParent, listTransactions, yearList.get(cekYear[0]),cekMonth[0]);
                            recyclerView.setAdapter(penjualanAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(TransactionActivity.this));
                        }
                    }
                }else{
                    recyclerView.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //Ambil semua data yang ada
        //Ambil tanggal diInstinct
        //dibagi berdasarkan kode akun 300,400,500

        //parent transaction -> Tanggal, rv semua transaksi
        //separates transaction-> kode akun, rv transaksi berdasarkan kodeakun
        //child transaction-> list data, rv transaksi berdasarkan kode akun dan tanggal


    }
    void readAllData(String tanggalParent){
        Cursor cursor = db.readAllData(tanggalParent, yearSelect.getSelectedItem().toString(), monthSelect.getSelectedItem().toString());
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No Data Exists.", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                if(tanggalParent.compareTo(cursor.getString(4)) == 0){
                    listTransactions.add(new
                            ListTransaction(
                            cursor.getString(4),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(0)));
                }
            }
        }
    }
    void storeData(){
        Cursor cursor = db.readDataTransaction();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No Data Exists.", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                tanggalParent.add(cursor.getString(0));
            }
        }
    }
    void getYearList(){
        Cursor cursor = db.readYearData();
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