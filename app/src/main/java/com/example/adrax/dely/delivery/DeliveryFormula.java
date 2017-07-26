package com.example.adrax.dely.delivery;

@Deprecated
public final class DeliveryFormula {

    private static final int[] coefficients = new int[] { 15, 10, 5 };
    private static final int[] freeTerms = new int[] { 80, 125, 155 };
    private static final int[] distances = new int[] { 3, 6, Integer.MAX_VALUE };
    private static DeliveryFormula ourInstance = new DeliveryFormula();

    public static DeliveryFormula getInstance() {
        return ourInstance;
    }

    public Integer calculate(Integer distance) {
        Integer rv = Integer.MAX_VALUE;
        for (int i = 0; i < distances.length; ++i) {
            if (distance <= distances[i]) {
                rv = coefficients[i] * distance + freeTerms[i];
            }
        }
        return rv;
    }

    private DeliveryFormula() {
    }
}