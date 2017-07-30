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
import com.example.adrax.dely.core.OrderList;

import java.util.ArrayList;

import static com.example.adrax.dely.LoginActivity.user;
import static com.example.adrax.dely.MActivity.orders;


public class FragmentGet extends Fragment {
    //Объявляем RecyclerView
    RecyclerView rvMain;

    //Объявляем адаптер
    AdapterForGet adapterForGet;

    Context mContext;

    public static final Object ordersLock = new Object();

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
        adapterForGet = new AdapterForGet(mContext);

        //Применим наш адаптер к RecyclerView
        rvMain.setAdapter(adapterForGet);

        // Inflate the layout for this fragment
        return root;

    }

    public void update(Context myContext) {
        //Создаём адаптер
        adapterForGet = new AdapterForGet(myContext);
        //Применим наш адаптер к RecyclerView
        rvMain.setAdapter(adapterForGet);
        // Inflate the layout for this fragment
    }

    /* private void getDeliveries() {
        user.syncOrders(new InternetCallback<OrderList>() {
            @Override
            public void call(OrderList result) {
                synchronized (ordersLock) {
                    orders = result;
                }
            }
        });
    } */

        // orders = user.orderList();

        /*if (null != orders) {
            for (Integer i = 0; i < orders.length; ++i) {
                // DeliveryOrder cur = orders[i];
                Order cur = orders[i];

                String name = cur.getStringProp("Name");
                String from = cur.getStringProp("From");
                String to = cur.getStringProp("To");
                String customer = cur.getStringProp("Customer");
                String phone = cur.getStringProp("Num");
                String payment = cur.getStringProp("Payment");
                String cost = cur.getStringProp("Cost");
                String weight = cur.getStringProp("Wt");
                String size = cur.getStringProp("Size");
                String code = cur.getStringProp("Code");
                String entrance = cur.getStringProp("Padik");
                String floor = cur.getStringProp("Floor");

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
    }*/
}
