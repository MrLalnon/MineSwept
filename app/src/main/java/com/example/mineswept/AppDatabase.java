package com.example.mineswept;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {GameEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GameDao gameDao();
}
