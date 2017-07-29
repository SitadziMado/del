package com.example.adrax.dely.core;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
        appendFieldValue(sb, "Название: ", NAME);
        appendFieldValue(sb, "Откуда: ", FROM);
        appendFieldValue(sb, "Куда: ", TO);
        appendFieldValue(sb, "Заказчик: ", CUSTOMER);
        appendFieldValue(sb, "Номер телефона: ", PHONE);
        appendFieldValue(sb, "Оплата: ", PAYMENT);

        String cost = getStringProp(COST);
        String weight = getStringProp(WEIGHT);
        String size = getStringProp(SIZE);
        String code = getStringProp(CODE);
        String entrance = getStringProp(ENTRANCE);
        String floor = getStringProp(FLOOR);

        if (cost != null && !cost.equals("")) {
            sb.append("Аванс: ")
                    .append(cost)
                    .append("руб. \n");
        }
        if (weight != null && !weight.equals("")) {
            sb.append("Вес: ")
                    .append(weight)
                    .append("\n");
        }
        if (size != null && !size.equals("")) {
            sb.append("Размер: ")
                    .append(size)
                    .append("\n");
        }
        if (code !=null && !code.equals("")) {
            sb.append("Код домофона: ")
                    .append(code)
                    .append("\n");
        }
        if (entrance != null && !entrance.equals("")) {
            sb.append("Подъезд: ")
                    .append(entrance)
                    .append("\n");
        }
        if (floor != null && !floor.equals("")) {
            sb.append("Этаж: ")
                    .append(floor)
                    .append("\n");
        }

        return sb.toString();
    }

    public void post(@NonNull final InternetCallback<Boolean> callback) {
        InternetTask task = new InternetTask(ORDER_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                Boolean result = Boolean.FALSE;

                switch (User.requestStatusFromString(s)) {
                    case ORDER_ERROR:
                        LogHelper.error("Ошибка при отправлении заказа на сервер.");
                        break;

                    case ORDER_LOADED:
                        result = Boolean.TRUE;
                        break;
                }

                callback.call(result);
            }
        });

        task.execute(
                Order.CUSTOMER, getStringProp(Order.CUSTOMER),
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
                HASH, ((User)getProp(PARENT)).getHash(),
                Order.DESCRIPTION, getStringProp(Order.DESCRIPTION),
                // Order.ROOM, getStringProp(Order.ROOM),
                "recnum", "undefined"
        );
    }

    public void cancel(@NonNull final InternetCallback<Boolean> callback) {
        InternetTask task = new InternetTask(CANCEL_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                callback.call(Boolean.FALSE);
            }
        });

        task.execute();
    }

    public void start(@NonNull final InternetCallback<Boolean> callback) {
        InternetTask task = new InternetTask(START_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                Boolean result = Boolean.FALSE;
                switch (User.requestStatusFromString(s)) {
                    case ORDER_BUSY:
                        LogHelper.error("Заказ уже начат.");
                        break;

                    case ORDER_TOO_MANY:
                        LogHelper.error("Слишком много начатых заказов.");
                        break;

                    case ORDER_STARTED:
                        result = Boolean.TRUE;
                        break;
                }

                callback.call(result);
            }
        });

        // assert m_parent != null;

        task.execute(
                HASH, getStringProp(HASH),
                User.ID, getStringProp(ID),
                User.COURIER, ((User)getProp(PARENT)).getLogin()
        );
    }

    public void finish(@NonNull final InternetCallback<Boolean> callback) {
        InternetTask task = new InternetTask(FINISH_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                Boolean result = Boolean.FALSE;
                switch (User.requestStatusFromString(s)) {
                    case ORDER_BUSY:
                        LogHelper.error("Заказ уже начат.");
                        break;

                    case ORDER_STARTED:
                        result = Boolean.TRUE;
                        break;
                }

                callback.call(result);
            }
        });

        task.execute(
                HASH, getStringProp(HASH),
                User.ID, getStringProp(ID),
                User.SMS_CODE, "0000"
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
        InternetTask task = new InternetTask(STATUS_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                callback.call(statusFromString(s));
            }
        });

        task.execute(
                HASH, getStringProp(HASH),
                User.ID, getStringProp(ID)
        );
    }

    @Override
    public int compareTo(@NonNull Order o) {
        // ToDo: написать сортировку по дате.
        return 0;
    }

    private static OrderStatus statusFromString(String statusString) {
        if (statusString == null) {
            statusString = "";
        }

        switch (statusString.toLowerCase()) {
            case User.WAITING:
                return OrderStatus.WAITING;

            case User.DELIVERING:
                return OrderStatus.DELIVERING;

            case User.DELIVERED:
                return OrderStatus.DELIVERED;

            case User.DELIVERY_DONE:
                return OrderStatus.DELIVERY_DONE;

            case User.ERROR:
                return OrderStatus.ERROR;

            default:
                LogHelper.error("Неизвестный статус заказа.");
                return OrderStatus.ERROR;
        }
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
        setProp(STATUS, statusFromString(getStringProp(STATUS)));
    }

    private static final String ORDER_URL = "http://adrax.pythonanywhere.com/load_delys";
    private static final String CANCEL_URL = null; // "http://adrax.pythonanywhere.com/cancel";
    private static final String START_URL = "http://adrax.pythonanywhere.com/ch_dely";
    private static final String FINISH_URL = "http://adrax.pythonanywhere.com/delivered";
    private static final String ACCEPT_URL = "http://adrax.pythonanywhere.com/delivery_done";
    private static final String STATUS_URL = "http://adrax.pythonanywhere.com/chosen";

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

    private OrderStatus m_orderStatus;
}
