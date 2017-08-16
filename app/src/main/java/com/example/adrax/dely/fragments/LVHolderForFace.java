package com.example.adrax.dely.fragments;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.adrax.dely.R;

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
    TextView tvDistance;
    ConstraintLayout item;
    ToggleButton btnExpandItem;
    private Context mContext;

    //объявляем конструктор
    public LVHolderForFace(Context context, View itemView){
        super(itemView);

        mContext = context;

        //привязываем элементы к полям
        tvDescription = (TextView)itemView.findViewById(R.id.tvDescription);
        tvDistance = (TextView) itemView.findViewById(R.id.tvDistance);
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
