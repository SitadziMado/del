package com.example.adrax.dely.core;

import android.util.Log;

/**
 * Created by Максим on 27.07.2017.
 */

public class LogHelper {
    public static void verbose(String message) {
        Log.v(getDebugInfo(), message);
    }

    public static void error(String message) {
        Log.e(getDebugInfo(), message);
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
