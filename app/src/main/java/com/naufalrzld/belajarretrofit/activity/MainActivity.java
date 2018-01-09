package com.naufalrzld.belajarretrofit.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.naufalrzld.belajarretrofit.R;
import com.naufalrzld.belajarretrofit.model.member.Member;
import com.naufalrzld.belajarretrofit.utils.SessionManager;
import com.naufalrzld.belajarretrofit.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.icn_logout)
    ImageView icnLogout;
    @BindView(R.id.profile_image)
    ImageView profileImage;
    @BindView(R.id.tvNama)
    TextView tvNama;
    @BindView(R.id.tvEmail)
    TextView tvEmail;

    private SessionManager seesion;
    private SharedPreferencesUtils sharedPreferencesUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        seesion = new SessionManager(this);
        sharedPreferencesUtils = new SharedPreferencesUtils(this, "DataMember");
        if (sharedPreferencesUtils.checkIfDataExists("profile")) {
            Member member = sharedPreferencesUtils.getObjectData("profile", Member.class);
            String nama = member.getFirstName() + " " + member.getLastName();
            String email = member.getEmail();
            tvNama.setText(nama);
            tvEmail.setText(email);
        }

        icnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seesion.setLogin(false);
                sharedPreferencesUtils.clearAllData();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}
