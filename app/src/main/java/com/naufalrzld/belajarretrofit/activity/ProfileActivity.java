package com.naufalrzld.belajarretrofit.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.naufalrzld.belajarretrofit.R;
import com.naufalrzld.belajarretrofit.model.member.Member;
import com.naufalrzld.belajarretrofit.model.member.MemberResponse;
import com.naufalrzld.belajarretrofit.services.RetrofitServices;
import com.naufalrzld.belajarretrofit.utils.SharedPreferencesUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.profile_image)
    ImageView profileImage;
    @BindView(R.id.tvNama)
    TextView tvNama;
    @BindView(R.id.tvUsername)
    TextView tvUsername;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.btnUbahNama)
    Button btnUbahNama;
    @BindView(R.id.btnUbahEmail)
    Button btnUbahEmail;
    @BindView(R.id.btnUbahPass)
    Button btnUbahPass;

    private SharedPreferencesUtils sharedPreferencesUtils;
    private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
    private TextDrawable mDrawableBuilder;
    private Member member;
    private JSONObject param = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        sharedPreferencesUtils = new SharedPreferencesUtils(this, "DataMember");

        try {
            if (sharedPreferencesUtils.checkIfDataExists("profile")) {
                member = sharedPreferencesUtils.getObjectData("profile", Member.class);
                String nama = member.getFirstName() + " " + member.getLastName();
                String username = member.getUsername();
                String email = member.getEmail();
                tvNama.setText(nama);
                tvUsername.setText(username);
                tvEmail.setText(email);

                setProfileImage(nama);

                param.put("username", username);
                param.put("firstName", member.getFirstName());
                param.put("lastName", member.getLastName());
                param.put("email", email);
                param.put("password", member.getPassword());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnUbahNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeProfile("name");
            }
        });

        btnUbahEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeProfile("email");
            }
        });

        btnUbahPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeProfile("pass");
            }
        });
    }

    private void changeProfile(final String type) {
        @SuppressLint("RestrictedApi")
        final AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        final AlertDialog dialog = alert.create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.change_profile_layout, null);
        final ChangeNameViewHolder viewHolder = new ChangeNameViewHolder(dialogView);
        if (type.equals("name")) {
            dialog.setTitle("Nama Lengkap");
            viewHolder.lytChangeEmail.setVisibility(View.GONE);
            viewHolder.lytChangePass.setVisibility(View.GONE);

            String fName = member.getFirstName();
            String lName = member.getLastName();
            viewHolder.etFName.setText(fName);
            viewHolder.etLName.setText(lName);
        } else if (type.equals("email")) {
            dialog.setTitle("Email");
            viewHolder.lytChangeName.setVisibility(View.GONE);
            viewHolder.lytChangePass.setVisibility(View.GONE);

            String email = member.getEmail();
            viewHolder.etEmail.setText(email);
        } else if (type.equals("pass")) {
            dialog.setTitle("Password");
            viewHolder.lytChangeName.setVisibility(View.GONE);
            viewHolder.lytChangeEmail.setVisibility(View.GONE);
        }
        dialog.setView(dialogView);
        dialog.show();
        viewHolder.btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (type.equals("name")) {
                        String fName = viewHolder.etFName.getText().toString();
                        String lName = viewHolder.etLName.getText().toString();

                        param.put("firstName", fName);
                        param.put("lastName", lName);
                    } else if (type.equals("email")) {
                        String email = viewHolder.etEmail.getText().toString();

                        param.put("email", email);
                    } else if (type.equals("pass")) {
                        String currPass = viewHolder.etCurrPass.getText().toString();
                        String newPass = viewHolder.etNewPass.getText().toString();

                        member = sharedPreferencesUtils.getObjectData("profile", Member.class);

                        if (currPass.equals(member.getPassword())) {
                            viewHolder.ilCurrPass.setErrorEnabled(false);
                            param.put("password", newPass);
                        } else {
                            viewHolder.ilCurrPass.setErrorEnabled(true);
                            viewHolder.ilCurrPass.setError("Password sekarang tidak sesuai!");
                            return;
                        }
                    }

                    sendReqChangeName(param);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
    }

    private void setProfileImage(String nama) {
        String letter = "A";

        if(nama != null && !nama.isEmpty()) {
            letter = nama.substring(0, 1);
        }

        int color = mColorGenerator.getRandomColor();
        mDrawableBuilder = TextDrawable.builder().buildRound(letter, color);
        profileImage.setImageDrawable(mDrawableBuilder);
    }

    protected static class ChangeNameViewHolder {
        @BindView(R.id.lytChangeName)
        LinearLayout lytChangeName;
        @BindView(R.id.lytChangeEmail)
        LinearLayout lytChangeEmail;
        @BindView(R.id.lytChangePass)
        LinearLayout lytChangePass;

        @BindView(R.id.ilFName)
        TextInputLayout ilFName;
        @BindView(R.id.ilLName)
        TextInputLayout ilLName;
        @BindView(R.id.ilEmail)
        TextInputLayout ilEmail;
        @BindView(R.id.ilCurrPass)
        TextInputLayout ilCurrPass;
        @BindView(R.id.ilNewPass)
        TextInputLayout ilNewPass;

        @BindView(R.id.etFName)
        TextInputEditText etFName;
        @BindView(R.id.etLName)
        TextInputEditText etLName;
        @BindView(R.id.etEmail)
        TextInputEditText etEmail;
        @BindView(R.id.etCurrPass)
        TextInputEditText etCurrPass;
        @BindView(R.id.etNewPass)
        TextInputEditText etNewPass;

        @BindView(R.id.btnSimpan)
        Button btnSimpan;

        ChangeNameViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

    private void sendReqChangeName(JSONObject data) {
        try {
            Call<MemberResponse> call = null;
            call = RetrofitServices.sendMemberRequset().APIUpdate(data);
            if (call != null) {
                call.enqueue(new Callback<MemberResponse>() {
                    @Override
                    public void onResponse(Call<MemberResponse> call, Response<MemberResponse> response) {
                        if (response.isSuccessful()) {
                            Member member = response.body().getData();
                            sharedPreferencesUtils.storeData("profile", member);
                            String nama = member.getFirstName() + " " + member.getLastName();
                            String email = member.getEmail();
                            tvNama.setText(nama);
                            tvEmail.setText(email);
                            setProfileImage(nama);
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MemberResponse> call, Throwable t) {
                        Log.d("error", t.getMessage());
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
