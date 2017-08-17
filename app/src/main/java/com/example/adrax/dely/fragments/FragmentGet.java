package com.example.adrax.dely.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.example.adrax.dely.R;
import com.example.adrax.dely.core.Order;

import static com.example.adrax.dely.MActivity.orders;
import static com.example.adrax.dely.MActivity.sorted_orders;

public class FragmentGet extends Fragment {
    //Объявляем RecyclerView
    RecyclerView rvMain;

    //Объявляем адаптер
    AdapterForGet adapterForGet;

    Spinner _spinnerSort;
    Context mContext;

    ToggleButton btnReverse;

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

        sorted_orders = orders; // Copy...


        // SORTING ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        _spinnerSort = (Spinner) root.findViewById(R.id.spinner_sort);
        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        getActivity(),
                        R.array.sort_array,
                        R.layout.item_spinner);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        _spinnerSort.setAdapter(adapter);

        _spinnerSort.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // An item was selected. You can retrieve the selected item using
                        // parent.getItemAtPosition(pos)
                       switch (position){
                           case 0: // Расстояние
                               sorted_orders.sortBy(Order.DISTANCE);
                               break;
                           case 1:  // Оплата
                               sorted_orders.sortBy(Order.PAYMENT);
                               break;
                           case 2:  // Вес
                               sorted_orders.sortBy(Order.WEIGHT);
                               break;
                           case 3:  // Вес
                               sorted_orders.sortBy(Order.COST);
                               break;
                           default:
                               break;
                       }
                        update(mContext);
                        btnReverse.setChecked(true);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Another interface callback
                    }
                }
        );

        btnReverse = (ToggleButton) root.findViewById(R.id.btnReverse);
        btnReverse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // increasing
                    // ToDo: revrse function
                } else { // decreasing
                    // ToDo: revrse function
                }
            }
        });
        // End SORTING----------------------------------------------------

            //RecyclerView
        //Привязываем RecyclerView к элементу
        rvMain = (RecyclerView)root.findViewById(R.id.delys_list);

        //И установим LayoutManager
        rvMain.setLayoutManager(new LinearLayoutManager(getActivity()));

        // свистелки-перделки
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvMain.getContext(),
                llm.getOrientation());
        rvMain.addItemDecoration(dividerItemDecoration);

        // Создаём адаптер
        adapterForGet = new AdapterForGet(mContext);

        // Применим наш адаптер к RecyclerView
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
