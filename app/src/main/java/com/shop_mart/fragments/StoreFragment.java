package com.shop_mart.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.liuguangqiang.asyncokhttp.AsyncOkHttp;
import com.liuguangqiang.asyncokhttp.BaseResponseHandler;
import com.shop_mart.R;
import com.shop_mart.activities.MainActivity;
import com.shop_mart.activities.SignUpActivity;
import com.shop_mart.adapters.ShopAdapter;
import com.shop_mart.constants.Constant;
import com.shop_mart.model.ShopModel;
import com.shop_mart.utils.EmptyRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StoreFragment extends Fragment {

    private List<ShopModel> shopModelList;
    private ShopAdapter adapter;
    private EmptyRecyclerView recyclerView;
    private TextView nothingToShowTxt;

    public StoreFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_store,container,false);

        shopModelList = new ArrayList<>();

        recyclerView = (EmptyRecyclerView) v.findViewById(R.id.recycler_view);
        nothingToShowTxt = (TextView) v.findViewById(R.id.nothingToShowTxt);

        adapter = new ShopAdapter(getActivity(), shopModelList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        sendRequest();

        return v;
    }

    private void showToast(String message) {
        if (message != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }
    }

    private void sendRequest() {
        final Dialog alertDialog = new Dialog(getActivity());
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.loading);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.show();
        AsyncOkHttp okHttp = AsyncOkHttp.getInstance();
        okHttp.addHeader(Constant.HEADER_CONTENT_TYPE, Constant.CONTENT_TYPE);
        okHttp.get(Constant.STORE_URL, new BaseResponseHandler() {
            @Override
            public void onSuccess(int code, String responseString) {
                alertDialog.dismiss();
                try {
                    if (responseString != null) {
                        JSONObject jsonResponse = new JSONObject(responseString);
                        if (jsonResponse.optBoolean("status")) {
                            JSONArray data =  jsonResponse.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                ShopModel model = new ShopModel();
                                model.setTitle(data.getJSONObject(i).getString("title"));
                                model.setDescription(data.getJSONObject(i).getString("description"));
                                model.setStreetName(data.getJSONObject(i).getString("street_name"));
                                model.setLat(data.getJSONObject(i).getString("latitude"));
                                model.setLng(data.getJSONObject(i).getString("longitude"));

                                shopModelList.add(model);

                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int code, String responseString) {
                alertDialog.dismiss();
                try {
                    JSONObject response = new JSONObject(responseString);
                    if (!response.optBoolean("status")) {
                        String message = response.getString("message");
                        showToast(message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

}
