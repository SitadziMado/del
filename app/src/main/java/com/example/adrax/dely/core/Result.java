package com.example.adrax.dely.core;

import android.support.annotation.Nullable;

import org.jetbrains.annotations.Contract;

/**
 * Создан Максимом Сунцевым 21.08.2017.
 */

public class Result<TData> {
    public Result(Boolean isSuccessful, TData data) {
        if (isSuccessful) {
            init(RequestStatus.ORDER_OK);
        } else {
            init(RequestStatus.ORDER_ERROR);
        }

        m_data = data;
    }

    public Result(String statusString, TData data) {
        init(requestStatusFromString(statusString));
        m_data = data;
    }

    /**
     * Копирующий конструктор для копии из любой другой конкретизации класса.
     * @param other другая конкретизация класса.
     * @param data новые даннные.
     */
    public Result(Result<?> other, TData data) {
        m_status = other.m_status;
        m_message = other.m_message;
        m_successful = other.m_successful;
        m_data = data;
    }

    @Contract(pure = true)
    public static Boolean getDefaultSuccessful(RequestStatus status) {
        Boolean result = Boolean.FALSE;

        switch (status) {
            case OTHER:
            case LOGIN_REGISTERED:
            case ORDER_LOADED:
            case ORDER_STARTED:
            case ORDER_OK:
                result = Boolean.TRUE;
        }

        return result;
    }

    @Contract(pure = true)
    public static String getDefaultMessage(RequestStatus status) {
        switch (status) {
            case OTHER:
                return "Объект успешно создан.";
                
            case INCORRECT_AUTHORIZATION_DATA:
                return "Некорректные данные для авторизации.";
                
            case SERVER_PROBLEMS:
                return "Проблемы с сервером.";
                
            case REQUEST_INCORRECT:
                return "Некорректный запрос.";
                
            case LOGIN_REGISTERED:
                return "Регистрация прошла успешно.";
                
            case LOGIN_ALREADY_TAKEN:
                return "Имя пользователя уже занято.";
                
            case ORDER_LOADED:
                return "Заказ оформлен.";
                
            case ORDER_TOO_MANY:
                return "Слишком много доставок.";
                
            case ORDER_STARTED:
                return "Заказ начат.";
                
            case ORDER_BUSY:
                return "Похоже, заказ уже начат.";

            case ORDER_OK:
                return "Действие успешно.";

            case ORDER_ERROR:
                return "Произошла ошибка.";

            case ORDER_NO_DATA:
                return "Отсутствует информация для представления.";

            case ACCESS_ERROR:
                return "Ошибка доступа.";

            case IO_ERROR:
                return "Ошибка ввода-вывода, возможно, " +
                        "остутствуют один или несколько параметров запроса.";

            case URL_ERROR:
                return "Некорректный URL-адрес запроса.";

            case SYNC_ERROR:
                return "При синхронизации заказов произошла ошибка.";

            default:
                return "Неизвестный код статуса.";
        }
    }

    static OrderStatus statusFromString(String statusString) {
        if (statusString == null) {
            statusString = "";
        }

        switch (statusString.toLowerCase()) {
            case Result.WAITING:
                return OrderStatus.WAITING;

            case Result.DELIVERING:
                return OrderStatus.DELIVERING;

            case Result.DELIVERED:
                return OrderStatus.DELIVERED;

            case Result.DELIVERY_DONE:
                return OrderStatus.DELIVERY_DONE;

            case Result.ERROR:
                return OrderStatus.ERROR;

            default:
                LogHelper.error("Неизвестный статус заказа.");
                return OrderStatus.ERROR;
        }
    }

