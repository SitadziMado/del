package com.example.adrax.dely.core;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

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
import java.nio.charset.Charset;

class InternetTask extends AsyncTask<String, Void, String> {
    InternetTask(
            @NonNull String method,
            @NonNull String address,
            @NonNull InternetCallback<String> callable
    ) {
        if (address.equals("")) {
            String m = "Адрес не может быть пустым.";
            LogHelper.error(m);
            throw new IllegalArgumentException(m);
        }

        m_method = method;
        m_address = address;
        m_callback  = callable;
    }

    @Override
    protected String doInBackground(String... params) {
        String result = null;
        HttpURLConnection urlConnection = null;
        try {
            String data = concatenateParameters(params);

            URL url;

            if (false) {// (m_method.equals(METHOD_POST)) {
                url = new URL(m_address);
            } else {
                url = new URL(m_address + "?" + data);
            }

            urlConnection = (HttpURLConnection)url.openConnection();

            urlConnection.setRequestMethod(m_method);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Content-Length", String.valueOf(data.length()));

            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);

            if (m_method.equals(METHOD_POST)) {
                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                out.write(data.getBytes(Charset.forName("UTF-8")));
                out.flush();
            }

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            result = readStream(in);

            if (result.length() < 32) {
                LogHelper.verbose("Запрос " + m_address + " успешен: `" + result + "`.");
            } else {
                LogHelper.verbose(
                        "Запрос " + m_address + " успешен: `" + result.subSequence(0, 28) + "...`."
                );
            }
        } catch (MalformedURLException e) {
            LogHelper.error(m_address + " некорректен.");
            result = User.URL_ERROR;
        } catch (IOException e) {
            LogHelper.error("Ошибка соединения с " + m_address + ".");
            result = User.IO_ERROR;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        m_callback.call(s);
    }

    private String readStream(InputStream in)
            throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) != -1)
                result.write(buffer, 0, length);
            return result.toString("UTF-8");
        } catch (IOException e) {
            LogHelper.error("Ошибка при чтении потока");
            throw e;
        }
    }

    @NonNull
    private static String concatenateParameters(@NonNull String... params)
            throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        boolean first = true;

        // Если параметров больше двух, то парсим их
        if (params.length >= 2) {
            try {
                // Преобразуем всё к виду "param1=value1&param2=value2..."
                for (int i = 0; i < params.length; i += 2) {
                    String key = params[i];
                    String value = params[i + 1];

                    if ((key != null) &&
                        (value != null) &&
                        !key.equals("") /*&&
                        !value.equals("") */ ) {
                        if (!first) {
                            sb.append("&");
                        }
                        sb.append(URLEncoder.encode(params[i], "UTF-8"));
                        sb.append("=");
                        sb.append(URLEncoder.encode(params[i + 1], "UTF-8"));
                        first = false;
                    }
                }
            } catch (UnsupportedEncodingException e) {
                LogHelper.error("Неподдерживаемая кодировка");
                throw e;
            }
        }
        return sb.toString();
    }

    static final String METHOD_POST = "POST";
    static final String METHOD_GET = "GET";

    private String m_method = null;
    private String m_address = null;
    private InternetCallback<String> m_callback = null;
}
