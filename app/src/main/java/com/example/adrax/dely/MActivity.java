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
import android.widget.Toast;

import com.example.adrax.dely.core.InternetCallback;
import com.example.adrax.dely.core.LogHelper;
import com.example.adrax.dely.core.Order;
import com.example.adrax.dely.core.OrderList;
import com.example.adrax.dely.core.OrderStatus;
import com.example.adrax.dely.fragments.FragmentAbout;
import com.example.adrax.dely.fragments.FragmentDeliveriesShow;
import com.example.adrax.dely.fragments.FragmentFace;
import com.example.adrax.dely.fragments.FragmentGet;
import com.example.adrax.dely.fragments.FragmentOrder;
import com.example.adrax.dely.fragments.FragmentOrderShow;

import static com.example.adrax.dely.LoginActivity.user;

public class MActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentFace fface;         // Страница ккаунта
    FragmentOrder forder;       // Страница заказа
    FragmentGet fget;           // Заказы
    // FragmentTools ftools;       // Настройки
    FragmentAbout fabout;       // О нас
    FragmentDeliveriesShow fdelyshow; // Показ выбранной доставки
    FragmentOrderShow fordershow; // Показ выбранного заказа

    // Fragments keeper
    Fragment mFragmentToSet = null;

    boolean addToStack = false;

    boolean is_update = false;
    static UpdateTimer updateTimer;

    //static FragmentTransaction fTrans;
    public static int fragment_id=-1;

    public static OrderList orders = new OrderList();
    public static OrderList face_orders = new OrderList();
    public static Order face_delivery = null;

    public static final Object faceOrdersLock = new Object();
    public static final Object faceDeliveryLock = new Object();

    public static Integer selected_id = 0;
    public static String delyDescription = "Тест";
    public static String face_cur_order_text = "Тест";
    public static String face_deliver_text = "На данный момент нет активных заказов"; // Текст окна заказчика в щачле

    Context this_context;

    ActionBar actBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_view);

        // ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        actBar = getSupportActionBar();

        // Fragments Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
        fdelyshow = new FragmentDeliveriesShow();
        fordershow = new FragmentOrderShow();

        // загружаем заказы/доставки
        ordersUpdate();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fface);
        transaction.commit();

        // Timer
        //5min, 1min / update every 1min
        //updateTimer = new UpdateTimer(300000, 15000, this);
        //updateTimer.disable(); // Turn off for now
        this_context = getApplicationContext();
    }
    //загрузка заказов/доставок
    public static void ordersUpdate()
    {
        user.syncOrders(new InternetCallback<OrderList>() {
            @Override
            public void call(OrderList orders) {
                synchronized (faceOrdersLock) {
                    face_orders = orders;
                }
            }
        });

        if (orders != null) {
            user.currentOrder(new InternetCallback<OrderList>() {
                @Override
                public void call(OrderList orders) {
                    synchronized (faceDeliveryLock) {
                        face_delivery = orders.firstOrDefault(); // ToDo: заказов может быть > 1.
                    }
                }
            });
        }

        // face_orders = user.currentOrders();
        // face_delivery = user.currentDelivery();
    }

    /**
     * Запрос обновлений с сервера
     * @return
     */
    public boolean updateData()
    {
        //#ServerRequest
        //if (курьер появился)
        //updateTime.disable();
        return false;
    }

    public boolean updateUserData()
    {
        //#ServerRequest

        return true;
    }

    /**
     * Запускает процесс ожидания начала доставки
     */
    public static void waitingForCourier()
    {
    }

    public static void stopWaiting()
    {
    }

    /**
     * Обновляет face
     */
    public static void updateFace(){
        if (face_delivery != null) {
            face_delivery.status(new InternetCallback<OrderStatus>() {
                @Override
                public void call(OrderStatus orderStatus) {
                    if (orderStatus != OrderStatus.DELIVERED &&
                            orderStatus != OrderStatus.DELIVERY_DONE) {
                        face_deliver_text = face_delivery.toString();
                    } else {
                        face_deliver_text = "На данный момент нет активных заказов";
                    }
                }
            });
        }
    }

    /**
     * Обработка нажатия "назад"
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {                                                 // Close NavBar if it opened
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {                                 // Clean stack of fragments
            getSupportFragmentManager().popBackStack();
        } else if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);   // Open NavBar if it closed
        }
        // super.onBackPressed();
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
                } else if (fragment_id == R.id.frag_face_id) {
                    updateFace();
                } else {
                    LogHelper.warn("При обновлении не были загружены данные с сервера.");
                }
                Toast.makeText(
                        getApplicationContext(),
                        "Информация обновлена!",
                        Toast.LENGTH_LONG)
                        .show();
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
    public void showFragmentGet(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fget);
        transaction.addToBackStack(null);
        transaction.commit();

        //fragment_id = 1050;
        //updateData(); //#updateData
    }

    /**
     * Для вызова фрагмента доставки из других фрагментов
     */
    public void deliveryShowFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fdelyshow);
        transaction.addToBackStack(null);
        transaction.commit();

        //fragment_id = 1050;
        updateData(); //#updateData
    }

    /**
     * Для вызова фрагмента заказа из других фрагментов
     */
    public void orderShowFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fordershow);
        transaction.addToBackStack(null);
        transaction.commit();

        //fragment_id = 5010;
        updateData(); //#updateData
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
            updateData(); //#updateData
        } else if (id == R.id.frag_tools_id) {
            //mFragmentToSet = ftools;
            Intent i = new Intent(MActivity.this, ProfileActivity.class);
            startActivity(i);
        } else if (id == R.id.frag_about_id) {
            mFragmentToSet = fabout;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}