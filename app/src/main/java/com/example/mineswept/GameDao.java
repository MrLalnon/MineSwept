package com.example.mineswept;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface GameDao {
    @Insert
    public void insertGame(Game gameEntity);

    @Query("DELETE FROM game")
    public void ClearGames();

    @Query("SELECT * FROM game")
    public Game[] loadAllGames();

    @Query("SELECT COALESCE((SELECT MAX(id) FROM game), 1)")
    public int GetLatestId();

    @Query("SELECT * FROM game WHERE id=(SELECT MAX(id) FROM game)")
    public Game GetLatestGame();
}
