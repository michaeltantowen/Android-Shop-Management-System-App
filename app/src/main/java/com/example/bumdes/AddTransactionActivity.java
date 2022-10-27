package com.example.bumdes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddTransactionActivity extends AppCompatActivity {

    EditText inputTanggalTransaksi, etQtyTransaksi, etHargaTransaksi;
    Spinner etNamaItemTransaksi;
    RadioGroup parentRadio;
    RadioButton radioPenjualan, radioModal;
    TextView maxQtyTransaksi, close;
    Button addTransaksi;
    ViewGroup transaksiContainer;
    ArrayList<String> namaItem, harga, qty, spinnerNamaItem;
    DatabaseHelper databaseHelper;
    int harga_jual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width * .8), (int)(height * .8));

        inputTanggalTransaksi = findViewById(R.id.inputTanggalTransaksi);
        etNamaItemTransaksi = findViewById(R.id.etNamaItemTransaksi);
        etQtyTransaksi = findViewById(R.id.etQtyTransaksi);
        etHargaTransaksi = findViewById(R.id.etHargaTransaksi);
        parentRadio = findViewById(R.id.parentRadio);
        radioPenjualan = findViewById(R.id.radioPenjualan);
        radioModal = findViewById(R.id.radioModal);
        maxQtyTransaksi = findViewById(R.id.maxQtyTransaksi);
        addTransaksi = findViewById(R.id.addTransaksi);
        transaksiContainer = findViewById(R.id.transaksiContainer);
        close = findViewById(R.id.close);
        Calendar calendar = Calendar.getInstance();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent close = new Intent(AddTransactionActivity.this, TransactionActivity.class);
                startActivity(close);
            }
        });

        inputTanggalTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(AddTransactionActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);

                        SimpleDateFormat format= null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            format = new SimpleDateFormat("YYYY/MM/dd");
                        }
                        inputTanggalTransaksi.setText(format.format(calendar.getTime()));
                    }
                }, year, month, day).show();
            }
        });
        radioModal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(transaksiContainer);
                etHargaTransaksi.setVisibility(View.VISIBLE);
                etNamaItemTransaksi.setVisibility(View.GONE);
                etQtyTransaksi.setVisibility(View.GONE);
                maxQtyTransaksi.setVisibility(View.GONE);
            }
        });
        radioPenjualan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(transaksiContainer);
                etNamaItemTransaksi.setVisibility(View.VISIBLE);
                etQtyTransaksi.setVisibility(View.VISIBLE);
                etHargaTransaksi.setVisibility(View.VISIBLE);
            }
        });

        databaseHelper = new DatabaseHelper(AddTransactionActivity.this);
        namaItem = new ArrayList<>();
        harga = new ArrayList<>();
        qty = new ArrayList<>();
        spinnerNamaItem = new ArrayList<>();

        storeData();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerNamaItem);
        etNamaItemTransaksi.setAdapter(adapter);

        etNamaItemTransaksi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) etNamaItemTransaksi.getSelectedView()).setTextColor(Color.WHITE);
                if(namaItem.get(i).compareToIgnoreCase("empty") != 0){
                    harga_jual = Integer.valueOf(harga.get(i).toString());
                    maxQtyTransaksi.setVisibility(View.VISIBLE);
                    maxQtyTransaksi.setText(qty.get(i));
                }else{
                    maxQtyTransaksi.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputTanggalTransaksi.getText().toString().isEmpty()){
                    Toast.makeText(AddTransactionActivity.this, "Masukkan tanggal transaksi", Toast.LENGTH_SHORT).show();
                }else if(radioModal.isChecked()){
                    if(etHargaTransaksi.getText().toString().isEmpty()){
                        Toast.makeText(AddTransactionActivity.this, "Harga harus lebih besar dari 0", Toast.LENGTH_SHORT).show();
                    }else if(Integer.valueOf(etHargaTransaksi.getText().toString()) <= 0){
                        Toast.makeText(AddTransactionActivity.this, "Harga harus lebih besar dari 0", Toast.LENGTH_SHORT).show();
                    }else{
//                    add modal
                        String transaction_date = inputTanggalTransaksi.getText().toString().trim();
                        String modal = etHargaTransaksi.getText().toString().trim();
                        int modalInt = Integer.valueOf(modal);
                        databaseHelper.addTransaction("Modal", modalInt, 1, 300, transaction_date);
                        Intent intent = new Intent(AddTransactionActivity.this, TransactionActivity.class);
                        startActivity(intent);

                        Toast.makeText(AddTransactionActivity.this, "Modal berhasil ditambahkan.", Toast.LENGTH_SHORT).show();
                        inputTanggalTransaksi.setText("");
                        etHargaTransaksi.setText("");
                    }
                }else if(radioPenjualan.isChecked()){
                    if(etNamaItemTransaksi.getSelectedItem().toString().compareToIgnoreCase("Empty") == 0){
                        Toast.makeText(AddTransactionActivity.this, "Nama Item tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    }else if(etQtyTransaksi.getText().toString().isEmpty()){
                        Toast.makeText(AddTransactionActivity.this, "Jumlah barang harus lebih dari 0", Toast.LENGTH_SHORT).show();
                    }else if(Integer.valueOf(etQtyTransaksi.getText().toString()) > Integer.valueOf(maxQtyTransaksi.getText().toString())){
                        Toast.makeText(AddTransactionActivity.this, "Stok tidak cukup", Toast.LENGTH_SHORT).show();
                    }else if(etHargaTransaksi.getText().toString().isEmpty()){
                        Toast.makeText(AddTransactionActivity.this, "Harga harus lebih besar dari 0", Toast.LENGTH_SHORT).show();
                    }else if(Integer.valueOf(etHargaTransaksi.getText().toString()) <= 0){
                        Toast.makeText(AddTransactionActivity.this, "Harga harus lebih besar dari 0", Toast.LENGTH_SHORT).show();
                    }else{
//                    inputTanggalTransaksi, etHargaTransaksi, etNamaItemTransaksi, etQtyTransaksi, jenis transaksi
//                    Cari kodeAkun penjualan
//                    Add Transaksi Penjualan
                        String transaction_date = inputTanggalTransaksi.getText().toString().trim();
                        String nama_item_before = etNamaItemTransaksi.getSelectedItem().toString();
                        String[] nama_item_after = nama_item_before.split(" -");
                        String nama_item = nama_item_after[0];
                        String qty = etQtyTransaksi.getText().toString().trim();
                        String harga = etHargaTransaksi.getText().toString().trim();

                        int qtyInt = Integer.valueOf(qty);
                        int hargaInt = Integer.valueOf(harga);

                        databaseHelper.addTransaction(nama_item, hargaInt, qtyInt, 400, transaction_date);
                        databaseHelper.updateAsset(nama_item, harga_jual, qtyInt, transaction_date, "reduce_stock");
                        Intent intent = new Intent(AddTransactionActivity.this, TransactionActivity.class);
                        startActivity(intent);

                        Toast.makeText(AddTransactionActivity.this, "Transaksi berhasil ditambahkan.", Toast.LENGTH_SHORT).show();
                        inputTanggalTransaksi.setText("");
                        etHargaTransaksi.setText("");
                        etQtyTransaksi.setText("");

                    }
                }else{
                    Toast.makeText(AddTransactionActivity.this, "Pilih jenis transaksi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void storeData(){
        Cursor cursor = databaseHelper.readDataAsset();
        namaItem.add("Empty");
        harga.add("Empty");
        qty.add("Empty");
        spinnerNamaItem.add("Empty");
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No Data Exists.", Toast.LENGTH_LONG).show();
        }else{
            while (cursor.moveToNext()){
                if(cursor.getString(2).compareToIgnoreCase("0") != 0){
                    spinnerNamaItem.add(cursor.getString(0) + " - Rp " + formatNumberCurrency(cursor.getString(1)) + " -");
                    namaItem.add(cursor.getString(0));
                    harga.add(cursor.getString(1));
                    qty.add(cursor.getString(2));
                }
            }
        }
    }

    public static String formatNumberCurrency(String number){
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(Double.parseDouble(number));
    }
}