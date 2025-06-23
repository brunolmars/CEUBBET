package com.example.ceubetjava.blackjack.controller;

import android.content.Context;
import com.example.ceubetjava.data.AppDatabase;
import com.example.ceubetjava.data.CreditosUsuario;
import com.example.ceubetjava.data.CreditosUsuarioDao;
import com.example.ceubetjava.blackjack.model.Game;
import com.example.ceubetjava.blackjack.model.Player;
import com.example.ceubetjava.blackjack.model.Dealer;
import com.example.ceubetjava.blackjack.model.Hand;
import com.example.ceubetjava.blackjack.model.Bet;
import com.example.ceubetjava.data.GameDao;

/**
 * Controlador principal do jogo de Blackjack
 */
public class GameController {
    private com.example.ceubetjava.blackjack.model.Game game;
    private GameListener listener;
    private final Context context;
    private final int userId;
    private int gameId = -1; // será definido dinamicamente
    private final CreditosUsuarioDao creditosUsuarioDao;
    private CreditosUsuario creditosUsuario;
    
    /**
     * Interface para notificar eventos do jogo
     */
    public interface GameListener {
        void onGameStateChanged(int newState);
        void onPlayerTurn(int handIndex);
        void onDealerTurn();
        void onGameOver();
        void onCardDealt(boolean toPlayer, int handIndex, boolean faceUp);
        void onPlayerBusted(int handIndex);
        void onDealerBusted();
        void onPlayerBlackjack();
        void onDealerBlackjack();
        void onPlayerWin(int handIndex, boolean isBlackjack, boolean isSpecialWin);
        void onDealerWin();
        void onPush(); // Empate
        void onInsurancePaid();
    }
    
    /**
     * Construtor do controlador
     * @param context contexto da aplicação
     * @param userId ID do usuário
     * @param playerName nome do jogador
     * @param initialChips fichas iniciais
     * @param numDecks número de baralhos
     * @param minBet aposta mínima
     * @param maxBet aposta máxima
     */
    public GameController(Context context, int userId, String playerName, int initialChips, int numDecks, int minBet, int maxBet) {
        this.context = context;
        this.userId = userId;
        AppDatabase db = AppDatabase.getInstance(context);
        this.creditosUsuarioDao = db.creditosUsuarioDao();
        GameDao gameDao = db.gameDao();
        this.game = new com.example.ceubetjava.blackjack.model.Game(playerName, initialChips, numDecks, minBet, maxBet);

        AppDatabase.executor.execute(() -> {
            com.example.ceubetjava.data.Game blackjackGame = gameDao.getGameByName("Blackjack");
            if (blackjackGame == null) return;
            gameId = blackjackGame.id;
            creditosUsuario = creditosUsuarioDao.getCreditos(userId, gameId);
            if (creditosUsuario == null) {
                creditosUsuario = new CreditosUsuario();
                creditosUsuario.usuarioId = userId;
                creditosUsuario.jogoId = gameId;
                creditosUsuario.quantidadeDeCreditos = initialChips;
                creditosUsuarioDao.insertOrUpdate(creditosUsuario);
            }
            game.getPlayer().setChips(creditosUsuario.quantidadeDeCreditos);
        });
    }
    
    /**
     * Define o listener para eventos do jogo
     * @param listener listener para eventos
     */
    public void setGameListener(GameListener listener) {
        this.listener = listener;
    }
    
    /**
     * Inicia uma nova rodada
     * @param betAmount valor da aposta
     * @return true se a rodada foi iniciada com sucesso
     */
    public boolean startRound(int betAmount) {
        boolean success = game.startRound(betAmount);
        
        if (success && listener != null) {
            listener.onGameStateChanged(Game.STATE_PLAYER_TURN);
            listener.onPlayerTurn(0);
            
            // Verifica se o jogador tem Blackjack
            if (game.getPlayer().getMainHand().isBlackjack()) {
                listener.onPlayerBlackjack();
                
                // Verifica se o dealer também tem Blackjack
                if (game.getDealer().getHand().isBlackjack()) {
                    game.playDealerTurn();
                    listener.onDealerBlackjack();
                    listener.onPush();
                    listener.onGameOver();
                } else {
                    game.playDealerTurn();
                    listener.onPlayerWin(0, true, false);
                    listener.onGameOver();
                }
            }
        }
        
        return success;
    }
    
    /**
     * Jogador pede mais uma carta
     * @return true se a operação foi bem-sucedida
     */
    public boolean playerHit() {
        int currentHandIndex = game.getCurrentHandIndex();
        boolean success = game.playerHit();
        
        if (success && listener != null) {
            listener.onCardDealt(true, currentHandIndex, true);
            
            // Verifica se o jogador estourou
            if (game.getPlayer().getHands().get(currentHandIndex).isBusted()) {
                listener.onPlayerBusted(currentHandIndex);
                
                // Verifica se o jogo acabou
                if (game.getCurrentState() == Game.STATE_DEALER_TURN) {
                    listener.onDealerTurn();
                } else if (game.getCurrentState() == Game.STATE_GAME_OVER) {
                    listener.onDealerWin();
                    listener.onGameOver();
                } else {
                    listener.onPlayerTurn(game.getCurrentHandIndex());
                }
            }
        }
        
        return success;
    }
    
