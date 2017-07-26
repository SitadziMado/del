/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.adrax.dely.delivery;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 * @author Максим
 */
@Deprecated
public class DeliveryUser {
    /** Константы авторизации
     * 
     */
    private static final String INCORRECT_AUTHORIZATION_DATA = "incorrect_auth";    /** Некорректная инфа для авторизации */
    private static final String INCORRECT_REQUEST = "405";                          /** Тоже какая-то ошибка */
    private static final String SERVER_PROBLEMS = "login_error";                    /** Сервак гуфнулся... */

    /** Константы регистрации
     * 
     */
    private static final String LOGIN_REGISTERED = "registered";            /** Регистрация прошла успешно */
    private static final String LOGIN_ALREADY_TAKEN = "already_taken";      /** Логин занят */

    /** Константы заказов
     *
     */
    private static final String ORDER_LOADED = "loaded";		        /** Новый заказ успешно создан */
    private static final String ORDER_ERROR = "error";                  /** Ошибка отправки нового заказа на сервер */
    private static final String ORDER_WAITING = "waiting";              /** Статусы заказов ниже */
    private static final String ORDER_DELIVERING = "delivering";        /** В процессе... */
    private static final String ORDER_DELIVERED = "delivered";          /** Вроде бы доставлено */
    private static final String ORDER_DELIVERY_DONE = "delivery_done";  /** Абсолютный суккесс */
    private static final String ORDER_STARTED = "delivery_started";     /** Всё хорошо, доставка началась */
    private static final String ORDER_BUSY = "delivery_busy";           /** Пользователь - слоупок, рекомендуем обновиться до 10-ки */
    private static final String ORDER_OK = "ok";                        /** Заказ успешно подтверждён (обеими сторонами) */
    private static final String ORDER_TOO_MANY = "already_enough";      /** Только 1 заказ можно доставлять */

    /** Статус, полученный при старте заказа
     *
     */
    public enum StartingStatus {
        ERROR,
        TOO_MANY,
        FAILURE,
        SUCCESS,
    }

    /** Переменные, которые мы получаем после выполнения авторизации
     * 
     */
    private String about;   /** Информация о юзвере  */
    private String hash;    /** Выданный хэш */
    private String mail;    /** Мыло пользователя */
    private String midname; /** Его отчество */
    private String money;   /** Его капуста (не используется) */
    private String name;    /** Его имя */
    private String selnum;  /** Телефонный номер */
    private String surname; /** Фамилия */
    private String login;   /** Текущий логин */

    public String GetName()
    {
        return name;
    }

    public String GetSurname()
    {
        return surname;
    }

    public String GetMidname()
    {
        return midname;
    }

    public String GetSelnum()
    {
        return selnum;
    }

    public String GetMoney()
    {
        return money;
    }

    public String GetMail()
    {
        return mail;
    }

    public String GetAbout()
    {
        return about;
    }

    public String Getlogin()
    {
        return login;
    }
    /**
     * Заменить Desktop на Android для ведра
     */
    static IDeliveryReporter reporter;
    static DeliveryHttpRequester requester;

    /** Преобразует текстовый JSON в список заказов
     *
     * @param orderList список заказов в формате JSON
     * @return null при ошибке,
     */
    private DeliveryOrder[] stringToOrders(String orderList) {
        DeliveryOrder[] ret;
        try {
            /// Список текущих заказов
            ArrayList<DeliveryOrder> orders = new ArrayList<>();
            JSONObject list = new JSONObject(orderList);
            for (Integer i = 1; list.has(i.toString()); ++i) {
                // По порядку выгружаем доступные заказы
                JSONObject cur = list.getJSONObject(i.toString());
                orders.add(new DeliveryOrder(   /*i.toString()*/cur.getString("id"),
                        cur.getString("customer"),
                        cur.getString("from"),
                        cur.getString("to"),
                        cur.getString("cost"),
                        String.valueOf(cur.getDouble("payment")),
                        /*cur.getString("padik")*/"",
                        cur.getString("code"),
                        cur.getString("floor"),
                        cur.getString("ko"),
                        cur.getString("num"),
                        cur.getString("recnum"),
                        cur.getString("wt"),
                        cur.getString("size"),
                        cur.getString("time"),
                        cur.getString("description")));
            }
            ret = new DeliveryOrder[orders.size()];
            ret = orders.toArray(ret);
        }
        catch (JSONException ex) {
            reporter.reportError("JSONException: " + ex.getMessage());
            ret = null;
        }
        return ret;
    }

