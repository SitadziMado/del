package com.example.adrax.dely.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adrax.dely.R;
import com.example.adrax.dely.core.Order;

import static com.example.adrax.dely.MActivity.orders;

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
        if (!orders.isEmpty()) {
            //Получаем элемент набора данных для заполнения
            Order cur = orders.get(i);

            newViewHolder.tvDescription.setText(cur.getStringProp(Order.DESCRIPTION));
            newViewHolder.tvFrom.setText("От: " + cur.getStringProp(Order.FROM));
            newViewHolder.tvId = Integer.toString(i);
            newViewHolder.tvTo.setText("До: " + cur.getStringProp(Order.TO));
            newViewHolder.tvTimeTake.setText(cur.getStringProp(Order.TAKE_TIME));
            newViewHolder.tvTimeBring.setText(cur.getStringProp(Order.BRING_TIME));
            newViewHolder.tvCustomer.setText("Заказчик: " + cur.getStringProp(Order.CUSTOMER));
            newViewHolder.tvPhoneNumber.setText("Номер телефона: " + cur.getStringProp(Order.PHONE));
            newViewHolder.tvCost.setText("Аванс: " + cur.getStringProp(Order.COST));
            newViewHolder.tvPayment.setText(cur.getStringProp(Order.PAYMENT) + "руб.");
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
        return orders.size();
    }
}