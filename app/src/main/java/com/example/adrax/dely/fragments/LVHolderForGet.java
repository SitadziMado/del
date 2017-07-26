package com.example.adrax.dely.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.adrax.dely.MActivity;
import com.example.adrax.dely.R;
import com.example.adrax.dely.core.Order;

import static com.example.adrax.dely.MActivity.delyDescription;
import static com.example.adrax.dely.MActivity.orders;
import static com.example.adrax.dely.MActivity.selected_id;

public final class LVHolderForGet extends RecyclerView.ViewHolder {
    //объявим поля, созданные в файле интерфейса itemView.xml
    TextView tvFrom;
    TextView tvTo;
    String tvId;
    TextView tvCustomer;
    TextView tvPhoneNumber;
    TextView tvCost;
    TextView tvPayment;
    TextView tvDescription;
    LinearLayout Item;

    private Context mContext;

    // объявляем конструктор
    public LVHolderForGet(View itemView, Context context){
        super(itemView);
        mContext = context;
        //привязываем элементы к полям
        tvDescription = (TextView)itemView.findViewById(R.id.tvDescription);
        tvFrom = (TextView)itemView.findViewById(R.id.tvFrom);
        tvTo = (TextView)itemView.findViewById(R.id.tvTo);
        tvCustomer = (TextView)itemView.findViewById(R.id.tvCustomer);
        tvPhoneNumber = (TextView)itemView.findViewById(R.id.tvPhoneNumber);
        tvCost = (TextView)itemView.findViewById(R.id.tvCost);
        tvPayment = (TextView)itemView.findViewById(R.id.tvPayment);
        Item = (LinearLayout)itemView.findViewById(R.id.item);
        Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != orders) {
                    int id = Integer.parseInt(tvId);
                    //id = 0;

                    selected_id = Integer.parseInt(orders.get(id).getProp(Order.ID));
                    delyDescription = orders.get(id).toString();

                    switchFragment();
                }
            }
        });
    }

    private void switchFragment() {
        if (mContext == null) {
            return;
        }
        if (mContext instanceof MActivity) {
            MActivity feeds = (MActivity) mContext;
            feeds.deliveryShowFragment();
        }
    }
}
