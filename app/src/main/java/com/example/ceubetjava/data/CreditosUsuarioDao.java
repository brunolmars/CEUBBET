package com.example.ceubetjava.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CreditosUsuarioDao {
    @Query("SELECT * FROM creditos_usuario WHERE usuarioId = :usuarioId AND jogoId = :jogoId LIMIT 1")
    CreditosUsuario getCreditos(int usuarioId, int jogoId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(CreditosUsuario creditosUsuario);

    @Query("UPDATE creditos_usuario SET quantidadeDeCreditos = :creditos WHERE usuarioId = :usuarioId AND jogoId = :jogoId")
    void updateCreditos(int usuarioId, int jogoId, int creditos);
} 