    /** Конструктор по умолчанию
     *
     * @param msgContext контекст, где будут показываться всплывающие уведомления об ошибках
     */
    public DeliveryUser(Context msgContext) {
        reporter = new DeliveryReporterAndroid(msgContext);
        requester = new DeliveryHttpRequester(msgContext);
        about = "";
        hash = "";
        mail = "";
        midname = "";
        money = "";
        name = "";
        selnum = "";
        surname = "";
        login = "";
    }
    
    /** Отправка запроса на авторизацию
     * 
     * @param name имя пользователя нашей прекрасной службы
     * @param password его пароль (небезопасно)
     * @return истина клиент с данным логином и паролем авторизован, ложь произошла ошибка
     */
    public Boolean login(String name, String password) {
        Boolean ret = Boolean.TRUE;
        login = name;
        try {
            String link = "http://adrax.pythonanywhere.com/login";
            String rc = requester.httpPostRequestVar(link, "Username", name, "Password", password);

            if (rc.equalsIgnoreCase(INCORRECT_AUTHORIZATION_DATA))
                ret = Boolean.FALSE;
            else if (rc.equalsIgnoreCase(SERVER_PROBLEMS))
                ret = Boolean.FALSE;
            else if (rc.equalsIgnoreCase(INCORRECT_REQUEST))	// Этого не должно случиться
                ret = Boolean.FALSE;

            if (ret) {
                JSONObject userData = new JSONObject(rc);
                about = userData.getString("About");
                hash = userData.getString("Hash");
                mail = userData.getString("Mail");
                midname = userData.getString("Midname");
                money = String.valueOf(userData.getDouble("Money"));
                this.name = userData.getString("Name");
                selnum = userData.getString("Selnum");
                surname = userData.getString("Surname");
            }
            else {
                about = "";
                hash = "";
                mail = "";
                midname = "";
                money = "";
                this.name = "";
                selnum = "";
                surname = "";
            }
        }
        catch (JSONException ex) {
            reporter.reportError("JSONException: " + ex.getMessage());
            ret = Boolean.FALSE;
        }
        return ret;
    }

    /** Отправка запроса регистрации
     * 
     * @param name имя (логин), который мы намереваемся создать
     * @param password пароль для нового аккаунта
     * @param mail почта, на которую можно будет выслать инфу о восстановлении
     * @return истина зарегистрирован, ложь произошла ошибка
     */
    public Boolean register(String name, String password, String mail) {
        String link = "http://adrax.pythonanywhere.com/register";
        String rc = requester.httpPostRequestVar(link,
                "Password", password,
                "Mail", mail,
                "Name", name);

        if (rc.equalsIgnoreCase(LOGIN_REGISTERED))
            return Boolean.TRUE;
        else if (rc.equalsIgnoreCase(LOGIN_ALREADY_TAKEN))
            return Boolean.FALSE;
        return Boolean.FALSE;
    }

    /** Отправка запроса регистрации
     *
     * @param name имя (логин), который мы намереваемся создать
     * @param password пароль для нового аккаунта
     * @param mail почта, на которую можно будет выслать инфу о восстановлении
     * @return истина зарегистрирован, ложь произошла ошибка
     */
    public Boolean register(String login,
                            String password,
                            String mail,
                            String name,
                            String surname,
                            String midname,
                            String number,
                            String about) {
        String link = "http://adrax.pythonanywhere.com/register";
        String rc = requester.httpPostRequestVar(link,
                "Username", login,
                "Password", password,
                "Mail", mail,
                "Name", name,
                "Surname", surname,
                "Midname", midname,
                "Selnum", number,
                "About", about);

        if (rc.equalsIgnoreCase(LOGIN_REGISTERED))
            return Boolean.TRUE;
        else if (rc.equalsIgnoreCase(LOGIN_ALREADY_TAKEN))
            return Boolean.FALSE;
        return Boolean.FALSE;
    }

