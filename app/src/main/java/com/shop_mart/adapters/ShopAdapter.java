package com.shop_mart.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shop_mart.R;
import com.shop_mart.model.ShopModel;


import java.util.List;

/**
 * Created by funmi on 28/07/2017.
 */

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopHolder> {

    private Context context;
    private List<ShopModel> orderHistoryModelList;


    public class ShopHolder extends RecyclerView.ViewHolder {
        public TextView title, description, orderPlacedItems, streetNameText;
        public ShopHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);

        }
    }

    public ShopAdapter(Context context, List<ShopModel> orderHistoryHolders) {
        this.context = context;
        this.orderHistoryModelList = orderHistoryHolders;
    }

    @Override
    public ShopAdapter.ShopHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_list, parent, false);
        return new ShopHolder(view);
    }

    @Override
    public void onBindViewHolder(final ShopAdapter.ShopHolder holder, int position) {
        final ShopModel orderHistoryModel = orderHistoryModelList.get(position);
        holder.title.setText(orderHistoryModel.getTitle());
        holder.description.setText(orderHistoryModel.getDescription());
//        holder.streetNameText.setText(orderHistoryModel.getStreetName());

    }

    @Override
    public int getItemCount() {
        return orderHistoryModelList.size();
    }


}
