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

import static com.example.adrax.dely.LoginActivity.user;
import static com.example.adrax.dely.MActivity.face_cur_order_text;
import static com.example.adrax.dely.MActivity.selected_id;

@Deprecated
public class FragmentOrderShow extends Fragment {

    public FragmentOrderShow() {
        // Required empty public constructor
    }

    public TextView face_order_view;
    public Button btn_done;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_order_show, container, false);
        // Inflate the layout for this fragment

        face_order_view = (TextView) root.findViewById(R.id.text_cur_order);

        face_order_view.setText(face_cur_order_text);

        //конец заказа
        btn_done = (Button) root.findViewById(R.id.btn_done);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                //user.orderAccept(selected_id);
                //Toast.makeText(getActivity(),"Заказ завершён!", Toast.LENGTH_LONG).show();

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();

                }
            }

        });

        return root;

    }

}
