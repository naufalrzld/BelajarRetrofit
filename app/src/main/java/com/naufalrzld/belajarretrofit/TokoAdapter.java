package com.naufalrzld.belajarretrofit;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        Toko toko = listToko.get(position);
        holder.namaToko.setText(toko.getNamaToko());
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
