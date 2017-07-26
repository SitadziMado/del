package com.example.adrax.dely.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adrax.dely.R;
import com.example.adrax.dely.core.Order;
import com.example.adrax.dely.core.OrderList;

import java.util.ArrayList;

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

        //Получаем элемент набора данных для заполнения
        Order cur = orders.get(i);

        //Заполняем поля viewHolder'а данными из элемента набора данных
        try {
            newViewHolder.tvDescription.setText("Название: " + cur.getProp(Order.DESCRIPTION));
            newViewHolder.tvFrom.setText(cur.getProp("Откуда: " + Order.FROM));
            newViewHolder.tvId = cur.getProp(Order.ID);
            newViewHolder.tvTo.setText("Куда: " + cur.getProp(Order.TO));
            newViewHolder.tvCustomer.setText("Заказчик: " + cur.getProp(Order.CUSTOMER));
            newViewHolder.tvPhoneNumber.setText("Номер телефона: " + cur.getProp(Order.PHONE));
            newViewHolder.tvCost.setText("Аванс: " + cur.getProp(Order.COST));
            newViewHolder.tvPayment.setText("Оплата: " + cur.getProp(Order.PAYMENT));
        } catch (IndexOutOfBoundsException e) {
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

    //количество элементов списка
    @Override
    public int getItemCount(){
        return orders.size();
    }
}