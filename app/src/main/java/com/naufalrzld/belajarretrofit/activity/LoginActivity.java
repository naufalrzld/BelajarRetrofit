package com.naufalrzld.belajarretrofit.activity;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.naufalrzld.belajarretrofit.R;

import butterknife.BindView;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.ilUsername)
    private TextInputLayout ilUsername;
    @BindView(R.id.ilPassword)
    private TextInputLayout ilPassword;

    @BindView(R.id.etUsername)
    private TextInputEditText etUsername;
    @BindView(R.id.etPassword)
    private TextInputEditText etPassword;

    @BindView(R.id.btnDaftar)
    private Button btnDaftar;
    @BindView(R.id.btnMasuk)
    private Button btnMasuk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
