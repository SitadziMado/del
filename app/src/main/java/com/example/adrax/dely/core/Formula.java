package com.example.adrax.dely.core;

/**
 * Создано Максимом Сунцевым 18.08.2017.
 */

public class Formula {
    private Formula() {

    }

    public static Formula getDefault() {
        return defaultFormula;
    }

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

        return pricesMatrix[distanceIndex][weightIndex];
    }

    private static final Double[][] pricesMatrix = {
            { 50., 90., 170., 270., 420. },
            { 60., 100., 180., 290., 430. },
            { 70., 110., 190., 310., 450. },
            { 80., 130., 210., 330., 480. },
            { 100., 150., 230., 350., 500. },
    };

    private static Formula defaultFormula = new Formula();
}
