package com.example.adrax.dely.delivery;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Максим
 */
@Deprecated
public interface IDeliveryReporter {
    
    /**
     * 
     * @param message послание, которое требуется вывести
     */
    public abstract void reportError(String message);
}
