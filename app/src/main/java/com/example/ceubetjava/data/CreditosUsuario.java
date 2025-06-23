package com.example.ceubetjava.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
    tableName = "creditos_usuario",
    primaryKeys = {"usuarioId", "jogoId"},
    foreignKeys = {
        @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "usuarioId"),
        @ForeignKey(entity = Game.class, parentColumns = "id", childColumns = "jogoId")
    }
)
public class CreditosUsuario {
    public int usuarioId;
    public int jogoId;
    public int quantidadeDeCreditos;
} 