    /** Выход из аккаунта
     *
     */
    public void logout() {
        hash = "";
        name = "";
        login = "";
        String link = "http://adrax.pythonanywhere.com/logout";
        String rc = requester.httpPostRequestVar(link);
    }
    
    /** Создание нового заказа
     * 
     * @param customer имя целевого клиента
     * @param from адрес, откуда идёт заказ
     * @param to адрес, куда идёт заказ
     * @param cost стоимость заказа
     * @param payment залог (не используется пока)
     * @param entrance подъезд, куда следует подъехать доставщику
     * @param code код от домофона (думаю, излишне, хотя, как знать)
     * @param floor этаж клиента
     * @param room комната, квартира или офис
     * @param telephoneNumber телефонный номер, по которому можно связаться с клиентом
     * @param additionalTelephoneNumber доп. номер
     * @param weight вес заказа
     * @param size его примерный размер
     * @return истина заказ принят системой, ложь произошла ошибка
     */
    public Boolean order(   String customer,
                            String from,
                            String to,
                            String cost,
                            String payment,
                            String entrance,
                            String code,
                            String floor,
                            String room,
                            String telephoneNumber,
                            String additionalTelephoneNumber,
                            String weight,
                            String size,
                            String _name) {
        String link = "http://adrax.pythonanywhere.com/load_delys";
        String rc = requester.httpPostRequestVar(link,
                "customer", customer,
                "from", from,
                "to", to,
                "cost", cost,
                "payment", payment,
                "padik", entrance,
                "code", code,
                "floor", floor,
                "ko", room,
                "num", telephoneNumber,
                "recnum", additionalTelephoneNumber,
                "wt", weight,
                "size", size,
                "hash", hash,
                "description", _name);

        if (rc.equalsIgnoreCase(ORDER_LOADED))
            return Boolean.TRUE;
        else if (rc.equalsIgnoreCase(ORDER_ERROR))
            return Boolean.FALSE;
        return Boolean.FALSE;
    }

    /** Создание нового заказа
     * 
     * @param order инициализированный заказ
     * @return истина заказ принят системой, ложь произошла ошибка
     */
    public Boolean order(DeliveryOrder order) {
        if (null == order)
            return Boolean.FALSE;
        String link = "http://adrax.pythonanywhere.com/load_delys";
        String rc = requester.httpPostRequestVar(link,
                "customer", order.getCustomer(),
                "from", order.getFrom(),
                "to", order.getTo(),
                "cost", order.getCost(),
                "payment", order.getPayment(),
                "padik", order.getEntrance(),
                "code", order.getCode(),
                "floor", order.getFloor(),
                "ko", order.getRoom(),
                "num", order.getTelephoneNumber(),
                "recnum", order.getAdditionalTelephoneNumber(),
                "wt", order.getWeight(),
                "size", order.getSize(),
                "hash", hash,
                "description", order.getName());

        if (rc.equalsIgnoreCase(ORDER_LOADED))
            return Boolean.TRUE;
        else if (rc.equalsIgnoreCase(ORDER_ERROR))
            return Boolean.FALSE;

        return Boolean.FALSE;
    }
    
    /** Запроса списка всех активных заказов
     * 
     * @return список заказов, доступных на данный момент, null иначе
     */
    public DeliveryOrder[] orderList() {
        String link = "http://adrax.pythonanywhere.com/send_delys";
        String rc = requester.httpPostRequestVar(link, "refr", "delys");
        if (rc.equals("404"))
            return null;

        return stringToOrders(rc);
    }

