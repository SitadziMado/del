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

            newViewHolder.tvDescription.setText("Название: " + cur.getStringProp(Order.DESCRIPTION));
            newViewHolder.tvFrom.setText("Откуда: " + cur.getStringProp(Order.FROM));
            newViewHolder.tvId = cur.getStringProp(Order.ID);
            newViewHolder.tvTo.setText("Куда: " + cur.getStringProp(Order.TO));
            newViewHolder.tvCustomer.setText("Заказчик: " + cur.getStringProp(Order.CUSTOMER));
            newViewHolder.tvPhoneNumber.setText("Номер телефона: " + cur.getStringProp(Order.PHONE));
            newViewHolder.tvCost.setText("Аванс: " + cur.getStringProp(Order.COST));
            newViewHolder.tvPayment.setText("Оплата: " + cur.getStringProp(Order.PAYMENT));
        } else {
            newViewHolder.tvDescription.setText("На данный момент");
            newViewHolder.tvFrom.setText("");
            newViewHolder.tvId = "заказов нет, ";
            newViewHolder.tvTo.setText("");
            newViewHolder.tvCustomer.setText("но Вы всегда");
            newViewHolder.tvPhoneNumber.setText("");
            newViewHolder.tvCost.setText("можете оформить новый!");
            newViewHolder.tvPayment.setText("");
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