package com.example.bumdes;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ParentTransactionAdapter extends RecyclerView.Adapter<ParentTransactionAdapter.ViewHolder> {
    private Context context;
    Activity activity;
    DatabaseHelper db;
    ChildTransactionAdapter childTransactionAdapter;
    ArrayList<String> tanggalParent;
    ArrayList<ListTransaction> listTransactions;
    String year;
    int month, penjualan = 0, pembelian = 0, modal = 0, total = 0;

    public ParentTransactionAdapter(Context context,
                                    Activity activity,
                                    ArrayList<String> tanggalParent,
                                    ArrayList<ListTransaction> listTransactions,
                                    String year,
                                    int month) {
        this.context = context;
        this.activity = activity;
        this.tanggalParent = tanggalParent;
        this.listTransactions = listTransactions;
        this.year = year;
        this.month = month;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        db = new DatabaseHelper(activity);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.parent_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        String[] tanggal = tanggalParent.get(position).split("/");
        String Tahun = tanggal[0];
        String Bulan = tanggal[1];

        if (Tahun.compareToIgnoreCase(year) == 0 && Integer.valueOf(Bulan) == month){
            getTotalPenjualan(String.valueOf(tanggalParent.get(position)));
            getTotalPembelian(String.valueOf(tanggalParent.get(position)));
            getTotalModal(String.valueOf(tanggalParent.get(position)));

            total = penjualan - pembelian + modal;
            holder.tanggalParent.setText(String.valueOf(tanggalParent.get(position)));
            holder.totalPerDay.setText(formatNumberCurrency(String.valueOf(total)));
            childTransactionAdapter = new ChildTransactionAdapter(context, activity, listTransactions, String.valueOf(tanggalParent.get(position)));
            holder.rvChild.setAdapter(childTransactionAdapter);
            holder.rvChild.setLayoutManager(new LinearLayoutManager(activity));
        }else {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
    }

    @Override
    public int getItemCount() {
        return tanggalParent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tanggalParent, totalPerDay;
        RecyclerView rvChild;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tanggalParent = itemView.findViewById(R.id.tanggalParent);;
            totalPerDay = itemView.findViewById(R.id.totalPerDay);
            rvChild = itemView.findViewById(R.id.rvChild);
        }
    }

    void getTotalPenjualan(String tanggal){
        Cursor cursor = db.getTotalPenjualan(tanggal);
        if(cursor.getCount() == 0){
            penjualan = 0;
        }else{
            while (cursor.moveToNext()){
                penjualan += Integer.valueOf(cursor.getString(0)) * Integer.valueOf(cursor.getString(1));
            }
        }
    }

    void getTotalPembelian(String tanggal){
        Cursor cursor = db.getTotalPembelian(tanggal);
        if(cursor.getCount() == 0){
            pembelian = 0;
        }else{
            while (cursor.moveToNext()){
                pembelian += Integer.valueOf(cursor.getString(0)) * Integer.valueOf(cursor.getString(1));
            }
        }
    }
    void getTotalModal(String tanggal){
        Cursor cursor = db.getTotalModal(tanggal);
        if(cursor.getCount() == 0){
            modal = 0;
        }else{
            while (cursor.moveToNext()){
                modal += Integer.valueOf(cursor.getString(0)) * Integer.valueOf(cursor.getString(1));
            }
        }
    }
    public static String formatNumberCurrency(String number){
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(Double.parseDouble(number));
    }
}
