package com.example.ceubetjava.slot77.model;

/**
 * Representa um símbolo do caça-níquel com suas propriedades
 */
public class SlotSymbol {
    private String symbol;
    private int weight;
    private int multiplier;
    private int color;

    public SlotSymbol(String symbol, int weight, int multiplier, int color) {
        this.symbol = symbol;
        this.weight = weight;
        this.multiplier = multiplier;
        this.color = color;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getWeight() {
        return weight;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public int getColor() {
        return color;
    }
} 