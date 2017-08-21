package com.example.adrax.dely.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.adrax.dely.R;


/* В фрагменте родителе:
FeedbackDialog feedbackDialog = new FeedbackDialog();
feedbackDialog.setTargetFragment(this, REQUEST_CODE);
feedbackDialog.show(getFragmentManager(), "FeedbackDialog");

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    String feedback = data.getStringExtra("feedback");
    int rating = data.getIntExtra("rating",0);
    // Делаем, что хотим
}
*/
public class FeedbackDialog extends DialogFragment {

    TextView feedbackText;
    RatingBar ratingBar;

    int SEND_CODE = 1;
    int CLOSE_CODE = 0;

    public FeedbackDialog() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setMessage(R.string.dialog_waiting)
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View content = inflater.inflate(R.layout.fragment_feedback_dialog, null);
        feedbackText = (TextView) content.findViewById(R.id.textFeedback);
        ratingBar = (RatingBar) content.findViewById(R.id.ratingBar);

        builder.setView(content)
                .setTitle(R.string.leave_feedback)
                .setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent();
                        intent.putExtra("feedback",feedbackText.getText().toString());
                        intent.putExtra("rating",ratingBar.getNumStars());
                        getTargetFragment().onActivityResult(getTargetRequestCode(),SEND_CODE, intent);
                    }
                })
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

}
