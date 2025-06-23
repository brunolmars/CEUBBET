package com.example.ceubetjava.blackjack.model;

/**
 * Classe que representa uma aposta no jogo de Blackjack
 */
public class Bet {
    private int amount;
    private int insuranceAmount;
    private boolean surrendered;
    private boolean doubled;
    
    /**
     * Construtor da aposta
     * @param amount valor da aposta
     */
    public Bet(int amount) {
        this.amount = amount;
        this.insuranceAmount = 0;
        this.surrendered = false;
        this.doubled = false;
    }
    
    /**
     * Obtém o valor da aposta
     * @return valor da aposta
     */
    public int getAmount() {
        return amount;
    }
    
    /**
     * Define o valor da aposta
     * @param amount novo valor da aposta
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    /**
     * Dobra a aposta
     */
    public void doubleBet() {
        this.amount *= 2;
        this.doubled = true;
    }
    
    /**
     * Verifica se a aposta foi dobrada
     * @return true se a aposta foi dobrada
     */
    public boolean isDoubled() {
        return doubled;
    }
    
    /**
     * Faz um seguro da aposta
     * @param insuranceAmount valor do seguro
     */
    public void insure(int insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }
    
    /**
     * Obtém o valor do seguro
     * @return valor do seguro
     */
    public int getInsuranceAmount() {
        return insuranceAmount;
    }
    
    /**
     * Marca a aposta como rendida (surrender)
     */
    public void surrender() {
        this.surrendered = true;
    }
    
    /**
     * Verifica se a aposta foi rendida
     * @return true se a aposta foi rendida
     */
    public boolean isSurrendered() {
        return surrendered;
    }
    
    /**
     * Calcula o pagamento para uma vitória normal (1:1)
     * @return valor do pagamento
     */
    public int getRegularWinPayout() {
        return amount;
    }
    
    /**
     * Calcula o pagamento para um Blackjack (3:2)
     * @return valor do pagamento
     */
    public int getBlackjackPayout() {
        return (int)(amount * 1.5);
    }
    
    /**
     * Calcula o pagamento para um prêmio especial (3:1)
     * @return valor do pagamento
     */
    public int getSpecialPayout() {
        return amount * 3;
    }
    
    /**
     * Calcula o pagamento do seguro (2:1)
     * @return valor do pagamento do seguro
     */
    public int getInsurancePayout() {
        return insuranceAmount * 2;
    }
    
    /**
     * Calcula o valor perdido em caso de rendição (metade da aposta)
     * @return valor perdido
     */
    public int getSurrenderLoss() {
        return amount / 2;
    }
}
