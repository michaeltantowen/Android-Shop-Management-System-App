package com.example.bumdes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObservable;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "bumdes.db";
    private static final int DB_VERSION = 1;
    private static final String ASSET_TABLE = "ms_asset";
    private static final String ITEM_NAME = "nama";
    private static final String ITEM_PRICE = "harga";
    private static final String ITEM_QUANTITY = "quantity";
    private static final String TRANSACTION_TABLE = "ms_transaction";
    private static final String TRANSACTION_TYPE = "kode_akun";
    private static final String TRANSACTION_DATE = "tanggal_transaksi";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createAssetTable = "CREATE TABLE " + ASSET_TABLE + " ("
                + ITEM_NAME + " TEXT,"
                + ITEM_PRICE + " INTEGER,"
                + ITEM_QUANTITY + " INTEGER, PRIMARY KEY(" + ITEM_NAME + ", " + ITEM_PRICE + "));";

        String createTransactionTable = "CREATE TABLE " + TRANSACTION_TABLE + " ("
                + ITEM_NAME + " TEXT,"
                + TRANSACTION_TYPE + " INTEGER,"
                + ITEM_PRICE + " INTEGER,"
                + ITEM_QUANTITY + " INTEGER,"
                + TRANSACTION_DATE + " TEXT);";

        db.execSQL(createAssetTable);
        db.execSQL(createTransactionTable);
    }

    //    Insert
    public Cursor checkAsset(String nama_asset, int harga_asset) {
        SQLiteDatabase db = this.getReadableDatabase();

        String checkAssetTable = "SELECT * FROM " + ASSET_TABLE + " WHERE " + ITEM_NAME + " LIKE '" + nama_asset + "' AND "
                + ITEM_PRICE + " LIKE '" + harga_asset + "'";

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(checkAssetTable, null);
        }
        return cursor;
    }

    public void updateAsset(String nama_asset, int harga_asset, int qty_asset, String date_transaction, String type_update) {
        SQLiteDatabase db = this.getWritableDatabase();


        if(type_update.compareToIgnoreCase("add") == 0) {
            String updateAssetTable = "UPDATE " + ASSET_TABLE + " SET " + ITEM_QUANTITY + " = " + ITEM_QUANTITY + " + "
                    + qty_asset + " WHERE " + ITEM_NAME + " LIKE '" + nama_asset + "' AND " + ITEM_PRICE +
                    " LIKE " + harga_asset + ";";
            db.execSQL(updateAssetTable);
        } else {
            String updateAssetTable = "UPDATE " + ASSET_TABLE + " SET " + ITEM_QUANTITY + " = " + ITEM_QUANTITY + " - "
                    + qty_asset + " WHERE " + ITEM_NAME + " LIKE '" + nama_asset + "' AND " + ITEM_PRICE +
                    " LIKE " + harga_asset + ";";
            db.execSQL(updateAssetTable);
        }

        db.close();
    }

    public void addTransaction(String nama_asset, int harga_asset, int qty_asset, int transaction_type, String date_transaction) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues transactionValues = new ContentValues();
        transactionValues.put(ITEM_NAME, nama_asset);
        transactionValues.put(ITEM_PRICE, harga_asset);
        transactionValues.put(ITEM_QUANTITY, qty_asset);
        transactionValues.put(TRANSACTION_TYPE, transaction_type);
        transactionValues.put(TRANSACTION_DATE, date_transaction);

        db.insert(TRANSACTION_TABLE, null, transactionValues);

        db.close();
    }

    public void addAsset(String nama_asset, int harga_asset, int qty_asset, String date_transaction) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ITEM_NAME, nama_asset);
        values.put(ITEM_PRICE, harga_asset);
        values.put(ITEM_QUANTITY, qty_asset);


        db.insert(ASSET_TABLE, null, values);

        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ASSET_TABLE);
        onCreate(db);
    }

