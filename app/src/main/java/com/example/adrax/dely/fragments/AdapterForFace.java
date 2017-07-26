package com.example.adrax.dely.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adrax.dely.R;

import java.util.ArrayList;

/**
 * Created by adrax on 30.10.16.
 */

public class AdapterForFace extends RecyclerView.Adapter<LVHolderForFace> {
    //Здесь мы будем хранить набор наших данных
    private ArrayList<Dely> delys;

    private  Context mContext;

    //Простенький конструктор
    public AdapterForFace(Context context, ArrayList<Dely> delivery){
        this.delys = delivery;
        mContext = context;
    }

    //Этот метод вызывается при прикреплении нового элемента к RecyclerView
    @Override
    public void onBindViewHolder(LVHolderForFace newViewHolder, int i){
        newViewHolder.setIsRecyclable(false);
        //Получаем элемент набора данных для заполнения
        Dely currentDely = delys.get(i);
        //Заполняем поля viewHolder'а данными из элемента набора данных
        newViewHolder.tvDescription.setText(currentDely.description);
        newViewHolder.tvFrom.setText(currentDely.from);
        newViewHolder.tvId = currentDely.id;
        newViewHolder.tvTo.setText(currentDely.to);
        newViewHolder.tvCustomer.setText(currentDely.customer);
        newViewHolder.tvPhoneNumber.setText(currentDely.phonenumber);
        newViewHolder.tvCost.setText(currentDely.cost);
        newViewHolder.tvPayment.setText(currentDely.payment);
    }

    //Этот метод вызывается при создании нового ViewHolder'а

    @Override
    public LVHolderForFace onCreateViewHolder(ViewGroup viewGroup, int i){
        //Создаём новый view при помощи LayoutInflater
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);

        return new LVHolderForFace(mContext, itemView);
    }

    //количество элементов списка
    @Override
    public int getItemCount(){
        return delys.size();
    }
}
