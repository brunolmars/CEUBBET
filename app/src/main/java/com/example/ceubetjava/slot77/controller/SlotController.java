package com.example.ceubetjava.slot77.controller;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.ceubetjava.R;
import com.example.ceubetjava.data.AppDatabase;
import com.example.ceubetjava.data.CreditosUsuario;
import com.example.ceubetjava.data.CreditosUsuarioDao;
import com.example.ceubetjava.data.Game;
import com.example.ceubetjava.data.GameDao;
import com.example.ceubetjava.data.UserDao;
import com.example.ceubetjava.slot77.model.SlotMachine;
import com.example.ceubetjava.slot77.model.SpinResult;

/**
 * Controlador que gerencia a comunicação entre a View e o Model do caça-níquel
 */
public class SlotController {
    private final SlotMachine slotMachine;
    private final Context context;
    private final boolean isGuest;
    private final int userId;
    private int gameId = -1; // será definido dinamicamente
    private final CreditosUsuarioDao creditosUsuarioDao;
    private CreditosUsuario creditosUsuario;
    private MediaPlayer spinningSound;
    private MediaPlayer winSound;
    private MediaPlayer buttonClickSound;
    private OnCreditsUpdateListener creditsUpdateListener;

    public interface OnCreditsUpdateListener {
        void onCreditsUpdated(long newCredits);
    }

    public void setOnCreditsUpdateListener(OnCreditsUpdateListener listener) {
        this.creditsUpdateListener = listener;
    }

    public SlotController(Context context, boolean isGuest, int userId, long initialCredits) {
        this.context = context;
        this.isGuest = isGuest;
        this.userId = userId;
        AppDatabase db = AppDatabase.getInstance(context);
        this.creditosUsuarioDao = db.creditosUsuarioDao();
        GameDao gameDao = db.gameDao();
        UserDao userDao = db.userDao();
        this.slotMachine = new SlotMachine(initialCredits);

        if (!isGuest && userId != -1) {
            AppDatabase.executor.execute(() -> {
                Game slotGame = gameDao.getGameByName("Slot77");
                if (slotGame == null) return;
                gameId = slotGame.id;
                com.example.ceubetjava.data.User user = userDao.getUserById(userId);
                if (user == null) return;
                creditosUsuario = creditosUsuarioDao.getCreditos(userId, gameId);
                if (creditosUsuario == null) {
                    creditosUsuario = new CreditosUsuario();
                    creditosUsuario.usuarioId = userId;
                    creditosUsuario.jogoId = gameId;
                    creditosUsuario.quantidadeDeCreditos = (int) initialCredits;
                    creditosUsuarioDao.insertOrUpdate(creditosUsuario);
                }
                slotMachine.setCredits(creditosUsuario.quantidadeDeCreditos);
                if (creditsUpdateListener != null) {
                    creditsUpdateListener.onCreditsUpdated(creditosUsuario.quantidadeDeCreditos);
                }
            });
        }

        initializeAudioPlayers();
    }

    private void initializeAudioPlayers() {
        spinningSound = MediaPlayer.create(context, R.raw.spinning_counter);
        winSound = MediaPlayer.create(context, R.raw.prize_win);
        buttonClickSound = MediaPlayer.create(context, R.raw.button_click);

        if (spinningSound != null) {
            spinningSound.setLooping(true);
        }
    }

    public void releaseAudioPlayers() {
        if (spinningSound != null) {
            spinningSound.release();
            spinningSound = null;
        }
        if (winSound != null) {
            winSound.release();
            winSound = null;
        }
        if (buttonClickSound != null) {
            buttonClickSound.release();
            buttonClickSound = null;
        }
    }

    public SpinResult spin() {
        SpinResult result = slotMachine.spin();
        if (result.isWin() && winSound != null) {
            winSound.start();
        }
        if (!isGuest && userId != -1) {
            saveCredits();
        }
        return result;
    }

    public void playButtonClickSound() {
        if (buttonClickSound != null) {
            buttonClickSound.start();
        }
    }

    public void saveCredits() {
        if (!isGuest && userId != -1 && creditosUsuario != null && gameId != -1) {
            int novosCreditos = (int) slotMachine.getCurrentCredits();
            creditosUsuario.quantidadeDeCreditos = novosCreditos;
            AppDatabase.executor.execute(() -> {
                creditosUsuarioDao.updateCreditos(userId, gameId, novosCreditos);
                if (creditsUpdateListener != null) {
                    creditsUpdateListener.onCreditsUpdated(novosCreditos);
                }
            });
        }
    }

    public long getCurrentCredits() {
        return slotMachine.getCurrentCredits();
    }

    public long getCurrentBet() {
        return slotMachine.getCurrentBet();
    }

    public boolean isSpinning() {
        return slotMachine.isSpinning();
    }

    public void setCurrentBet(long bet) {
        slotMachine.setCurrentBet(bet);
    }

    public void addCredits(long amount) {
        slotMachine.addCredits(amount);
    }

    public static long getMinBet() {
        return SlotMachine.getMinBet();
    }

    public boolean isGuest() {
        return isGuest;
    }

    public int getUserId() {
        return userId;
    }

    public void startSpinningSound() {
        if (spinningSound != null && !spinningSound.isPlaying()) {
            spinningSound.start();
        }
    }

    public void stopSpinningSound() {
        if (spinningSound != null && spinningSound.isPlaying()) {
            spinningSound.pause();
            spinningSound.seekTo(0);
        }
    }
} 