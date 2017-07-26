/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.adrax.dely.delivery;

import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 *
 * @author Максим
 */
@Deprecated
public class DeliveryHttpRequester {
    /** Константы запросов
     * 
     */
    public static final String REQUEST_METHOD_GET = "GET";      /// Ну это и школьник знает
    public static final String REQUEST_METHOD_POST = "POST";    /// Надеюсь
    
    static IDeliveryReporter reporter;

    DeliveryHttpRequester(Context msgContext) {
        reporter = new DeliveryReporterAndroid(msgContext);
    }

    /** Преобразование входного потока в строку
     *
     * @param in входной поток
     * @return строка, содержащая данные со входного потока
     */
    private static String inputStreamToString(InputStream in) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) != -1)
                result.write(buffer, 0, length);
            return result.toString("UTF-8");
        }
        catch (IOException ex) {
            reporter.reportError("IOException: " + ex.getMessage());
            return "";
        }
    }

    private static Boolean checkParameterCount(int length) {
        return !(1 == (length % 2));
    }

    private static String concatenateParameters(String... params) {
        StringBuilder sb = new StringBuilder();

        // Если параметров больше двух, то парсим их
        if (params.length >= 2) {
            try {
                sb.append(URLEncoder.encode(params[0], "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(params[1], "UTF-8"));

                // Преобразуем всё к виду "param1=value1&param2=value2..."
                for (Integer i = 2; i < params.length; i += 2) {
                    sb.append("&");
                    sb.append(URLEncoder.encode(params[i], "UTF-8"));
                    sb.append("=");
                    sb.append(URLEncoder.encode(params[i + 1], "UTF-8"));
                }
            }
            catch (UnsupportedEncodingException ex) {
                reporter.reportError("UnsupportedEncodingException: " + ex.getMessage());
                return "";
            }
        }
        return sb.toString();
    }

    /** HTTP запрос
     * 
     * @param url адрес ресурса
     * @param data информация, т.е. параметры для запроса
     * @param method соответствующий метод, например, GET или POST
     * @return строка с ответом от вервера
     */
    public String httpRequest(URL url, String data, String method) {
        // Подключаем костыль
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Ответ от сервера
        String output = "";

        try {
            // URL, куда мы посылаем дату
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Настраиваем все тонкости
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod(method);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty( "Content-Length", String.valueOf(data.length()));
            urlConnection.setChunkedStreamingMode(0);

            // Отправляем запрос серверу
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(data.getBytes());
            out.flush();

            // Получаем ответ
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            output = inputStreamToString(in); // readStream(in);

            urlConnection.disconnect();

        }
        catch (IOException ex) {
            reporter.reportError("IOException: " + ex.getMessage());
        }
        return output;
    }
    
    /** HTTP POST запрос
     * 
     * @param url адрес ресурса
     * @param data информация, т.е. параметры для запроса
     * @return строка с ответом от вервера
     */
    @Deprecated
    public String httpPostRequest(URL url, String data) {
        // Подключаем костыль
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Ответ от сервера
        String output = "";

        try {
            // URL, куда мы посылаем дату
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Настраиваем все тонкости
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod(REQUEST_METHOD_POST);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty( "Content-Length", String.valueOf(data.length()));
            urlConnection.setChunkedStreamingMode(0);

            // Отправляем запрос серверу
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(data.getBytes());
            out.flush();

            // Получаем ответ
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            output = inputStreamToString(in); // readStream(in);

            urlConnection.disconnect();

        }
        catch (IOException ex) {
            reporter.reportError("IOException: " + ex.getMessage());
        }
        return output;
    }

    /** HTTP POST запрос
     *
     * @param link адрес ресурса
     * @param params информация, т.е. параметры для запроса: (...param1, value1, param2, value2...)
     * @return строка с ответом от вервера
     */
    public String httpPostRequestVar(String link, String... params) {
        // Подключаем костыль
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Ответ от сервера
        String output = "";
        String data;

        if (!checkParameterCount(params.length))
            return output;

        // Преобразуем список параметров в строку
        data = concatenateParameters(params);

        try {
            // URL, куда мы посылаем дату
            URL url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Настраиваем все тонкости
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod(REQUEST_METHOD_POST);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty( "Content-Length", String.valueOf(data.length()));
            urlConnection.setChunkedStreamingMode(0);

            // Отправляем запрос серверу
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(data.getBytes());
            out.flush();

            // Получаем ответ
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            output = inputStreamToString(in); // readStream(in);

            urlConnection.disconnect();

        }
        catch (IOException ex) {
            reporter.reportError("IOException: " + ex.getMessage());
        }
        return output;
    }

    public DeliveryAsyncTask httpAsyncPostRequest(String link, String... params) {
        // Ответ от сервера
        String output = "";
        String data;

        if (!checkParameterCount(params.length))
            return null;

        // Преобразуем список параметров в строку
        data = concatenateParameters(params);

        DeliveryAsyncTask task = new DeliveryAsyncTask();
        task.execute(data);
        return task;
    }

    class DeliveryAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return httpPostRequest(params[0], params[1]);
        }

        /** HTTP POST запрос
         *
         * @param _url адрес ресурса
         * @param data информация, т.е. параметры для запроса
         * @return строка с ответом от вервера
         */
        private String httpPostRequest(String _url, String data) {
            // Подключаем костыль
            // StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            // StrictMode.setThreadPolicy(policy);

            // Ответ от сервера
            String output = "";

            try {
                // URL, куда мы посылаем дату
                URL url = new URL(_url);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                // Настраиваем все тонкости
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod(REQUEST_METHOD_POST);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty( "Content-Length", String.valueOf(data.length()));
                urlConnection.setChunkedStreamingMode(0);

                // Отправляем запрос серверу
                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                out.write(data.getBytes());
                out.flush();

                // Получаем ответ
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                output = inputStreamToString(in); // readStream(in);

                urlConnection.disconnect();

            }
            catch (MalformedURLException ex) {
                reporter.reportError("MalformedURLException: " + ex.getMessage());
            }
            catch (IOException ex) {
                reporter.reportError("IOException: " + ex.getMessage());
            }
            return output;
        }
    }
}