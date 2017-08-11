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
}
