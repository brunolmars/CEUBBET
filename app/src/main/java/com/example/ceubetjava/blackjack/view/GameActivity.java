package com.example.ceubetjava.blackjack.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.ceubetjava.R;
import com.example.ceubetjava.blackjack.controller.GameController;
import com.example.ceubetjava.blackjack.model.Card;
import com.example.ceubetjava.blackjack.model.Game;
import com.example.ceubetjava.blackjack.model.Hand;
import com.example.ceubetjava.blackjack.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity principal do jogo de Blackjack
 */
public class GameActivity extends AppCompatActivity implements GameController.GameListener {
    private GameController gameController;
    
    // Views da interface
    private TextView tvPlayerName;
    private TextView tvPlayerChips;
    private TextView tvPlayerScore;
    private TextView tvDealerScore;
    private TextView tvBetAmount;
    private TextView tvGameStatus;
    
    private ConstraintLayout layoutPlayerCards;
    private ConstraintLayout layoutDealerCards;
    private ConstraintLayout layoutBetting;
    private ConstraintLayout layoutGameActions;
    
    private Button btnDeal;
    private Button btnHit;
    private Button btnStand;
    private Button btnDouble;
    private Button btnSplit;
    private Button btnInsurance;
    private Button btnSurrender;
    
    // Listas de ImageViews para as cartas
    private List<ImageView> playerCardViews;
    private List<ImageView> dealerCardViews;
    
    // Valores de aposta
    private int currentBet = 100;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        
        // Recupera dados do usuário
        boolean isGuest = getIntent().getBooleanExtra("isGuest", false);
        int userId = getIntent().getIntExtra("userId", -1);
        String playerName = getIntent().getStringExtra("username");
        if (playerName == null) playerName = "Jogador";
        
        // Inicializa o controlador do jogo
        gameController = new GameController(this, userId, playerName, 1000, 4, 50, 500);
        gameController.setGameListener(this);
        
        // Inicializa as views
        initViews();
        
        // Configura os listeners dos botões
        setupButtonListeners(isGuest);
        
