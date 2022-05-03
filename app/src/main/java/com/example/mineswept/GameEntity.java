package com.example.mineswept;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class GameEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public Date date;
    public int Size;
    public int Population;
    public int time;
}
