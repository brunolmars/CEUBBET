package com.example.ceubetjava.blackjack.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe principal que gerencia o jogo de Blackjack
 */
public class Game {
    // Constantes para os estados do jogo
    public static final int STATE_BETTING = 0;
    public static final int STATE_PLAYER_TURN = 1;
    public static final int STATE_DEALER_TURN = 2;
    public static final int STATE_GAME_OVER = 3;
    
    private Deck deck;
    private Player player;
    private Dealer dealer;
    private int currentState;
    private int currentHandIndex;
    private int minBet;
    private int maxBet;
    
    /**
     * Construtor do jogo
     * @param playerName nome do jogador
     * @param initialChips fichas iniciais do jogador
     * @param numDecks número de baralhos a serem usados
     * @param minBet aposta mínima
     * @param maxBet aposta máxima
     */
    public Game(String playerName, int initialChips, int numDecks, int minBet, int maxBet) {
        this.deck = new Deck(numDecks);
        this.player = new Player(playerName, initialChips);
        this.dealer = new Dealer();
        this.currentState = STATE_BETTING;
        this.currentHandIndex = 0;
        this.minBet = minBet;
        this.maxBet = maxBet;
    }
    
    /**
     * Inicia uma nova rodada
     * @param betAmount valor da aposta inicial
     * @return true se a rodada foi iniciada com sucesso
     */
    public boolean startRound(int betAmount) {
        if (currentState != STATE_BETTING) {
            return false;
        }
        
        if (betAmount < minBet || betAmount > maxBet) {
            return false;
        }
        
        // Limpa as mãos anteriores
        player.clearHands();
        dealer.clearHand();
        
        // Faz a aposta inicial
        if (!player.placeBet(betAmount)) {
            return false; // Jogador não tem fichas suficientes
        }
        
        // Distribui as cartas iniciais
        player.getMainHand().addCard(deck.dealCard());
        dealer.getHand().addCard(deck.dealCard());
        player.getMainHand().addCard(deck.dealCard());
        
        // A segunda carta do dealer fica virada para baixo
        Card dealerSecondCard = deck.dealCard();
        dealerSecondCard.setFaceUp(false);
        dealer.getHand().addCard(dealerSecondCard);
        
        // Verifica se o baralho precisa ser embaralhado
        if (deck.needsShuffle()) {
            deck.shuffle();
        }
        
        // Muda para o turno do jogador
        currentState = STATE_PLAYER_TURN;
        currentHandIndex = 0;
        
        return true;
    }
    
    /**
     * Jogador pede mais uma carta
     * @return true se a operação foi bem-sucedida
     */
    public boolean playerHit() {
        if (currentState != STATE_PLAYER_TURN) {
            return false;
        }
        
        List<Hand> hands = player.getHands();
        if (currentHandIndex >= hands.size()) {
            return false;
        }
        
        Hand currentHand = hands.get(currentHandIndex);
        
        // Adiciona uma carta à mão atual
        currentHand.addCard(deck.dealCard());
        
        // Verifica se o jogador estourou
        if (currentHand.isBusted()) {
            // Passa para a próxima mão ou para o turno do dealer
            advanceToNextHandOrDealer();
        }
        
        // Verifica se o baralho precisa ser embaralhado
        if (deck.needsShuffle()) {
            deck.shuffle();
        }
        
        return true;
    }
    
    /**
     * Jogador decide parar
     * @return true se a operação foi bem-sucedida
     */
    public boolean playerStand() {
        if (currentState != STATE_PLAYER_TURN) {
            return false;
        }
        
        // Passa para a próxima mão ou para o turno do dealer
        advanceToNextHandOrDealer();
        
        return true;
    }
    
    /**
     * Jogador dobra a aposta
     * @return true se a operação foi bem-sucedida
     */
    public boolean playerDouble() {
        if (currentState != STATE_PLAYER_TURN) {
            return false;
        }
        
        List<Hand> hands = player.getHands();
        if (currentHandIndex >= hands.size()) {
            return false;
        }
        
        Hand currentHand = hands.get(currentHandIndex);
        
        // Só pode dobrar com 9, 10 ou 11 pontos e com apenas duas cartas
        int value = currentHand.getValue();
        if (currentHand.getCards().size() != 2 || (value != 9 && value != 10 && value != 11)) {
            return false;
        }
        
        // Dobra a aposta
        if (!player.doubleBet(currentHandIndex)) {
            return false; // Jogador não tem fichas suficientes
        }
        
        // Adiciona uma carta à mão atual
        currentHand.addCard(deck.dealCard());
        
        // Passa para a próxima mão ou para o turno do dealer
        advanceToNextHandOrDealer();
        
        // Verifica se o baralho precisa ser embaralhado
        if (deck.needsShuffle()) {
            deck.shuffle();
        }
        
        return true;
    }
    
    /**
     * Jogador divide um par
     * @return true se a operação foi bem-sucedida
     */
    public boolean playerSplit() {
        if (currentState != STATE_PLAYER_TURN) {
            return false;
        }
        
        if (currentHandIndex != 0) {
            return false; // Só pode dividir a mão inicial
        }
        
        Hand currentHand = player.getMainHand();
        
        // Verifica se a mão tem um par
        if (!currentHand.isPair()) {
            return false;
        }
        
        // Divide o par
        if (!player.splitPair()) {
            return false; // Jogador não tem fichas suficientes
        }
        
        // Adiciona uma carta a cada mão
        player.getHands().get(0).addCard(deck.dealCard());
        player.getHands().get(1).addCard(deck.dealCard());
        
        // Verifica se o baralho precisa ser embaralhado
        if (deck.needsShuffle()) {
            deck.shuffle();
        }
        
        return true;
    }
    
