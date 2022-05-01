package com.example.mineswept;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.icu.util.Output;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Size;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class BoardGame extends View {
    int sizeW = 9, sizeH = 9, population = 10; //Game Settings
    int flagcount = 0;
    int time=0;
    Square[][]squares;
    Context context;
    Boolean[][] Bombs, Open, Flags;
    int[][] nums; //Field Numbers
    static int count; //Amount of open cells
    public Boolean FlagMode;
    Boolean Playing = false;
    Boolean LoseState = false; //Graphics should display in lose state
    Button btnReset;
    TextView txtBombCount;
    TextView txtTimer;
    CountDownTimer CountDown;
    int[] LastCellPressed = new int[]{-1,-1};

    public BoardGame(Context context, View[] views)
    {
        super(context);
        this.context=context;
        squares=new Square[sizeW][sizeH];
        Bombs = new Boolean[sizeW][sizeH];
        Open = new Boolean[sizeW][sizeH];
        nums = new int[sizeW][sizeH];
        Flags = new Boolean[sizeW][sizeH];
        count = sizeW*sizeH;
        FlagMode = false;
        for (int i = 0; i < Open.length; i++){
            for (int j = 0; j < Open[0].length; j++){
                Open[i][j] = false;
                Bombs[i][j] = false;
                Flags[i][j] = false;
            }
        }
        Bombs = GenField(sizeW, sizeH, population, 0,0);
        this.btnReset = (Button)views[0];
        this.txtBombCount = (TextView)views[1];
        this.txtTimer = (TextView)views[2];
        txtBombCount.setText(NumToDisplayStr(population));

        time = 0;
        CountDown = new CountDownTimer(999000, 1000) {

            public void onTick(long millisUntilFinished) {
                txtTimer.setText(NumToDisplayStr((int)(1000-((double)millisUntilFinished/1000))));
                time++;
            }

            public void onFinish() {

            }
        };
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        drawBoard(canvas);
    }
    public void drawBoard(Canvas canvas) {
        int h = canvas.getHeight()/sizeH;
        int w = canvas.getWidth()/sizeW;
        for (int i = 0; i < sizeW; i++) {
            for (int j = 0; j < sizeH; j++) {
                squares[i][j] = new Square(this, i*w, j*h, w, h, Open[i][j], Bombs[i][j], nums[i][j], Flags[i][j], LastCellPressed, LoseState);
                squares[i][j].draw(canvas);
                //Log.d("seem", "drew");
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int[] XY = onTouchGetCellXY(event);
        int i = XY[0], j = XY[1];
        if (Playing && i!= -1) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (!FlagMode) {
                    if (!Open[i][j] && !Bombs[i][j] && !Flags[i][j]) {
                        Spread(i, j);
                    }else
                    if (Bombs[i][j] && !Flags[i][j]) {
                        Lose();
                    }else
                    if(Open[i][j] && nums[i][j]!= 0){
                        if(nums[i][j] == FlagsAroundCell(i,j)){
                            Log.d("seem", "used QuickOpen at " + i + ", " + j);
                            QuickOpen(i,j);
                        }
                    }
                } else if (FlagMode) {
                    if (!Open[i][j]) {
                        if (Flags[i][j])
                            flagcount--;
                        if (!Flags[i][j])
                            flagcount++;
                        Flags[i][j] = !Flags[i][j];
                        txtBombCount.setText(NumToDisplayStr(population - flagcount));
                    }
                }
                if (Playing)
                    btnReset.setText("\uD83D\uDE42");
            }
            invalidate();

            if (event.getAction() == MotionEvent.ACTION_DOWN && Playing)
                btnReset.setText("\uD83D\uDE2E");
        }

        if (!Playing && count == sizeW * sizeH) {
            CountDown.start();
            if (i != -1 && j != -1)
                Bombs = GenField(sizeW, sizeH, population, i, j);
            Playing = true;
        }

        win();
        return true;
    }

    public int[] onTouchGetCellXY(MotionEvent event){
        if(squares[0][0] != null) {
            for (int i = 0; i < squares.length; i++) {
                for (int j = 0; j < squares[0].length; j++) {
                    if (event.getX() > i * squares[i][j].w && event.getX() < (i + 1) * squares[i][j].w) {
                        if (event.getY() > j * squares[i][j].h && event.getY() < (j + 1) * squares[i][j].h) {
                            Log.d("seem", "square " + i + " " + j);
                            LastCellPressed = new int[]{i, j};
                            return new int[]{i, j};
                        }
                    }
                }
            }
        }

        return new int[]{-1,-1};
    }

    public int FlagsAroundCell(int x, int y){
        int count = 0;
        for(int i = -1; i < 2; i++){
            for (int j = -1; j <2; j++){
                if(i == 0 && j == 0)
                    continue;
                try {
                    if(Flags[x+i][y+j])
                        count++;
                } catch (ArrayIndexOutOfBoundsException e) {}
            }
        }

        return count;
    }

    public void QuickOpen(int x, int y) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0)
                    continue;
                try {
                    if (!Flags[x + i][y + j]){
                        if (!Open[x + i][y + j] && Bombs[x + i][y + j]) {
                            LastCellPressed = new int[]{x + i, y + j};
                            Lose();
                        } else if (!Open[x + i][y + j]) {
                            Spread(x + i, y + j);
                        }
                    }
                }catch(ArrayIndexOutOfBoundsException e){
                }

            }
        }
    }

    public Boolean[][] GenField(int width, int height, int population, int xp, int yp){
        Random rnd = new Random();
        Boolean[][] field = new Boolean[width][height];
        for(int i = 0; i < field.length; i++){
            Arrays.fill(field[i], false);
        }
        int rw, rh;
        for (int i = 0; i < population; i++){
            rw = rnd.nextInt(width);
            rh = rnd.nextInt(height);
            if(field[rw][rh] || (rw == xp && rh == yp) )
                i--;
            else
                field[rw][rh] = true;
        }


        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                int count = 0;
                for(int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        try {
                            if(field[x+i][y+j])
                                count++;
                        } catch (ArrayIndexOutOfBoundsException e) {

                        }
                    }
                }
                nums[x][y] = count;
            }
        }

        return field;
    }

    public void Spread(int x, int y){  //modified flood_fill() algorithm - opens all cells that are 0 and also neighbors
        if(!Open[x][y] && !Bombs[x][y]) {
            Open[x][y] = true;
            count--;
        }

        if(nums[x][y] == 0){
            for(int i = -1; i < 2; i++){
                for (int j = -1; j <2; j++){
                    if(i == 0 && j == 0)
                        continue;
                    try {
                        if(Open[x+i][y+j] || Flags[x+i][y+j])
                            continue;
                        Open[x+i][y+j] = true;
                        count--;
                        Spread(x+i,y+j);
                    } catch (ArrayIndexOutOfBoundsException e) {}
                }
            }
        }

        win();
        return;
    }

    public void Reset(){
        count = sizeW*sizeH;
        squares=new Square[sizeW][sizeH];
        Bombs = new Boolean[sizeW][sizeH];
        Open = new Boolean[sizeW][sizeH];
        nums = new int[sizeW][sizeH];
        Flags = new Boolean[sizeW][sizeH];
        for (int i = 0; i < Open.length; i++){
            for (int j = 0; j < Open[0].length; j++){
                Open[i][j] = false;
                Bombs[i][j] = false;
                Flags[i][j] = false;
            }
        }
        Bombs = GenField(sizeW, sizeH, population,0,0);
        btnReset.setText("\uD83D\uDE42");
        flagcount = 0;
        txtBombCount.setText(NumToDisplayStr(population));
        CountDown.cancel();
        time = 0;
        Playing = false;
        txtTimer.setText("000");
        LoseState = false;
        invalidate();
    }

    public String NumToDisplayStr(int in){
        if(in > 999)
            return "999";
        if(in<=0)
            return "000";

        if(in>99)
            return String.valueOf(in);
        if(in>9)
            return "0" + String.valueOf(in);
        else
            return "00" + String.valueOf(in);
    }

    public void Lose(){
        CountDown.cancel();
        Playing = false;
        LoseState = true;
        btnReset.setText("\uD83D\uDE41");
    }

    public boolean win() {
        if (count == population) {
            btnReset.setText("\uD83D\uDE0E");
            txtBombCount.setText("000");
            CountDown.cancel();
            Playing = false;
            WinDialog();
            return true;
        }
        return false;
    }

    public void GameSettings(int sizeH, int sizeW, int population){
        this.sizeH = sizeH;
        this.sizeW = sizeW;
        this.population = population;
        Reset();
    }

    public void WinDialog(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        int a;
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Save your Score?\n" + this.toString()).setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    @Override
    public String toString(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        StringBuilder OutputString = new StringBuilder();
        OutputString.append(formatter.format(date) + " | ");
        if(sizeW == 9 && population == 10)
            OutputString.append("Beginner");
        else if (sizeW == 16 && population == 40)
            OutputString.append("Intermediate");
        else if (sizeW == 22 && population == 99)
            OutputString.append("Expert");
        else
            OutputString.append("Size: " + sizeW + " | " + "Mines: " + population);
        OutputString.append(" | " + "Time: " + (time/60+":"+time%60));
        Log.d("seem",OutputString.toString());
        return OutputString.toString();
    }
}
