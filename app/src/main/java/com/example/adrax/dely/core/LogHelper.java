package com.example.adrax.dely.core;

import android.util.Log;

/**
 * Created by Максим on 27.07.2017.
 */

public class LogHelper {
    static void log(String message) {
        Log.d(getDebugInfo(), message);
    }

    /** Получить номер строки
     * @return возвращает текущую строку кода
     */
    private static String getDebugInfo() {
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        return ste[2].getClassName() + ", "
                + ste[2].getFileName() + ", "
                + ste[2].getLineNumber() + ": ";
    }
}
