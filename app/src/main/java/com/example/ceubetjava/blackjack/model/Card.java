package com.example.ceubetjava.blackjack.model;

/**
 * Classe que representa uma carta do baralho
 */
public class Card {
    // Constantes para os naipes
    public static final int OUROS = 0;
    public static final int PAUS = 1;
    public static final int COPAS = 2;
    public static final int ESPADAS = 3;
    
    // Constantes para os valores especiais
    public static final int ACE = 1;
    public static final int JACK = 11;
    public static final int QUEEN = 12;
    public static final int KING = 13;
    
    private int suit; // naipe
    private int value; // valor (1-13)
    private boolean faceUp; // se a carta está virada para cima
    
    /**
     * Construtor da carta
     * @param value valor da carta (1-13)
     * @param suit naipe da carta (0-3)
     */
    public Card(int value, int suit) {
        this.value = value;
        this.suit = suit;
        this.faceUp = true; // por padrão, a carta está virada para cima
    }
    
    /**
     * Obtém o valor da carta
     * @return valor da carta
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Obtém o naipe da carta
     * @return naipe da carta
     */
    public int getSuit() {
        return suit;
    }
    
    /**
     * Verifica se a carta está virada para cima
     * @return true se estiver virada para cima, false caso contrário
     */
    public boolean isFaceUp() {
        return faceUp;
    }
    
    /**
     * Define se a carta está virada para cima ou para baixo
     * @param faceUp true para virar para cima, false para virar para baixo
     */
    public void setFaceUp(boolean faceUp) {
        this.faceUp = faceUp;
    }
    
    /**
     * Obtém o valor da carta no jogo de Blackjack
     * @return valor da carta no Blackjack
     */
    public int getBlackjackValue() {
        if (value == ACE) {
            return 11; // Ás vale 11 por padrão (será tratado como 1 quando necessário)
        } else if (value >= 10) {
            return 10; // J, Q, K e 10 valem 10
        } else {
            return value; // Outras cartas valem seu valor nominal
        }
    }
    
    /**
     * Obtém o nome do recurso da imagem da carta
     * @return nome do recurso da imagem
     */
    public String getImageResourceName() {
        String suitName;
        String valueName;
        
        // Determina o nome do naipe
        switch (suit) {
            case OUROS:
                suitName = "ouros";
                break;
            case PAUS:
                suitName = "paus";
                break;
            case COPAS:
                suitName = "copas";
                break;
            case ESPADAS:
                suitName = "espadas";
                break;
            default:
                suitName = "";
        }
        
        // Determina o nome do valor
        if (value == ACE) {
            valueName = "A";
        } else if (value == JACK) {
            valueName = "J";
        } else if (value == QUEEN) {
            valueName = "Q";
        } else if (value == KING) {
            valueName = "K";
        } else {
            valueName = String.valueOf(value);
        }
        
        // Retorna o nome do recurso
        return "carta_" + valueName + "_" + suitName;
    }
    
    /**
     * Retorna uma representação em string da carta
     */
    @Override
    public String toString() {
        String valueStr;
        String suitStr;
        
        // Determina a string do valor
        switch (value) {
            case ACE:
                valueStr = "Ás";
                break;
            case JACK:
                valueStr = "Valete";
                break;
            case QUEEN:
                valueStr = "Dama";
                break;
            case KING:
                valueStr = "Rei";
                break;
            default:
                valueStr = String.valueOf(value);
        }
        
        // Determina a string do naipe
        switch (suit) {
            case OUROS:
                suitStr = "Ouros";
                break;
            case PAUS:
                suitStr = "Paus";
                break;
            case COPAS:
                suitStr = "Copas";
                break;
            case ESPADAS:
                suitStr = "Espadas";
                break;
            default:
                suitStr = "";
        }
        
        return valueStr + " de " + suitStr;
    }
}
