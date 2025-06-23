package com.example.ceubetjava.slot77.model;

/**
 * Representa o resultado de um giro no caça-níquel
 */
public class SpinResult {
    private final String[] reelResults;
    private final long prize;
    private final String message;
    private final boolean isWin;

    public SpinResult(String[] reelResults, long bet) {
        this.reelResults = reelResults;
        this.prize = PayTable.calculatePrize(reelResults, bet);
        this.message = PayTable.getPrizeMessage(prize, bet);
        this.isWin = prize > 0;
    }

    public String[] getReelResults() {
        return reelResults;
    }

    public long getPrize() {
        return prize;
    }

    public String getMessage() {
        return message;
    }

    public boolean isWin() {
        return isWin;
    }
} 