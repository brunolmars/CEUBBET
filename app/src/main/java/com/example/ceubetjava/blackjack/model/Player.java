package com.example.ceubetjava.blackjack.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um jogador no jogo de Blackjack
 */
public class Player {
    private String name;
    private int chips;
    private List<Hand> hands;
    private List<Bet> bets;
    
    /**
     * Construtor do jogador
     * @param name nome do jogador
     * @param initialChips quantidade inicial de fichas
     */
    public Player(String name, int initialChips) {
        this.name = name;
        this.chips = initialChips;
        this.hands = new ArrayList<>();
        this.bets = new ArrayList<>();
        
        // Inicializa com uma mão vazia
        hands.add(new Hand(false));
        bets.add(new Bet(0));
    }
    
    /**
     * Obtém o nome do jogador
     * @return nome do jogador
     */
    public String getName() {
        return name;
    }
    
    /**
     * Obtém a quantidade de fichas do jogador
     * @return quantidade de fichas
     */
    public int getChips() {
        return chips;
    }
    
    /**
     * Define a quantidade de fichas do jogador
     * @param chips nova quantidade de fichas
     */
    public void setChips(int chips) {
        this.chips = chips;
    }
    
    /**
     * Adiciona fichas ao jogador
     * @param amount quantidade de fichas a adicionar
     */
    public void addChips(int amount) {
        this.chips += amount;
    }
    
    /**
     * Remove fichas do jogador
     * @param amount quantidade de fichas a remover
     * @return true se a operação foi bem-sucedida, false se não houver fichas suficientes
     */
    public boolean removeChips(int amount) {
        if (chips < amount) {
            return false;
        }
        
        this.chips -= amount;
        return true;
    }
    
    /**
     * Obtém a lista de mãos do jogador
     * @return lista de mãos
     */
    public List<Hand> getHands() {
        return hands;
    }
    
    /**
     * Obtém a mão principal do jogador
     * @return mão principal
     */
    public Hand getMainHand() {
        return hands.get(0);
    }
    
    /**
     * Obtém a lista de apostas do jogador
     * @return lista de apostas
     */
    public List<Bet> getBets() {
        return bets;
    }
    
    /**
     * Obtém a aposta principal do jogador
     * @return aposta principal
     */
    public Bet getMainBet() {
        return bets.get(0);
    }
    
    /**
     * Faz uma aposta
     * @param amount valor da aposta
     * @return true se a aposta foi feita com sucesso
     */
    public boolean placeBet(int amount) {
        if (chips < amount) {
            return false;
        }
        
        chips -= amount;
        bets.get(0).setAmount(amount);
        return true;
    }
    
    /**
     * Faz um seguro na aposta principal
     * @param amount valor do seguro
     * @return true se o seguro foi feito com sucesso
     */
    public boolean placeInsurance(int amount) {
        if (chips < amount) {
            return false;
        }
        
        chips -= amount;
        bets.get(0).insure(amount);
        return true;
    }
    
    /**
     * Dobra a aposta para uma mão específica
     * @param handIndex índice da mão
     * @return true se a aposta foi dobrada com sucesso
     */
    public boolean doubleBet(int handIndex) {
        if (handIndex >= bets.size() || handIndex >= hands.size()) {
            return false;
        }
        
        int amount = bets.get(handIndex).getAmount();
        if (chips < amount) {
            return false;
        }
        
        chips -= amount;
        bets.get(handIndex).doubleBet();
        return true;
    }
    
    /**
     * Divide um par em duas mãos
     * @return true se o par foi dividido com sucesso
     */
    public boolean splitPair() {
        if (hands.size() == 0 || !hands.get(0).isPair()) {
            return false;
        }
        
        int betAmount = bets.get(0).getAmount();
        if (chips < betAmount) {
            return false;
        }
        
        // Remove fichas para a nova aposta
        chips -= betAmount;
        
        // Divide a mão
        Hand newHand = hands.get(0).split();
        hands.add(newHand);
        
        // Cria uma nova aposta igual à original
        bets.add(new Bet(betAmount));
        
        return true;
    }
    
    /**
     * Rende-se (surrender)
     * @return true se a rendição foi bem-sucedida
     */
    public boolean surrender() {
        if (hands.size() > 1 || hands.get(0).getCards().size() > 2) {
            return false; // Só pode se render com a mão inicial
        }
        
        bets.get(0).surrender();
        
        // Devolve metade da aposta
        chips += bets.get(0).getAmount() / 2;
        
        return true;
    }
    
    /**
     * Limpa todas as mãos e apostas para uma nova rodada
     */
    public void clearHands() {
        hands.clear();
        bets.clear();
        
        // Reinicia com uma mão vazia
        hands.add(new Hand(false));
        bets.add(new Bet(0));
    }
    
    /**
     * Recebe pagamento por uma vitória
     * @param amount valor do pagamento
     */
    public void receiveWinnings(int amount) {
        chips += amount;
    }
}
