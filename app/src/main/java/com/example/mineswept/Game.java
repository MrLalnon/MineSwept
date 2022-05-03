package com.example.mineswept;

import android.util.Log;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Game {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public Date date;
    public int Size;
    public int Population;
    public int time;

    public Game(int id, Date date, int Size, int Population, int time){
        this.id = id;
        this.date = date;
        this.Size = Size;
        this.Population = Population;
        this.time = time;
    }

    @Override
    public String toString(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        StringBuilder OutputString = new StringBuilder();
        OutputString.append(formatter.format(date) + " | ");
        OutputString.append("<b>");
        if(Size == 9 && Population == 10)
            OutputString.append("Beginner");
        else if (Size == 16 && Population == 40)
            OutputString.append("Intermediate");
        else if (Size == 22 && Population == 99)
            OutputString.append("Expert");
        else
            OutputString.append("Size: " + Size + " | " + "Mines: " + Population);
        OutputString.append("</b>");
        OutputString.append(" | " + "Time: " + (time/60+":"+time%60));
        Log.d("seem",OutputString.toString());
        return OutputString.toString();
    }
}
