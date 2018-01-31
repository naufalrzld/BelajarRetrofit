package com.naufalrzld.belajarretrofit.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.naufalrzld.belajarretrofit.R;
import com.naufalrzld.belajarretrofit.adapter.BarangAdapter;
import com.naufalrzld.belajarretrofit.model.barang.Barang;
import com.naufalrzld.belajarretrofit.model.barang.BarangResponse;
import com.naufalrzld.belajarretrofit.model.toko.Toko;
import com.naufalrzld.belajarretrofit.services.RetrofitServices;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTokoActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvNamaToko)
    TextView tvNamaToko;
    @BindView(R.id.tvDescToko)
    TextView tvDescToko;
    @BindView(R.id.btnHapusToko)
    Button btnHapusToko;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvListBarang)
    RecyclerView rvListToko;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.fabAdd)
    FloatingActionButton fabAdd;

    private List<Barang> listBarang;
    private BarangAdapter adapter;

    private String idToko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_toko);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_detailToko);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent i = getIntent();
        Toko toko = new Gson().fromJson(i.getStringExtra("data"), Toko.class);
        idToko = String.valueOf(toko.getIdToko());

        tvNamaToko.setText(toko.getNamaToko());
        tvDescToko.setText(toko.getDescToko());

        listBarang = new ArrayList<>();

        rvListToko.setHasFixedSize(true);
        rvListToko.setLayoutManager(new LinearLayoutManager(this));
        rvListToko.setNestedScrollingEnabled(false);

        btnHapusToko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Button Hapus Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                getAllBarang(idToko);
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailTokoActivity.this, AddBarangActivity.class);
                i.putExtra("idToko", idToko);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllBarang(idToko);
    }

    private void getAllBarang(String idToko) {
        swipeRefreshLayout.setRefreshing(true);
        listBarang.clear();
        try {
            Call<BarangResponse> call = RetrofitServices.sendBarangRequest().getAllBarang(idToko);
            if (call != null) {
                call.enqueue(new Callback<BarangResponse>() {
                    @Override
                    public void onResponse(Call<BarangResponse> call, Response<BarangResponse> response) {
                        swipeRefreshLayout.setRefreshing(false);
                        listBarang = response.body().getData();

                        if (listBarang.isEmpty()) {
                            tvNoData.setVisibility(View.VISIBLE);
                            rvListToko.setVisibility(View.GONE);
                        } else {
                            tvNoData.setVisibility(View.GONE);
                            rvListToko.setVisibility(View.VISIBLE);

                            adapter = new BarangAdapter(getApplicationContext(), listBarang);
                            rvListToko.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<BarangResponse> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.e("error", t.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
