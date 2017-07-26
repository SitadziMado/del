package com.example.adrax.dely.core;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class Order implements Comparable<Order> {
    public Order(User parent, Object... params) {
        if ((params.length & 1) == 0 && parent != null) {
            for (int i = 0; i < params.length; i += 2) {
                String key = (String)params[i];
                Object value = params[i + 1];

                if (key == null || value == null || key.equals("")) { // || value.equals("")
                    String m = "Параметры не могут быть пустыми.";
                    LogHelper.log(m);
                    throw new IllegalArgumentException(m);
                }

                setProp(key, value);
            }

            // Установить значения хэша, статуса, родителя. Не трогать!
            setupSpecialProps(parent);
        } else {
            String m = "Требуется четное количество аргументов для составления пар.";
            LogHelper.log(m);
            throw new IllegalArgumentException(m);
        }
    }

    public static OrderList fromString(String jsonString, User parent) {
        if (parent == null) {
            String m = "Пользователь не может быть null.";
            LogHelper.log(m);
            throw new IllegalArgumentException(m);
        }

        OrderList ret = new OrderList();

        try {
            /// Список текущих заказов
            JSONObject list = new JSONObject(jsonString);

            for (Integer i = 1; list.has(i.toString()); ++i) {
                // По порядку выгружаем доступные заказы
                JSONObject cur = list.getJSONObject(i.toString());

                Order order = new Order();

                Iterator<String> it = cur.keys();

                while (it.hasNext()) {
                    String key = it.next();
                    order.setStringProp(key, cur.getString(key));
                }

                // Установить значения хэша, статуса, родителя. Не трогать!
                order.setupSpecialProps(parent);
                ret.add(order);
            }
        } catch (JSONException ex) {
            String m = "Исключение при чтении JSON.";
            LogHelper.log(m);
            ret = null;
        }
        return ret;
    }

    private Order() {

    }

    public void setProp(String propName, Object value) {
        m_props.put(propName.toLowerCase(), value);
    }

    public Object getProp(String propName) {
        String name = propName.toLowerCase();

        if (m_props.containsKey(name)) {
            return m_props.get(name);
        } else {
            LogHelper.log(
                    "Свойства `" + propName + "` не существует."
            );
            return null;
        }
    }

    public void setStringProp(String propName, String value) {
        setProp(propName.toLowerCase(), value.toLowerCase());
    }

    public String getStringProp(String propName) {
        return (String)getProp(propName);
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

    public void post(final InternetCallback<Boolean> callback) {
        InternetTask task = new InternetTask(ORDER_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                Boolean result = Boolean.FALSE;

                switch (User.requestStatusFromString(s.toLowerCase())) {
                    case ORDER_ERROR:
                        LogHelper.log("Ошибка при отправлении заказа на сервер.");
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
                // Order.ROOM, getStringProp(Order.ROOM),
                Order.PHONE, getStringProp(Order.PHONE),
                Order.WEIGHT, getStringProp(Order.WEIGHT),
                Order.SIZE, getStringProp(Order.SIZE),
                HASH, ((User)getProp(PARENT)).getHash(),
                Order.DESCRIPTION, getStringProp(Order.DESCRIPTION)
        );
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
                        LogHelper.log("Заказ уже начат.");
                        break;

                    case ORDER_TOO_MANY:
                        LogHelper.log("Слишком много начатых заказов.");
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
                ID, getStringProp(ID),
                User.COURIER, ((User)getProp(PARENT)).getLogin()
        );
    }

    public void finish(final InternetCallback<Boolean> callback) {
        InternetTask task = new InternetTask(FINISH_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                Boolean result = Boolean.FALSE;
                switch (User.requestStatusFromString(s.toLowerCase())) {
                    case ORDER_BUSY:
                        LogHelper.log("Заказ уже начат.");
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
                ID, getStringProp(ID),
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
                        LogHelper.log("Ошибка при подтверждении заказа.");
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
                ID, getStringProp(ID)
        );
    }

    public void status(final InternetCallback<OrderStatus> callback) {
        InternetTask task = new InternetTask(STATUS_URL, new InternetCallback<String>() {
            @Override
            public void call(String s) {
                callback.call(statusFromString(s));
            }
        });

        task.execute(
                HASH, getStringProp(HASH),
                ID, getStringProp(ID)
        );
    }

    @Override
    public int compareTo(@NonNull Order o) {
        // ToDo: написать сортировку по дате.
        return 0;
    }

    private static OrderStatus statusFromString(String statusString) {
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
                LogHelper.log("Неизвестный статус заказа.");
                return OrderStatus.ERROR;
        }
    }

    private void appendFieldValue(
            StringBuilder sb,
            String header,
            String fieldName) {
        sb.append(header);
        sb.append(getStringProp(fieldName));
        sb.append("\n");
    }

    /**
     * Установка специальных свойств объекта.
     * @param parent User, чьим заказом является данный.
     */
    private void setupSpecialProps(User parent) {
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
    // public static final String ROOM = "room";
    public static final String PHONE = "num";
    public static final String WEIGHT = "wt";
    public static final String SIZE = "size";
    public static final String COST = "cost";
    public static final String DESCRIPTION = "description";
    public static final String STATUS = "status";
    public static final String PARENT = "parent";

    private OrderStatus m_orderStatus;
    private HashMap<String, Object> m_props = new HashMap<>();

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
