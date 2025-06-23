package com.example.ceubetjava.blackjack.model;

/**
 * Classe que representa o dealer (banca) no jogo de Blackjack
 */
public class Dealer {
    private Hand hand;
    
    /**
     * Construtor do dealer
     */
    public Dealer() {
        this.hand = new Hand(true);
    }
    
    /**
     * Obtém a mão do dealer
     * @return mão do dealer
     */
    public Hand getHand() {
        return hand;
    }
    
    /**
     * Limpa a mão do dealer para uma nova rodada
     */
    public void clearHand() {
        hand.clear();
    }
    
    /**
     * Verifica se o dealer deve pedir mais cartas
     * @return true se o dealer deve pedir mais cartas
     */
    public boolean shouldHit() {
        // Dealer deve pedir cartas até atingir 17 ou mais
        return hand.getValue() < 17;
    }
    
    /**
     * Verifica se a carta visível do dealer é um Ás
     * @return true se a carta visível for um Ás
     */
    public boolean hasAceShowing() {
        if (hand.getCards().isEmpty()) {
            return false;
        }
        
        return hand.getCards().get(0).getValue() == Card.ACE;
    }
    
    /**
     * Revela a segunda carta do dealer (que estava virada para baixo)
     */
    public void revealHoleCard() {
        if (hand.getCards().size() >= 2) {
            hand.getCards().get(1).setFaceUp(true);
        }
    }
}
