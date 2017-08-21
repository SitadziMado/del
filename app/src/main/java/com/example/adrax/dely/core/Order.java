package com.example.adrax.dely.core;

import android.support.annotation.NonNull;

import com.example.adrax.dely.R;

import java.util.ArrayList;

public class Order extends DynamicObject implements Comparable<Order> {
    public Order(@NonNull User parent, Object... params) {
        if ((params.length & 1) == 0) {
            for (int i = 0; i < params.length; i += 2) {
                String key = (String)params[i];
                Object value = params[i + 1];

                if (key == null || value == null || key.equals("")) { // || value.equals("")
                    String m = "Параметры не могут быть пустыми.";
                    LogHelper.error(m);
                    throw new IllegalArgumentException(m);
                }

                setProp(key, value);
            }

            // Установить значения хэша, статуса, родителя. Не трогать!
            setupSpecialProps(parent);
        } else {
            String m = "Требуется четное количество аргументов для составления пар.";
            LogHelper.error(m);
            throw new IllegalArgumentException(m);
        }
    }

    private Order(@NonNull DynamicObject obj) {
        m_props = obj.m_props;
    }

    private Order() {

    }

    @NonNull
    public static OrderList fromString(String jsonString, @NonNull User parent) {
        OrderList list = new OrderList();
        ArrayList<DynamicObject> objects = DynamicObject.fromString(jsonString);

        for (DynamicObject obj : objects) {
            Order order = new Order(obj);

            // Установить значения хэша, статуса, родителя. Не трогать!
            order.setupSpecialProps(parent);
            list.add(order);
        }

        return list;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        appendFieldValue(sb, App.getContext().getString(R.string.order_name), DESCRIPTION);
        appendFieldValue(sb, App.getContext().getString(R.string.order_from), FROM);
        appendFieldValue(sb, App.getContext().getString(R.string.order_to), TO);
        appendFieldValue(sb, App.getContext().getString(R.string.order_customer), CUSTOMER);
        appendFieldValue(sb, App.getContext().getString(R.string.order_phone), PHONE);
        appendFieldValue(sb, App.getContext().getString(R.string.payment), PAYMENT);

        String cost = getStringProp(COST);
        String weight = getStringProp(WEIGHT);
        String size = getStringProp(SIZE);
        String code = getStringProp(CODE);
        String entrance = getStringProp(ENTRANCE);
        String floor = getStringProp(FLOOR);

        if (cost != null && !cost.equals("")) {
            appendFieldValue(
                    sb,
                    App.getContext().getString(R.string.order_cost),
                    String.format(App.getContext().getString(R.string.currency_text), cost)
            );
        }
        if (weight != null && !weight.equals("")) {
            appendFieldValue(
                    sb,
                    App.getContext().getString(R.string.order_weight),
                    String.format(App.getContext().getString(R.string.grams), weight)
            );
        }
        if (size != null && !size.equals("")) {
            appendFieldValue(
                    sb,
                    App.getContext().getString(R.string.order_size),
                    size
            );
        }
        if (code != null && !code.equals("")) {
            appendFieldValue(
                    sb,
                    App.getContext().getString(R.string.order_code),
                    code
            );
        }
        if (entrance != null && !entrance.equals("")) {
            appendFieldValue(
                    sb,
                    App.getContext().getString(R.string.order_entrance),
                    entrance
            );
        }
        if (floor != null && !floor.equals("")) {
            appendFieldValue(
                    sb,
                    App.getContext().getString(R.string.order_floor),
                    floor
            );
        }

        return sb.toString();
    }

    public void post(@NonNull final InternetCallback<String> callback) {
        InternetTask task = new InternetTask(InternetTask.METHOD_POST, ORDER_URL, new InternetCallback<String>() {
            @Override
            public void call(Result<String> s) {
                if (s.isSuccessful()) {
                    s.setMessage(App.getContext().getString(R.string.core_order_posted));
                } else {
                    // ToDo: возможно, убрать
                    s.setMessage(App.getContext().getString(R.string.core_error_posting_an_order));
                }

                callback.call(s);
            }
        });

        task.execute(
                // Order.CUSTOMER, getStringProp(Order.CUSTOMER),
                Order.FROM, getStringProp(Order.FROM),
                Order.TO, getStringProp(Order.TO),
                Order.COST, getStringProp(Order.COST),
                Order.PAYMENT, getStringProp(Order.PAYMENT),
                Order.ENTRANCE, getStringProp(Order.ENTRANCE),
                Order.CODE, getStringProp(Order.CODE),
                Order.FLOOR, getStringProp(Order.FLOOR),
                Order.PHONE, getStringProp(Order.PHONE),
                Order.WEIGHT, getStringProp(Order.WEIGHT),
                Order.SIZE, getStringProp(Order.SIZE),
                Order.DESCRIPTION, getStringProp(Order.DESCRIPTION),
                Order.TAKE_TIME, getStringProp(Order.TAKE_TIME),
                Order.BRING_TIME, getStringProp(Order.BRING_TIME),
                Order.DISTANCE, getStringProp(Order.DISTANCE),
                HASH, ((User)getProp(PARENT)).getHash(),
                // Order.ROOM, getStringProp(Order.ROOM),
                "recnum", "undefined"
        );
    }

    public void cancel(@NonNull final InternetCallback<String> callback) {
        InternetTask task = new InternetTask(InternetTask.METHOD_POST, CANCEL_URL, new InternetCallback<String>() {
            @Override
            public void call(Result<String> s) {
                callback.call(s);
            }
        });

        task.execute();
    }

