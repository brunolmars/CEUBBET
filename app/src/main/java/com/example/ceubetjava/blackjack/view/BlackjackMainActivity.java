package com.example.ceubetjava.blackjack.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ceubetjava.R;

/**
 * Activity inicial do jogo de Blackjack
 */
public class BlackjackMainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_blackjack);
        
        // Inicializa as views
        TextView tvTitle = findViewById(R.id.tv_title);
        Button btnPlay = findViewById(R.id.btn_play);
        Button btnRules = findViewById(R.id.btn_rules);
        
        // Configura os listeners dos botões
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia o jogo
                Intent intent = new Intent(BlackjackMainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });
        
        btnRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostra as regras
                Intent intent = new Intent(BlackjackMainActivity.this, RulesActivity.class);
                startActivity(intent);
            }
        });
    }
}
