package com.example.adrax.dely;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.adrax.dely.core.InternetCallback;
import com.example.adrax.dely.core.Result;
import com.example.adrax.dely.core.ToastHelper;
import com.example.adrax.dely.core.User;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @Bind(R.id.input_name)
    EditText _nameText;
    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.input_firstname)
    EditText _firstnameText;
    @Bind(R.id.input_surname)
    EditText _surnameText;
    @Bind(R.id.input_midname)
    EditText _midnameText;
    @Bind(R.id.input_selnum)
    EditText _selnumText;
    @Bind(R.id.input_about)
    EditText _aboutText;
    @Bind(R.id.btn_signup)
    Button _signupButton;
    @Bind(R.id.link_login)
    TextView _loginLink;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    signup();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void signup() throws UnsupportedEncodingException {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed(getString(R.string.check_info_validity));
            return;
        }
/// костыль -----------------------------------------------------------------------------------------
        // StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        // StrictMode.setThreadPolicy(policy);
/// -------------------------------------------------------------------------------------------------
        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.creating_account));
        progressDialog.show();

        final String name = _nameText.getText().toString();
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();
        final String firstname = _firstnameText.getText().toString();
        final String surname = _surnameText.getText().toString();
        final String midname = _midnameText.getText().toString();
        final String selnum = _selnumText.getText().toString();
        final String about = _aboutText.getText().toString();

        // Регистрация пользователя через асинхронную процедуру.
        User.register(
                name,
                password,
                email,
                firstname,
                surname,
                midname,
                selnum,
                about,
                new InternetCallback<String>() {
            @Override
            public void call(Result<String> result) {
                progressDialog.dismiss();

                if (result.isSuccessful()) {
                    onSignupSuccess();
                } else {
                    onSignupFailed(result.getMessage());
                }

            }
        });

        /* final DeliveryUser user = new DeliveryUser(getBaseContext());

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        if (user.register(name,password,email,firstname,surname,midname,selnum,about)) {
                            onSignupSuccess();
                        }
                        else {
                            onSignupFailed();
                        }
                        progressDialog.dismiss();
                    }
                }, 3000); */
    }

    /*private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }*/


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
        Intent myIntent;
        myIntent = new Intent(SignupActivity.this, LoginActivity.class);
        SignupActivity.this.startActivity(myIntent);
    }

    public void onSignupFailed(String message) {
        ToastHelper.createToast(getBaseContext(), message);
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError(getString(R.string.name_constraint));
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError(getString(R.string.email_constraint));
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError(getString(R.string.password_constraint));
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        /*
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Signup Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.adrax.dely/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);*/
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        /*Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Signup Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.adrax.dely/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();*/
    }
}
