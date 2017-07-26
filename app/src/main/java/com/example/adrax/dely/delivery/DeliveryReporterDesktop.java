/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.adrax.dely.delivery;

/**
 *
 * @author Максим
 */
@Deprecated
public class DeliveryReporterDesktop implements IDeliveryReporter {
    
    /** Получить номер строки
    * @return возвращает текущую строку кода
    */
    private String getDebugInfo() {
        StackTraceElement st = Thread.currentThread().getStackTrace()[3];
        return st.getClassName() + ", "
                + st.getFileName() + ", "
                + st.getLineNumber() + ": ";
    }
    
    @Override
    public void reportError(String Message)
    {
        System.out.println(getDebugInfo() + Message);
    }
}
