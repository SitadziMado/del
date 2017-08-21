package com.example.adrax.dely.core;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Создано Максимом Сунцевым 28.07.2017.
 * Класс, представляющий собой динамически типизированный объект.
 */

class DynamicObject {
    public DynamicObject() {

    }

    public DynamicObject(String jsonString) {
        try {
            JSONObject cur = new JSONObject(jsonString);

            Iterator<String> it = cur.keys();

            while (it.hasNext()) {
                String key = it.next();
                setStringProp(key, cur.getString(key));
            }
        } catch (JSONException ex) {
            String m = "Исключение при чтении JSON.";
            LogHelper.error(m);
            m_props.clear();
        }
    }

    /**
     * Создать массив доставок из JSON'а.
     * @param jsonString JSON-объект.
     * @return список заказов, null при ошибке.
     */
    static ArrayList<DynamicObject> fromString(String jsonString) {
        ArrayList<DynamicObject> ret = new ArrayList<>();

        try {
            /// Список текущих заказов
            JSONObject list = new JSONObject(jsonString);

            for (Integer i = 1; list.has(i.toString()); ++i) {
                // По порядку выгружаем доступные заказы
                JSONObject cur = list.getJSONObject(i.toString());

                DynamicObject dyn = new DynamicObject();

                Iterator<String> it = cur.keys();

                while (it.hasNext()) {
                    String key = it.next();
                    dyn.setStringProp(key, cur.getString(key));
                }

                ret.add(dyn);
            }
        } catch (JSONException ex) {
            String m = "Исключение при чтении JSON.";
            LogHelper.error(m);
            ret.clear();
        }
        return ret;
    }

    public Object getProp(@NonNull String propName) {
        String name = propName.toLowerCase();

        if (m_props.containsKey(name)) {
            return m_props.get(name);
        } else {
            LogHelper.error("Свойства `" + propName + "` не существует.");
            return null;
        }
    }

    public void setProp(@NonNull String propName, Object value) {
        m_props.put(propName.toLowerCase(), value);
    }

    public String getStringProp(@NonNull String propName) {
        Object prop = getProp(propName);
        String result;

        try {
            result = (String)prop;
            if (result == null) {
                result = "";
            }
        } catch (ClassCastException e) {
            LogHelper.error("Ошибка преобразования в строку.");
            result = prop.toString();
            LogHelper.error("Возвращаю: " + result + ".");
        }
        return result;
    }

    public void setStringProp(@NonNull String propName, String value) {
        setProp(propName.toLowerCase(), value);
    }

    public boolean hasProp(@NonNull String propName) {
        return m_props.containsKey(propName);
    }

    protected HashMap<String, Object> m_props = new HashMap<>();
}
