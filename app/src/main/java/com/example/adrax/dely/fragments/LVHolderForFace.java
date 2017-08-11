package com.example.adrax.dely.fragments;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.adrax.dely.MActivity;
import com.example.adrax.dely.R;
import com.example.adrax.dely.core.InternetCallback;
import com.example.adrax.dely.core.Order;

import static com.example.adrax.dely.LoginActivity.user;
import static com.example.adrax.dely.MActivity.face_cur_order_text;
import static com.example.adrax.dely.MActivity.face_delivery;
import static com.example.adrax.dely.MActivity.face_orders;
import static com.example.adrax.dely.MActivity.orders;
import static com.example.adrax.dely.MActivity.selected_id;
import static com.example.adrax.dely.MActivity.updateFace;
import static com.example.adrax.dely.MActivity.updateOrders;

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
    TextView tvTimeTake;
    TextView tvTimeBring;
    ConstraintLayout item;
    ToggleButton btnExpandItem;
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
        tvTimeTake = (TextView) itemView.findViewById(R.id.tvTimeTake);
        tvTimeBring = (TextView) itemView.findViewById(R.id.tvTimeBring);
        item = (ConstraintLayout) itemView.findViewById(R.id.item_order);

        btnExpandItem = (ToggleButton) itemView.findViewById(R.id.btnExpandItem);
        btnExpandItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked) {
                    tvCost.setVisibility(View.VISIBLE);
                    tvPhoneNumber.setVisibility(View.VISIBLE);
                    tvCustomer.setVisibility(View.VISIBLE);
                } else {
                    tvCost.setVisibility(View.GONE);
                    tvPhoneNumber.setVisibility(View.GONE);
                    tvCustomer.setVisibility(View.GONE);
                }
            }
        });
    }
}
