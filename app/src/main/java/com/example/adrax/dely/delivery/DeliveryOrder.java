/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.adrax.dely.delivery;

import com.google.android.gms.common.api.Api;

/**
 *
 * @author Максим
 */
@Deprecated
public class DeliveryOrder {
    /// Номер заказа
    private String id;
    private String customer;
    private String from;
    private String to;
    private String cost;
    private String payment;
    private String entrance;
    private String code;
    private String floor;
    private String room;
    private String telephoneNumber;
    private String additionalTelephoneNumber;
    private String weight;
    private String size;
    private String time;                         /** Время заказа */
    private String widthHeightLength;
    private String name;

    /** Конструктор для класса, где указываются все необходимые свойства заказа */
    public DeliveryOrder(String _id,
                         String _customer,
                         String _from,
                         String _to,
                         String _cost,
                         String _payment,
                         String _entrance,
                         String _code,
                         String _floor,
                         String _room,
                         String _telephoneNumber,
                         String _additionalTelephoneNumber,
                         String _weight,
                         String _size,
                         String _time) {
        initialize(
                _id,
                _customer,
                _from,
                _to,
                _cost,
                _payment,
                _entrance,
                _code,
                _floor,
                _room,
                _telephoneNumber,
                _additionalTelephoneNumber,
                _weight,
                _size,
                _time,
                ""
        );
    }

    /** Конструктор для класса, где указываются все необходимые свойства заказа */
    public DeliveryOrder(String _id,
                         String _customer,
                         String _from,
                         String _to,
                         String _cost,
                         String _payment,
                         String _entrance,
                         String _code,
                         String _floor,
                         String _room,
                         String _telephoneNumber,
                         String _additionalTelephoneNumber,
                         String _weight,
                         String _size,
                         String _time,
                         String _name) {
        initialize(
                _id,
                _customer,
                _from,
                _to,
                _cost,
                _payment,
                _entrance,
                _code,
                _floor,
                _room,
                _telephoneNumber,
                _additionalTelephoneNumber,
                _weight,
                _size,
                _time,
                _name
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /** Идентификатор заказа */
    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    /** Имя клиента */
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    /** Откуда идёт заказ */
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    /** Куда идёт заказ */
    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    /** Стоимость заказа */
    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    /** Залог (вроде бы не используем пока) */
    public String getEntrance() {
        return entrance;
    }

    public void setEntrance(String entrance) {
        this.entrance = entrance;
    }

    /** Номер подъезда */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /** Код от домофона */
    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    /** Этаж назначения */
    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    /** Квартира/офис */
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    /** Номер телефона */
    @Deprecated
    public String getAdditionalTelephoneNumber() {
        return additionalTelephoneNumber;
    }

    @Deprecated
    public void setAdditionalTelephoneNumber(String additionalTelephoneNumber) {
        this.additionalTelephoneNumber = additionalTelephoneNumber;
    }

    /** Дополнительный номер телефона */
    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    /** Вес товара */
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    /** Размер товара */
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWidthHeightLength() {
        return widthHeightLength;
    }

    public void setWidthHeightLength(String _widthHeightLength) {
        widthHeightLength = _widthHeightLength;
    }

    public String getName() {
        return name;
    }

    public void setName(String _name) {
        name = _name;
    }

    private void initialize(String _id,
                            String _customer,
                            String _from,
                            String _to,
                            String _cost,
                            String _payment,
                            String _entrance,
                            String _code,
                            String _floor,
                            String _room,
                            String _telephoneNumber,
                            String _additionalTelephoneNumber,
                            String _weight,
                            String _size,
                            String _time,
                            String _name) {
        setId(_id);
        setCustomer(_customer);
        setFrom(_from);
        setTo(_to);
        setCost(_cost);
        setPayment(_payment);
        setEntrance(_entrance);
        setCode(_code);
        setFloor(_floor);
        setRoom(_room);
        setTelephoneNumber(_telephoneNumber);
        setAdditionalTelephoneNumber(_additionalTelephoneNumber);
        setWeight(_weight);
        setSize(_size);
        setTime(_time);
        setName(_name);
    }

}