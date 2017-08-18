package com.example.adrax.dely.core;

import android.support.annotation.NonNull;

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

    public OrderList(@NonNull Order[] orders) {
        m_orders = new ArrayList<Order>(Arrays.asList(orders));
    }

    private OrderList(@NonNull ArrayList<Order> init) {
        m_orders = new ArrayList<Order>();

        for (Order o : init) {
            m_orders.add(o);
        }
    }

    public boolean add(Order item) {
        m_orders.add(item);
        return true;
    }

    public boolean isEmpty() {
        return m_orders.size() == 0;
    }

    @Override
    public Order get(int index)
            throws IndexOutOfBoundsException {
        if (index < 0 || index >= m_orders.size()) {
            String m =
                    "Индекс вне границ: `" +
                    Integer.toString(index) +
                    "/[0; " +
                    Integer.toString(m_orders.size()) +
                    ")`.";
            LogHelper.error(m);
            throw new IndexOutOfBoundsException(m);
        }
        return m_orders.get(index);
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

    @Override
    public int size() {
        return m_orders.size();
    }

    public void sortBy() {
        Collections.sort(m_orders);
    }

    /**
     * Сортировка заказов по выбранному свойству.
     * @param propToSortBy свойство, по которому следует сортировать.
     */
    public void sortBy(@NonNull final String propToSortBy) {
        Comparator<Order> cmp = new Comparator<Order>() {
            @Override
            public int compare(Order lhs, Order rhs) {
                String lhsDate = lhs.getStringProp(propToSortBy);
                String rhsDate = rhs.getStringProp(propToSortBy);
                return lhsDate.compareTo(rhsDate);
            }
        };

        Collections.sort(m_orders, cmp);
    }

    /**
     * Выборка элементов из коллекции.
     * @param predicate предикат, по которому осуществляется выборка.
     * @return выборка элементов по предикату.
     */
    public OrderList where(@NonNull Predicate<Order> predicate) {
        ArrayList<Order> list = new ArrayList<>();
        for (Order order : m_orders) {
            if (predicate.apply(order)) {
                list.add(order);
            }
        }

        return new OrderList(list);
    }

    private ArrayList<Order> m_orders;
}
