package com.example.adrax.dely.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adrax.dely.R;
import com.example.adrax.dely.core.InternetCallback;
import com.example.adrax.dely.core.Order;

import static com.example.adrax.dely.LoginActivity.user;
import static com.example.adrax.dely.MActivity.delyDescription;
import static com.example.adrax.dely.MActivity.face_delivery;
import static com.example.adrax.dely.MActivity.updateOrders;
import static com.example.adrax.dely.MActivity.selected_id;
import static com.example.adrax.dely.MActivity.updateFace;


public class FragmentDeliveriesShow extends Fragment {


    public static TextView text_description;
    public static Button btn_start;

    public FragmentDeliveriesShow() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_dely_show, container, false);


        text_description = (TextView)root.findViewById(R.id.text_description);

        text_description.setText(delyDescription);

        //начало доставки
        btn_start = (Button) root.findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //if(user.orderStart(selected_id))
                if (face_delivery == null) {
                    new Order(user, Order.ID, selected_id.toString()).start(
                            new InternetCallback<Boolean>() {
                                @Override
                                public void call(Boolean result) {
                                    if (result) {
                                        updateOrders();
                                        updateFace();
                                        Toast.makeText(getActivity(),"Доставка началась!", Toast.LENGTH_LONG).show();
                                        if (getFragmentManager().getBackStackEntryCount() > 0) {
                                            getFragmentManager().popBackStack();
                                        }
                                    } else {
                                        Toast.makeText(
                                                getActivity(),
                                                "Не удалось начать заказ, возможно, он уже начат.",
                                                Toast.LENGTH_LONG
                                        ).show();
                                    }
                                }
                    });

                } else {
                    Toast.makeText(
                            getActivity(),
                            "У вас уже есть активная доставка!",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

        });

        return root;
    }
}
