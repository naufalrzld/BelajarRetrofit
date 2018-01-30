package com.naufalrzld.belajarretrofit.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.naufalrzld.belajarretrofit.R;
import com.naufalrzld.belajarretrofit.activity.DetailTokoActivity;
import com.naufalrzld.belajarretrofit.model.toko.Toko;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Naufal on 26/01/2018.
 */

public class TokoAdapter extends RecyclerView.Adapter<TokoAdapter.ViewHolder> {
    private Context context;
    private List<Toko> listToko;

    public TokoAdapter(Context context, List<Toko> listToko) {
        this.context = context;
        this.listToko = listToko;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_toko, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Toko toko = listToko.get(position);
        holder.namaToko.setText(toko.getNamaToko());
        holder.totalBarang.setText(String.valueOf(toko.getJumlahBarang()));
        holder.cvItemToko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = new Gson().toJson(toko);

                Intent i = new Intent(context, DetailTokoActivity.class);
                i.putExtra("data", data);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listToko.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cvItemToko)
        CardView cvItemToko;
        @BindView(R.id.namaToko)
        TextView namaToko;
        @BindView(R.id.totalBarang)
        TextView totalBarang;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
