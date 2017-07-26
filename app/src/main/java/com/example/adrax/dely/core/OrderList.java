package com.example.adrax.dely.core;

import android.util.Log;

import com.android.internal.util.Predicate;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;

public class OrderList extends AbstractList<Order> {
    public OrderList() {
        m_orders = new ArrayList<Order>();
    }

    public OrderList(Order[] orders) {
        if (orders == null) {
            Log.d(getClass().getName(), "Список заказов не может быть null");
            throw new NullPointerException();
        }

        m_orders = new ArrayList<Order>(Arrays.asList(orders));
    }

    private OrderList(ArrayList<Order> init) {
        if (init == null) {
            Log.d(getClass().getName(), "Внутренний массив не может быть null");
            throw new NullPointerException();
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
            Log.d(getClass().getName(), "Индекс вне границ.");
            throw new IndexOutOfBoundsException();
        }
        return m_orders.get(index);
    }

    @Override
    public int size() {
        return m_orders.size();
    }

    public Order firstOrDefault() {
        if (size() > 0) {
            return m_orders.get(0);
        } else {
            return null;
        }
    }

    /**
     * Выборка элементов из коллекции.
     * @param predicate предикат, по которому осуществляется выборка.
     * @return выборка элементов по предикату.
     */
    public OrderList where(Predicate<Order> predicate) {
        ArrayList<Order> list = new ArrayList<>();
        if (predicate == null) {
            Log.d(getClass().getName(), "Предикат не может быть null.");
            throw new IllegalArgumentException();
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
