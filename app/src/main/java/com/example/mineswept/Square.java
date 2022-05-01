package com.example.mineswept;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

public class Square {
    BoardGame boardGame;
    float x,y,w,h;//top left
    Paint p;
    public Boolean open;
    public Boolean bomb;
    int num;
    Boolean Flag, LoseState;
    int[] LastCellPressed;

    public Square(BoardGame boardGame,float x,float y,float w,float h, Boolean open, Boolean bomb, int num, Boolean Flag, int[] LastCellPressed, Boolean LoseState)
    {
        this.x = x;
        this.y = y;
        this.boardGame = boardGame;
        p = new Paint();
        this.w = w;
        this.h = h;
        this.open = open;
        this.bomb = bomb;
        this.num = num;
        this.Flag = Flag;
        this.LoseState = LoseState;
        this.LastCellPressed = LastCellPressed;
    }
    public void draw(Canvas canvas)
    {
        //Regular Cell
        if(!open)
            drawBlock(canvas);

        //Regular Open Cell
        if(open){
            drawCell(canvas);
        }

        //Unmarked Bombs when Lose
        if(bomb && LoseState && !Flag){
            drawCell(canvas);
            p.setStyle(Paint.Style.FILL);
            //Is this the bomb that made you lose?
            if(x/w == LastCellPressed[0] && y/h == LastCellPressed[1]){
                p.setColor(Color.rgb(255,0,0));
                canvas.drawRect(x,y,x+w,y+h,p);
            }
            drawBomb(canvas);

            /*if(open){
                p.setColor(Color.rgb(255,0,0));
                p.setStrokeWidth(w/12);
                canvas.drawLine(x+(w/6),y+(h/6),x+(5*w/6),y+(5*h/6),p);
                canvas.drawLine(x+(5*w/6),y+(h/6),x+(w/6),y+(5*h/6),p);
            }*/
        }

        //Open Cell With Number
        if(num > 0 && !bomb && open){
            drawNum(canvas);
        }

        if(Flag){
            /*p.setStyle(Paint.Style.FILL);
            p.setColor(Color.argb(125,0,255,0));
            canvas.drawRect(x,y,x+w,y+h,p);*/
            drawBlock(canvas);
            p.setStyle(Paint.Style.FILL);
            p.setTextSize(2*h/3);
            p.setTypeface(ResourcesCompat.getFont(boardGame.context, R.font.minesweeper));
            p.setTextAlign(Paint.Align.CENTER);
            p.setColor(Color.rgb(255,0,0));
            canvas.drawText("!", x+(w/2), y+(5* h/6), p);
            //Wrong Flag when Lost
            if(!bomb && LoseState){
                drawCell(canvas);
                drawBomb(canvas);
                p.setColor(Color.rgb(255,0,0));
                p.setStrokeWidth((h+w)/2/15);
                canvas.drawLine(x+(w/15),y+(h/15),x+(14*w/15),y+(14*h/15),p);
                canvas.drawLine(x+(w/15),y+(14*h/15),x+(14*w/15),y+(h/15),p);
            }
        }
    }

    public void drawBlock(Canvas canvas){
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.rgb(255,255,255));
        canvas.drawRect(x,y,x+w,y+h,p);

        Path path = new Path();
        path.moveTo(x, y + h); // Bottom Left
        path.lineTo(x + w, y); // Top Right
        path.lineTo(x + w, y + h); // Bottom right
        path.lineTo(x, y + h); // Back to Bottom Left
        path.close();
        p.setColor(Color.rgb(123,123,123));
        canvas.drawPath(path, p);

        p.setColor(Color.rgb(189,189,189));
        canvas.drawRect(x+(w/6),y+(h/6),x+(5*w/6),y+(5*h/6),p);
    }

    public void drawCell(Canvas canvas){
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.rgb(189,189,189));
        canvas.drawRect(x,y,x+w,y+h,p);

        p.setStyle(Paint.Style.STROKE);
        float SW = 4;
        p.setStrokeWidth(SW);
        p.setColor(Color.rgb(123,123,123));
        canvas.drawRect(x+(SW/2),y+(SW/2),x+w-(SW/2),y+h-(SW/2),p);
    }

    public void drawBomb(Canvas canvas){
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);
        //canvas.drawCircle(x+(w/2), y+(h/2), w*0.3f,p);
        canvas.drawOval(x+(w/2)-w*0.3f,y+(h/2)-h*0.3f,x+(w/2)+w*0.3f,y+(h/2)+h*0.3f,p);
        p.setStrokeWidth(w/15);
        canvas.drawLine(x+(w/2),y+(h/15),x+(w/2),y+(14*h/15),p);
        canvas.drawLine(x+(w/15),y+(h/2),x+(14*w/15),y+(h/2),p);
        canvas.drawLine(x+(3*w/15), y+(3*h/15), x+(12*w/15), y+(12*h/15),p);
        canvas.drawLine(x+(3*w/15), y+(12*h/15), x+(12*w/15), y+(3*h/15),p);
    }

    public void drawNum(Canvas canvas){
        p.setStyle(Paint.Style.FILL);
        p.setTextSize(3*h/5);
        p.setTypeface(ResourcesCompat.getFont(boardGame.context, R.font.minesweeper));
        p.setTextAlign(Paint.Align.CENTER);
        switch (num){
//            case 0:
//                p.setColor(Color.rgb(128,128,128));
//                break;
            case 1:
                p.setColor(Color.rgb(0,0,255));
                break;
            case 2:
                p.setColor(Color.rgb(0,128,0));
                break;
            case 3:
                p.setColor(Color.rgb(255,0,0));
                break;
            case 4:
                p.setColor(Color.rgb(0,0,128));
                break;
            case 5:
                p.setColor(Color.rgb(128,0,0));
                break;
            case 6:
                p.setColor(Color.rgb(0,128,128));
                break;
            case 7:
                p.setColor(Color.rgb(0,0,0));
                break;
            case 8:
                p.setColor(Color.rgb(128,128,128));
                break;
        }
        canvas.drawText(String.valueOf(num), x+(w/2), y+(5* h/6), p);
    }
}
