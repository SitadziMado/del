package com.example.adrax.dely.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.adrax.dely.R;

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
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = this.getArguments();
        String from = args.getString("from");
        String to = args.getString("to");
        String cost = args.getString("cost");
        String num = args.getString("number");
        String description = args.getString("description");
        String payment = args.getString("money");
        String timeTake = args.getString("timeTake");
        String timeBring = args.getString("timeBring");
        String weight = args.getString("weight");

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
                .setPositiveButton("Создать", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(),1, new Intent("done"));
                    }
                })
                .setNegativeButton("Назад", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        fromText.setText(from);
        toText.setText(to);
        costText.setText(cost+"руб");
        timeTakeText.setText(timeTake);
        timeBringText.setText(timeBring);
        paymentText.setText(payment+"руб");
        descriptionText.setText(description);
        weightText.setText(weight+"гр");
        numText.setText(num);

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
