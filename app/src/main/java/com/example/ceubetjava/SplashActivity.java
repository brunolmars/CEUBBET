package com.example.ceubetjava;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class SplashActivity extends AppCompatActivity {
    private MaterialButton btnMember;
    private MaterialButton btnGuest;
    private ImageView logoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize views
        btnMember = findViewById(R.id.btnMember);
        btnGuest = findViewById(R.id.btnGuest);
        logoImage = findViewById(R.id.logoImage);

        // Set up click listeners
        btnMember.setOnClickListener(v -> {
            // Navigate to login screen
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btnGuest.setOnClickListener(v -> {
            // Navigate to game selection as guest
            Intent intent = new Intent(SplashActivity.this, GameSelectionActivity.class);
            intent.putExtra("isGuest", true);
            startActivity(intent);
        });
    }
} 