package com.naufalrzld.belajarretrofit.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
    @BindView(R.id.lytListToko)
    LinearLayout lytListToko;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvListToko)
    RecyclerView rvListToko;
    @BindView(R.id.tvNoData)
    TextView tvNoData;

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

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedPreferencesUtils = new SharedPreferencesUtils(getContext(), "DataMember");
        if (sharedPreferencesUtils.checkIfDataExists("profile")) {
            Member member = sharedPreferencesUtils.getObjectData("profile", Member.class);
            username = member.getUsername();

            getToko(username);
        }
    }

    @Override
    public void onRefresh() {
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
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<TokoResponse> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("error", t.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
