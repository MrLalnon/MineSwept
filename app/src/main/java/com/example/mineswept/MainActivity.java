package com.example.mineswept;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    BoardGame boardGame;
    Boolean Search = true;
    ImageButton btnFlag;
    Button btnReset;
    TextView txtBombCount;
    TextView txtTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFlag = new ImageButton(this);
        btnFlag = findViewById(R.id.btnFlag);
        btnReset = findViewById(R.id.btnReset);
        txtBombCount = findViewById(R.id.txtBombCount);
        txtTimer = findViewById(R.id.txtTimer);

        frameLayout=findViewById(R.id.frame);
        boardGame = new BoardGame(this, new View[]{btnReset, txtBombCount, txtTimer});
        frameLayout.addView(boardGame);
    }

    public void Reset(View view) {
        boardGame.Reset();
    }

    public void Flagger(View view) {
        Search = !Search;
        if(Search){
            btnFlag.setImageResource(R.drawable.ic_search);
            boardGame.FlagMode = false;
        }else{
            btnFlag.setImageResource(R.drawable.ic_flag);
            boardGame.FlagMode = true;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.difficultymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.beginner:
                boardGame.GameSettings(9,9,10);
                return true;
            case R.id.intermediate:
                boardGame.GameSettings(16,16,40);
                return true;
            case R.id.expert:
                boardGame.GameSettings(22,22,99);
                return true;
            case R.id.custom:
                customField(boardGame);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void customField(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Custom Field");

        LayoutInflater inflater = this.getLayoutInflater();
        View inflated = inflater.inflate(R.layout.customgrid, null);
        builder.setView(inflated);
        final EditText editTextSize = inflated.findViewById(R.id.editTextSize);
        final EditText editTextPopulation = inflated.findViewById(R.id.editTextPopulation);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!editTextSize.getText().toString().equals("") && !editTextPopulation.getText().toString().equals("")) {
                    int customSize = Integer.valueOf(editTextSize.getText().toString());
                    int customPopulation = Integer.valueOf(editTextPopulation.getText().toString());
                    boardGame.GameSettings(Math.max(10,Math.min(customSize, 22)),
                            Math.max(10,Math.min(customSize, 22)),
                            (int) Math.max(Math.min(Math.pow(Math.min(customSize,22) - 1, 2), customPopulation), 10));
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


}