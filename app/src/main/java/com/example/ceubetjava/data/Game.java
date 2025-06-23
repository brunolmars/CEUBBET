package com.example.ceubetjava.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "games")
public class Game {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String nomeDoJogo;
} 