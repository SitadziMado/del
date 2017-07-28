package com.example.adrax.dely.core;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User {
    private User() {

    }

    public static User fromString(String jsonString) {
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

    public static void register(
            String username,
            String password,
            String mail,
            final InternetCallback<Boolean> callback) {
        register(
                username,
                password,
                mail,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public static void register(
            String username,
            String password,
            String mail,
            String name,
            String surname,
            String middleName,
            String number,
            String about,
            final InternetCallback<Boolean> callback) {
        InternetTask task = new InternetTask(REGISTER_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                Boolean result = Boolean.FALSE;

                switch (requestStatusFromString(s)) {
                    case LOGIN_REGISTERED:
                        result = Boolean.TRUE;
                        break;

                    case LOGIN_ALREADY_TAKEN:
                        LogHelper.error("Логин занят.");
                        // result = Boolean.FALSE;
                        break;

                    default:
                        LogHelper.error("Неизвестный код возврата при регистрации.");
                        // result = Boolean.FALSE;
                        break;
                }

                callback.call(result);
            }
        });

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
            String username,
            String password,
            final InternetCallback<User> callback) {
        InternetTask task = new InternetTask(LOGIN_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                User user = null;
                switch (requestStatusFromString(s)) {
                    case INCORRECT_AUTHORIZATION_DATA:
                        LogHelper.error("Некорректные данные авторизации.");
                        break;

                    case SERVER_PROBLEMS:
                        LogHelper.error("Проблемы с сервером.");
                        break;

                    case REQUEST_INCORRECT:
                        LogHelper.error("Некорректный запрос.");
                        break;

                    case OTHER:
                        user = User.fromString(s);
                        break;
                }

                callback.call(user);
            }
        });

        task.execute(
                USERNAME, username,
                PASSWORD, password
        );
    }

    public void logout(final InternetCallback<Boolean> callback) {
        InternetTask task = new InternetTask(LOGOUT_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                callback.call(Boolean.TRUE);
            }
        });

        task.execute();
    }

    public void syncOrders(final InternetCallback<OrderList> callback) {
        final User user = this;
        InternetTask task = new InternetTask(SYNC_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                OrderList orders = null;
                if (!s.equals("404")) {
                    orders = Order.fromString(s, user);
                    if (orders == null) {
                        orders = new OrderList();
                    }
                } else {
                    LogHelper.error("При синхронизации заказов произошла ошибка.");
                }

                callback.call(orders);
            }
        });

        task.execute(REFRESH, DELIVERIES);
    }

    public void currentOrder(final InternetCallback<OrderList> callback) {
        final User user = this;
        InternetTask task = new InternetTask(CURRENT_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                OrderList orders = null;
                if (!s.equals(ERROR)) {
                    orders = Order.fromString(s, user);
                    if (orders == null) {
                        orders = new OrderList();
                    }
                } else {
                    LogHelper.error("При запросе текущего заказа произошла ошибка.");
                }

                callback.call(orders);
            }
        });

        task.execute(HASH.toLowerCase(), m_hash);
    }

    static RequestStatus requestStatusFromString(String status) {
        if (status == null) {
            status = "";
        }

        switch (status.toLowerCase())
        {
            case INCORRECT_AUTHORIZATION_DATA:
                return RequestStatus.INCORRECT_AUTHORIZATION_DATA;
            case SERVER_PROBLEMS:
                return RequestStatus.SERVER_PROBLEMS;
            case INCORRECT_REQUEST:
                return RequestStatus.REQUEST_INCORRECT;
            case LOGIN_REGISTERED:
                return RequestStatus.LOGIN_REGISTERED;
            case LOGIN_ALREADY_TAKEN:
                return RequestStatus.LOGIN_ALREADY_TAKEN;
            case LOADED:
                return RequestStatus.ORDER_LOADED;
            case ERROR:
                return RequestStatus.ORDER_ERROR;
            case STARTED:
                return RequestStatus.ORDER_STARTED;
            case BUSY:
                return RequestStatus.ORDER_BUSY;
            case TOO_MANY:
                return RequestStatus.ORDER_TOO_MANY;
            case OK:
                return RequestStatus.ORDER_OK;
            default:
                LogHelper.error("Неизвестный код возврата с сервера.");
                return RequestStatus.OTHER;
        }
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
    private static final String CURRENT_URL = "http://adrax.pythonanywhere.com/current_delivery";

    /// Константы авторизации
    static final String INCORRECT_AUTHORIZATION_DATA = "incorrect_auth";    /** Некорректная инфа для авторизации */
    static final String INCORRECT_REQUEST = "405";                          /** Тоже какая-то ошибка */
    static final String SERVER_PROBLEMS = "login_error";                    /** Сервак гуфнулся... */

    /// Константы регистрации
    private static final String LOGIN_REGISTERED = "registered";            /** Регистрация прошла успешно */
    private static final String LOGIN_ALREADY_TAKEN = "already_taken";      /** Логин занят */

    /// Константы заказов
    static final String LOADED = "loaded";		          /** Новый заказ успешно создан */
    static final String ERROR = "error";                  /** Ошибка отправки нового заказа на сервер */
    static final String WAITING = "waiting";              /** Статусы заказов ниже */
    static final String DELIVERING = "delivering";        /** В процессе... */
    static final String DELIVERED = "delivered";          /** Вроде бы доставлено */
    static final String DELIVERY_DONE = "delivery_done";  /** Абсолютный суккесс */
    static final String STARTED = "delivery_started";     /** Всё хорошо, доставка началась */
    static final String BUSY = "delivery_busy";           /** Пользователь - слоупок, рекомендуем обновиться до 10-ки */
    static final String OK = "ok";                        /** Заказ успешно подтверждён (обеими сторонами) */
    static final String TOO_MANY = "already_enough";      /** Только 1 заказ можно доставлять */

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
