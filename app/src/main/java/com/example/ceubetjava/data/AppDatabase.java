package com.example.ceubetjava.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.util.Log;

import com.example.ceubetjava.data.UserDao;
import com.example.ceubetjava.data.User;
import com.example.ceubetjava.data.GameDao;
import com.example.ceubetjava.data.Game;
import com.example.ceubetjava.data.CreditosUsuarioDao;
import com.example.ceubetjava.data.CreditosUsuario;

@Database(entities = {User.class, Game.class, CreditosUsuario.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "ceubet_db";
    private static volatile AppDatabase instance;
    public static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final String TAG = "AppDatabase";

    public abstract UserDao userDao();
    public abstract GameDao gameDao();
    public abstract CreditosUsuarioDao creditosUsuarioDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .addCallback(new RoomDatabase.Callback() {
                @Override
                public void onCreate(SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    Log.d(TAG, "Database created for the first time. Populating games table.");
                    executor.execute(() -> {
                        AppDatabase database = getInstance(context);
                        GameDao gameDao = database.gameDao();
                        if (gameDao.getAllGames().isEmpty()) {
                            Game slot = new Game();
                            slot.nomeDoJogo = "Slot77";
                            gameDao.insert(slot);
                            Game bj = new Game();
                            bj.nomeDoJogo = "Blackjack";
                            gameDao.insert(bj);
                            Game bn = new Game();
                            bn.nomeDoJogo = "Batalha Naval";
                            gameDao.insert(bn);
                        }
                    });
                }
            })
            .build();
        }
        return instance;
    }
} 