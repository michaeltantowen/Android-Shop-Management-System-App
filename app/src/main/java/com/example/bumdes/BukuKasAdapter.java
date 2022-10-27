package com.example.bumdes;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BukuKasAdapter extends RecyclerView.Adapter<BukuKasAdapter.ViewHolder> {

    private Context context;
    Activity activity;
    ArrayList<String> Tanggal, Keterangan, Qty, Harga, kode_akun, Saldo;
    String year;
    int saldoTotal = 0;
    int month;

    public BukuKasAdapter(Context context,
                          Activity activity,
                          ArrayList<String> tanggal,
                          ArrayList<String> keterangan,
                          ArrayList<String> qty,
                          ArrayList<String> harga,
                          ArrayList<String> kode_akun,
                          String year,
                          int month) {
        this.context = context;
        this.activity = activity;
        Tanggal = tanggal;
        Keterangan = keterangan;
        Qty = qty;
        Harga = harga;
        this.kode_akun = kode_akun;
        this.year = year;
        this.month = month;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.child_bukukas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position != 0){
            holder.kasTable.setVisibility(View.GONE);
        }
        String[] tanggal = Tanggal.get(position).split("/");
        String Tahun = tanggal[0];
        String Bulan = tanggal[1];

        int saldo_item = Integer.valueOf(Harga.get(position)) * Integer.valueOf(Qty.get(position));
        if(kode_akun.get(position).compareToIgnoreCase("500") == 0) {
            saldoTotal -= saldo_item;
        } else {
            saldoTotal += saldo_item;
        }
        if (Tahun.compareToIgnoreCase(year) == 0 && Integer.valueOf(Bulan) == month){
            holder.tvTanggalKas.setText(Tanggal.get(position));
            holder.tvKeteranganKas.setText(Keterangan.get(position));
            holder.tvQtyKas.setText(formatNumberCurrency(Qty.get(position)));
            holder.tvHargaKas.setText(formatNumberCurrency(Harga.get(position)));
            holder.tvKodeAkunKas.setText(kode_akun.get(position));
            holder.tvSaldoKas.setText(formatNumberCurrency(String.valueOf(saldoTotal)));
        }else {
            saldoTotal = 0;
            if(position == 0){
                holder.kasTable.setVisibility(View.VISIBLE);
                holder.secondRow.setVisibility(View.GONE);
            }else{
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }
    }

    @Override
    public int getItemCount() {
        return Tanggal.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTanggalKas, tvKeteranganKas, tvQtyKas, tvHargaKas, tvKodeAkunKas, tvSaldoKas;
        TableRow kasTable, secondRow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHargaKas = itemView.findViewById(R.id.tvHargaKas);
            tvTanggalKas = itemView.findViewById(R.id.tvTanggalKas);
            tvKeteranganKas = itemView.findViewById(R.id.tvKeteranganKas);
            tvQtyKas = itemView.findViewById(R.id.tvQtyKas);
            tvKodeAkunKas = itemView.findViewById(R.id.tvKodeAkunKas);
            tvSaldoKas = itemView.findViewById(R.id.tvSaldoKas);
            kasTable = itemView.findViewById(R.id.kasTable);
            secondRow = itemView.findViewById(R.id.secondRow);
        }
    }
    public static String formatNumberCurrency(String number){
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(Double.parseDouble(number));
    }
}
