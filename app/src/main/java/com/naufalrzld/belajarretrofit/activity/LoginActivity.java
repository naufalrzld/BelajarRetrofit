package com.naufalrzld.belajarretrofit.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.naufalrzld.belajarretrofit.R;
import com.naufalrzld.belajarretrofit.model.APIErrorModel;
import com.naufalrzld.belajarretrofit.model.member.Member;
import com.naufalrzld.belajarretrofit.model.member.MemberResponse;
import com.naufalrzld.belajarretrofit.services.RetrofitServices;
import com.naufalrzld.belajarretrofit.utils.APIErrorUtils;
import com.naufalrzld.belajarretrofit.utils.SessionManager;
import com.naufalrzld.belajarretrofit.utils.SharedPreferencesUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.ilUsername)
    TextInputLayout ilUsername;
    @BindView(R.id.ilPassword)
    TextInputLayout ilPassword;

    @BindView(R.id.etUsername)
    TextInputEditText etUsername;
    @BindView(R.id.etPassword)
    TextInputEditText etPassword;

    @BindView(R.id.btnDaftar)
    Button btnDaftar;
    @BindView(R.id.btnMasuk)
    Button btnMasuk;

    private ProgressDialog loginLoading;
    private SessionManager session;
    private SharedPreferencesUtils sharedPreferencesUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        session = new SessionManager(this);

        if (session.isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        sharedPreferencesUtils = new SharedPreferencesUtils(this, "DataMember");

        loginLoading = new ProgressDialog(this);
        loginLoading.setMessage("Loading . . .");
        loginLoading.setCancelable(false);

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidInput()) {
                    String username = etUsername.getText().toString();
                    String password = etPassword.getText().toString();
                    try {
                        JSONObject loginParam = new JSONObject();

                        loginParam.put("username", username);
                        loginParam.put("password", password);

                        login(loginParam);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private boolean isValidInput() {
        ilUsername.setErrorEnabled(false);
        ilPassword.setErrorEnabled(false);

        if (TextUtils.isEmpty(etUsername.getText().toString()) ||
                TextUtils.isEmpty(etPassword.getText().toString())) {
            if (TextUtils.isEmpty(etUsername.getText().toString())) {
                ilUsername.setErrorEnabled(true);
                ilUsername.setError("Username tidak boleh kosong!");
            }

            if (TextUtils.isEmpty(etPassword.getText().toString())) {
                ilPassword.setErrorEnabled(true);
                ilPassword.setError("Password tidak boleh kosong");
            }

            return false;
        } else {
            if (etPassword.getText().toString().length() < 6) {
                ilPassword.setErrorEnabled(true);
                ilPassword.setError("Password minimal 6 karakter");

                return false;
            }
        }

        return true;
    }

    private void login(JSONObject data) {
        try {
            loginLoading.show();
            Call<MemberResponse> call = null;
            call = RetrofitServices.sendMemberRequset().APILogin(data);
            if (call != null) {
                call.enqueue(new Callback<MemberResponse>() {
                    @Override
                    public void onResponse(Call<MemberResponse> call, Response<MemberResponse> response) {
                        try {
                            loginLoading.dismiss();
                            if (response.isSuccessful()) {
                                Member member = response.body().getData();
                                sharedPreferencesUtils.storeData("profile", member);
                                session.setLogin(true);
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                APIErrorModel error = APIErrorUtils.parserError(response);
                                String message = error.getMessage();
                                new AlertDialog.Builder(LoginActivity.this)
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

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<MemberResponse> call, Throwable t) {
                        loginLoading.dismiss();
                        Log.d("Error", t.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