    /**
     * Jogador faz um seguro
     * @return true se a operação foi bem-sucedida
     */
    public boolean playerInsurance() {
        if (currentState != STATE_PLAYER_TURN) {
            return false;
        }
        
        if (currentHandIndex != 0) {
            return false; // Só pode fazer seguro na mão inicial
        }
        
        // Verifica se a carta visível do dealer é um Ás
        if (!dealer.hasAceShowing()) {
            return false;
        }
        
        // Calcula o valor do seguro (metade da aposta original)
        int insuranceAmount = player.getMainBet().getAmount() / 2;
        
        // Faz o seguro
        return player.placeInsurance(insuranceAmount);
    }
    
    /**
     * Jogador se rende
     * @return true se a operação foi bem-sucedida
     */
    public boolean playerSurrender() {
        if (currentState != STATE_PLAYER_TURN) {
            return false;
        }
        
        if (currentHandIndex != 0 || player.getHands().size() > 1) {
            return false; // Só pode se render na mão inicial
        }
        
        // Verifica se a carta visível do dealer não é um Ás
        if (dealer.hasAceShowing()) {
            return false;
        }
        
        // Rende-se
        if (!player.surrender()) {
            return false;
        }
        
        // Finaliza o jogo
        currentState = STATE_GAME_OVER;
        
        return true;
    }
    
    /**
     * Executa o turno do dealer
     */
    public void playDealerTurn() {
        if (currentState != STATE_DEALER_TURN) {
            return;
        }
        
        // Revela a carta virada para baixo do dealer
        dealer.revealHoleCard();
        
        // Dealer pede cartas até atingir pelo menos 17
        while (dealer.shouldHit()) {
            dealer.getHand().addCard(deck.dealCard());
        }
        
        // Finaliza o jogo
        currentState = STATE_GAME_OVER;
        
        // Verifica se o baralho precisa ser embaralhado
        if (deck.needsShuffle()) {
            deck.shuffle();
        }
        
        // Processa os pagamentos
        processPayouts();
    }
    
    /**
     * Processa os pagamentos no final da rodada
     */
    private void processPayouts() {
        boolean dealerBusted = dealer.getHand().isBusted();
        boolean dealerBlackjack = dealer.getHand().isBlackjack();
        int dealerValue = dealer.getHand().getValue();
        
        // Processa o seguro primeiro
        if (dealerBlackjack && player.getMainBet().getInsuranceAmount() > 0) {
            player.receiveWinnings(player.getMainBet().getInsuranceAmount());
            player.receiveWinnings(player.getMainBet().getInsurancePayout());
        }
        
        // Processa cada mão do jogador
        List<Hand> hands = player.getHands();
        List<Bet> bets = player.getBets();
        
        for (int i = 0; i < hands.size(); i++) {
            Hand hand = hands.get(i);
            Bet bet = bets.get(i);
            
            // Ignora mãos rendidas
            if (bet.isSurrendered()) {
                continue;
            }
            
            // Verifica prêmios especiais
            if (hand.has678SameSuit() || hand.hasThreeSevens()) {
                player.receiveWinnings(bet.getAmount());
                player.receiveWinnings(bet.getSpecialPayout());
                continue;
            }
            
            // Verifica se o jogador estourou
            if (hand.isBusted()) {
                // Jogador perde
                continue;
            }
            
            // Verifica se o jogador tem Blackjack
            if (hand.isBlackjack()) {
                if (dealerBlackjack) {
                    // Empate
                    player.receiveWinnings(bet.getAmount());
                } else {
                    // Jogador ganha com Blackjack
                    player.receiveWinnings(bet.getAmount());
                    player.receiveWinnings(bet.getBlackjackPayout());
                }
                continue;
            }
            
            // Verifica se o dealer estourou
            if (dealerBusted) {
                // Jogador ganha
                player.receiveWinnings(bet.getAmount());
                player.receiveWinnings(bet.getRegularWinPayout());
                continue;
            }
            
            // Verifica se o dealer tem Blackjack
            if (dealerBlackjack) {
                // Dealer ganha
                continue;
            }
            
            // Compara os valores
            int handValue = hand.getValue();
            if (handValue > dealerValue) {
                // Jogador ganha
                player.receiveWinnings(bet.getAmount());
                player.receiveWinnings(bet.getRegularWinPayout());
            } else if (handValue < dealerValue) {
                // Dealer ganha
                continue;
            } else {
                // Empate
                player.receiveWinnings(bet.getAmount());
            }
        }
    }
    
    /**
     * Avança para a próxima mão ou para o turno do dealer
     */
    private void advanceToNextHandOrDealer() {
        currentHandIndex++;
        
        if (currentHandIndex >= player.getHands().size()) {
            // Todas as mãos foram jogadas, passa para o turno do dealer
            currentState = STATE_DEALER_TURN;
            playDealerTurn();
        }
    }
    
    /**
     * Obtém o estado atual do jogo
     * @return estado atual do jogo
     */
    public int getCurrentState() {
        return currentState;
    }
    
    /**
     * Obtém o índice da mão atual
     * @return índice da mão atual
     */
    public int getCurrentHandIndex() {
        return currentHandIndex;
    }
    
    /**
     * Obtém o jogador
     * @return jogador
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Obtém o dealer
     * @return dealer
     */
    public Dealer getDealer() {
        return dealer;
    }
    
    /**
     * Obtém a aposta mínima
     * @return aposta mínima
     */
    public int getMinBet() {
        return minBet;
    }
    
    /**
     * Obtém a aposta máxima
     * @return aposta máxima
     */
    public int getMaxBet() {
        return maxBet;
    }
}