    public void start(@NonNull final InternetCallback<String> callback) {
        InternetTask task = new InternetTask(InternetTask.METHOD_POST, START_URL, new InternetCallback<String>() {
            @Override
            public void call(Result<String> s) {
                if (s.isSuccessful()) {
                    // s.setMessage(App.getContext().getString(R.string.core_order_started));
                } else {
                    // ToDo: возможно, убрать
                    s.setMessage(App.getContext().getString(R.string.core_error_starting_an_order));
                }

                callback.call(s);
            }
        });

        task.execute(
                HASH, getStringProp(HASH),
                User.ID, getStringProp(ID) /*,
                User.COURIER, ((User)getProp(PARENT)).getLogin()*/
        );
    }

    public void finish(
            @NonNull String smsCode,
            @NonNull final InternetCallback<String> callback
    ) {
        InternetTask task = new InternetTask(InternetTask.METHOD_POST, FINISH_URL, new InternetCallback<String>() {
            @Override
            public void call(Result<String> s) {
                if (s.isSuccessful()) {
                    s.setMessage(App.getContext().getString(R.string.core_order_finished));
                } else {
                    s.setMessage(App.getContext().getString(R.string.core_error_finishing_an_order));
                }

                callback.call(s);
            }
        });

        task.execute(
                HASH, getStringProp(HASH),
                User.ID, getStringProp(ID),
                User.SMS_CODE, smsCode
        );
    }

    /* @Deprecated
    public void accept(@NonNull final InternetCallback<Boolean> callback) {
        InternetTask task = new InternetTask(ACCEPT_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                Boolean result = Boolean.FALSE;
                switch (User.requestStatusFromString(s)) {
                    case ORDER_ERROR:
                        LogHelper.error("Ошибка при подтверждении заказа.");
                        break;

                    case ORDER_OK:
                        result = Boolean.TRUE;
                        break;
                }

                callback.call(result);
            }
        });

        task.execute(
                HASH, getStringProp(HASH),
                User.ID, getStringProp(ID)
        );
    } */

    public void status(@NonNull final InternetCallback<OrderStatus> callback) {
        InternetTask task = new InternetTask(InternetTask.METHOD_POST, STATUS_URL, new InternetCallback<String>() {
            @Override
            public void call(Result<String> s) {
                callback.call(
                        new Result<OrderStatus>(
                                s,
                                Result.statusFromString(
                                        s.getData()
                                )
                        )
                );
            }
        });

        task.execute(
                HASH, getStringProp(HASH),
                User.ID, getStringProp(ID)
        );
    }

    public void feedback(
            @NonNull Rating rating,
            @NonNull final InternetCallback<String> callback
    ) {
        InternetTask task = new InternetTask(InternetTask.METHOD_POST, FEEDBACK_URL, new InternetCallback<String>() {
            @Override
            public void call(Result<String> result) {
                if (result.isSuccessful()) {
                    result.setMessage("Спасибо за отзыв!");
                }

                callback.call(result);
            }
        });

        task.execute(
                HASH, getStringProp(HASH),
                TEXT, rating.getFeedback(),
                RATING, rating.getStars().toString(),
                "delivery_id", getStringProp(ID)
        );
    }

    @Override
    public int compareTo(@NonNull Order rhs) {
        String thisDate = getStringProp(TAKE_TIME);
        String rhsDate = rhs.getStringProp(TAKE_TIME);
        return thisDate.compareTo(rhsDate);
    }

    private void appendFieldValue(
            @NonNull StringBuilder sb,
            @NonNull String header,
            @NonNull String fieldName) {
        sb.append(header);
        sb.append(getStringProp(fieldName));
        sb.append("\n");
    }

    /**
     * Установка специальных свойств объекта.
     * @param parent User, чьим заказом является данный.
     */
    private void setupSpecialProps(@NonNull User parent) {
        setProp(PARENT, parent);
        setStringProp(HASH, parent.getHash());
        setProp(STATUS, Result.statusFromString(getStringProp(STATUS)));
    }

    private static final String ORDER_URL = "http://adrax.pythonanywhere.com/load_delys";
    private static final String CANCEL_URL = null; // "http://adrax.pythonanywhere.com/cancel";
    private static final String START_URL = "http://adrax.pythonanywhere.com/ch_dely";
    private static final String FINISH_URL = "http://adrax.pythonanywhere.com/delivered";
    private static final String ACCEPT_URL = "http://adrax.pythonanywhere.com/delivery_done";
    private static final String STATUS_URL = "http://adrax.pythonanywhere.com/chosen";
    private static final String FEEDBACK_URL = "http://adrax.pythonanywhere.com/feedback";

    public static final String HASH = "hash";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String CUSTOMER = "customer";
    public static final String PAYMENT = "payment";
    public static final String ENTRANCE = "entrance";
    public static final String CODE = "code";
    public static final String FLOOR = "floor";
    public static final String ROOM = "ko";
    public static final String PHONE = "num";
    public static final String WEIGHT = "wt";
    public static final String SIZE = "size";
    public static final String COST = "cost";
    public static final String DESCRIPTION = "description";
    public static final String STATUS = "status";
    public static final String PARENT = "parent";
    public static final String TAKE_TIME = "take_time";
    public static final String BRING_TIME = "bring_time";
    public static final String DISTANCE = "distance";
    public static final String DAY = "day";
    public static final String TEXT = "text";
    public static final String RATING = "rating";

    private OrderStatus m_orderStatus;
}
