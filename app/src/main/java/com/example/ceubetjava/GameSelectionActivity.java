package com.example.ceubetjava;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.ceubetjava.slot77.view.Slot77MainActivity;
import com.example.ceubetjava.batalhanaval.BatalhaNavalActivity;
import com.example.ceubetjava.blackjack.view.BlackjackMainActivity;

public class GameSelectionActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    private boolean isGuest;
    private String currentUser;
    private int userId = -1;
    private Button logoutButton;
    private CardView slotMachineCard;
    private CardView batalhaNavalCard;
    private CardView blackjackCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selection);

        // Initialize SharedPreferences
        prefs = getSharedPreferences("CeubetPrefs", MODE_PRIVATE);
        
        // Get user info
        isGuest = getIntent().getBooleanExtra("isGuest", true);
        if (!isGuest) {
            currentUser = prefs.getString("currentUser", "");
            userId = getIntent().getIntExtra("userId", -1);
        }

        // Initialize views
        logoutButton = findViewById(R.id.logoutButton);
        slotMachineCard = findViewById(R.id.slotMachineCard);
        batalhaNavalCard = findViewById(R.id.batalhaNavalCard);
        blackjackCard = findViewById(R.id.blackjackCard);

        // Set up logout button
        setupLogoutButton();

        // Set up game cards
        setupGameCards();
    }

    private void setupLogoutButton() {
        if (isGuest) {
            logoutButton.setText("Fazer Login");
        } else {
            logoutButton.setText("Sair");
        }
        
        logoutButton.setOnClickListener(v -> {
            if (!isGuest) {
                // Save user data before logout
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();
            }
            
            // Return to login screen
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setupGameCards() {
        // Slot Machine card click listener
        slotMachineCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, Slot77MainActivity.class);
            intent.putExtra("isGuest", isGuest);
            if (!isGuest) {
                intent.putExtra("username", currentUser);
                intent.putExtra("userId", userId);
            }
            startActivity(intent);
        });

        // Batalha Naval card click listener
        batalhaNavalCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, BatalhaNavalActivity.class);
            intent.putExtra("isGuest", isGuest);
            if (!isGuest) {
                intent.putExtra("username", currentUser);
                intent.putExtra("userId", userId);
            }
            startActivity(intent);
        });

        // Blackjack card click listener
        blackjackCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, BlackjackMainActivity.class);
            intent.putExtra("isGuest", isGuest);
            if (!isGuest) {
                intent.putExtra("username", currentUser);
                intent.putExtra("userId", userId);
            }
            startActivity(intent);
        });
    }
} 