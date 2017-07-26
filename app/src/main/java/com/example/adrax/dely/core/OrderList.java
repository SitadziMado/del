package com.example.adrax.dely.core;

import android.util.Log;

import com.android.internal.util.Predicate;

import java.util.AbstractList;
import java.util.ArrayList;

public class OrderList extends AbstractList {
    public OrderList(Order[] orders) {
        if (orders == null) {
            throw new NullPointerException();
        }

        m_orders = orders.clone();
    }

    @Override
    public Order get(int index) {
        if (index < 0 || index >= m_orders.length) {
            Log.d(getClass().getName(), "Индекс вне границ.");
            throw new IndexOutOfBoundsException();
        }
        return m_orders[index];
    }

    @Override
    public int size() {
        return m_orders.length;
    }

    public Order[] where(Predicate<Order> predicate) {
        ArrayList<Order> list = new ArrayList<>();

        for (Order order : m_orders) {
            if (predicate.apply(order)) {
                list.add(order);
            }
        }

        return list.toArray(new Order[0]);
    }

    private Order[] m_orders;
}
