package com.example.ceubetjava.slot77.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.ceubetjava.LoginActivity;
import com.example.ceubetjava.R;
import com.example.ceubetjava.slot77.controller.SlotController;
import com.example.ceubetjava.slot77.model.SpinResult;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class Slot77MainActivity extends AppCompatActivity {
    // Componentes visuais principais
    private TextView reel1, reel2, reel3, reel4, reel5;
    private TextView reel1Above, reel2Above, reel3Above, reel4Above, reel5Above;
    private TextView reel1Below, reel2Below, reel3Below, reel4Below, reel5Below;
    private Button spinButton;
    private TextView creditsDisplay;
    private TextView titleDisplay;
    private TextView messageDisplay;
    private View slotMachineContainer;
    private ConstraintLayout mainContainer;

    // Controles de aposta
    private MaterialButton decreaseBetButton;
    private MaterialButton increaseBetButton;
    private MaterialButton maxBetButton;
    private EditText betInput;
    private Button logoutButton;

    // Sistema de mensagens
    private Handler messageHandler = new Handler(Looper.getMainLooper());
    private Runnable hideMessageRunnable;

    // Controlador
    private SlotController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initializeViews();

        boolean isGuest = getIntent().getBooleanExtra("isGuest", true);
        int userId = getIntent().getIntExtra("userId", -1);
        String username = getIntent().getStringExtra("username");
        long initialCredits = isGuest ? 30 : 100; // Corrigido: usuários logados começam com 100 créditos

        controller = new SlotController(this, isGuest, userId, initialCredits);
        controller.setOnCreditsUpdateListener(newCredits -> {
            runOnUiThread(() -> {
                updateCreditsDisplay();
                updateBetControls();
            });
        });

        setupBetControls();
        setupLogoutButton();
        updateCreditsDisplay();

        spinButton.setOnClickListener(v -> {
            if (!controller.isSpinning() && controller.getCurrentCredits() >= controller.getCurrentBet()) {
                controller.playButtonClickSound();
                startSpin();
            } else if (controller.getCurrentCredits() < controller.getCurrentBet()) {
                showToast("Créditos insuficientes!");
            }
        });

        Button btnBackToHome = findViewById(R.id.btnBackToHome);
        btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, com.example.ceubetjava.GameSelectionActivity.class);
            intent.putExtra("isGuest", isGuest);
            if (!isGuest) {
                intent.putExtra("userId", userId);
                if (username != null) intent.putExtra("username", username);
            }
            startActivity(intent);
            finish();
        });

        logoutButton.setOnClickListener(v -> {
            if (!isGuest && userId != -1 && controller != null) {
                controller.saveCredits();
            }
            // Limpa o estado de logado
            SharedPreferences prefs = getSharedPreferences("CeubetPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();
            Intent intent = new Intent(this, com.example.ceubetjava.LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void initializeViews() {
        reel1 = findViewById(R.id.reel1);
        reel2 = findViewById(R.id.reel2);
        reel3 = findViewById(R.id.reel3);
        reel4 = findViewById(R.id.reel4);
        reel5 = findViewById(R.id.reel5);
        reel1Above = findViewById(R.id.reel1_above);
        reel2Above = findViewById(R.id.reel2_above);
        reel3Above = findViewById(R.id.reel3_above);
        reel4Above = findViewById(R.id.reel4_above);
        reel5Above = findViewById(R.id.reel5_above);
        reel1Below = findViewById(R.id.reel1_below);
        reel2Below = findViewById(R.id.reel2_below);
        reel3Below = findViewById(R.id.reel3_below);
        reel4Below = findViewById(R.id.reel4_below);
        reel5Below = findViewById(R.id.reel5_below);
        spinButton = findViewById(R.id.spin_button);
        creditsDisplay = findViewById(R.id.credits_display);
        titleDisplay = findViewById(R.id.title_display);
        messageDisplay = findViewById(R.id.message_display);
        slotMachineContainer = findViewById(R.id.slot_machine_container);
        mainContainer = findViewById(R.id.main);
    }

    private void setupLogoutButton() {
        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setText(controller.isGuest() ? "Fazer Login" : "Sair");
    }

    private void setupBetControls() {
        decreaseBetButton = findViewById(R.id.decreaseBetButton);
        increaseBetButton = findViewById(R.id.increaseBetButton);
        maxBetButton = findViewById(R.id.maxBetButton);
        betInput = findViewById(R.id.betInput);

        betInput.setText(String.valueOf(controller.getCurrentBet()));

        betInput.setOnEditorActionListener((v, actionId, event) -> {
            try {
                long newBet = Long.parseLong(betInput.getText().toString());
                if (newBet < SlotController.getMinBet()) newBet = SlotController.getMinBet();
                else if (newBet > controller.getCurrentCredits()) newBet = controller.getCurrentCredits();
                controller.setCurrentBet(newBet);
                betInput.setText(String.valueOf(controller.getCurrentBet()));
                updateBetControls();
            } catch (NumberFormatException e) {
                betInput.setText(String.valueOf(controller.getCurrentBet()));
            }
            return true;
        });

        decreaseBetButton.setOnClickListener(v -> {
            if (!controller.isSpinning() && controller.getCurrentBet() > SlotController.getMinBet()) {
                controller.playButtonClickSound();
                controller.setCurrentBet(Math.max(SlotController.getMinBet(), controller.getCurrentBet() - 1));
                betInput.setText(String.valueOf(controller.getCurrentBet()));
                updateBetControls();
            }
        });

        increaseBetButton.setOnClickListener(v -> {
            if (!controller.isSpinning() && controller.getCurrentBet() < controller.getCurrentCredits()) {
                controller.playButtonClickSound();
                controller.setCurrentBet(Math.min(controller.getCurrentCredits(), controller.getCurrentBet() + 1));
                betInput.setText(String.valueOf(controller.getCurrentBet()));
                updateBetControls();
            }
        });

        maxBetButton.setOnClickListener(v -> {
            if (!controller.isSpinning()) {
                controller.playButtonClickSound();
                controller.setCurrentBet(controller.getCurrentCredits());
                betInput.setText(String.valueOf(controller.getCurrentBet()));
                updateBetControls();
            }
        });

        updateBetControls();
    }

    private void updateBetControls() {
        boolean controlsEnabled = !controller.isSpinning();
        if (decreaseBetButton != null) {
            decreaseBetButton.setEnabled(controlsEnabled && controller.getCurrentBet() > SlotController.getMinBet());
        }
        if (increaseBetButton != null) {
            increaseBetButton.setEnabled(controlsEnabled && controller.getCurrentBet() < controller.getCurrentCredits());
        }
        if (maxBetButton != null) {
            maxBetButton.setEnabled(controlsEnabled && controller.getCurrentBet() < controller.getCurrentCredits());
        }
        if (betInput != null) {
            betInput.setEnabled(controlsEnabled);
        }
    }

    private void startSpin() {
        controller.startSpinningSound();
        SpinResult result = controller.spin();
        String[] reelResults = result.getReelResults();

        spinButton.setEnabled(false);
        updateBetControls();

        TextView[] reels = {reel1, reel2, reel3, reel4, reel5};
        TextView[] reelsAbove = {reel1Above, reel2Above, reel3Above, reel4Above, reel5Above};
        TextView[] reelsBelow = {reel1Below, reel2Below, reel3Below, reel4Below, reel5Below};

        int[] durations = {1500, 1700, 2000, 2500, 2550};

        AnimatorSet fullAnimationSet = new AnimatorSet();
        List<Animator> allReelAnimations = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            allReelAnimations.add(createReelScrollAnimation(
                    reels[i], reelsAbove[i], reelsBelow[i],
                    reelResults[i],
                    durations[i]
            ));
        }

        fullAnimationSet.playTogether(allReelAnimations);

        fullAnimationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                controller.stopSpinningSound();
                spinButton.setEnabled(true);
                updateBetControls();
                updateCreditsDisplay();
                showMessage(result.getMessage(), result.isWin());
            }
        });

        fullAnimationSet.start();
    }

    private Animator createReelScrollAnimation(TextView main, TextView above, TextView below, String finalSymbol, int duration) {
        ValueAnimator scrollAnimator = ValueAnimator.ofFloat(0f, 1f);
        scrollAnimator.setDuration(duration);
        scrollAnimator.setInterpolator(new DecelerateInterpolator(1.5f));

        String[] symbols = {"💎", "🔮", "💠", "🌟", "♦️", "⭐", "✨"};
        int symbolIndex = 0;

        ObjectAnimator mainScaleX = ObjectAnimator.ofFloat(main, "scaleX", 1f, 1.2f);
        ObjectAnimator mainScaleY = ObjectAnimator.ofFloat(main, "scaleY", 1f, 1.2f);
        mainScaleX.setDuration(duration);
        mainScaleY.setDuration(duration);
        mainScaleX.setInterpolator(new DecelerateInterpolator());
        mainScaleY.setInterpolator(new DecelerateInterpolator());

        scrollAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            int currentIndex = (int) (value * 20) % symbols.length;
            
            String currentSymbol = symbols[currentIndex];
            String aboveSymbol = symbols[(currentIndex + 1) % symbols.length];
            String belowSymbol = symbols[(currentIndex - 1 + symbols.length) % symbols.length];
            
            main.setText(currentSymbol);
            above.setText(aboveSymbol);
            below.setText(belowSymbol);
        });

        scrollAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                main.setText(finalSymbol);
                above.setText("✨");
                below.setText("✨");
                main.setScaleX(1f);
                main.setScaleY(1f);
            }
        });

        AnimatorSet reelAnimation = new AnimatorSet();
        reelAnimation.playTogether(scrollAnimator, mainScaleX, mainScaleY);
        return reelAnimation;
    }

    private void updateCreditsDisplay() {
        String formattedCredits = String.format("%,d", controller.getCurrentCredits());
        creditsDisplay.setText(String.format("Créditos: %s", formattedCredits));
        creditsDisplay.setTextColor(getResources().getColor(
            controller.getCurrentCredits() > 0 ? R.color.money_color : R.color.credits_negative
        ));
    }

    private void showMessage(String message, boolean isWin) {
        if (messageDisplay == null) return;
        if (hideMessageRunnable != null) messageHandler.removeCallbacks(hideMessageRunnable);
        
        messageDisplay.setText(message);
        messageDisplay.setBackgroundResource(R.drawable.message_background);
        messageDisplay.setVisibility(View.VISIBLE);
        messageDisplay.setAlpha(0f);
        messageDisplay.setTextColor(getResources().getColor(isWin ? R.color.title_glow : R.color.gem_red));
        messageDisplay.setShadowLayer(5, 0, 0, Color.parseColor("#80000000"));
        messageDisplay.animate().alpha(1f).setDuration(500).start();
        
        hideMessageRunnable = () -> {
            if (messageDisplay != null) {
                messageDisplay.animate().alpha(0f).setDuration(500).withEndAction(() -> {
                    if (messageDisplay != null) messageDisplay.setVisibility(View.GONE);
                }).start();
            }
        };
        messageHandler.postDelayed(hideMessageRunnable, 3000);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (controller != null) {
            controller.releaseAudioPlayers();
        }
        if (messageHandler != null && hideMessageRunnable != null) {
            messageHandler.removeCallbacks(hideMessageRunnable);
        }
    }
} 