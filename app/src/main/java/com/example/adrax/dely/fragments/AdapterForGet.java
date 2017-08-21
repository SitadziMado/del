package com.example.adrax.dely.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adrax.dely.R;
import com.example.adrax.dely.core.Order;

import static com.example.adrax.dely.MActivity.sorted_orders;

/**
 * Created by adrax on 30.10.16.
 */

public class AdapterForGet extends RecyclerView.Adapter<LVHolderForGet> {
    private Context mContext;

    //Простенький конструктор
    public AdapterForGet(Context context){
        this.mContext = context;
    }

    //Этот метод вызывается при прикреплении нового элемента к RecyclerView
    @Override
    public void onBindViewHolder(LVHolderForGet newViewHolder, int i){
        newViewHolder.setIsRecyclable(false);

        //Заполняем поля viewHolder'а данными из элемента набора данных
        if (!sorted_orders.isEmpty()) {
            //Получаем элемент набора данных для заполнения
            Order cur = sorted_orders.get(i);

            newViewHolder.tvDescription.setText(String.format(mContext.getString(R.string.order_name), cur.getStringProp(Order.DESCRIPTION)));
            newViewHolder.tvFrom.setText(String.format(mContext.getString(R.string.order_from), cur.getStringProp(Order.FROM)));
            newViewHolder.tvId = Integer.toString(i);
            newViewHolder.tvTo.setText(String.format(mContext.getString(R.string.order_to), cur.getStringProp(Order.TO)));
            newViewHolder.tvTimeTake.setText(cur.getStringProp(Order.TAKE_TIME));
            newViewHolder.tvTimeBring.setText(cur.getStringProp(Order.BRING_TIME));
            newViewHolder.tvCustomer.setText(String.format(mContext.getString(R.string.order_customer), cur.getStringProp(Order.CUSTOMER)));
            newViewHolder.tvPhoneNumber.setText(String.format(mContext.getString(R.string.order_phone), cur.getStringProp(Order.PHONE)));
            newViewHolder.tvCost.setText(String.format(mContext.getString(R.string.order_cost), cur.getStringProp(Order.COST)));
            newViewHolder.tvPayment.setText(String.format(mContext.getString(R.string.currency_text), cur.getStringProp(Order.PAYMENT)));
            newViewHolder.tvDistance.setText(String.format(mContext.getString(R.string.meters_text), cur.getStringProp(Order.DISTANCE)));
        } else {
            // ToDo: Show picture of emptiness
        }
    }

    //Этот метод вызывается при создании нового ViewHolder'а
    @Override
    public LVHolderForGet onCreateViewHolder(ViewGroup viewGroup, int i){
        //Создаём новый view при помощи LayoutInflater
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);

        return new LVHolderForGet(itemView, mContext);
    }

    // количество элементов списка
    @Override
    public int getItemCount(){
        return sorted_orders.size();
    }
}