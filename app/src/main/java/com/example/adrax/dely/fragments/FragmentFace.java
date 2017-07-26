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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adrax.dely.MActivity;
import com.example.adrax.dely.R;
import com.example.adrax.dely.core.InternetCallback;
import com.example.adrax.dely.core.Order;
import com.example.adrax.dely.core.OrderStatus;

import java.util.ArrayList;

import static com.example.adrax.dely.LoginActivity.user;
import static com.example.adrax.dely.MActivity.face_deliver_text;
import static com.example.adrax.dely.MActivity.face_delivery;
import static com.example.adrax.dely.MActivity.face_orders;
import static com.example.adrax.dely.MActivity.update_face;


public class FragmentFace extends Fragment {

    public TabHost face_tab;
    public TextView face_deliver_text_view;
    public Button btn_finish;
    public EditText text_code;
    //Объявляем RecyclerView
    RecyclerView rvMain;
    //Объявляем адаптер
    AdapterForFace adapterForFace;


    Context mContext;


    public FragmentFace() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_face, container, false);

        mContext = this.getActivity();


        face_deliver_text_view = (TextView) root.findViewById(R.id.text_cur_dely);

        face_tab = (TabHost) root.findViewById(R.id.fhost_tab);
        face_tab.setup();
        TabHost.TabSpec ts = face_tab.newTabSpec("ftab1");
        ts.setContent(R.id.ftab1);
        ts.setIndicator("Доставки");
        face_tab.addTab(ts);
        ts = face_tab.newTabSpec("ftab2");
        ts.setContent(R.id.ftab2);
        ts.setIndicator("Заказы");
        face_tab.addTab(ts);
        ts = face_tab.newTabSpec("ftab3");
        ts.setContent(R.id.ftab3);
        ts.setIndicator("История");
        face_tab.addTab(ts);
        face_tab.getTabWidget().getChildAt(2).setVisibility(View.GONE);
        // Inflate the layout for this fragment

        //обновляем заказы/доставки
        // update_face();

        //окончание доставки
        btn_finish = (Button) root.findViewById(R.id.btn_finish);
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //if(user.orderStart(selected_id))
                // user.orderFinish(Integer.parseInt(face_delivery.getId()));
                face_delivery.finish(new InternetCallback<Boolean>() {
                    @Override
                    public void call(Boolean result) {
                        if (result) {
                            Toast.makeText(
                                    getActivity(),
                                    "Доставка завершена!",
                                    Toast.LENGTH_LONG
                            ).show();

                            face_deliver_text_view.append("\n Дождитесь подтверждения заказчика.");
                        } else {
                            Toast.makeText(
                                    getActivity(),
                                    "Произошла ошибка при завершении заказа.",
                                    Toast.LENGTH_LONG
                            ).show();
                        }

                        face_delivery.status(new InternetCallback<OrderStatus>() {
                            @Override
                            public void call(OrderStatus orderStatus) {
                                switch (orderStatus) {
                                    case DELIVERY_DONE:
                                    case DELIVERED:
                                        btn_finish.setVisibility(View.GONE);
                                        break;

                                    default:
                                        btn_finish.setVisibility(View.VISIBLE);
                                        break;
                                }
                            }
                        });
                    }
                });
            }

        });

        // Code
        text_code = (EditText) root.findViewById(R.id.text_code);

        //Привязываем RecyclerView к элементу
        rvMain = (RecyclerView)root.findViewById(R.id.face_orders_list);
        //И установим LayoutManager
        rvMain.setLayoutManager(new LinearLayoutManager(getActivity()));
        //свистелки-перделки
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvMain.getContext(),
                llm.getOrientation());
        rvMain.addItemDecoration(dividerItemDecoration);
        //Создаём адаптер
        adapterForFace = new AdapterForFace(mContext,getOrders());
        //Применим наш адаптер к RecyclerView
        rvMain.setAdapter(adapterForFace);
        // Inflate the layout for this fragment

        UpdateUserInfo();

        face_deliver_text_view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                //Intent intent = new Intent(getActivity(), SignupActivity.class);
                //startActivityForResult(intent, REQUEST_SIGNUP);
                if (face_deliver_text_view.getText() == "Начните новую доставку!")
                {
                    switchFragment();
                }
            }
        });

        return root;
    }

    private void switchFragment() {
        if (mContext == null)
            return;
        if (mContext instanceof MActivity) {
            MActivity feeds = (MActivity) mContext;
            feeds.ShowFragmenGet();
        }
    }

    private ArrayList<Dely> getOrders() {
        ArrayList<Dely> delys = new ArrayList<>();
        Dely del;
        if (face_orders != null) {
            for (Integer i = 0; i < face_orders.length; i++) {
                Order cur = face_orders[i];

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
                if (cost.equals("")) {
                    del.cost = "Аванс: 0 руб.";
                } else {
                    del.cost = "Аванс: " + cost + " руб.";
                }

                if (payment.equals("")) {
                    del.payment = "Оплата: 0 руб.";
                } else {
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
            del.payment = "новый!";
            delys.add(del);
        }
        return delys;
    }

    private void UpdateUserInfo()
    {
        //обновляем заказы/доставки
        update_face();

        if (face_delivery != null)
        {
            face_delivery.status(new InternetCallback<OrderStatus>() {
                @Override
                public void call(OrderStatus orderStatus) {
                    switch (orderStatus) {
                        case DELIVERY_DONE:
                        case DELIVERED:
                            text_code.setVisibility(View.GONE);
                            btn_finish.setVisibility(View.GONE);
                            break;

                        default:
                            text_code.setVisibility(View.VISIBLE);
                            btn_finish.setVisibility(View.VISIBLE);
                            Kostyl_GetUpdateText();
                            break;
                    }
                }
            });
        } else {
            text_code.setVisibility(View.GONE);
            btn_finish.setVisibility(View.GONE);
        }

    }

    //обновление доставки
    public void Kostyl_GetUpdateText()
    {
        face_deliver_text_view.setText(face_deliver_text);
    }
}
