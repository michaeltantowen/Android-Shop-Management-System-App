package com.example.bumdes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AssetsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton tambahAsset;
    DatabaseHelper databaseHelper;
    ArrayList<String> namaItem, harga, qty;
    ParentAdapter parentAdapter;
    TextView assetBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets);

        recyclerView = findViewById(R.id.rvParentAsset);
        tambahAsset = findViewById(R.id.tambahAsset);
        assetBack = findViewById(R.id.assetBack);

        assetBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(AssetsActivity.this, MainActivity.class);
                startActivity(back);
            }
        });

        tambahAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addAsset = new Intent(AssetsActivity.this, AddAssetsActivity.class);
                startActivity(addAsset);
            }
        });
        databaseHelper = new DatabaseHelper(AssetsActivity.this);
        namaItem = new ArrayList<>();
        harga = new ArrayList<>();
        qty = new ArrayList<>();

        storeData();


        for (int groupInt = 0; groupInt < namaItem.size(); groupInt++) {
            parentAdapter = new ParentAdapter(AssetsActivity.this, this, namaItem, harga, qty);
            recyclerView.setAdapter(parentAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(AssetsActivity.this));
        }
    }

    void storeData(){
        Cursor cursor = databaseHelper.readDataAsset();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No Data Exists.", Toast.LENGTH_LONG).show();
        }else{
            while (cursor.moveToNext()){
                namaItem.add(cursor.getString(0));
                harga.add(cursor.getString(1));
                qty.add(cursor.getString(2));
            }
        }
    }
}