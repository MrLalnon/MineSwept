package com.example.mineswept;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface GameDao {
    @Insert
    public void insertGame(GameEntity gameEntity);

    @Query("DELETE FROM game")
    public void ClearGames();

    @Query("SELECT * FROM game")
    public GameEntity[] loadAllGames();
}
