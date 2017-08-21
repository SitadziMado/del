package com.example.adrax.dely.core;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class User {
    private User() {

    }

    public static void register(
            @NonNull String username,
            @NonNull String password,
            @NonNull String mail,
            @NonNull final InternetCallback<String> callback
    ) {
        register(
                username,
                password,
                mail,
                "",
                "",
                "",
                "",
                "",
                callback
        );
    }

    public static void register(
            @NonNull String username,
            @NonNull String password,
            @NonNull String mail,
            @NonNull String name,
            @NonNull String surname,
            @NonNull String middleName,
            @NonNull String number,
            @NonNull String about,
            @NonNull final InternetCallback<String> callback
    ) {
        InternetTask task = new InternetTask(InternetTask.METHOD_POST, REGISTER_URL, callback);

        task.execute(
                USERNAME, username,
                PASSWORD, password,
                MAIL, mail,
                NAME, name,
                SURNAME, surname,
                MIDDLE_NAME, middleName,
                PHONE, number,
                ABOUT, about
        );
    }

    public static void login(
            @NonNull final Activity context,
            @NonNull String username,
            @NonNull String password,
            @NonNull final InternetCallback<User> callback
    ) {
        InternetTask task = new InternetTask(InternetTask.METHOD_POST, LOGIN_URL, new InternetCallback<String>() {
            @Override
            public void call(Result<String> s) {
                User user;

                if (s.isSuccessful()) {
                    user = fromString(s.getData());

                    // Пишем хэш.
                    storeHash(context, user.getHash());
                } else {
                    user = null;
                }

                // Оставляем статус и сообщение, меняем инфу на пользователя.
                callback.call(new Result<>(s, user));
            }
        });

        task.execute(
                USERNAME, username,
                PASSWORD, password
        );
    }

    public static void restoreLastSession(
            @NonNull final Activity context,
            @NonNull final InternetCallback<User> callback
    ) {
        InternetTask task = new InternetTask(InternetTask.METHOD_POST, RESTORE_URL, new InternetCallback<String>() {
            @Override
            public void call(Result<String> s) {
                User user;

                if (s.isSuccessful()) {
                    user = fromString(s.getData());
                } else {
                    user = null;
                }

                callback.call(new Result<>(s, user));
            }
        });

        String restoredHash = restoreHash(context);

        if (restoredHash.equals("none")) {
            callback.call(new Result<User>(Boolean.FALSE, null));
        } else {
            task.execute(HASH, restoreHash(context));
        }
    }

    private static User fromString(String jsonString) {
        User user = new User();
        try {
            JSONObject userData = new JSONObject(jsonString);
            user.setAbout(userData.getString(ABOUT));
            user.m_hash = userData.getString(HASH);
            user.setMail(userData.getString(MAIL));
            user.setMiddleName(userData.getString(MIDDLE_NAME));
            user.setMoney(String.valueOf(userData.getDouble(MONEY)));
            user.setName(userData.getString(NAME));
            user.setPhone(userData.getString(PHONE));
            user.setSurname(userData.getString(SURNAME));
        } catch (JSONException ex) {
            LogHelper.error("Ошибка при чтении JSON.");
            user = null;
        }

        return user;
    }

    @NonNull
    private static String restoreHash(@NonNull Activity context) {
        String restoredHash = "none";

        try {
            char[] buf = new char[256];
            InputStream inputStream = context.openFileInput("hash.txt");
            InputStreamReader isr = new InputStreamReader(inputStream);
            int bytesRead = isr.read(buf);

            if (bytesRead > 0) {
                restoredHash = String.copyValueOf(buf, 0, bytesRead);
            }

            isr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return restoredHash;
    }

    private static void storeHash(
            @NonNull Activity context,
            @NonNull String hash
    ) {
        try {
            OutputStream outputStream = context.openFileOutput("hash.txt", 0);
            OutputStreamWriter osw = new OutputStreamWriter(outputStream);
            osw.write(hash);
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logout(
            @NonNull final Activity context,
            @NonNull final InternetCallback<String> callback
    ) {
        InternetTask task = new InternetTask(InternetTask.METHOD_POST, LOGOUT_URL, callback);

        task.execute(HASH, m_hash);
    }

    public void syncOrders(@NonNull final InternetCallback<OrderList> callback) {
        final User user = this;
        InternetTask task = new InternetTask(InternetTask.METHOD_POST, SYNC_URL, new InternetCallback<String>() {
            @Override
            public void call(Result<String> s) {
                OrderList orders;

                if (s.isSuccessful()) {
                    orders = Order.fromString(s.getData(), user);
                } else {
                    orders = new OrderList();
                }

                callback.call(new Result<>(s, orders));
            }
        });

        task.execute(REFRESH, DELIVERIES);
    }

    public void currentDelivery(@NonNull final InternetCallback<OrderList> callback) {
        final User user = this;
        InternetTask task = new InternetTask(InternetTask.METHOD_POST, CURRENT_DELIVERY_URL, new InternetCallback<String>() {
            @Override
            public void call(Result<String> s) {
                OrderList orders;

                if (s.isSuccessful()) {
                    orders = Order.fromString(s.getData(), user);
                } else {
                    orders = new OrderList();
                }

                callback.call(new Result<>(s, orders));
            }
        });

        task.execute(HASH, m_hash);
    }

    public void currentOrders(@NonNull final InternetCallback<OrderList> callback) {
        final User user = this;
        InternetTask task = new InternetTask(InternetTask.METHOD_POST, CURRENT_ORDERS_URL, new InternetCallback<String>() {
            @Override
            public void call(Result<String> s) {
                OrderList orders;

                if (s.isSuccessful()) {
                    orders = Order.fromString(s.getData(), user);
                } else {
                    orders = new OrderList();
                }

                callback.call(new Result<>(s, orders));
            }
        });

        task.execute(HASH, m_hash);
    }

    public void addPassport(
            @NonNull String serial,
            @NonNull String number,
            @NonNull String given,
            @NonNull String date,
            Boolean isMale,
            @NonNull final InternetCallback<String> callback
    ) {
        InternetTask task = new InternetTask(InternetTask.METHOD_POST, ADD_PASSPORT_URL, new InternetCallback<String>() {
            @Override
            public void call(Result<String> s) {
                if (s.isSuccessful()) {
                    s.setMessage("Паспорт загружен!");
                } else {
                    String msg = "При загрузке паспорта произошла ошибка.";
                    s.setMessage(msg);
                    LogHelper.error(msg);
                }

                callback.call(s);
            }
        });

        task.execute(
                HASH, m_hash,
                PASSPORT_SERIAL, serial,
                PASSPORT_NUMBER, number,
                PASSPORT_GIVEN, given,
                PASSPORT_DATE, date,
                PASSPORT_GENDER, isMale ? "male" : "female"
        );
    }

    public void addCreditCard(
            @NonNull String cardNumber,
            @NonNull String cardValid,
            @NonNull String cardOwner,
            @NonNull final InternetCallback<String> callback) {
        InternetTask task = new InternetTask(InternetTask.METHOD_POST, ADD_CARD_URL, new InternetCallback<String>() {
            @Override
            public void call(Result<String> s) {
                if (s.isSuccessful()) {
                    s.setMessage("Данные кредитной карточки загружены!");
                } else {
                    String msg = "При загрузке данных кредитной карточки произошла ошибка.";
                    s.setMessage(msg);
                    LogHelper.error(msg);
                }

                callback.call(s);
            }
        });

        task.execute(
                HASH, m_hash,
                CARD_NUMBER, cardNumber,
                CARD_VALID, cardValid,
                CARD_OWNER, cardOwner
        );
    }

    public void syncInfo(@Nullable final InternetCallback<String> callback) {
        final User me = this;
        InternetTask task = new InternetTask(InternetTask.METHOD_POST, USER_INFO_URL, new InternetCallback<String>() {
            @Override
            public void call(Result<String> s) {
                if (s.isSuccessful()) {
                    User temp = fromString(s.getData());

                    me.setAbout(temp.getAbout());
                    me.setMail(temp.getMail());
                    me.setMiddleName(temp.getMiddleName());
                    me.setMoney(temp.getMoney());
                    me.setName(temp.getName());
                    me.setPhone(temp.getPhone());
                    me.setSurname(temp.getSurname());
                } else {
                    s.setMessage("При синхронизации информации произошла ошибка.");
                }

                if (callback != null) {
                    callback.call(s);
                }
            }
        });

        task.execute(HASH, m_hash);
    }

    public String getAbout() {
        return m_about;
    }

    /** Мыло пользователя */
    public String getMail() {
        return m_mail;
    }

    /** Его отчество */
    public String getMiddleName() {
        return m_middleName;
    }

    /** Его капуста (не используется) */
    public String getMoney() {
        return m_money;
    }

    /** Его имя */
    public String getName() {
        return m_name;
    }

    /** Телефонный номер */
    public String getPhone() {
        return m_phone;
    }

    /** Фамилия */
    public String getSurname() {
        return m_surname;
    }

    /** Текущий логин */
    public String getLogin() {
        return m_login;
    }

    /** Выданный хэш */
    String getHash() {
        return m_hash;
    }

    public void setAbout(String about) {
        m_about = about;
    }

    public void setMail(String mail) {
        m_mail = mail;
    }

    public void setMiddleName(String middleName) {
        m_middleName = middleName;
    }

    public void setMoney(String money) {
        m_money = money;
    }

    public void setName(String name) {
        m_name = name;
    }

    public void setPhone(String phone) {
        m_phone = phone;
    }

    public void setSurname(String surname) {
        m_surname = surname;
    }

    public void setLogin(String login) {
        m_login = login;
    }

    private static final String REGISTER_URL = "http://adrax.pythonanywhere.com/register";
    private static final String LOGIN_URL = "http://adrax.pythonanywhere.com/login";
    private static final String LOGOUT_URL = "http://adrax.pythonanywhere.com/logout";
    private static final String SYNC_URL = "http://adrax.pythonanywhere.com/send_delys";
    private static final String PEEK_URL = null; // "http://adrax.pythonanywhere.com/send_delys";
    private static final String CURRENT_DELIVERY_URL = "http://adrax.pythonanywhere.com/current_delivery";
    private static final String CURRENT_ORDERS_URL = "http://adrax.pythonanywhere.com/current_orders";
    private static final String ADD_PASSPORT_URL = "http://adrax.pythonanywhere.com/add_passport";
    private static final String ADD_CARD_URL = "http://adrax.pythonanywhere.com/add_card";
    private static final String USER_INFO_URL = "http://adrax.pythonanywhere.com/user_info";
    private static final String RESTORE_URL = "http://adrax.pythonanywhere.com/fast_login";

    static final String ID = "dely_id";
    static final String SMS_CODE = "sms_code";
    static final String COURIER = "couriers";
    static final String REFRESH = "refr";
    static final String DELIVERIES = "delys";
    static final String HASH = "hash";
    static final String USERNAME = "username";
    static final String PASSWORD = "password";
    static final String MAIL = "mail";
    static final String NAME = "name";
    static final String SURNAME = "surname";
    static final String MIDDLE_NAME = "midname";
    static final String PHONE = "selnum";
    static final String ABOUT = "about";
    static final String MONEY = "money";

    private static final String PASSPORT_SERIAL = "passport_serial";
    private static final String PASSPORT_NUMBER = "passport_number";
    private static final String PASSPORT_GIVEN = "passport_given";
    private static final String PASSPORT_DATE = "passport_date";
    private static final String PASSPORT_GENDER = "passport_gender";
    private static final String CARD_NUMBER = "card_number";
    private static final String CARD_VALID = "card_valid";
    private static final String CARD_OWNER = "card_owner";

    private ArrayList<Order> m_orders = new ArrayList<>();
    private String m_about;         /** Информация о юзвере  */
    private String m_hash;          /** Выданный хэш */
    private String m_mail;          /** Мыло пользователя */
    private String m_middleName;    /** Его отчество */
    private String m_money;         /** Его капуста (не используется) */
    private String m_name;          /** Его имя */
    private String m_phone;         /** Телефонный номер */
    private String m_surname;       /** Фамилия */
    private String m_login;
}
