package com.example.adrax.dely.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adrax.dely.core.InternetCallback;
import com.example.adrax.dely.core.Order;
import com.example.adrax.dely.R;

import java.util.ArrayList;

import static com.example.adrax.dely.LoginActivity.user;
import static com.example.adrax.dely.MActivity.orders;


public class FragmentGet extends Fragment {

    //delys list
    //delys list
    //Объявляем RecyclerView
    RecyclerView rvMain;
    //Объявляем адаптер
    AdapterForGet adapterForGet;

    Context mContext;

    public static Object ordersLock = new Object();

    public FragmentGet() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = this.getActivity();
        View root = inflater.inflate(R.layout.fragment_get, container, false);

            //RecyclerView
        //Привязываем RecyclerView к элементу
        rvMain = (RecyclerView)root.findViewById(R.id.delys_list);
        //И установим LayoutManager
        rvMain.setLayoutManager(new LinearLayoutManager(getActivity()));
        //свистелки-перделки
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvMain.getContext(),
                llm.getOrientation());
        rvMain.addItemDecoration(dividerItemDecoration);
        //Создаём адаптер
        adapterForGet = new AdapterForGet(mContext,getDelys());
        //Применим наш адаптер к RecyclerView
        rvMain.setAdapter(adapterForGet);
        // Inflate the layout for this fragment
        return root;

    }

    public void update(Context mycontext) {
        //Создаём адаптер
        adapterForGet = new AdapterForGet(mycontext, getDelys());
        //Применим наш адаптер к RecyclerView
        rvMain.setAdapter(adapterForGet);
        // Inflate the layout for this fragment
    }

    private ArrayList<Dely> getDelys() {
        user.syncOrders(new InternetCallback<Order[]>() {
            @Override
            public void call(Order[] result) {
                synchronized (ordersLock) {
                    orders = result;
                }
            }
        });

        ArrayList<Dely> delys = new ArrayList<>();
        Dely del;

        user.syncOrders(new InternetCallback<Order[]>() {
            @Override
            public void call(Order[] result) {
                synchronized (ordersLock) {
                    orders = result;
                }
            }
        });

        // orders = user.orderList();

        if (null != orders) {
            for (Integer i = 0; i < orders.length; ++i) {
                // DeliveryOrder cur = orders[i];
                Order cur = orders[i];

                String name = cur.getProp("Name");
                String from = cur.getProp("From");
                String to = cur.getProp("To");
                String customer = cur.getProp("Customer");
                String phone = cur.getProp("Num");
                String payment = cur.getProp("Payment");
                String cost = cur.getProp("Cost");
                String weight = cur.getProp("Wt");
                String size = cur.getProp("Size");
                String code = cur.getProp("Code");
                String entrance = cur.getProp("Padik");
                String floor = cur.getProp("Floor");

                del = new Dely();
                del.id = i.toString();
                del.description = "Название: " + name;
                del.from = "Откуда: " + from;
                del.to = "Куда: " + to;
                del.customer = "Заказчик: " + customer;
                del.phonenumber = "Номер телефона: " + phone;

                if (cost != null && cost.equals("")) {
                    del.cost = "Аванс: 0 руб.";
                }
                else {
                    del.cost = "Аванс: " + cost + " руб.";
                }

                if (payment != null && payment.equals("")) {
                    del.payment = "Оплата: 0 руб.";
                }
                else {
                    del.payment = "Оплата: " + payment + " руб.";
                }

                delys.add(del);
            }
        } else {
            del = new Dely();
            del.description = "на данный момент";
            del.from = "заказов нет.";
            del.to = "";
            del.id = "Увы,";
            del.customer = "Но вы всегда";
            del.phonenumber = "можете оформить";
            del.cost = "";
            del.payment = "новый заказ!";
            delys.add(del);
        }
        return delys;
    }
}
