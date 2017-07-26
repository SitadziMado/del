package com.example.adrax.dely.core;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Order {
    public Order(User parent, String... params) {
        if ((params.length & 1) == 0 && parent != null) {
            m_parent = parent;

            for (int i = 0; i < params.length; i += 2) {
                String key = params[i];
                String value = params[i + 1];

                if (key == null || value == null || key.equals("")) { // || value.equals("")
                    throw new IllegalArgumentException("Параметры не могут быть пустыми.");
                }

                setProp(key, value);
            }
        } else {
            throw new IllegalArgumentException(
                    "Требуется четное количество аргументов для составления пар."
            );
        }
    }

    public static Order[] fromString(String jsonString, User parent) {
        if (parent == null) {
            throw new IllegalArgumentException(
                    "Пользователь не может быть null."
            );
        }

        Order[] ret;
        try {
            /// Список текущих заказов
            ArrayList<Order> orders = new ArrayList<>();
            JSONObject list = new JSONObject(jsonString);

            for (Integer i = 1; list.has(i.toString()); ++i) {
                // По порядку выгружаем доступные заказы
                JSONObject cur = list.getJSONObject(i.toString());

                Order order = new Order(parent);

                order.m_parent = parent;
                order.setProp(HASH, parent.getHash());
                Iterator<String> it = cur.keys();

                while (it.hasNext()) {
                    String key = it.next();
                    order.setProp(key, cur.getString(key));
                }

                orders.add(order);
            }

            ret = new Order[orders.size()];
            ret = orders.toArray(ret);
        } catch (JSONException ex) {
            ret = null;
        }
        return ret;
    }

    public void setProp(String fieldName, String fieldValue) {
        m_props.put(fieldName.toLowerCase(), fieldValue.toLowerCase());
    }

    public String getProp(String fieldName) {
        String name = fieldName.toLowerCase();

        if (m_props.containsKey(name)) {
            return m_props.get(name);
        } else {
            return "undefined";
        }
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

        String cost = getProp(COST);
        String weight = getProp(WEIGHT);
        String size = getProp(SIZE);
        String code = getProp(CODE);
        String entrance = getProp(ENTRANCE);
        String floor = getProp(FLOOR);

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
    public void cancel(final InternetCallback<Boolean> callback) {
        InternetTask task = new InternetTask(CANCEL_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                callback.call(Boolean.FALSE);
            }
        });

        task.execute();
    }

    public void start(final InternetCallback<Boolean> callback) {
        InternetTask task = new InternetTask(START_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                Boolean result = Boolean.FALSE;
                switch (User.requestStatusFromString(s.toLowerCase())) {
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

        // assert m_parent != null;

        task.execute(
                HASH, getProp(HASH),
                ID, getProp(ID),
                User.COURIER, m_parent.getLogin()
        );
    }

    public void finish(final InternetCallback<Boolean> callback) {
        InternetTask task = new InternetTask(FINISH_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                Boolean result = Boolean.FALSE;
                switch (User.requestStatusFromString(s.toLowerCase())) {
                    case ORDER_BUSY:
                        break;

                    case ORDER_STARTED:
                        result = Boolean.TRUE;
                        break;
                }

                callback.call(result);
            }
        });

        task.execute(
                HASH, getProp(HASH),
                ID, getProp(ID),
                User.SMS_CODE, "0000"
        );
    }

    public void accept(final InternetCallback<Boolean> callback) {
        InternetTask task = new InternetTask(ACCEPT_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                Boolean result = Boolean.FALSE;
                switch (User.requestStatusFromString(s.toLowerCase())) {
                    case ORDER_ERROR:
                        break;

                    case ORDER_OK:
                        result = Boolean.TRUE;
                        break;
                }

                callback.call(result);
            }
        });

        task.execute(
                HASH, getProp(HASH),
                ID, getProp(ID)
        );
    }

    public void status(final InternetCallback<OrderStatus> callback) {
        InternetTask task = new InternetTask(STATUS_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                OrderStatus result;
                switch (s.toLowerCase()) {
                    case User.WAITING:
                        result = OrderStatus.WAITING;
                        break;

                    case User.DELIVERING:
                        result = OrderStatus.DELIVERING;
                        break;

                    case User.DELIVERED:
                        result = OrderStatus.DELIVERED;
                        break;

                    case User.DELIVERY_DONE:
                        result = OrderStatus.DELIVERY_DONE;
                        break;

                    case User.ERROR:
                        result = OrderStatus.ERROR;
                        break;

                    default:
                        result = OrderStatus.ERROR;
                        break;
                }

                callback.call(result);
            }
        });

        task.execute(
                HASH, getProp(HASH),
                ID, getProp(ID)
        );
    }

    private void appendFieldValue(
            StringBuilder sb,
            String header,
            String fieldName) {
        sb.append(header);
        sb.append(getProp(fieldName));
        sb.append("\n");
    }

    private static final String CANCEL_URL = null; // "http://adrax.pythonanywhere.com/send_delys";
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
    public static final String ENTRANCE = "padik";
    public static final String CODE = "code";
    public static final String FLOOR = "floor";
    public static final String ROOM = "ko";
    public static final String PHONE = "num";
    public static final String WEIGHT = "wt";
    public static final String SIZE = "size";
    public static final String COST = "cost";
    public static final String DESCRIPTION = "dayoff";

    private OrderStatus m_orderStatus;
    private HashMap<String, String> m_props = new HashMap<>();
    private User m_parent = null;

    // private int m_id = -1;
    // private String m_customer;
    // private String m_from;
    // private String m_to;
    // private String m_cost;
    // private String m_payment;
    // private String m_entrance;
    // private String m_code;
    // private String m_floor;
    // private String m_room;
    // private String m_telephoneNumber;
    // private String m_weight;
    // private String m_size;
    // private String m_time;
    // private String m_widthHeightLength;
    // private String m_name;

    // private String m_additionalTelephoneNumber;
}
