package com.example.adrax.dely;

import android.support.v4.app.DialogFragment;
import android.os.CountDownTimer;

import com.example.adrax.dely.fragments.WaitingDialog;

/**
 * Created by Adrax on 09.07.2017.
 */

public class UpdateTimer extends CountDownTimer
        implements WaitingDialog.WaitingDialogListener
{
    private boolean is_update = true;
    private long time = 12;
    private long updateInterval = 10000;
    private long superInterval = 0;
    private long counter = 0;
    private MActivity activity;

    public UpdateTimer(long millisInFuture, long countDownInterval, MActivity activity) {
        super(millisInFuture, countDownInterval);
        superInterval = countDownInterval;
        updateInterval = countDownInterval;
        this.activity = activity;
        this.start();
    }

    @Override
    public void onTick(long l) {

    }

    public void showWaitingDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new WaitingDialog();
        dialog.show(activity.getSupportFragmentManager(), "DialogFragment");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        Enable();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        is_update = false;
        activity.StopWaiting();
    }

    public void Disable()
    {
        is_update = false;
        counter = 0;
    }

    public void Enable()
    {
        is_update = true;
        this.start();
    }

    public void SetUpdateInterval(long updateInterval){
        this.updateInterval = updateInterval;
    }

    public void SetTime(long time){
        this.time = time;
    }

    @Override
    public void onFinish() {
        showWaitingDialog();
    }
}
