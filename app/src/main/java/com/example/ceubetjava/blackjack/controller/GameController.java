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
import com.example.ceubetjava.data.UserDao;

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
        UserDao userDao = db.userDao();
        this.game = new com.example.ceubetjava.blackjack.model.Game(playerName, initialChips, numDecks, minBet, maxBet);

        AppDatabase.executor.execute(() -> {
            com.example.ceubetjava.data.Game blackjackGame = gameDao.getGameByName("Blackjack");
            if (blackjackGame == null) return;
            gameId = blackjackGame.id;
            com.example.ceubetjava.data.User user = userDao.getUserById(userId);
            if (user == null) return;
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
    
   
    public void setGameListener(GameListener listener) {
        this.listener = listener;
    }
    
 
    public boolean startRound(int betAmount) {
        boolean success = game.startRound(betAmount);
        
        if (success && listener != null) {
            listener.onGameStateChanged(Game.STATE_PLAYER_TURN);
            listener.onPlayerTurn(0);
            
      
            if (game.getPlayer().getMainHand().isBlackjack()) {
                listener.onPlayerBlackjack();
               
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
    

    public boolean playerHit() {
        int currentHandIndex = game.getCurrentHandIndex();
        boolean success = game.playerHit();
        
        if (success && listener != null) {
            listener.onCardDealt(true, currentHandIndex, true);
          
            if (game.getPlayer().getHands().get(currentHandIndex).isBusted()) {
                listener.onPlayerBusted(currentHandIndex);
          
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
    

    public boolean playerStand() {
        boolean success = game.playerStand();
        
        if (success && listener != null) {
          
            if (game.getCurrentState() == Game.STATE_DEALER_TURN) {
                listener.onDealerTurn();
              
                checkGameResult();
            } else {
                listener.onPlayerTurn(game.getCurrentHandIndex());
            }
        }
        
        return success;
    }
  
    public boolean playerDouble() {
        int currentHandIndex = game.getCurrentHandIndex();
        boolean success = game.playerDouble();
        
        if (success && listener != null) {
            listener.onCardDealt(true, currentHandIndex, true);
            
      
            if (game.getPlayer().getHands().get(currentHandIndex).isBusted()) {
                listener.onPlayerBusted(currentHandIndex);
                listener.onDealerWin();
            }
            
       
            if (game.getCurrentState() == Game.STATE_DEALER_TURN) {
                listener.onDealerTurn();
                
              
                checkGameResult();
            } else if (game.getCurrentState() == Game.STATE_PLAYER_TURN) {
                listener.onPlayerTurn(game.getCurrentHandIndex());
            }
        }
        
        return success;
    }
   
    public boolean playerSplit() {
        boolean success = game.playerSplit();
        
        if (success && listener != null) {
            listener.onCardDealt(true, 0, true);
            listener.onCardDealt(true, 1, true);
        }
        
        return success;
    }
 
    public boolean playerInsurance() {
        boolean success = game.playerInsurance();
        
        if (success && listener != null) {
          
            if (game.getDealer().getHand().isBlackjack()) {
                listener.onInsurancePaid();
            }
        }
        
        return success;
    }
    
 
    public boolean playerSurrender() {
        boolean success = game.playerSurrender();
        
        if (success && listener != null) {
            listener.onGameStateChanged(Game.STATE_GAME_OVER);
            listener.onDealerWin();
            listener.onGameOver();
        }
        
        return success;
    }
  
    private void checkGameResult() {
        Player player = game.getPlayer();
        Dealer dealer = game.getDealer();
        
  
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
               
                for (int i = 0; i < player.getHands().size(); i++) {
                    Hand hand = player.getHands().get(i);
                    Bet bet = player.getBets().get(i);
                    
                    if (bet.isSurrendered() || hand.isBusted()) {
                        continue; // Mão já perdida
                    }
                    
                
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
    
  
    public com.example.ceubetjava.blackjack.model.Game getGame() {
        return game;
    }

   
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
