package com.example.bumdes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ChildTransactionAdapter extends RecyclerView.Adapter<ChildTransactionAdapter.ViewHolder> {

    private Context context;
    Activity activity;
    ArrayList<ListTransaction> listTransactions = new ArrayList<>();
    String Tanggal;

    public ChildTransactionAdapter(Context context, Activity activity, ArrayList<ListTransaction> listTransactions, String Tanggal) {
        this.context = context;
        this.activity = activity;
        this.listTransactions = listTransactions;
        this.Tanggal = Tanggal;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.child_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        if (Tanggal.compareTo(listTransactions.get(position).getTanggal()) == 0) {
            if(listTransactions.get(position).getKode_akun().compareToIgnoreCase("400") == 0){
                holder.total.setTextColor(Color.GREEN);
            }else if(listTransactions.get(position).getKode_akun().compareToIgnoreCase("500") == 0){
                holder.total.setTextColor(Color.RED);
            }
            holder.total.setText(formatNumberCurrency(String.valueOf(Integer.valueOf(listTransactions.get(position).getQuantity()) * Integer.valueOf(listTransactions.get(position).getHarga()))));
            holder.deskripsi.setText(listTransactions.get(position).getItemName());
            holder.qty.setText(formatNumberCurrency(listTransactions.get(position).getQuantity()));
            holder.harga.setText(formatNumberCurrency(listTransactions.get(position).getHarga()));
            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }else {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
    }

    @Override
    public int getItemCount() {
        return listTransactions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView deskripsi, qty, harga, total;
        LinearLayout mainLayout;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            deskripsi = itemView.findViewById(R.id.tvDeskripsi);
            qty = itemView.findViewById(R.id.tvQuantity);
            harga = itemView.findViewById(R.id.tvHarga);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            total = itemView.findViewById(R.id.tvTotal);
        }
    }

    public static String formatNumberCurrency(String number){
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(Double.parseDouble(number));
    }
}
