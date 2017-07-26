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
            user.m_about = userData.getString("About");
            user.m_hash = userData.getString("Hash");
            user.m_mail = userData.getString("Mail");
            user.m_middleName = userData.getString("Midname");
            user.m_money = String.valueOf(userData.getDouble("Money"));
            user.m_name = userData.getString("Name");
            user.m_phone = userData.getString("Selnum");
            user.m_surname = userData.getString("Surname");
        } catch (JSONException ex) {
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

                switch (requestStatusFromString(s.toLowerCase())) {
                    case LOGIN_REGISTERED:
                        result = Boolean.TRUE;
                        break;

                    case LOGIN_ALREADY_TAKEN:
                        // result = Boolean.FALSE;
                        break;

                    default:
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
                switch (requestStatusFromString(s.toLowerCase())) {
                    case INCORRECT_AUTHORIZATION_DATA:
                        break;

                    case SERVER_PROBLEMS:
                        break;

                    case REQUEST_INCORRECT:
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

    public void order(Order order, final InternetCallback<Boolean> callback) {
        InternetTask task = new InternetTask(ORDER_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                Boolean result = Boolean.FALSE;

                switch (requestStatusFromString(s.toLowerCase())) {
                    case ORDER_ERROR:
                        break;

                    case ORDER_LOADED:
                        result = Boolean.TRUE;
                        break;
                }

                callback.call(result);
            }
        });

        task.execute(
                Order.CUSTOMER, order.getField(Order.CUSTOMER),
                Order.FROM, order.getField(Order.FROM),
                Order.TO, order.getField(Order.TO),
                Order.COST, order.getField(Order.COST),
                Order.PAYMENT, order.getField(Order.PAYMENT),
                Order.ENTRANCE, order.getField(Order.ENTRANCE),
                Order.CODE, order.getField(Order.CODE),
                Order.FLOOR, order.getField(Order.FLOOR),
                Order.ROOM, order.getField(Order.ROOM),
                Order.PHONE, order.getField(Order.PHONE),
                Order.WEIGHT, order.getField(Order.WEIGHT),
                Order.SIZE, order.getField(Order.SIZE),
                HASH, m_hash,
                Order.DESCRIPTION, order.getField(Order.DESCRIPTION)
        );
    }

    public void syncOrders(final InternetCallback<Order[]> callback) {
        InternetTask task = new InternetTask(SYNC_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                // Order[] orders = null;
                Order[] orders = new Order[0];
                if (!s.equals("404")) {
                    orders = Order.fromString(s);
                }

                callback.call(orders);
            }
        });

        task.execute(REFRESH, DELIVERIES);
    }

    public void currentOrder(final InternetCallback<Order> callback) {
        InternetTask task = new InternetTask(CURRENT_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                Order order = null;
                if (!s.equals(ERROR)) {
                    order = Order.fromString(s)[0];
                }

                callback.call(order);
            }
        });

        task.execute(HASH, m_hash);
    }

    public void cancel(final InternetCallback<Boolean> callback) {
        InternetTask task = new InternetTask(CANCEL_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                callback.call(Boolean.FALSE);
            }
        });

        task.execute();
    }

    public void start(Order order, final InternetCallback<Boolean> callback) {
        InternetTask task = new InternetTask(START_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                Boolean result = Boolean.FALSE;
                switch (requestStatusFromString(s.toLowerCase())) {
                    case ORDER_BUSY:
                        break;

                    case ORDER_TOO_MANY:
                        break;

                    case ORDER_STARTED:
                        result = Boolean.TRUE;
                        break;
                }

                callback.call(result);
            }
        });

        if (order == null) {
            throw new NullPointerException();
        }

        task.execute(
                HASH, m_hash,
                ID, order.getField("id"),
                COURIER, getLogin()
        );
    }

    public void finish(Order order, final InternetCallback<Boolean> callback) {
        InternetTask task = new InternetTask(FINISH_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                Boolean result = Boolean.FALSE;
                switch (requestStatusFromString(s.toLowerCase())) {
                    case ORDER_BUSY:
                        break;

                    case ORDER_STARTED:
                        result = Boolean.TRUE;
                        break;
                }

                callback.call(result);
            }
        });

        if (order == null) {
            throw new NullPointerException();
        }

        task.execute(
                HASH, m_hash,
                ID, order.getField(Order.ID),
                SMS_CODE, "0000"
        );
    }

    public void accept(Order order, final InternetCallback<Boolean> callback) {
        InternetTask task = new InternetTask(ACCEPT_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                Boolean result = Boolean.FALSE;
                switch (requestStatusFromString(s.toLowerCase())) {
                    case ORDER_ERROR:
                        break;

                    case ORDER_OK:
                        result = Boolean.TRUE;
                        break;
                }

                callback.call(result);
            }
        });

        if (order == null) {
            throw new NullPointerException();
        }

        task.execute(
                HASH, m_hash,
                ID, order.getField(Order.ID)
        );
    }

    public void getOrderStatus(Order order, final InternetCallback<OrderStatus> callback) {
        InternetTask task = new InternetTask(STATUS_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                OrderStatus result;
                switch (s.toLowerCase()) {
                    case WAITING:
                        result = OrderStatus.WAITING;
                        break;

                    case DELIVERING:
                        result = OrderStatus.DELIVERING;
                        break;

                    case DELIVERED:
                        result = OrderStatus.DELIVERED;
                        break;

                    case DELIVERY_DONE:
                        result = OrderStatus.DELIVERY_DONE;
                        break;

                    case ERROR:
                        result = OrderStatus.ERROR;
                        break;

                    default:
                        result = OrderStatus.ERROR;
                        break;
                }

                callback.call(result);
            }
        });

        if (order == null) {
            throw new NullPointerException();
        }

        task.execute(
                HASH, m_hash,
                ID, order.getField(Order.ID)
        );
    }

    private static RequestStatus requestStatusFromString(String status) {
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
                return RequestStatus.OTHER;
        }
    }

    private static final String REGISTER_URL = "http://adrax.pythonanywhere.com/register";
    private static final String LOGIN_URL = "http://adrax.pythonanywhere.com/login";
    private static final String LOGOUT_URL = "http://adrax.pythonanywhere.com/login";
    private static final String ORDER_URL = "http://adrax.pythonanywhere.com/load_delys";
    private static final String SYNC_URL = "http://adrax.pythonanywhere.com/send_delys";
    private static final String PEEK_URL = null; // "http://adrax.pythonanywhere.com/send_delys";
    private static final String CURRENT_URL = "http://adrax.pythonanywhere.com/current_delivery";

    private static final String CANCEL_URL = null; // "http://adrax.pythonanywhere.com/send_delys";
    private static final String START_URL = "http://adrax.pythonanywhere.com/ch_dely";
    private static final String FINISH_URL = "http://adrax.pythonanywhere.com/delivered";
    private static final String ACCEPT_URL = "http://adrax.pythonanywhere.com/delivery_done";
    private static final String STATUS_URL = "http://adrax.pythonanywhere.com/chosen";

    /// Константы авторизации
    private static final String INCORRECT_AUTHORIZATION_DATA = "incorrect_auth";    /** Некорректная инфа для авторизации */
    private static final String INCORRECT_REQUEST = "405";                          /** Тоже какая-то ошибка */
    private static final String SERVER_PROBLEMS = "login_error";                    /** Сервак гуфнулся... */

    /// Константы регистрации
    private static final String LOGIN_REGISTERED = "registered";            /** Регистрация прошла успешно */
    private static final String LOGIN_ALREADY_TAKEN = "already_taken";      /** Логин занят */

    /// Константы заказов
    private static final String LOADED = "loaded";		          /** Новый заказ успешно создан */
    private static final String ERROR = "error";                  /** Ошибка отправки нового заказа на сервер */
    private static final String WAITING = "waiting";              /** Статусы заказов ниже */
    private static final String DELIVERING = "delivering";        /** В процессе... */
    private static final String DELIVERED = "delivered";          /** Вроде бы доставлено */
    private static final String DELIVERY_DONE = "delivery_done";  /** Абсолютный суккесс */
    private static final String STARTED = "delivery_started";     /** Всё хорошо, доставка началась */
    private static final String BUSY = "delivery_busy";           /** Пользователь - слоупок, рекомендуем обновиться до 10-ки */
    private static final String OK = "ok";                        /** Заказ успешно подтверждён (обеими сторонами) */
    private static final String TOO_MANY = "already_enough";      /** Только 1 заказ можно доставлять */

    private static final String HASH = "hash";
    private static final String ID = "dely_id";
    private static final String SMS_CODE = "sms_code";
    private static final String COURIER = "couriers";
    private static final String REFRESH = "refr";
    private static final String DELIVERIES = "delys";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String MAIL = "mail";
    private static final String NAME = "name";
    private static final String SURNAME = "surname;";
    private static final String MIDDLE_NAME = "midname";
    private static final String PHONE = "selnum";
    private static final String ABOUT = "about";

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
}