    /** Начать исполнять один из заказов
     * 
     * @param order объект класса DeliveryOrder, который представляет из себя заказ
     * @return истина заказ начат, ложь произошла ошибка
     */
    public StartingStatus orderStart(DeliveryOrder order) {
        if (null == order)
            return StartingStatus.ERROR;
        String link = "http://adrax.pythonanywhere.com/ch_dely";
        String rc = requester.httpPostRequestVar(link,
                "hash", hash, "dely_id", order.getId(), "courier", login);

        if (rc.equalsIgnoreCase(ORDER_STARTED))
            return StartingStatus.SUCCESS;
        else if (rc.equalsIgnoreCase(ORDER_BUSY))
            return StartingStatus.FAILURE;
        else if (rc.equalsIgnoreCase(ORDER_TOO_MANY))
            return StartingStatus.TOO_MANY;
        return StartingStatus.ERROR;
    }

    /** Начать исполнять один из заказов
     *
     * @param id идентификатор заказа на сервере
     * @return истина заказ начат, ложь произошла ошибка
     */
    public StartingStatus orderStart(Integer id) {

        String link = "http://adrax.pythonanywhere.com/ch_dely";
        String rc = requester.httpPostRequestVar(link,
                "hash", hash, "dely_id", id.toString(), "courier", login);

        if (rc.equalsIgnoreCase(ORDER_STARTED))
            return StartingStatus.SUCCESS;
        else if (rc.equalsIgnoreCase(ORDER_BUSY))
            return StartingStatus.FAILURE;
        else if (rc.equalsIgnoreCase(ORDER_TOO_MANY))
            return StartingStatus.TOO_MANY;
        return StartingStatus.ERROR;
    }

    /** Закончить исполнение заказа
     * 
     * @param order объект класса DeliveryOrder, который представляет из себя заказ
     * @return истина заказ pfdthity, ложь произошла ошибка
     */
    public Boolean orderFinish(DeliveryOrder order) {
        if (null == order)
            return Boolean.FALSE;
        String link = "http://adrax.pythonanywhere.com/delivered";
        String rc = requester.httpPostRequestVar(link, "hash", hash, "dely_id", order.getId());

        if (rc.equalsIgnoreCase(ORDER_STARTED))
            return Boolean.TRUE;
        else if (rc.equalsIgnoreCase(ORDER_BUSY))
            return Boolean.FALSE;
        return Boolean.FALSE;
    }

    /** Закончить исполнение заказа
     *
     * @param id идентификатор заказа на сервере
     * @return истина заказ pfdthity, ложь произошла ошибка
     */
    public Boolean orderFinish(Integer id) {
        String link = "http://adrax.pythonanywhere.com/delivered";
        String rc = requester.httpPostRequestVar(link, "hash", hash, "dely_id", id.toString());

        if (rc.equalsIgnoreCase(ORDER_STARTED))
            return Boolean.TRUE;
        else if (rc.equalsIgnoreCase(ORDER_BUSY))
            return Boolean.FALSE;

        return Boolean.FALSE;
    }

    /** Подтвердить исполнение заказа
     * 
     * @param order заказ, требующий подтверждения
     * @return истина выполнение заказа подтверждено, ложь произошла ошибка
     */
    public Boolean orderAccept(DeliveryOrder order) {
        if (null == order)
            return Boolean.FALSE;
        String link = "http://adrax.pythonanywhere.com/delivery_done";
        String rc = requester.httpPostRequestVar(link, "hash", hash, "dely_id", order.getId());

        if (rc.equalsIgnoreCase(ORDER_OK))
            return Boolean.TRUE;
        else if (rc.equalsIgnoreCase(ORDER_ERROR))
            return Boolean.FALSE;
        return Boolean.FALSE;
    }

