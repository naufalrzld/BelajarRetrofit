package com.naufalrzld.belajarretrofit.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.ilFName)
    TextInputLayout ilFName;
    @BindView(R.id.ilLName)
    TextInputLayout ilLName;
    @BindView(R.id.ilUsername)
    TextInputLayout ilUsername;
    @BindView(R.id.ilEmail)
    TextInputLayout ilEmail;
    @BindView(R.id.ilPassword)
    TextInputLayout ilPassword;

    @BindView(R.id.etFName)
    TextInputEditText etFName;
    @BindView(R.id.etLName)
    TextInputEditText etLName;
    @BindView(R.id.etUsername)
    TextInputEditText etUsername;
    @BindView(R.id.etEmail)
    TextInputEditText etEmail;
    @BindView(R.id.etPassword)
    TextInputEditText etPassword;

    @BindView(R.id.btnDaftar)
    Button btnDaftar;

    private ProgressDialog registerLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        registerLoading = new ProgressDialog(this);
        registerLoading.setMessage("Pendaftaran sedang di proses . . .");
        registerLoading.setCancelable(false);

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidInput()) {
                    String fName = etFName.getText().toString();
                    String lName = etLName.getText().toString();
                    String username = etUsername.getText().toString();
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();
                    try {
                        JSONObject registerParam = new JSONObject();
                        registerParam.put("firstName", fName);
                        registerParam.put("lastName", lName);
                        registerParam.put("username", username);
                        registerParam.put("email", email);
                        registerParam.put("password", password);

                        register(registerParam);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private boolean isValidInput() {
        ilFName.setErrorEnabled(false);
        ilLName.setErrorEnabled(false);
        ilUsername.setErrorEnabled(false);
        ilEmail.setErrorEnabled(false);
        ilPassword.setErrorEnabled(false);

        if (TextUtils.isEmpty(etFName.getText().toString()) ||
                TextUtils.isEmpty(etLName.getText().toString()) ||
                TextUtils.isEmpty(etUsername.getText().toString()) ||
                TextUtils.isEmpty(etEmail.getText().toString()) ||
                TextUtils.isEmpty(etPassword.getText().toString())) {

            if (TextUtils.isEmpty(etFName.getText().toString())) {
                ilFName.setErrorEnabled(true);
                ilFName.setError("Nama depan tidak boleh kosong!");
            }

            if (TextUtils.isEmpty(etLName.getText().toString())) {
                ilLName.setErrorEnabled(true);
                ilLName.setError("Nama belakang tidak boleh kosong!");
            }

            if (TextUtils.isEmpty(etUsername.getText().toString())) {
                ilUsername.setErrorEnabled(true);
                ilUsername.setError("Username tidak boleh kosong!");
            }

            if (TextUtils.isEmpty(etEmail.getText().toString())) {
                ilEmail.setErrorEnabled(true);
                ilEmail.setError("Email tidak boleh kosong!");
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

    private void register(JSONObject data) {
        registerLoading.show();
        Call<MemberResponse> call = RetrofitServices.sendMemberRequset().APIRegister(data);
        if (call != null) {
            call.enqueue(new Callback<MemberResponse>() {
                @Override
                public void onResponse(Call<MemberResponse> call, Response<MemberResponse> response) {
                    registerLoading.dismiss();
                    if (response.isSuccessful()) {
                        new AlertDialog.Builder(RegisterActivity.this)
                                .setTitle("Pesan")
                                .setMessage(response.body().getMessage())
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                                .show();
                    } else {
                        APIErrorModel error = APIErrorUtils.parserError(response);
                        String message = error.getMessage();
                        new AlertDialog.Builder(RegisterActivity.this)
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
                public void onFailure(Call<MemberResponse> call, Throwable t) {
                    registerLoading.dismiss();
                    Log.e("RegisterError", t.getMessage());
                }
            });
        }
    }
}
