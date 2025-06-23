package com.example.ceubetjava.blackjack.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe que representa um baralho de cartas
 */
public class Deck {
    private List<Card> cards;
    private int currentCardIndex;
    
    /**
     * Construtor que cria um baralho com o número especificado de jogos de cartas
     * @param numDecks número de jogos de cartas (4-6 para Blackjack)
     */
    public Deck(int numDecks) {
        cards = new ArrayList<>();
        
        // Adiciona o número especificado de jogos de cartas
        for (int d = 0; d < numDecks; d++) {
            for (int s = 0; s < 4; s++) {
                for (int v = 1; v <= 13; v++) {
                    cards.add(new Card(v, s));
                }
            }
        }
        
        shuffle();
    }
    
    /**
     * Embaralha o baralho
     */
    public void shuffle() {
        Collections.shuffle(cards);
        currentCardIndex = 0;
    }
    
    /**
     * Retira uma carta do topo do baralho
     * @return a próxima carta do baralho
     */
    public Card dealCard() {
        if (currentCardIndex >= cards.size()) {
            // Se não houver mais cartas, embaralha novamente
            shuffle();
        }
        
        return cards.get(currentCardIndex++);
    }
    
    /**
     * Verifica se é necessário embaralhar (quando restam menos de 25% das cartas)
     * @return true se for necessário embaralhar
     */
    public boolean needsShuffle() {
        return currentCardIndex >= (cards.size() * 0.75);
    }
    
    /**
     * Obtém o número de cartas restantes no baralho
     * @return número de cartas restantes
     */
    public int getRemainingCards() {
        return cards.size() - currentCardIndex;
    }
}
