package com.naufalrzld.belajarretrofit.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
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
import com.naufalrzld.belajarretrofit.model.APIErrorModel;
import com.naufalrzld.belajarretrofit.model.member.Member;
import com.naufalrzld.belajarretrofit.model.toko.TokoResponse;
import com.naufalrzld.belajarretrofit.services.RetrofitServices;
import com.naufalrzld.belajarretrofit.utils.APIErrorUtils;
import com.naufalrzld.belajarretrofit.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTokoActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
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

    private SharedPreferencesUtils sharedPreferencesUtils;
    private ProgressDialog addTokoLoading;

    private JSONObject params = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_toko);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_addToko);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        addTokoLoading = new ProgressDialog(this);
        addTokoLoading.setMessage("Sedang memproses . . .");
        addTokoLoading.setCancelable(false);

        sharedPreferencesUtils = new SharedPreferencesUtils(this, "DataMember");
        try {
            if (sharedPreferencesUtils.checkIfDataExists("profile")) {
                Member member = sharedPreferencesUtils.getObjectData("profile", Member.class);
                String username = member.getUsername();

                params.put("username", username);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputValid()) {
                    try {
                        String namaToko = etNamaToko.getText().toString();
                        String descToko = etDescToko.getText().toString();

                        params.put("namaToko", namaToko);
                        params.put("descToko", descToko);

                        addToko(params);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private boolean isInputValid() {
        ilNamaToko.setErrorEnabled(false);
        ilDescToko.setErrorEnabled(false);

        if (TextUtils.isEmpty(etNamaToko.getText().toString()) ||
                TextUtils.isEmpty(etDescToko.getText().toString())) {
            if (TextUtils.isEmpty(etNamaToko.getText().toString())) {
                ilNamaToko.setErrorEnabled(true);
                ilNamaToko.setError("Nama toko tidak boleh kosong!");
            }

            if (TextUtils.isEmpty(etDescToko.getText().toString())) {
                ilDescToko.setErrorEnabled(true);
                ilDescToko.setError("Deskripsi toko tidak boleh kosong!");
            }

            return false;
        }

        return true;
    }

    private void refreshLayout() {
        etNamaToko.setText("");
        etDescToko.setText("");
    }

    private void addToko(JSONObject data) {
        addTokoLoading.show();
        Call<String> call = null;
        call = RetrofitServices.sendTokoRequest().APIAddToko(data);
        if (call != null) {
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    addTokoLoading.dismiss();
                    if (response.isSuccessful()) {
                        try {
                            JSONObject result = new JSONObject(response.body());
                            Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_SHORT).show();

                            refreshLayout();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        APIErrorModel error = APIErrorUtils.parserError(response);
                        String message = error.getMessage();
                        new AlertDialog.Builder(AddTokoActivity.this)
                                .setTitle("Pesan")
                                .setMessage(message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    addTokoLoading.dismiss();
                    Log.e("addTokoError", t.getMessage());
                }
            });
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
