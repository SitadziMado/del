package com.example.adrax.dely.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.adrax.dely.MActivity;
import com.example.adrax.dely.R;
import com.example.adrax.dely.core.Order;

import static com.example.adrax.dely.MActivity.face_cur_order_text;
import static com.example.adrax.dely.MActivity.face_orders;
import static com.example.adrax.dely.MActivity.selected_id;

public final class LVHolderForFace extends RecyclerView.ViewHolder {
    //объявим поля, созданные в файле интерфейса itemView.xml
    TextView tvFrom;
    TextView tvTo;
    String tvId;
    TextView tvCustomer;
    TextView tvPhoneNumber;
    TextView tvCost;
    TextView tvPayment;
    TextView tvDescription;
    public LinearLayout Item;

    private Context mContext;

    //объявляем конструктор
    public LVHolderForFace(Context context, View itemView){
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
                if (face_orders != null) {
                    // ToDo: id оказывается на единицу больше.
                    int id = Integer.parseInt(tvId) - 1;

                    //face_tab.getTabWidget().getChildAt(3).setVisibility(View.VISIBLE);
                    //face_tab.setCurrentTabByTag("ftab4");

                    selected_id = Integer.parseInt(face_orders.get(id).getStringProp(Order.ID));
                    face_cur_order_text = face_orders.get(id).toString();
                    switchFragment();
                }
            }
        });
    }

    private void switchFragment() {
        if (mContext == null)
            return;
        if (mContext instanceof MActivity) {
            MActivity feeds = (MActivity) mContext;
            feeds.orderShowFragment();
        }
    }
}
