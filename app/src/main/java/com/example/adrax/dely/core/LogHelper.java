package com.example.adrax.dely.core;

import android.text.TextUtils;
import android.util.Log;

public class LogHelper {
    public static void error(String msg) {
        Log.e("ERROR", getLocation() + msg);
    }

    public static void verbose(String msg) {
        Log.v("VERBOSE", getLocation() + msg);
    }

    public static void warn(String msg) {
        Log.w("WARN", getLocation() + msg);
    }

    private static String getLocation() {
        final String className = Log.class.getName();
        final StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        boolean found = false;

        for (int i = 0; i < traces.length; i++) {
            StackTraceElement trace = traces[i];

            try {
                if (found) {
                    if (!trace.getClassName().startsWith(className)) {
                        Class<?> clazz = Class.forName(trace.getClassName());
                        return "[" + getClassName(clazz) + ":" + trace.getMethodName() + ":" + trace.getLineNumber() + "]: ";
                    }
                }
                else if (trace.getClassName().startsWith(className)) {
                    found = true;
                }
            }
            catch (ClassNotFoundException e) {

            }
        }

        return "[]: ";
    }

    private static String getClassName(Class<?> clazz) {
        if (clazz != null) {
            if (!TextUtils.isEmpty(clazz.getSimpleName())) {
                return clazz.getSimpleName();
            }

            return getClassName(clazz.getEnclosingClass());
        }

        return "";
    }

    /** Получить номер строки
     * @return возвращает текущую строку кода
     */
    private static String getDebugInfo() {
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        return ste[3].getClassName() + ", "
                + ste[3].getFileName() + ", "
                + ste[3].getLineNumber() + ": ";
    }
}
