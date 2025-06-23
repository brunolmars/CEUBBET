package com.example.ceubetjava.batalhanaval;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ceubetjava.R;
import com.example.ceubetjava.data.AppDatabase;
import com.example.ceubetjava.data.CreditosUsuario;
import com.example.ceubetjava.data.CreditosUsuarioDao;
import com.example.ceubetjava.data.Game;
import com.example.ceubetjava.data.GameDao;
import com.example.ceubetjava.data.UserDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BatalhaNavalActivity extends AppCompatActivity {
    private GridLayout gridLayout;
    private List<Button> buttons;
    private int numQuadrados = 16;
    private TextView tvScore;
    private int pontosApostados = 10;
    private int multiplicador = 1;
    private Button btnStart;
    private Button btnEnd;
    private boolean jogoAtivo = false;
    private boolean isGuest;
    private int userId;
    private int gameId = -1; 
    private CreditosUsuarioDao creditosUsuarioDao;
    private CreditosUsuario creditosUsuario;
    private Button[] gridButtons;
    private int[] grid;
    private int score;
    private boolean gameStarted;
    private Random random;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batalha_naval);

        isGuest = getIntent().getBooleanExtra("isGuest", true);
        userId = getIntent().getIntExtra("userId", -1);
        AppDatabase db = AppDatabase.getInstance(this);
        creditosUsuarioDao = db.creditosUsuarioDao();
        GameDao gameDao = db.gameDao();
        UserDao userDao = db.userDao();

        if (!isGuest && userId != -1) {
            AppDatabase.executor.execute(() -> {
                Game bnGame = gameDao.getGameByName("Batalha Naval");
                if (bnGame == null) return;
                gameId = bnGame.id;
                com.example.ceubetjava.data.User user = userDao.getUserById(userId);
                if (user == null) return;
                creditosUsuario = creditosUsuarioDao.getCreditos(userId, gameId);
                if (creditosUsuario == null) {
                    creditosUsuario = new CreditosUsuario();
                    creditosUsuario.usuarioId = userId;
                    creditosUsuario.jogoId = gameId;
                    creditosUsuario.quantidadeDeCreditos = 100;
                    creditosUsuarioDao.insertOrUpdate(creditosUsuario);
                }
                score = creditosUsuario.quantidadeDeCreditos;
                runOnUiThread(this::updateScore);
            });
        } else {
            score = 30;
        }

        gridButtons = new Button[16];
        grid = new int[16];
        gameStarted = false;
        random = new Random();

        for (int i = 0; i < 16; i++) {
            int buttonId = getResources().getIdentifier("btn" + (i + 1), "id", getPackageName());
            gridButtons[i] = findViewById(buttonId);
            final int index = i;
            gridButtons[i].setOnClickListener(v -> onButtonClick(index));
        }

        btnStart = findViewById(R.id.btnStart);
        btnEnd = findViewById(R.id.btnEnd);
        tvScore = findViewById(R.id.tvScore);

        btnStart.setOnClickListener(v -> startGame());
        btnEnd.setOnClickListener(v -> endGame());

        gridLayout = findViewById(R.id.gridLayout);
        buttons = new ArrayList<>();

        for (int i = 0; i < numQuadrados; i++) {
            int resID = getResources().getIdentifier("btn" + (i + 1), "id", getPackageName());
            Button button = findViewById(resID);
            buttons.add(button);
        }

        Button btnBackToHome = findViewById(R.id.btnBackToHome);
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setText(isGuest ? "Fazer Login" : "Sair");

        btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, com.example.ceubetjava.GameSelectionActivity.class);
            intent.putExtra("isGuest", isGuest);
            if (!isGuest) {
                intent.putExtra("userId", userId);
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
                salvarCreditos();
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

    private void startGame() {
        if (gameStarted) {
            Toast.makeText(this, "Jogo já iniciado!", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < 16; i++) {
            grid[i] = 0;
            gridButtons[i].setBackgroundTintList(getResources().getColorStateList(R.color.purple_500, null));
            gridButtons[i].setEnabled(true);
        }
        int shipsPlaced = 0;
        while (shipsPlaced < 4) {
            int position = random.nextInt(16);
            if (grid[position] == 0) {
                grid[position] = 1;
                shipsPlaced++;
            }
        }
        gameStarted = true;
        updateScore();
        Toast.makeText(this, "Jogo iniciado! Encontre os navios!", Toast.LENGTH_SHORT).show();
    }

    private void endGame() {
        if (!gameStarted) {
            Toast.makeText(this, "Nenhum jogo em andamento!", Toast.LENGTH_SHORT).show();
            return;
        }
        gameStarted = false;
        for (Button button : gridButtons) {
            button.setEnabled(false);
        }
        for (int i = 0; i < 16; i++) {
            if (grid[i] == 1) {
                gridButtons[i].setBackgroundTintList(getResources().getColorStateList(R.color.green, null));
            }
        }
        Toast.makeText(this, "Jogo finalizado! Pontuação: " + score, Toast.LENGTH_LONG).show();
        salvarCreditos();
    }

    private void onButtonClick(int index) {
        if (!gameStarted) {
            Toast.makeText(this, "Inicie o jogo primeiro!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (grid[index] == 1) {
            gridButtons[index].setBackgroundTintList(getResources().getColorStateList(R.color.green, null));
            score += 10;
            grid[index] = 2;
        } else if (grid[index] == 0) {
            gridButtons[index].setBackgroundTintList(getResources().getColorStateList(R.color.red, null));
            score -= 5;
            grid[index] = 3;
        }
        gridButtons[index].setEnabled(false);
        updateScore();
        boolean gameOver = true;
        for (int value : grid) {
            if (value == 1) {
                gameOver = false;
                break;
            }
        }
        if (gameOver) {
            Toast.makeText(this, "Parabéns! Você venceu! Pontuação: " + score, Toast.LENGTH_LONG).show();
            gameStarted = false;
            for (Button button : gridButtons) {
                button.setEnabled(false);
            }
            salvarCreditos();
        }
    }

    private void updateScore() {
        tvScore.setText("Pontos: " + score);
    }

    private void salvarCreditos() {
        if (!isGuest && creditosUsuario != null && gameId != -1) {
            creditosUsuario.quantidadeDeCreditos = score;
            AppDatabase.executor.execute(() -> {
                creditosUsuarioDao.updateCreditos(userId, gameId, score);
            });
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        salvarCreditos();
    }
} 