        // Atualiza a interface
        updateUI();
    }
    
    /**
     * Inicializa as views da interface
     */
    private void initViews() {
        tvPlayerName = findViewById(R.id.tv_player_name);
        tvPlayerChips = findViewById(R.id.tv_player_chips);
        tvPlayerScore = findViewById(R.id.tv_player_score);
        tvDealerScore = findViewById(R.id.tv_dealer_score);
        tvBetAmount = findViewById(R.id.tv_bet_amount);
        tvGameStatus = findViewById(R.id.tv_game_status);
        
        layoutPlayerCards = findViewById(R.id.layout_player_cards);
        layoutDealerCards = findViewById(R.id.layout_dealer_cards);
        layoutBetting = findViewById(R.id.layout_betting);
        layoutGameActions = findViewById(R.id.layout_game_actions);
        
        btnDeal = findViewById(R.id.btn_deal);
        btnHit = findViewById(R.id.btn_hit);
        btnStand = findViewById(R.id.btn_stand);
        btnDouble = findViewById(R.id.btn_double);
        btnSplit = findViewById(R.id.btn_split);
        btnInsurance = findViewById(R.id.btn_insurance);
        btnSurrender = findViewById(R.id.btn_surrender);
        
        playerCardViews = new ArrayList<>();
        dealerCardViews = new ArrayList<>();
    }
    
    /**
     * Configura os listeners dos botões
     */
    private void setupButtonListeners(boolean isGuest) {
        btnDeal.setOnClickListener(v -> {
            // Inicia uma nova rodada
            if (gameController.startRound(currentBet)) {
                updateUI();
            } else {
                Toast.makeText(this, "Não foi possível iniciar a rodada", Toast.LENGTH_SHORT).show();
            }
        });
        
        btnHit.setOnClickListener(v -> {
            // Jogador pede mais uma carta
            if (gameController.playerHit()) {
                updateUI();
            }
        });
        
        btnStand.setOnClickListener(v -> {
            // Jogador para
            if (gameController.playerStand()) {
                updateUI();
            }
        });
        
        btnDouble.setOnClickListener(v -> {
            // Jogador dobra a aposta
            if (gameController.playerDouble()) {
                updateUI();
            } else {
                Toast.makeText(this, "Não é possível dobrar", Toast.LENGTH_SHORT).show();
            }
        });
        
        btnSplit.setOnClickListener(v -> {
            // Jogador divide o par
            if (gameController.playerSplit()) {
                updateUI();
            } else {
                Toast.makeText(this, "Não é possível dividir", Toast.LENGTH_SHORT).show();
            }
        });
        
        btnInsurance.setOnClickListener(v -> {
            // Jogador faz seguro
            if (gameController.playerInsurance()) {
                updateUI();
            } else {
                Toast.makeText(this, "Não é possível fazer seguro", Toast.LENGTH_SHORT).show();
            }
        });
        
        btnSurrender.setOnClickListener(v -> {
            // Jogador se rende
            if (gameController.playerSurrender()) {
                updateUI();
            } else {
                Toast.makeText(this, "Não é possível se render", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnBackToHome = findViewById(R.id.btnBackToHome);
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setText(isGuest ? "Fazer Login" : "Sair");

        btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, com.example.ceubetjava.GameSelectionActivity.class);
            intent.putExtra("isGuest", isGuest);
            if (!isGuest) {
                intent.putExtra("userId", getIntent().getIntExtra("userId", -1));
                String username = getIntent().getStringExtra("username");
                if (username != null) intent.putExtra("username", username);
            }
            startActivity(intent);
            finish();
        });

        logoutButton.setOnClickListener(v -> {
            if (isGuest) {
                Intent intent = new Intent(this, com.example.ceubetjava.LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                if (gameController != null) gameController.saveCredits();
                SharedPreferences prefs = getSharedPreferences("CeubetPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();
                Intent intent = new Intent(this, com.example.ceubetjava.LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    
    /**
     * Atualiza a interface do jogo
     */
    private void updateUI() {
        Game game = gameController.getGame();
        Player player = game.getPlayer();
        
        // Atualiza informações do jogador
        tvPlayerName.setText(player.getName());
        tvPlayerChips.setText("Fichas: " + player.getChips());
        tvBetAmount.setText("Aposta: " + currentBet);
        
        // Atualiza a pontuação
        if (game.getCurrentState() != Game.STATE_BETTING) {
            tvPlayerScore.setText("Pontuação: " + player.getMainHand().getValue());
            tvDealerScore.setText("Pontuação: " + game.getDealer().getHand().getValue());
        } else {
            tvPlayerScore.setText("Pontuação: 0");
            tvDealerScore.setText("Pontuação: 0");
        }
        
        // Atualiza o status do jogo
        updateGameStatus();
        
        // Atualiza a visibilidade dos layouts
        updateLayoutVisibility();
        
        // Atualiza a visibilidade dos botões
        updateButtonVisibility();
        
        // Atualiza as cartas
        updateCardViews();
    }
    
    /**
     * Atualiza o status do jogo
     */
    private void updateGameStatus() {
        Game game = gameController.getGame();
        
        switch (game.getCurrentState()) {
            case Game.STATE_BETTING:
                tvGameStatus.setText("Faça sua aposta");
                break;
            case Game.STATE_PLAYER_TURN:
                tvGameStatus.setText("Sua vez");
                break;
            case Game.STATE_DEALER_TURN:
                tvGameStatus.setText("Vez do dealer");
                break;
            case Game.STATE_GAME_OVER:
                tvGameStatus.setText("Fim da rodada");
                break;
        }
    }
    
    /**
     * Atualiza a visibilidade dos layouts
     */
    private void updateLayoutVisibility() {
        Game game = gameController.getGame();
        
        switch (game.getCurrentState()) {
            case Game.STATE_BETTING:
                layoutBetting.setVisibility(View.VISIBLE);
                layoutGameActions.setVisibility(View.GONE);
                break;
            case Game.STATE_PLAYER_TURN:
                layoutBetting.setVisibility(View.GONE);
                layoutGameActions.setVisibility(View.VISIBLE);
                break;
            case Game.STATE_DEALER_TURN:
            case Game.STATE_GAME_OVER:
                layoutBetting.setVisibility(View.GONE);
                layoutGameActions.setVisibility(View.GONE);
                break;
        }
    }
    
    /**
     * Atualiza a visibilidade dos botões
     */
    private void updateButtonVisibility() {
        Game game = gameController.getGame();
        
        if (game.getCurrentState() == Game.STATE_PLAYER_TURN) {
            btnHit.setVisibility(View.VISIBLE);
            btnStand.setVisibility(View.VISIBLE);
            
            // Verifica se é possível dobrar
            Hand currentHand = game.getPlayer().getHands().get(game.getCurrentHandIndex());
            boolean canDouble = currentHand.getCards().size() == 2 && 
                               (currentHand.getValue() == 9 || 
                                currentHand.getValue() == 10 || 
                                currentHand.getValue() == 11);
            btnDouble.setVisibility(canDouble ? View.VISIBLE : View.GONE);
            
            // Verifica se é possível dividir
            boolean canSplit = game.getCurrentHandIndex() == 0 && currentHand.isPair();
            btnSplit.setVisibility(canSplit ? View.VISIBLE : View.GONE);
            
            // Verifica se é possível fazer seguro
            boolean canInsure = game.getCurrentHandIndex() == 0 && 
                               game.getDealer().hasAceShowing();
            btnInsurance.setVisibility(canInsure ? View.VISIBLE : View.GONE);
            
            // Verifica se é possível se render
            boolean canSurrender = game.getCurrentHandIndex() == 0 && 
                                  currentHand.getCards().size() == 2 && 
                                  !game.getDealer().hasAceShowing();
            btnSurrender.setVisibility(canSurrender ? View.VISIBLE : View.GONE);
        } else {
            btnHit.setVisibility(View.GONE);
            btnStand.setVisibility(View.GONE);
            btnDouble.setVisibility(View.GONE);
            btnSplit.setVisibility(View.GONE);
            btnInsurance.setVisibility(View.GONE);
            btnSurrender.setVisibility(View.GONE);
        }
    }
    
    /**
     * Atualiza as views das cartas
     */
    private void updateCardViews() {
        Game game = gameController.getGame();
        
        // Limpa as views anteriores
        layoutPlayerCards.removeAllViews();
        layoutDealerCards.removeAllViews();
        playerCardViews.clear();
        dealerCardViews.clear();
        
        if (game.getCurrentState() != Game.STATE_BETTING) {
            // Adiciona as cartas do jogador
            List<Hand> playerHands = game.getPlayer().getHands();
            for (int h = 0; h < playerHands.size(); h++) {
                Hand hand = playerHands.get(h);
                List<Card> cards = hand.getCards();
                
                for (int i = 0; i < cards.size(); i++) {
                    Card card = cards.get(i);
                    ImageView cardView = new ImageView(this);
                    
                    // Define a imagem da carta
                    int resourceId = getResources().getIdentifier(
                            card.getImageResourceName(), "drawable", getPackageName());
                    cardView.setImageResource(resourceId);
                    
                    // Define o layout
                    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.WRAP_CONTENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    params.leftMargin = i * 50; // Desloca cada carta para a direita
                    cardView.setLayoutParams(params);
                    
                    // Adiciona a view
                    layoutPlayerCards.addView(cardView);
                    playerCardViews.add(cardView);
                }
            }
            
            // Adiciona as cartas do dealer
            List<Card> dealerCards = game.getDealer().getHand().getCards();
            for (int i = 0; i < dealerCards.size(); i++) {
                Card card = dealerCards.get(i);
                ImageView cardView = new ImageView(this);
                
                // Define a imagem da carta
                int resourceId;
                if (card.isFaceUp()) {
                    resourceId = getResources().getIdentifier(
                            card.getImageResourceName(), "drawable", getPackageName());
                } else {
                    resourceId = R.drawable.card_back; // Carta virada para baixo
                }
                cardView.setImageResource(resourceId);
                
                // Define o layout
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT);
                params.leftMargin = i * 50; // Desloca cada carta para a direita
                cardView.setLayoutParams(params);
                
                // Adiciona a view
                layoutDealerCards.addView(cardView);
                dealerCardViews.add(cardView);
            }
        }
    }
    
    // Implementação dos métodos da interface GameListener
    
    @Override
    public void onGameStateChanged(int newState) {
        updateUI();
    }
    
    @Override
    public void onPlayerTurn(int handIndex) {
        updateUI();
    }
    
    @Override
    public void onDealerTurn() {
        updateUI();
    }
    
    @Override
    public void onGameOver() {
        updateUI();
        
        // Habilita o botão de nova rodada
        btnDeal.setEnabled(true);
    }
    
    @Override
    public void onCardDealt(boolean toPlayer, int handIndex, boolean faceUp) {
        updateUI();
    }
    
    @Override
    public void onPlayerBusted(int handIndex) {
        Toast.makeText(this, "Você estourou!", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onDealerBusted() {
        Toast.makeText(this, "Dealer estourou!", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onPlayerBlackjack() {
        Toast.makeText(this, "Blackjack!", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onDealerBlackjack() {
        Toast.makeText(this, "Dealer tem Blackjack!", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onPlayerWin(int handIndex, boolean isBlackjack, boolean isSpecialWin) {
        if (isSpecialWin) {
            Toast.makeText(this, "Você ganhou com prêmio especial!", Toast.LENGTH_SHORT).show();
        } else if (isBlackjack) {
            Toast.makeText(this, "Você ganhou com Blackjack!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Você ganhou!", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public void onDealerWin() {
        Toast.makeText(this, "Dealer ganhou!", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onPush() {
        Toast.makeText(this, "Empate!", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onInsurancePaid() {
        Toast.makeText(this, "Seguro pago!", Toast.LENGTH_SHORT).show();
    }
}
