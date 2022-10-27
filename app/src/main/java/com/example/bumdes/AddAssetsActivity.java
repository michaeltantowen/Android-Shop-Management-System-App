package com.example.bumdes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddAssetsActivity extends AppCompatActivity {

    EditText etNamaItem, etQty, etHarga, inputTanggal;
    Button addAsset;
    DatabaseHelper databaseHelper;
    TextView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assets);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width * .8), (int)(height * .6));

        etNamaItem = findViewById(R.id.etNamaItem);
        etQty = findViewById(R.id.etQty);
        etHarga = findViewById(R.id.etHarga);
        addAsset = findViewById(R.id.addAsset);
        close = findViewById(R.id.close);

        inputTanggal = findViewById(R.id.inputTanggal);
        Calendar calendar = Calendar.getInstance();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent close = new Intent(AddAssetsActivity.this, AssetsActivity.class);
                startActivity(close);
            }
        });

        inputTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(AddAssetsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);

                        SimpleDateFormat format= null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            format = new SimpleDateFormat("YYYY/MM/dd");
                        }
                        inputTanggal.setText(format.format(calendar.getTime()));
                    }
                }, year, month, day).show();
            }
        });
        databaseHelper = new DatabaseHelper(AddAssetsActivity.this);

        addAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namaItem = etNamaItem.getText().toString().trim();
                String qty = etQty.getText().toString().trim();
                String harga = etHarga.getText().toString().trim();
                String transactionDate = inputTanggal.getText().toString().trim();

                if(namaItem.isEmpty() || qty.isEmpty() || harga.isEmpty() || transactionDate.isEmpty()){
                    Toast.makeText(AddAssetsActivity.this, "Isi semua data..", Toast.LENGTH_LONG).show();
                    return;
                }
                int priceInt = Integer.valueOf(harga);
                int quantityInt = Integer.valueOf(qty);

                Cursor cursor = databaseHelper.checkAsset(namaItem, priceInt);
                if(cursor.getCount() == 0){
                    databaseHelper.addAsset(namaItem, priceInt, quantityInt, transactionDate);
                    databaseHelper.addTransaction(namaItem, priceInt, quantityInt, 500, transactionDate);
                    Intent intent = new Intent(AddAssetsActivity.this, AssetsActivity.class);
                    startActivity(intent);
                }else {
                    databaseHelper.updateAsset(namaItem, priceInt, quantityInt, transactionDate, "add");
                    databaseHelper.addTransaction(namaItem, priceInt, quantityInt, 500, transactionDate);
                    Intent intent = new Intent(AddAssetsActivity.this, AssetsActivity.class);
                    startActivity(intent);
                }
                Toast.makeText(AddAssetsActivity.this, "Asset berhasil ditambahkan.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}