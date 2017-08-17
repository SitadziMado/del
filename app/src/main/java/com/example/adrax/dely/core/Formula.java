package com.example.adrax.dely.core;

/**
 * Создано Максимом Сунцевым 18.08.2017.
 */

public class Formula {
    private Formula() {

    }

    /**
     * Возвращает формулу по умолчанию.
     * @return инстанс формулы.
     */
    public static Formula getDefault() {
        return defaultFormula;
    }

    /**
     * Рассчитать стоимость заказа с помощью расстояния и веса.
     * @param weight вес заказа.
     * @param distance дистанция между пунктами А и Б.
     * @return стоимость заказа.
     */
    public Double calculate(Double weight, Double distance) {
        int weightIndex;
        int distanceIndex;

        if (weight <= 500) {
            weightIndex = 0;
        } else if (weight <=  1000) {
            weightIndex = 1;
        } else if (weight <=  2000) {
            weightIndex = 2;
        } else if (weight <=  5000) {
            weightIndex = 3;
        } else {
            weightIndex = 4;
        }

        if (distance <= 5000) {
            distanceIndex = 0;
        } else if (distance <= 10000) {
            distanceIndex = 1;
        } else if (distance <= 15000) {
            distanceIndex = 2;
        } else if (distance <= 20000) {
            distanceIndex = 3;
        } else {
            distanceIndex = 4;
        }

        return
                m_pricesMatrix[distanceIndex][weightIndex] *
                m_warrantyOnCost *
                m_warrantyOnRepayment;
    }

    private final Double[][] m_pricesMatrix = {
            { 50., 90., 170., 270., 420. },
            { 60., 100., 180., 290., 430. },
            { 70., 110., 190., 310., 450. },
            { 80., 130., 210., 330., 480. },
            { 100., 150., 230., 350., 500. },
    };
    private final Double m_warrantyOnCost = 1.009;
    private final Double m_warrantyOnRepayment = 1.009;

    private static Formula defaultFormula = new Formula();
}
