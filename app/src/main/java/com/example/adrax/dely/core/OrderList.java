package com.example.adrax.dely.core;

import android.util.Log;

import com.android.internal.util.Predicate;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class OrderList extends AbstractList<Order> {
    public OrderList() {
        m_orders = new ArrayList<Order>();
    }

    public OrderList(Order[] orders) {
        if (orders == null) {
            String m = "Список заказов не может быть null";
            LogHelper.log(m);
            throw new NullPointerException(m);
        }

        m_orders = new ArrayList<Order>(Arrays.asList(orders));
    }

    private OrderList(ArrayList<Order> init) {
        if (init == null) {
            String m = "Внутренний массив не может быть null";
            LogHelper.log(m);
            throw new NullPointerException(m);
        }

        m_orders = new ArrayList<Order>();

        for (Order o : init) {
            m_orders.add(o);
        }
    }

    public boolean add(Order item) {
        m_orders.add(item);
        return true;
    }

    @Override
    public Order get(int index)
            throws IndexOutOfBoundsException {
        if (index < 0 || index >= m_orders.size()) {
            String m = "Индекс вне границ.";
            LogHelper.log(m);
            throw new IndexOutOfBoundsException(m);
        }
        return m_orders.get(index);
    }

    @Override
    public int size() {
        return m_orders.size();
    }

    /**
     * Первый элемент или значение по умолчанию.
     * @return возвращает первый элемент или null, если его нет.
     */
    public Order firstOrDefault() {
        if (size() > 0) {
            return get(0);
        } else {
            return null;
        }
    }

    public void sortBy() {
        Collections.sort(m_orders);
    }

    public void sortBy(Comparator<Order> cmp) {
        Collections.sort(m_orders, cmp);
    }

    /**
     * Выборка элементов из коллекции.
     * @param predicate предикат, по которому осуществляется выборка.
     * @return выборка элементов по предикату.
     */
    public OrderList where(Predicate<Order> predicate) {
        ArrayList<Order> list = new ArrayList<>();
        if (predicate == null) {
            String m = "Предикат не может быть null.";
            LogHelper.log(m);
            throw new IllegalArgumentException(m);
        }

        for (Order order : m_orders) {
            if (predicate.apply(order)) {
                list.add(order);
            }
        }

        return new OrderList(list);
    }

    private ArrayList<Order> m_orders;
}
