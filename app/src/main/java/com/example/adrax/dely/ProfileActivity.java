package com.example.adrax.dely;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.adrax.dely.core.InternetCallback;
import com.example.adrax.dely.core.Result;
import com.example.adrax.dely.core.ToastHelper;

import static com.example.adrax.dely.LoginActivity.user;

public class ProfileActivity extends AppCompatActivity {

    TextView lvMoney;
    TextView lvMail;
    TextView lvNumber;
    TextView lvName;
    TextView lvLogin;
    TextView card_owner;
    TextView card_number;
    TextView card_valid;
    TextView cvv;
    TextView passport_serial;
    TextView passport_number;
    TextView passport_given;
    TextView passport_date;

    Button btnAddCard;
    Button btnAddPassport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        lvNumber = (TextView) findViewById(R.id.userNumber);
        lvLogin = (TextView) findViewById(R.id.userLogin);
        lvMoney = (TextView) findViewById(R.id.userMoney);
        lvMail = (TextView) findViewById(R.id.userMail);
        lvName = (TextView) findViewById(R.id.userName);

        card_owner      = (TextView) findViewById(R.id.card_owner);
        card_number     = (TextView) findViewById(R.id.card_number);
        card_valid      = (TextView) findViewById(R.id.card_valid);
        cvv             = (TextView) findViewById(R.id.cvv);
        passport_serial = (TextView) findViewById(R.id.passport_serial);
        passport_number = (TextView) findViewById(R.id.passport_number);
        passport_given  = (TextView) findViewById(R.id.passport_given);
        passport_date   = (TextView) findViewById(R.id.passport_date);

        writeUserInfo();

        btnAddCard = (Button) findViewById(R.id.btnAddCard);
        btnAddPassport = (Button) findViewById(R.id.btnAddPassport);

        // Добавление карты
        btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.addCreditCard(card_number.getText().toString(),
                        card_valid.getText().toString(),
                        card_owner.getText().toString(),
                        new InternetCallback<String>() {
                            @Override
                            public void call(Result<String> result) {
                                // if (result.isSuccessful()) {
                                ToastHelper.createToast(getApplicationContext(), result.getMessage());
                                /* } else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Ошибка при обработке данных",
                                            Toast.LENGTH_LONG
                                    ).show();
                                } */
                            }
                        });
            }
        });

        // Добавление паспорта
        btnAddPassport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ToDo: добавить выбор пола
                user.addPassport(passport_serial.getText().toString(),
                        passport_number.getText().toString(),
                        passport_given.getText().toString(),
                        passport_date.getText().toString(),
                        true,
                        new InternetCallback<String>() {
                            @Override
                            public void call(Result<String> result) {
                                // if (result.isSuccessful()) {
                                    ToastHelper.createToast(getApplicationContext(), result.getMessage());
                                /* } else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Ошибка при обработке данных",
                                            Toast.LENGTH_LONG
                                    ).show();
                                } */
                            }
                        });
            }
        });
    }

    void writeUserInfo() {
        lvName.setText(String.format("%s %s %s", user.getSurname(), user.getName(), user.getMiddleName()));
        lvMail.setText(user.getMail());
        lvMoney.setText(String.format(getString(R.string.currency_text), user.getMoney()));
        lvNumber.setText(user.getPhone());
        lvLogin.setText(user.getLogin());

        /*
        card_owner.setText(user.getSomeShit());
        card_number.setText(user.getSomeShit());
        card_valid.setText(user.getSomeShit());
        //cvv.setText(user.getLogin());
        passport_serial.setText(user.getSomeShit());
        passport_number.setText(user.getSomeShit());
        passport_given.setText(user.getSomeShit());
        passport_date.setText(user.getSomeShit());
        */
    }

    /**
     * Обработка нажатия "назад"
     */
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.home:
                finish();
                return true;

            case R.id.action_refresh:
                writeUserInfo();
                return true;

            case R.id.action_logout:
                user.logout(this, new InternetCallback<String>() {
                    @Override
                    public void call(Result<String> result) {
                        // ToDo: сделать действия по завершению текущего сеанса.
                        if (result.isSuccessful()) {
                            setResult(RESULT_OK, new Intent("ok"));
                            finish();
                        } else {

                        }
                    }
                });
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}