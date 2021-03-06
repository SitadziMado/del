package com.example.adrax.dely;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.adrax.dely.core.InternetCallback;
import com.example.adrax.dely.core.LogHelper;
import com.example.adrax.dely.core.Order;
import com.example.adrax.dely.core.OrderList;
import com.example.adrax.dely.core.OrderStatus;
import com.example.adrax.dely.core.Result;
import com.example.adrax.dely.fragments.FragmentAbout;
import com.example.adrax.dely.fragments.FragmentFace;
import com.example.adrax.dely.fragments.FragmentGet;
import com.example.adrax.dely.fragments.FragmentOrder;

import butterknife.Bind;

import static com.example.adrax.dely.LoginActivity.user;


public class MActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    FragmentFace fface;         // Страница аккаунта
    FragmentOrder forder;       // Страница заказа
    FragmentGet fget;           // Заказы
    FragmentAbout fabout;       // О нас

    // Fragments keeper
    Fragment mFragmentToSet = null;

    boolean addToStack = false;

    //static FragmentTransaction fTrans;
    public static int fragment_id = -1;

    public static OrderList orders = new OrderList();
    public static OrderList sorted_orders = new OrderList();
    public static OrderList face_orders = new OrderList();
    public static Order face_delivery = null;

    private static final Object ordersLock = new Object();
    private static final Object faceDeliveryLock = new Object();
    private static final Object faceOrdersLock = new Object();

    private static String noOrdersAvailable;
    public static String face_deliver_text; // Текст окна заказчика в щачле

    Context this_context;

    ActionBar actBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        noOrdersAvailable = getString(R.string.no_orders_available);
        face_deliver_text = noOrdersAvailable;

        setContentView(R.layout.activity_navigation_view);

        // ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        actBar = getSupportActionBar();

        // Fragments Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        drawer.addDrawerListener(toggle);

        // Listener which helps to close Fragment correctly, in time
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }
            @Override
            public void onDrawerOpened(View drawerView) {
            }
            @Override
            public void onDrawerStateChanged(int newState) {
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                //Set your new fragment here
                if (mFragmentToSet != null) {
                    if (addToStack)
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, mFragmentToSet)
                                .addToBackStack(null)
                                .commit();
                    else
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, mFragmentToSet)
                                .commit();
                    mFragmentToSet = null;
                    addToStack = false;
                }
            }
        });
        toggle.syncState();

        // Navigation slide-panel
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fface = new FragmentFace();
        forder = new FragmentOrder();
        fget = new FragmentGet();
        fabout = new FragmentAbout();

        // Уведомление
        //NotificationHelper.createNotification(this, "Del", "Заказ создан.");

        // загружаем заказы/доставки
        updateOrders();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fface);
        transaction.commit();

        this_context = getApplicationContext();

        // Фрагмент сначала: face.
        fragment_id = R.id.frag_face_id;
    }

    // загрузка заказов/доставок
    public static void updateOrders() {
        user.syncOrders(new InternetCallback<OrderList>() {
            @Override
            public void call(Result<OrderList> result) {
                synchronized (ordersLock) {
                    orders = result.getData();
                    sorted_orders = orders.clone();
                }
            }
        });
    }

    public void updateUserData() {
        user.syncInfo(new InternetCallback<String>() {
            @Override
            public void call(Result<String> result) {
                // Сделай что-то с результатом.
                /* if (result.isSuccessful()) {
                    // Действия при успехе.
                } else {
                    // Действия при неудаче.
                } */
            }
        });
    }

    /**
     * Обновляет face
     */
    public static void updateFace() {
        user.currentOrders(new InternetCallback<OrderList>() {
            @Override
            public void call(Result<OrderList> result) {
                synchronized (faceOrdersLock) {
                    face_orders = result.getData();
                }
            }
        });

        user.currentDelivery(new InternetCallback<OrderList>() {
            @Override
            public void call(Result<OrderList> result) {
                synchronized (faceDeliveryLock) {
                    // ToDo: доставок может быть > 1.
                    face_delivery = result.getData().firstOrDefault();

                    if (face_delivery != null) {
                        face_delivery.status(new InternetCallback<OrderStatus>() {
                            @Override
                            public void call(Result<OrderStatus> result) {
                                if (result.getData() != OrderStatus.DELIVERED &&
                                        result.getData() != OrderStatus.DELIVERY_DONE) {
                                    face_deliver_text = face_delivery.toString();
                                } else {
                                    face_deliver_text = noOrdersAvailable;
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * Обработка нажатия "назад"
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {   // Close NavBar if it opened
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) // Clean stack of fragments
            getSupportFragmentManager().popBackStack();
        else finish();
    }

    /**
     * Определяет свойтва и праметры меню-бара
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.m, menu);

        MenuItem refreshItem = menu.findItem(R.id.action_refresh);
        // SearchView searchView =
        //        (SearchView) MenuItemCompat.getActionView(searchItem);

        return true;
    }

    /**
     * Описывает действия при клике по меню-бару
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        switch (item.getItemId()) {
            //case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
            //    return true;

            case R.id.action_refresh:
                if (fragment_id == R.id.frag_get_id) {
                    fget.update(this);
                    updateOrders();
                } else if (fragment_id == R.id.frag_face_id) {
                    updateFace();
                } else {
                    LogHelper.warn("При обновлении не были загружены данные с сервера.");
                }

                return true;
            case android.R.id.home: // we get back here


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Для вызова фрагмента списка доставок из других фрагментов
     */
    public void showFragmentGet() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fget);
        transaction.addToBackStack(null);
        transaction.commit();

        //fragment_id = 1050;
        //updateData();
    }

    /**
     * Клик по пункту на шторке
     * @param item
     * @return
     */
    // @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here
        int id = item.getItemId();
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // ActionBar bar = getActionBar()

        // Clean stack
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }

        //actBar.setDisplayHomeAsUpEnabled(false); // hide '<-' from action bar
        fragment_id = id;

        if (id == R.id.frag_face_id) {
            mFragmentToSet = fface;
            // Handle the camera action
        } else if (id == R.id.frag_order_id) {
            //bar.hide();
            mFragmentToSet = forder;
        } else if (id == R.id.frag_get_id) {
            mFragmentToSet = fget;
            //updateData();
        } else if (id == R.id.frag_tools_id) {
            //mFragmentToSet = ftools;
            Intent i = new Intent(MActivity.this, ProfileActivity.class);
            startActivityForResult(i, 1);
        } else if (id == R.id.frag_about_id) {
            mFragmentToSet = fabout;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            finish();
            Intent myIntent;
            myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);
        }
    }
}