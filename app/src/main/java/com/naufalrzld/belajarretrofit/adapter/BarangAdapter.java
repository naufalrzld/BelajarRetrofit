package com.naufalrzld.belajarretrofit.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.naufalrzld.belajarretrofit.R;
import com.naufalrzld.belajarretrofit.model.barang.Barang;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Naufal on 30/01/2018.
 */

public class BarangAdapter extends RecyclerView.Adapter<BarangAdapter.ViewHolder> {
    private Context context;
    private List<Barang> listBarang;

    public BarangAdapter(Context context, List<Barang> listBarang) {
        this.context = context;
        this.listBarang = listBarang;
    }

    @Override
    public BarangAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barang, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BarangAdapter.ViewHolder holder, int position) {
        Barang barang = listBarang.get(position);

        holder.tvNamaBarang.setText(barang.getNamaBarang());
        holder.tvStokBarang.setText(String.valueOf(barang.getStok()));
        holder.tvHargaBarang.setText(String.valueOf(barang.getHarga()));
    }

    @Override
    public int getItemCount() {
        return listBarang.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cvItemBarang)
        CardView cvItemBarang;
        @BindView(R.id.tvNamaBarang)
        TextView tvNamaBarang;
        @BindView(R.id.tvStokBarang)
        TextView tvStokBarang;
        @BindView(R.id.tvHargaBarang)
        TextView tvHargaBarang;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