    /**
     * Jogador decide parar
     * @return true se a operação foi bem-sucedida
     */
    public boolean playerStand() {
        boolean success = game.playerStand();
        
        if (success && listener != null) {
            // Verifica se o jogo passou para o turno do dealer
            if (game.getCurrentState() == Game.STATE_DEALER_TURN) {
                listener.onDealerTurn();
                
                // Verifica o resultado do jogo
                checkGameResult();
            } else {
                listener.onPlayerTurn(game.getCurrentHandIndex());
            }
        }
        
        return success;
    }
    
    /**
     * Jogador dobra a aposta
     * @return true se a operação foi bem-sucedida
     */
    public boolean playerDouble() {
        int currentHandIndex = game.getCurrentHandIndex();
        boolean success = game.playerDouble();
        
        if (success && listener != null) {
            listener.onCardDealt(true, currentHandIndex, true);
            
            // Verifica se o jogador estourou
            if (game.getPlayer().getHands().get(currentHandIndex).isBusted()) {
                listener.onPlayerBusted(currentHandIndex);
                listener.onDealerWin();
            }
            
            // Verifica se o jogo passou para o turno do dealer
            if (game.getCurrentState() == Game.STATE_DEALER_TURN) {
                listener.onDealerTurn();
                
                // Verifica o resultado do jogo
                checkGameResult();
            } else if (game.getCurrentState() == Game.STATE_PLAYER_TURN) {
                listener.onPlayerTurn(game.getCurrentHandIndex());
            }
        }
        
        return success;
    }
    
    /**
     * Jogador divide um par
     * @return true se a operação foi bem-sucedida
     */
    public boolean playerSplit() {
        boolean success = game.playerSplit();
        
        if (success && listener != null) {
            listener.onCardDealt(true, 0, true);
            listener.onCardDealt(true, 1, true);
        }
        
        return success;
    }
    
    /**
     * Jogador faz um seguro
     * @return true se a operação foi bem-sucedida
     */
    public boolean playerInsurance() {
        boolean success = game.playerInsurance();
        
        if (success && listener != null) {
            // Se o dealer tiver Blackjack, o seguro é pago
            if (game.getDealer().getHand().isBlackjack()) {
                listener.onInsurancePaid();
            }
        }
        
        return success;
    }
    
    /**
     * Jogador se rende
     * @return true se a operação foi bem-sucedida
     */
    public boolean playerSurrender() {
        boolean success = game.playerSurrender();
        
        if (success && listener != null) {
            listener.onGameStateChanged(Game.STATE_GAME_OVER);
            listener.onDealerWin();
            listener.onGameOver();
        }
        
        return success;
    }
    
    /**
     * Verifica o resultado do jogo após o turno do dealer
     */
    private void checkGameResult() {
        Player player = game.getPlayer();
        Dealer dealer = game.getDealer();
        
        // Verifica se o dealer estourou
        if (dealer.getHand().isBusted()) {
            listener.onDealerBusted();
            
            // Todas as mãos não estouradas do jogador ganham
            for (int i = 0; i < player.getHands().size(); i++) {
                Hand hand = player.getHands().get(i);
                if (!hand.isBusted() && !player.getBets().get(i).isSurrendered()) {
                    boolean isSpecial = hand.has678SameSuit() || hand.hasThreeSevens();
                    listener.onPlayerWin(i, hand.isBlackjack(), isSpecial);
                }
            }
        } else {
            // Verifica se o dealer tem Blackjack
            if (dealer.getHand().isBlackjack()) {
                listener.onDealerBlackjack();
                
                // Verifica cada mão do jogador
                for (int i = 0; i < player.getHands().size(); i++) {
                    Hand hand = player.getHands().get(i);
                    if (hand.isBlackjack()) {
                        listener.onPush(); // Empate
                    } else {
                        listener.onDealerWin();
                    }
                }
            } else {
                int dealerValue = dealer.getHand().getValue();
                
                // Compara cada mão do jogador com a do dealer
                for (int i = 0; i < player.getHands().size(); i++) {
                    Hand hand = player.getHands().get(i);
                    Bet bet = player.getBets().get(i);
                    
                    if (bet.isSurrendered() || hand.isBusted()) {
                        continue; // Mão já perdida
                    }
                    
                    // Verifica prêmios especiais
                    if (hand.has678SameSuit() || hand.hasThreeSevens()) {
                        listener.onPlayerWin(i, false, true);
                        continue;
                    }
                    
                    int handValue = hand.getValue();
                    
                    if (handValue > dealerValue) {
                        listener.onPlayerWin(i, hand.isBlackjack(), false);
                    } else if (handValue < dealerValue) {
                        listener.onDealerWin();
                    } else {
                        listener.onPush(); // Empate
                    }
                }
            }
        }
        
        listener.onGameOver();
    }
    
    /**
     * Obtém o jogo
     * @return jogo
     */
    public com.example.ceubetjava.blackjack.model.Game getGame() {
        return game;
    }

    // Após qualquer alteração de créditos (ganho/perda), chame este método:
    public void saveCredits() {
        if (creditosUsuario != null && gameId != -1) {
            int novosCreditos = game.getPlayer().getChips();
            creditosUsuario.quantidadeDeCreditos = novosCreditos;
            AppDatabase.executor.execute(() -> {
                creditosUsuarioDao.updateCreditos(userId, gameId, novosCreditos);
            });
        }
    }
}
