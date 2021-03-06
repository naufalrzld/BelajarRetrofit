package com.naufalrzld.belajarretrofit.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.naufalrzld.belajarretrofit.R;
import com.naufalrzld.belajarretrofit.activity.AddTokoActivity;
import com.naufalrzld.belajarretrofit.adapter.TokoAdapter;
import com.naufalrzld.belajarretrofit.model.member.Member;
import com.naufalrzld.belajarretrofit.model.toko.Toko;
import com.naufalrzld.belajarretrofit.model.toko.TokoResponse;
import com.naufalrzld.belajarretrofit.services.RetrofitServices;
import com.naufalrzld.belajarretrofit.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvListToko)
    RecyclerView rvListToko;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.fabTambahToko)
    FloatingActionButton fabAddToko;

    private List<Toko> listToko = new ArrayList<>();
    private TokoAdapter adapter;

    private String username;

    private SharedPreferencesUtils sharedPreferencesUtils;

    public TokoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_toko, container, false);
        ButterKnife.bind(this, v);

        swipeRefreshLayout.setOnRefreshListener(this);

        rvListToko.setHasFixedSize(true);
        rvListToko.setLayoutManager(new LinearLayoutManager(getContext()));

        fabAddToko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddTokoActivity.class));
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedPreferencesUtils = new SharedPreferencesUtils(getContext(), "DataMember");
        if (sharedPreferencesUtils.checkIfDataExists("profile")) {
            Member member = sharedPreferencesUtils.getObjectData("profile", Member.class);
            username = member.getUsername();
        }
    }

    @Override
    public void onRefresh() {
        getToko(username);
    }

    @Override
    public void onResume() {
        super.onResume();
        getToko(username);
    }

    private void getToko(String username) {
        swipeRefreshLayout.setRefreshing(true);
        try {
            Call<TokoResponse> call = null;
            call = RetrofitServices.sendTokoRequest().APIGetToko(username);
            if (call != null) {
                call.enqueue(new Callback<TokoResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TokoResponse> call, @NonNull Response<TokoResponse> response) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (response.isSuccessful()) {
                            listToko = response.body().getData();

                            adapter = new TokoAdapter(getContext(), listToko);
                            rvListToko.setAdapter(adapter);

                            if (listToko.isEmpty()) {
                                tvNoData.setVisibility(View.VISIBLE);
                                rvListToko.setVisibility(View.GONE);
                            } else {
                                tvNoData.setVisibility(View.GONE);
                                rvListToko.setVisibility(View.VISIBLE);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<TokoResponse> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.e("error", t.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
