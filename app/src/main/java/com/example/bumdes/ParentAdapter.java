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

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ViewHolder> {
    private Context context;
    Activity activity;
    ArrayList <String> namaItem, harga, qty;

    ParentAdapter(
                Activity activity,
                Context context,
                ArrayList namaItem,
                ArrayList harga,
                ArrayList qty) {
        this.context = context;
        this.activity = activity;
        this.namaItem = namaItem;
        this.harga = harga;
        this.qty = qty;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.child_asset, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
            if(position != 0){
                holder.tableName.setVisibility(View.GONE);
//                holder.tableName.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
            if (Integer.valueOf(qty.get(position)) != 0){
                holder.NamaItem.setText(String.valueOf(namaItem.get(position)));
                holder.Qty.setText(formatNumberCurrency(String.valueOf(qty.get(position))));
                holder.Harga.setText(formatNumberCurrency(String.valueOf(harga.get(position))));
                holder.total.setText(formatNumberCurrency(String.valueOf((Integer.valueOf(harga.get(position))) * (Integer.valueOf(qty.get(position))))));
            }else{
                if(position == 0){
                    holder.tableName.setVisibility(View.VISIBLE);
                    holder.NamaItem.setVisibility(View.GONE);
                    holder.Qty.setVisibility(View.GONE);
                    holder.Harga.setVisibility(View.GONE);
                    holder.total.setVisibility(View.GONE);
                }else{
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                }
            }
    }

    @Override
    public int getItemCount() {
        return namaItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView NamaItem, Qty, Harga, total;
        TableRow tableName;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            NamaItem = itemView.findViewById(R.id.tvJenisItem);
            Qty = itemView.findViewById(R.id.tvQty);
            Harga = itemView.findViewById(R.id.tvHargaBeli);
            total = itemView.findViewById(R.id.tvTotal);
            tableName = itemView.findViewById(R.id.tableName);
        }
    }
    public static String formatNumberCurrency(String number){
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(Double.parseDouble(number));
    }
}