    private static RequestStatus requestStatusFromString(@Nullable String status) {
        if (status == null) {
            status = "";
        }

        switch (status.toLowerCase())
        {
            case INCORRECT_AUTHORIZATION_DATA:
                return RequestStatus.INCORRECT_AUTHORIZATION_DATA;
            case SERVER_PROBLEMS:
                return RequestStatus.SERVER_PROBLEMS;
            case INCORRECT_REQUEST:
                return RequestStatus.REQUEST_INCORRECT;
            case LOGIN_REGISTERED:
                return RequestStatus.LOGIN_REGISTERED;
            case LOGIN_ALREADY_TAKEN:
                return RequestStatus.LOGIN_ALREADY_TAKEN;
            case LOADED:
                return RequestStatus.ORDER_LOADED;
            case ERROR:
                return RequestStatus.ORDER_ERROR;
            case STARTED:
                return RequestStatus.ORDER_STARTED;
            case BUSY:
                return RequestStatus.ORDER_BUSY;
            case TOO_MANY:
                return RequestStatus.ORDER_TOO_MANY;
            case OK:
                return RequestStatus.ORDER_OK;
            case NO_DATA:
                return RequestStatus.ORDER_NO_DATA;
            case ACCESS_ERROR:
                return RequestStatus.ACCESS_ERROR;
            case IO_ERROR:
                return RequestStatus.IO_ERROR;
            case URL_ERROR:
                return RequestStatus.URL_ERROR;
            case SYNC_ERROR:
                return RequestStatus.SYNC_ERROR;
            default:
                LogHelper.warn("Неизвестный код возврата с сервера.");
                return RequestStatus.OTHER;
        }
    }

    public Boolean isSuccessful() {
        return m_successful;
    }

    public void setSuccessful(Boolean flag) {
        m_successful = flag;
    }

    public String getMessage() {
        return m_message;
    }

    public void setMessage(String message) {
        m_message = message;
    }

    public TData getData() {
        return m_data;
    }

    public void setData(TData data) {
        m_data = data;
    }

    RequestStatus getStatus() {
        return m_status;
    }

    void setStatus(RequestStatus status) {
        m_status = status;
    }

    private void init(RequestStatus status) {
        m_status = status;
        m_message = getDefaultMessage(status);
        m_successful = getDefaultSuccessful(status);
    }

    /// Константы авторизации
    static final String INCORRECT_AUTHORIZATION_DATA = "incorrect_auth";    /** Некорректная инфа для авторизации */
    static final String INCORRECT_REQUEST = "405";                          /** Тоже какая-то ошибка */
    static final String SERVER_PROBLEMS = "login_error";                    /** Сервак гуфнулся... */

    /// Константы регистрации
    private static final String LOGIN_REGISTERED = "registered";            /** Регистрация прошла успешно */
    private static final String LOGIN_ALREADY_TAKEN = "already_taken";      /** Логин занят */

    /// Константы заказов
    static final String LOADED = "loaded";		          /** Новый заказ успешно создан */
    static final String ERROR = "server_error";           /** Ошибка отправки нового заказа на сервер */
    static final String WAITING = "waiting";              /** Статусы заказов ниже */
    static final String DELIVERING = "delivering";        /** В процессе... */
    static final String DELIVERED = "delivered";          /** Вроде бы доставлено */
    static final String DELIVERY_DONE = "delivery_done";  /** Абсолютный суккесс */
    static final String STARTED = "delivery_started";     /** Всё хорошо, доставка началась */
    static final String BUSY = "delivery_busy";           /** Пользователь - слоупок, рекомендуем обновиться до 10-ки */
    static final String OK = "ok";                        /** Заказ успешно подтверждён (обеими сторонами) */
    static final String TOO_MANY = "already_enough";      /** Только 1 заказ можно доставлять */
    static final String NO_DATA = "nodata_error";
    static final String ACCESS_ERROR = "access_error";
    static final String IO_ERROR = "io_error";
    static final String URL_ERROR = "url_error";
    static final String SYNC_ERROR = "404";

    private Boolean m_successful = false;
    private RequestStatus m_status;
    private String m_message;
    private TData m_data;
}
