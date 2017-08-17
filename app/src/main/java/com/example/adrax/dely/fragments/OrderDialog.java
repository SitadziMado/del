package com.example.adrax.dely.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.adrax.dely.R;
import com.example.adrax.dely.core.Order;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class OrderDialog extends DialogFragment {

    //private OnFragmentInteractionListener mListener;
    //OrderDialog.OrderDialogListener mListener;

    TextView fromText;
    TextView toText;
    TextView costText;
    TextView timeTakeText;
    TextView timeBringText;
    TextView paymentText;
    TextView descriptionText;
    TextView weightText;
    TextView numText;

    public OrderDialog() {
        // Required empty public constructor
    }

    @Override
    @NonNull
    public Dialog onCreateDialog( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = this.getArguments();
        String from = args.getString(Order.FROM);
        String to = args.getString(Order.TO);
        String cost = args.getString(Order.COST);
        String num = args.getString(Order.PHONE);
        String description = args.getString(Order.DESCRIPTION);
        String payment = args.getString(Order.PAYMENT);
        String timeTake = args.getString(Order.TAKE_TIME);
        String timeBring = args.getString(Order.BRING_TIME);
        String weight = args.getString(Order.WEIGHT);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setMessage(R.string.dialog_waiting)
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View content = inflater.inflate(R.layout.fragment_order_dialog, null);
        fromText = (TextView) content.findViewById(R.id.tvFrom);
        toText = (TextView) content.findViewById(R.id.tvTo);
        costText = (TextView) content.findViewById(R.id.tvCost);
        timeTakeText = (TextView) content.findViewById(R.id.tvTimeTake);
        timeBringText = (TextView) content.findViewById(R.id.tvTimeBring);
        paymentText = (TextView) content.findViewById(R.id.tvPayment);
        descriptionText = (TextView) content.findViewById(R.id.tvDescription);
        weightText = (TextView) content.findViewById(R.id.tvWeight);
        numText = (TextView) content.findViewById(R.id.tvPhoneNumber);

        builder.setView(content)
                .setTitle("Подтвердите заказ")
                .setPositiveButton("Создать", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), 1, new Intent("done"));
                    }
                })
                .setNegativeButton("Назад", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        fromText.setText(from);
        toText.setText(to);
        costText.setText(cost + "руб");
        timeTakeText.setText(timeTake);
        timeBringText.setText(timeBring);
        paymentText.setText(payment + "руб");
        descriptionText.setText(description);
        weightText.setText(weight + "гр");
        numText.setText(num);

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