//    public Cursor readTotalDay(String tanggal){
//
//    }

    public Cursor readYearData(){
        String query = "SELECT DISTINCT STRFTIME('%Y',(SUBSTR("
                + TRANSACTION_DATE +",1,4) || '-' || SUBSTR("
                + TRANSACTION_DATE +",6,2) || '-' || SUBSTR("
                + TRANSACTION_DATE + ",9,2))) AS year FROM " + TRANSACTION_TABLE + " ORDER BY " + TRANSACTION_DATE + " DESC;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllData(String tanggal, String yearSelect, String monthSelect){
//        String query = "SELECT * FROM " + TRANSACTION_TABLE + " WHERE " + TRANSACTION_DATE + " LIKE '" + tanggal + "'";
        String query = "SELECT * FROM " + TRANSACTION_TABLE + " WHERE " + TRANSACTION_DATE + " LIKE '" + tanggal +
//                "' STRFTIME('%Y',(SUBSTR("
//                        + TRANSACTION_DATE +",1,4) || '-' || SUBSTR("
//                        + TRANSACTION_DATE +",6,2) || '-' || SUBSTR("
//                        + TRANSACTION_DATE + ",9,2))) LIKE '" + yearSelect +
//                "' AND STRFTIME('%m',(SUBSTR("
//                        + TRANSACTION_DATE +",1,4) || '-' || SUBSTR("
//                        + TRANSACTION_DATE +",6,2) || '-' || SUBSTR("
//                        + TRANSACTION_DATE + ",9,2))) LIKE '" + monthSelect +
                "' ORDER BY kode_akun ASC;";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor getBukuKas() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TRANSACTION_TABLE
                + " ORDER BY " + TRANSACTION_DATE + " , " +  TRANSACTION_TYPE + ";";

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readDataAsset(){
        String query = "SELECT * FROM " + ASSET_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor getTotalPenjualan(String tanggal){
        String query ="SELECT " + ITEM_PRICE + ", " + ITEM_QUANTITY + " FROM " + TRANSACTION_TABLE + " WHERE " + TRANSACTION_DATE + " LIKE '" + tanggal +
                "' AND " + TRANSACTION_TYPE + " LIKE 400;";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor getTotalPembelian(String tanggal){
        String query ="SELECT " + ITEM_PRICE + ", " + ITEM_QUANTITY + " FROM " + TRANSACTION_TABLE + " WHERE " + TRANSACTION_DATE + " LIKE '" + tanggal +
                "' AND " + TRANSACTION_TYPE + " LIKE 500;";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor getTotalModal(String tanggal){
        String query ="SELECT " + ITEM_PRICE + ", " + ITEM_QUANTITY + " FROM " + TRANSACTION_TABLE + " WHERE " + TRANSACTION_DATE + " LIKE '" + tanggal +
                "' AND " + TRANSACTION_TYPE + " LIKE 300;";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    public Cursor readDataTransaction(){
        String query = "SELECT DISTINCT " + TRANSACTION_DATE + " FROM " + TRANSACTION_TABLE + " ORDER BY " + TRANSACTION_DATE + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readPenjualan1Month(){
        String query = "SELECT SUM(" + ITEM_PRICE + " * " + ITEM_QUANTITY + ") AS TotalPenjualan1BulanTerakhir" +
                " FROM " + TRANSACTION_TABLE + " WHERE " + TRANSACTION_TYPE
                + " LIKE '400' AND strftime('%m','now') - strftime('%m',(substr("
                + TRANSACTION_DATE + ",1,4) || '-' || substr(" + TRANSACTION_DATE + ",6,2) || '-' || substr("
                + TRANSACTION_DATE + ",9,2))) LIKE 0 AND strftime('%Y',(substr    (" + TRANSACTION_DATE + ",1,4) || '-' || substr("
                + TRANSACTION_DATE + ",6,2) || '-' || substr(" + TRANSACTION_DATE + ",9,2))) LIKE strftime('%Y','now');";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readPembelian1Month(){
        String query = "SELECT SUM(" + ITEM_PRICE + " * " + ITEM_QUANTITY + ") AS TotalPenjualan1BulanTerakhir" +
                " FROM " + TRANSACTION_TABLE + " WHERE " + TRANSACTION_TYPE
                + " LIKE '500' AND strftime('%m','now') - strftime('%m',(substr("
                + TRANSACTION_DATE + ",1,4) || '-' || substr(" + TRANSACTION_DATE + ",6,2) || '-' || substr("
                + TRANSACTION_DATE + ",9,2))) LIKE 0 AND strftime('%Y',(substr    (" + TRANSACTION_DATE + ",1,4) || '-' || substr("
                + TRANSACTION_DATE + ",6,2) || '-' || substr(" + TRANSACTION_DATE + ",9,2))) LIKE strftime('%Y','now');";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readPenjualan3Month(){
        String query = "SELECT SUM(" + ITEM_PRICE + " * " + ITEM_QUANTITY + ") AS TotalPenjualan3BulanTerakhir" +
                " FROM " + TRANSACTION_TABLE + " WHERE " + TRANSACTION_TYPE
                + " LIKE '400' AND strftime('%m','now') - strftime('%m',(substr("
                + TRANSACTION_DATE + ",1,4) || '-' || substr(" + TRANSACTION_DATE + ",6,2) || '-' || substr("
                + TRANSACTION_DATE + ",9,2))) < 3 AND strftime('%Y',(substr    (" + TRANSACTION_DATE + ",1,4) || '-' || substr("
                + TRANSACTION_DATE + ",6,2) || '-' || substr(" + TRANSACTION_DATE + ",9,2))) LIKE strftime('%Y','now');";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readPembelian3Month(){
        String query = "SELECT SUM(" + ITEM_PRICE + " * " + ITEM_QUANTITY + ") AS TotalPenjualan3BulanTerakhir" +
                " FROM " + TRANSACTION_TABLE + " WHERE " + TRANSACTION_TYPE
                + " LIKE '500' AND strftime('%m','now') - strftime('%m',(substr("
                + TRANSACTION_DATE + ",1,4) || '-' || substr(" + TRANSACTION_DATE + ",6,2) || '-' || substr("
                + TRANSACTION_DATE + ",9,2))) < 3 AND strftime('%Y',(substr    (" + TRANSACTION_DATE + ",1,4) || '-' || substr("
                + TRANSACTION_DATE + ",6,2) || '-' || substr(" + TRANSACTION_DATE + ",9,2))) LIKE strftime('%Y','now');";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    public Cursor readPenjualan1Year(){
        String query = "SELECT SUM(" + ITEM_PRICE + " * " + ITEM_QUANTITY + ") AS TotalPenjualan1TahunTerakhir" +
                " FROM " + TRANSACTION_TABLE + " WHERE " + TRANSACTION_TYPE
                + " LIKE '400' AND strftime('%m','now') - strftime('%m',(substr("
                + TRANSACTION_DATE + ",1,4) || '-' || substr(" + TRANSACTION_DATE + ",6,2) || '-' || substr("
                + TRANSACTION_DATE + ",9,2))) >= 0 AND strftime('%Y',(substr    (" + TRANSACTION_DATE + ",1,4) || '-' || substr("
                + TRANSACTION_DATE + ",6,2) || '-' || substr(" + TRANSACTION_DATE + ",9,2))) LIKE strftime('%Y','now') OR " + TRANSACTION_TYPE
                + " LIKE '400' AND strftime('%m','now') - strftime('%m',(substr("
                + TRANSACTION_DATE + ",1,4) || '-' || substr(" + TRANSACTION_DATE + ",6,2) || '-' || substr("
                + TRANSACTION_DATE + ",9,2))) <= 0 AND strftime('%Y',(substr    (" + TRANSACTION_DATE + ",1,4) || '-' || substr("
                + TRANSACTION_DATE + ",6,2) || '-' || substr(" + TRANSACTION_DATE + ",9,2))) LIKE strftime('%Y','now') LIKE 1;";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readPembelian1Year(){
        String query = "SELECT SUM(" + ITEM_PRICE + " * " + ITEM_QUANTITY + ") AS TotalPenjualan1TahunTerakhir" +
                " FROM " + TRANSACTION_TABLE + " WHERE " + TRANSACTION_TYPE
                + " LIKE '500' AND strftime('%m','now') - strftime('%m',(substr("
                + TRANSACTION_DATE + ",1,4) || '-' || substr(" + TRANSACTION_DATE + ",6,2) || '-' || substr("
                + TRANSACTION_DATE + ",9,2))) >= 0 AND strftime('%Y',(substr    (" + TRANSACTION_DATE + ",1,4) || '-' || substr("
                + TRANSACTION_DATE + ",6,2) || '-' || substr(" + TRANSACTION_DATE + ",9,2))) LIKE strftime('%Y','now') OR " + TRANSACTION_TYPE
                + " LIKE '500' AND strftime('%m','now') - strftime('%m',(substr("
                + TRANSACTION_DATE + ",1,4) || '-' || substr(" + TRANSACTION_DATE + ",6,2) || '-' || substr("
                + TRANSACTION_DATE + ",9,2))) <= 0 AND strftime('%Y',(substr    (" + TRANSACTION_DATE + ",1,4) || '-' || substr("
                + TRANSACTION_DATE + ",6,2) || '-' || substr(" + TRANSACTION_DATE + ",9,2))) LIKE strftime('%Y','now') LIKE 1;";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
}