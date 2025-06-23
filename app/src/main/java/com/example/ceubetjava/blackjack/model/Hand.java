package com.example.ceubetjava.blackjack.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma mão de cartas (jogador ou dealer)
 */
public class Hand {
    private List<Card> cards;
    private boolean isDealer;
    
    /**
     * Construtor da mão
     * @param isDealer indica se esta mão pertence ao dealer
     */
    public Hand(boolean isDealer) {
        cards = new ArrayList<>();
        this.isDealer = isDealer;
    }
    
    /**
     * Adiciona uma carta à mão
     * @param card carta a ser adicionada
     */
    public void addCard(Card card) {
        cards.add(card);
    }
    
    /**
     * Limpa todas as cartas da mão
     */
    public void clear() {
        cards.clear();
    }
    
    /**
     * Obtém a lista de cartas na mão
     * @return lista de cartas
     */
    public List<Card> getCards() {
        return cards;
    }
    
    /**
     * Obtém o valor da mão no Blackjack
     * @return valor da mão
     */
    public int getValue() {
        int value = 0;
        int aces = 0;
        
        // Soma o valor de todas as cartas
        for (Card card : cards) {
            if (card.getValue() == Card.ACE) {
                aces++;
                value += 11; // Inicialmente, considera Ás como 11
            } else {
                value += card.getBlackjackValue();
            }
        }
        
        // Ajusta o valor dos Ases se necessário
        while (value > 21 && aces > 0) {
            value -= 10; // Converte um Ás de 11 para 1
            aces--;
        }
        
        return value;
    }
    
    /**
     * Verifica se a mão tem um Blackjack (21 com duas cartas)
     * @return true se for um Blackjack
     */
    public boolean isBlackjack() {
        return cards.size() == 2 && getValue() == 21;
    }
    
    /**
     * Verifica se a mão estourou (valor > 21)
     * @return true se estourou
     */
    public boolean isBusted() {
        return getValue() > 21;
    }
    
    /**
     * Verifica se a mão tem um par (duas cartas de mesmo valor)
     * @return true se tiver um par
     */
    public boolean isPair() {
        return cards.size() == 2 && 
               cards.get(0).getBlackjackValue() == cards.get(1).getBlackjackValue();
    }
    
    /**
     * Verifica se a mão tem um soft hand (contém um Ás contado como 11)
     * @return true se for um soft hand
     */
    public boolean isSoft() {
        boolean hasAce = false;
        int value = 0;
        
        for (Card card : cards) {
            if (card.getValue() == Card.ACE) {
                hasAce = true;
            }
            value += card.getBlackjackValue();
        }
        
        return hasAce && value <= 21;
    }
    
    /**
     * Verifica se a mão tem o padrão especial 6-7-8 do mesmo naipe
     * @return true se tiver o padrão 6-7-8 do mesmo naipe
     */
    public boolean has678SameSuit() {
        if (cards.size() != 3) return false;
        
        // Verifica se as cartas são 6, 7 e 8
        boolean has6 = false, has7 = false, has8 = false;
        int suit = -1;
        
        for (Card card : cards) {
            if (suit == -1) {
                suit = card.getSuit();
            } else if (suit != card.getSuit()) {
                return false; // Naipes diferentes
            }
            
            if (card.getValue() == 6) has6 = true;
            else if (card.getValue() == 7) has7 = true;
            else if (card.getValue() == 8) has8 = true;
        }
        
        return has6 && has7 && has8;
    }
    
    /**
     * Verifica se a mão tem três 7s
     * @return true se tiver três 7s
     */
    public boolean hasThreeSevens() {
        if (cards.size() != 3) return false;
        
        for (Card card : cards) {
            if (card.getValue() != 7) return false;
        }
        
        return true;
    }
    
    /**
     * Divide a mão em duas (para a opção de Split)
     * @return uma nova mão com a segunda carta
     */
    public Hand split() {
        if (cards.size() != 2) {
            throw new IllegalStateException("Só é possível dividir uma mão com duas cartas");
        }
        
        Hand newHand = new Hand(isDealer);
        newHand.addCard(cards.remove(1));
        return newHand;
    }
}
