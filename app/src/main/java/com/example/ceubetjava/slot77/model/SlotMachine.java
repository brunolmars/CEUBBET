package com.example.ceubetjava.slot77.model;

import java.util.Random;

/**
 * Classe principal que gerencia a lógica do caça-níquel
 */
public class SlotMachine {
    private static final long MIN_BET = 1;
    private final Random random;
    private long currentCredits;
    private long currentBet;
    private boolean isSpinning;

    public SlotMachine(long initialCredits) {
        this.random = new Random();
        this.currentCredits = initialCredits;
        this.currentBet = MIN_BET;
        this.isSpinning = false;
    }

    /**
     * Realiza um giro no caça-níquel
     * @return SpinResult com o resultado do giro
     * @throws IllegalStateException se não houver créditos suficientes ou se já estiver girando
     */
    public SpinResult spin() {
        if (isSpinning) {
            throw new IllegalStateException("O caça-níquel já está girando!");
        }
        if (currentCredits < currentBet) {
            throw new IllegalStateException("Créditos insuficientes!");
        }

        isSpinning = true;
        currentCredits -= currentBet;

        String[] results = new String[5];
        for (int i = 0; i < 5; i++) {
            results[i] = spinReel();
        }

        SpinResult result = new SpinResult(results, currentBet);
        if (result.isWin()) {
            currentCredits += result.getPrize();
        }

        isSpinning = false;
        return result;
    }

    private String spinReel() {
        SlotSymbol[] symbols = PayTable.getSymbols();
        int totalWeight = PayTable.getTotalWeight();
        
        int randomValue = random.nextInt(totalWeight);
        int currentSum = 0;
        
        for (SlotSymbol symbol : symbols) {
            currentSum += symbol.getWeight();
            if (randomValue < currentSum) {
                return symbol.getSymbol();
            }
        }
        
        return symbols[symbols.length - 1].getSymbol();
    }

    public long getCurrentCredits() {
        return currentCredits;
    }

    public long getCurrentBet() {
        return currentBet;
    }

    public boolean isSpinning() {
        return isSpinning;
    }

    public void setCurrentBet(long bet) {
        if (bet < MIN_BET) {
            throw new IllegalArgumentException("Aposta mínima é " + MIN_BET);
        }
        if (bet > currentCredits) {
            throw new IllegalArgumentException("Aposta maior que os créditos disponíveis");
        }
        this.currentBet = bet;
    }

    public void addCredits(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Não é possível adicionar créditos negativos");
        }
        this.currentCredits += amount;
    }

    public static long getMinBet() {
        return MIN_BET;
    }

    public void setCredits(long credits) {
        if (credits < 0) {
            throw new IllegalArgumentException("Não é possível definir créditos negativos");
        }
        this.currentCredits = credits;
    }
} 