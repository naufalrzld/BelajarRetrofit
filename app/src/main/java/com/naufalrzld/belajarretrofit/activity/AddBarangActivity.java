package com.naufalrzld.belajarretrofit.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.naufalrzld.belajarretrofit.R;
import com.naufalrzld.belajarretrofit.model.barang.BarangResponse;
import com.naufalrzld.belajarretrofit.services.RetrofitServices;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBarangActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ilNamaBarang)
    TextInputLayout ilNamaBarang;
    @BindView(R.id.ilStokBarang)
    TextInputLayout ilStokBarang;
    @BindView(R.id.ilHargaBarang)
    TextInputLayout ilHargaBarang;
    @BindView(R.id.etNamaBarang)
    TextInputEditText etNamaBarang;
    @BindView(R.id.etStokBarang)
    TextInputEditText etStokBarang;
    @BindView(R.id.etHargaBarang)
    TextInputEditText etHargaBarang;
    @BindView(R.id.btnSimpan)
    Button btnSimpan;

    private ProgressDialog addBarangLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_barang);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_addToko);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        addBarangLoading = new ProgressDialog(this);
        addBarangLoading.setMessage("Sedang memproses . . .");
        addBarangLoading.setCancelable(false);

        Intent dataIntent = getIntent();
        final String idToko = dataIntent.getStringExtra("idToko");

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputValid()) {
                    try {
                        JSONObject params = new JSONObject();

                        String namaBarang = etNamaBarang.getText().toString();
                        String stokBarang = etStokBarang.getText().toString();
                        String hargaBarang = etHargaBarang.getText().toString();

                        params.put("idToko", idToko);
                        params.put("namaBarang", namaBarang);
                        params.put("stok", stokBarang);
                        params.put("harga", hargaBarang);

                        addBarang(params);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private boolean isInputValid() {
        ilNamaBarang.setErrorEnabled(false);
        ilStokBarang.setErrorEnabled(false);
        ilHargaBarang.setErrorEnabled(false);

        if (TextUtils.isEmpty(etNamaBarang.getText().toString()) ||
                TextUtils.isEmpty(etStokBarang.getText().toString()) ||
                TextUtils.isEmpty(etHargaBarang.getText().toString())) {
            if (TextUtils.isEmpty(etNamaBarang.getText().toString())) {
                ilNamaBarang.setErrorEnabled(true);
                ilNamaBarang.setError("Nama Barang Tidak Boleh Kosong!");
            }

            if (TextUtils.isEmpty(etStokBarang.getText().toString())) {
                ilStokBarang.setErrorEnabled(true);
                ilStokBarang.setError("Stok Barang Tidak Boleh Kosong!");
            }

            if (TextUtils.isEmpty(etHargaBarang.getText().toString())) {
                ilHargaBarang.setErrorEnabled(true);
                ilHargaBarang.setError("Harga Barang Tidak Boleh Kosong!");
            }

            return false;
        }

        return true;
    }

    private void refreshLayout() {
        etNamaBarang.setText("");
        etStokBarang.setText("");
        etHargaBarang.setText("");
    }

    private void addBarang(JSONObject params) {
        addBarangLoading.show();
        try {
            Call<String> call = RetrofitServices.sendBarangRequest().APIAddBarang(params);
            if (call != null) {
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        addBarangLoading.dismiss();
                        try {
                            JSONObject result = new JSONObject(response.body());
                            Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_SHORT).show();

                            refreshLayout();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        addBarangLoading.dismiss();
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
