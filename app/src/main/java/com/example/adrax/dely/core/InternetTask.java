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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

class InternetTask extends AsyncTask<String, Void, String> {
    public InternetTask(String address, InternetCallback<String> callable) {
        if (callable == null || address == null || address.equals("")) {
            String m = "Адрес, callback-функция не могут быть пустыми.";
            LogHelper.log(m);
            throw new NullPointerException(m);
        }

        m_address = address;
        m_callback  = callable;
    }

    @Override
    protected String doInBackground(String... params) {
        String result = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(m_address);
            urlConnection = (HttpURLConnection)url.openConnection();

            String data = concatenateParameters(params);

            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Content-Length", String.valueOf(data.length()));

            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(data.getBytes(Charset.forName("UTF-8")));
            out.flush();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            result = readStream(in);

            Log.d(getClass().getName(), "Запрос " + m_address + " успешен.");
        } catch (MalformedURLException e) {
            LogHelper.log(m_address + " некорректен.");
            result = null;
        } catch (IOException e) {
            LogHelper.log("Ошибка соединения с " + m_address + ".");
            result = null;
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
            LogHelper.log("Ошибка при чтении потока");
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
                        !key.equals("") &&
                        !value.equals("")) {
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
                LogHelper.log("Неподдерживаемая кодировка");
                throw e;
            }
        }
        return sb.toString();
    }

    private String m_address = null;
    private InternetCallback<String> m_callback = null;
}
