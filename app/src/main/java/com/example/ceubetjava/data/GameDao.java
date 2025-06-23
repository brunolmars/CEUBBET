package com.example.ceubetjava.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface GameDao {
    @Insert
    long insert(Game game);

    @Query("SELECT * FROM games")
    List<Game> getAllGames();

    @Query("SELECT * FROM games WHERE nomeDoJogo = :nome LIMIT 1")
    Game getGameByName(String nome);
} 