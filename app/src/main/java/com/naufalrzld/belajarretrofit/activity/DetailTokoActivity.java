package com.naufalrzld.belajarretrofit.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.naufalrzld.belajarretrofit.R;
import com.naufalrzld.belajarretrofit.adapter.BarangAdapter;
import com.naufalrzld.belajarretrofit.model.APIErrorModel;
import com.naufalrzld.belajarretrofit.model.barang.Barang;
import com.naufalrzld.belajarretrofit.model.barang.BarangResponse;
import com.naufalrzld.belajarretrofit.model.member.Member;
import com.naufalrzld.belajarretrofit.model.toko.Toko;
import com.naufalrzld.belajarretrofit.services.RetrofitServices;
import com.naufalrzld.belajarretrofit.utils.APIErrorUtils;
import com.naufalrzld.belajarretrofit.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

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
    @BindView(R.id.btnEdit)
    ImageView btnEdit;
    @BindView(R.id.fabAdd)
    FloatingActionButton fabAdd;

    @BindView(R.id.lytNoneEdit)
    LinearLayout lytNoneEdit;
    @BindView(R.id.lytEdit)
    LinearLayout lytEdit;

    @BindView(R.id.ilNamaToko)
    TextInputLayout ilNamaToko;
    @BindView(R.id.ilDescToko)
    TextInputLayout ilDescToko;
    @BindView(R.id.etNamaToko)
    TextInputEditText etNamaToko;
    @BindView(R.id.etDescToko)
    TextInputEditText etDescToko;
    @BindView(R.id.btnSimpan)
    Button btnSimpan;

    private List<Barang> listBarang;
    private BarangAdapter adapter;

    private String idToko, username;
    private boolean statusEdit = false;

    private SharedPreferencesUtils sharedPreferencesUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_toko);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_detailToko);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        sharedPreferencesUtils = new SharedPreferencesUtils(this, "DataMember");
        try {
            if (sharedPreferencesUtils.checkIfDataExists("profile")) {
                Member member = sharedPreferencesUtils.getObjectData("profile", Member.class);
                username = member.getUsername();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent i = getIntent();
        Toko toko = new Gson().fromJson(i.getStringExtra("data"), Toko.class);
        idToko = String.valueOf(toko.getIdToko());

        tvNamaToko.setText(toko.getNamaToko());
        tvDescToko.setText(toko.getDescToko());

        listBarang = new ArrayList<>();

        rvListToko.setHasFixedSize(true);
        rvListToko.setLayoutManager(new LinearLayoutManager(this));
        rvListToko.setNestedScrollingEnabled(false);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!statusEdit) {
                    editToko(true);
                } else {
                    editToko(false);
                }
            }
        });

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

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputValid()) {
                    try {
                        String namaToko = etNamaToko.getText().toString();
                        String descToko = etDescToko.getText().toString();

                        JSONObject params = new JSONObject();
                        params.put("username", username);
                        params.put("idToko", idToko);
                        params.put("namaToko", namaToko);
                        params.put("descToko", descToko);

                        editToko(params);

                        editToko(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
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

    private void editToko(boolean status) {
        int visibleNoneEdit, visibleEdit;
        statusEdit = status;

        if (status) {
            visibleNoneEdit = View.GONE;
            visibleEdit = View.VISIBLE;

            btnEdit.setImageResource(R.drawable.ic_cancel);

            etNamaToko.setText(tvNamaToko.getText().toString());
            etDescToko.setText(tvDescToko.getText().toString());
        } else {
            visibleNoneEdit = View.VISIBLE;
            visibleEdit = View.GONE;

            btnEdit.setImageResource(R.drawable.ic_edit);
        }

        lytNoneEdit.setVisibility(visibleNoneEdit);
        lytEdit.setVisibility(visibleEdit);
    }

    private boolean isInputValid() {
        ilNamaToko.setErrorEnabled(false);
        ilDescToko.setErrorEnabled(false);

        if (TextUtils.isEmpty(etNamaToko.getText().toString()) ||
                TextUtils.isEmpty(etDescToko.getText().toString())) {
            if (TextUtils.isEmpty(etNamaToko.getText().toString())) {
                ilNamaToko.setErrorEnabled(true);
                ilNamaToko.setError("Nama Toko Tidak Boleh Kosong!");
            }

            if (TextUtils.isEmpty(etDescToko.getText().toString())) {
                ilDescToko.setErrorEnabled(true);
                ilDescToko.setError("Deskripsi Toko Tidak Boleh Kosong!");
            }

            return false;
        }
        return true;
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

    private void getAllBarang(String idToko) {
        swipeRefreshLayout.setRefreshing(true);
        listBarang.clear();
        try {
            Call<BarangResponse> call = RetrofitServices.sendBarangRequest().getAllBarang(idToko);
            if (call != null) {
                call.enqueue(new Callback<BarangResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<BarangResponse> call, @NonNull Response<BarangResponse> response) {
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

    private void editToko(JSONObject params) {
        try {
            Call<String> call = RetrofitServices.sendTokoRequest().APIEditToko(params);
            if (call != null) {
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject result = new JSONObject(response.body());
                                JSONObject data = result.getJSONObject("data");

                                String namaToko = data.getString("namaToko");
                                String descToko = data.getString("descToko");

                                tvNamaToko.setText(namaToko);
                                tvDescToko.setText(descToko);

                                Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            APIErrorModel error = APIErrorUtils.parserError(response);
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Log.e("error", t.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
