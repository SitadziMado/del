/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.adrax.dely.delivery;

import android.content.Context;

/**
 *
 * @author Максим
 */
@Deprecated
public class DeliveryReporterAndroid implements IDeliveryReporter {

    private Context context;

    DeliveryReporterAndroid(Context _context) {
        context = _context;
    }

    /** Получить номер строки
    * @return возвращает текущую строку кода
    */
    private String getDebugInfo() {
        return Thread.currentThread().getStackTrace()[2].getClassName() + ", "
                + Thread.currentThread().getStackTrace()[2].getFileName() + ", "
                + Thread.currentThread().getStackTrace()[2].getLineNumber() + ": ";
    }
    
    @Override
    public void reportError(String Message)
    {
        /// Откомменти для ведра
        /*Toast.makeText(getBaseContext(),
                            getDebugInfo() + Message,
                            Toast.LENGTH_LONG).show();*/
    }
}
