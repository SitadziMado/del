package com.example.adrax.dely;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
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

        WriteUserInfo();
    }

    void WriteUserInfo()
    {
        lvName.setText(user.getSurname() +" "+user.getName() + " " + user.getMiddleName());
        lvMail.setText(user.getMail());
        lvMoney.setText(user.getMoney() + " руб.");
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

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
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

        if (id == R.id.home) {
            //Intent intent = new Intent(ProfileActivity.this, MActivity.class);
            //startActivity(intent);
            finish();
            super.onBackPressed();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            WriteUserInfo();
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}