    /** Подтвердить исполнение заказа
     *
     * @param id идентификатор заказа на сервере
     * @return истина выполнение заказа подтверждено, ложь произошла ошибка
     */
    public Boolean orderAccept(Integer id) {
        String link = "http://adrax.pythonanywhere.com/delivery_done";
        String rc = requester.httpPostRequestVar(link, "hash", hash, "dely_id", id.toString());

        if (rc.equalsIgnoreCase(ORDER_OK))
            return Boolean.TRUE;
        else if (rc.equalsIgnoreCase(ORDER_ERROR))
            return Boolean.FALSE;

        return Boolean.FALSE;
    }

    /** Узнать статус заказа
     * 
     * @param order заказ, у которого следует узнать статус
     * @return возвращает один из статусов OrderStatus
     */
    public DeliveryOrderStatus orderStatus(DeliveryOrder order) {
        if (null == order)
            return DeliveryOrderStatus.ERROR;
        String link = "http://adrax.pythonanywhere.com/chosen";
        String rc = requester.httpPostRequestVar(link, "hash", hash, "dely_id", order.getId());

        if (rc.equalsIgnoreCase(ORDER_WAITING))
            return DeliveryOrderStatus.WAITING;
        else if (rc.equalsIgnoreCase(ORDER_DELIVERING))
            return DeliveryOrderStatus.DELIVERING;
        else if (rc.equalsIgnoreCase(ORDER_DELIVERED))
            return DeliveryOrderStatus.DELIVERED;
        else if (rc.equalsIgnoreCase(ORDER_DELIVERY_DONE))
            return DeliveryOrderStatus.DELIVERY_DONE;
        else if (rc.equalsIgnoreCase(ORDER_ERROR))
            return DeliveryOrderStatus.ERROR;
        else
            return DeliveryOrderStatus.ERROR;
    }

    /** Узнать статус заказа
     * 
     * @param id идентификатор заказа на сервере
     * @return возвращает один из статусов OrderStatus
     */
    public DeliveryOrderStatus orderStatus(Integer id) {
        String link = "http://adrax.pythonanywhere.com/chosen";
        String rc = requester.httpPostRequestVar(link, "hash", hash, "dely_id", id.toString());

        if (rc.equalsIgnoreCase(ORDER_WAITING))
            return DeliveryOrderStatus.WAITING;
        else if (rc.equalsIgnoreCase(ORDER_DELIVERING))
            return DeliveryOrderStatus.DELIVERING;
        else if (rc.equalsIgnoreCase(ORDER_DELIVERED))
            return DeliveryOrderStatus.DELIVERED;
        else if (rc.equalsIgnoreCase(ORDER_DELIVERY_DONE))
            return DeliveryOrderStatus.DELIVERY_DONE;
        else if (rc.equalsIgnoreCase(ORDER_ERROR))
            return DeliveryOrderStatus.ERROR;

        return DeliveryOrderStatus.ERROR;
    }

    /** Проверяет, авторизован ли пользователь
     *
     * @return истина или ложь
     */
    public Boolean alreadyLogined() {
        return !hash.equals("");
    }

    /** Логин пользователя
     *
     * @return логин пользователя
     */
    public String getLogin() {
        return login;
    }

    /** Запросить текущие заказы
     *
     * @return список текущих заказов либо null
     */
    public DeliveryOrder[] currentOrders() {
        String link = "http://adrax.pythonanywhere.com/current_orders";
        String rc = requester.httpPostRequestVar(link, "hash", hash);
        if (rc.equals(ORDER_ERROR))
            return null;

        return stringToOrders(rc);
    }

    /** Запросить текущие доставки (доставку на данный момент)
     *
     * @return список текущих доставок либо null
     */
    public DeliveryOrder currentDelivery() {
        String link = "http://adrax.pythonanywhere.com/current_delivery";
        String rc = requester.httpPostRequestVar(link, "hash", hash);
        if (rc.equals(ORDER_ERROR))
            return null;

        DeliveryOrder[] ret = stringToOrders(rc);
        if (null == ret)
            return null;
        else if (0 == ret.length)
            return null;
        else
            return ret[0];
    }
}