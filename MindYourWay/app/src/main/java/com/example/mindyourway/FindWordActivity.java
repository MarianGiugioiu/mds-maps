package com.example.mindyourway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.PorterDuff;
import android.widget.Toast;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class FindWordActivity extends AppCompatActivity {

    private static final String TAG = "FindWordActivity";

    private Button[][] buttonsMatrix = new Button[10][10];
    private Button[] buttonMesaj = new Button[10];
    private Button[] buttonCuvant = new Button[10];
    private Button Retry;
    private Button Back;
    private Button CompleteGame;

    private int Clickuri = 0; ///de asta nu-mi pasa ramane
    private int StartX = 0;   ///de asta nu-mi pasa ramane
    private int StartY = 0;   ///de asta nu-mi pasa ramane
    private int FinishX = 0;  ///de asta nu-mi pasa ramane
    private int FinishY = 0;  ///de asta nu-mi pasa ramane

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StartGame();
    }

    private void AfiseazaMesaj(String Mesaj) {

        for(int i = 1; i <= 9; ++i) {
            buttonMesaj[i].setText(String.valueOf(Mesaj.charAt(i)));
            if(String.valueOf(Mesaj.charAt(i)).contains(" ")) {
                buttonMesaj[i].getBackground().setColorFilter( 0xFF565472, PorterDuff.Mode.MULTIPLY);
            }
            else {
                buttonMesaj[i].getBackground().setColorFilter(0xFFe8ab71, PorterDuff.Mode.MULTIPLY);
            }
        }
    }

    private void AfiseazaStatus(int StatusX,int StatusY) {
        String stringID = "CuvinteGasite";
        int ID = MainActivity.this.getResources().getIdentifier(stringID, "id", MainActivity.this.getPackageName());
        final TextView Stare = (TextView) findViewById(ID);
        Stare.setText("Status: "+String.valueOf(StatusX)+"/"+String.valueOf(StatusY));
    }


    private void AfiseazaTimp(int Minute,int Secunde) {
        String stringID = "Timer";
        int ID = MainActivity.this.getResources().getIdentifier(stringID, "id", FindWordActivity.this.getPackageName());
        final TextView Stare = (TextView) findViewById(ID);
        String sec = "";
        if(Secunde > 9) {
            sec = String.valueOf(Secunde);
        }
        else {
            sec = "0" + String.valueOf(Secunde);
        }
        Stare.setText(String.valueOf(Minute)+":"+sec);
    }

    private void AfiseazaCuvant(String Mesaj) {

        for(int i = 1; i <= 9; ++i) {
            buttonCuvant[i].setText(String.valueOf(Mesaj.charAt(i)));
            if(String.valueOf(Mesaj.charAt(i)).contains(" ")) {
                buttonCuvant[i].getBackground().setColorFilter( 0xFF565472, PorterDuff.Mode.MULTIPLY);
            }
            else {
                buttonCuvant[i].getBackground().setColorFilter(0xFFe67575, PorterDuff.Mode.MULTIPLY);
            }
        }
    }

    private void AfiseazaMatrice(char[][] Lit) {
        for(int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                buttonsMatrix[i][j].setText(String.valueOf(Lit[i][j]));
            }
        }
    }

    private void drawLinie(int tip) {
        if(tip == 1) {
            for( int j = StartY; j <= FinishY ; ++j) {
                buttonsMatrix[StartX][j].getBackground().setColorFilter(0xFFB7F748, PorterDuff.Mode.MULTIPLY);
            }
        }
        else if(tip == 2) {
            for( int i = StartX; i <= FinishX ; ++i) {
                buttonsMatrix[i][StartY].getBackground().setColorFilter(0xFFB7F748, PorterDuff.Mode.MULTIPLY);
            }
        }
        final int Tip = tip;

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if(Tip == 1) {
                            for( int j = StartY; j <= FinishY ; ++j) {
                                buttonsMatrix[StartX][j].getBackground().setColorFilter(0xFFe67575, PorterDuff.Mode.MULTIPLY);
                            }
                        }
                        else if(Tip == 2) {
                            for( int i = StartX; i <= FinishX ; ++i) {
                                buttonsMatrix[i][StartY].getBackground().setColorFilter(0xFFe67575, PorterDuff.Mode.MULTIPLY);
                            }
                        }
                    }
                },
                500);
    }

    private void StartGame() {

        ArrayList<Integer> ListaOrdDif = new ArrayList<Integer>();
        ListaOrdDif.add(0);
        ListaOrdDif.add(1);
        ListaOrdDif.add(2);
        Collections.shuffle(ListaOrdDif);
        int dif = ListaOrdDif.get(0);
        final FindWordEngine GameState = new FindWordEngine(dif);

        if(GameState.getDificultate() == 0) {
            GameState.SetMinute(7);
            GameState.SetSecunde(0);
            GameState.SetStatusX(0);
            GameState.SetStatusY(6);
        }
        else if(GameState.getDificultate() == 1) {
            GameState.SetMinute(6);
            GameState.SetSecunde(0);
            GameState.SetStatusX(0);
            GameState.SetStatusY(7);
        }
        else if(GameState.getDificultate() == 2) {
            GameState.SetMinute(5);
            GameState.SetSecunde(30);
            GameState.SetStatusX(0);
            GameState.SetStatusY(8);
        }

        GameState.generateListaCuvinteJoc();

        for(int i = 1; i <= 9; ++i) {
            String stringID = "LiteraCuvant" + String.valueOf(i);
            int ID = FindWordActivity.this.getResources().getIdentifier(stringID, "id", FindWordActivity.this.getPackageName());
            buttonCuvant[i] = (Button) findViewById(ID);
        }

        for(int i = 1; i <= 9; ++i) {
            String stringID = "LiteraMesaj" + String.valueOf(i);
            int ID = FindWordActivity.this.getResources().getIdentifier(stringID, "id", FindWordActivity.this.getPackageName());
            buttonMesaj[i] = (Button) findViewById(ID);
        }

        for(int i = 1; i <= 9; i++) {
            for(int j = 1; j <= 9; j++) {
                String stringID = "buttonFindWord" + String.valueOf(i) + String.valueOf(j);
                int ID = FindWordActivity.this.getResources().getIdentifier(stringID, "id", FindWordActivity.this.getPackageName());
                buttonsMatrix[i][j] = (Button) findViewById(ID);
                buttonsMatrix[i][j].getBackground().setColorFilter(0xFFe67575, PorterDuff.Mode.MULTIPLY);
                final int I = i;
                final int J = j;

                buttonsMatrix[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(GameState.getStareJoc() != 0) {
                            return;
                        }
                        ///aici trebuie sa ma acupat de partea cu click click
                        Clickuri ^= 1;
                        if(Clickuri == 1) {
                            buttonsMatrix[I][J].getBackground().setColorFilter(0xFFB7F748, PorterDuff.Mode.MULTIPLY);
                            StartX = I;
                            StartY = J;
                        }
                        else {
                            buttonsMatrix[I][J].getBackground().setColorFilter(0xFFB7F748, PorterDuff.Mode.MULTIPLY);

                            FinishX = I;
                            FinishY = J;
                            int linie = GameState.checkPotrivireCuvant(StartX,StartY,FinishX,FinishY,String.valueOf(GameState.getCuvantX(GameState.getStatusX())));

                            if(linie != 0) {
                                drawLinie(linie);
                                GameState.IncreaseStatusX();
                                AfiseazaStatus(GameState.getStatusX(),GameState.getStatusY());
                                if(GameState.getStatusX() == GameState.getStatusY()) {
                                    GameState.SetStareJoc(2);
                                    GameState.SetMesajJoc("  YOU WIN ");
                                    AfiseazaMesaj(GameState.getMesajJoc());
                                    return ;
                                }
                                AfiseazaCuvant(String.valueOf(GameState.getCuvantX(GameState.getStatusX())));
                                GameState.SetMesajJoc(" FIND WORD");
                                AfiseazaMesaj(GameState.getMesajJoc());
                                GameState.generateLitere();
                                GameState.adaugaCuvant(GameState.getCuvantX(GameState.getStatusX()));
                                AfiseazaMatrice(GameState.getMatriceLitere());
                            }
                            else {
                                GameState.SetMesajJoc(" TRY AGAIN");
                                AfiseazaMesaj(GameState.getMesajJoc());
                                final int xxx = StartX;
                                final int yyy = StartY;
                                final int XXX = FinishX;
                                final int YYY = FinishY;
                                new android.os.Handler().postDelayed(
                                        new Runnable() {
                                            public void run() {
                                                GameState.SetMesajJoc(" FIND WORD");
                                                AfiseazaMesaj(GameState.getMesajJoc());
                                                buttonsMatrix[xxx][yyy].getBackground().setColorFilter(0xFFe67575, PorterDuff.Mode.MULTIPLY);
                                                buttonsMatrix[XXX][YYY].getBackground().setColorFilter(0xFFe67575, PorterDuff.Mode.MULTIPLY);
                                            }
                                        },
                                        500);
                            }
                        }
                    }
                });
            }
        }


        AfiseazaStatus(GameState.getStatusX(),GameState.getStatusY());
        AfiseazaCuvant(String.valueOf(GameState.getCuvantX(GameState.getStatusX())));
        GameState.SetMesajJoc(" FIND WORD");
        AfiseazaMesaj(GameState.getMesajJoc());
        GameState.generateLitere();
        GameState.adaugaCuvant(String.valueOf(GameState.getCuvantX(GameState.getStatusX())));
        AfiseazaMatrice(GameState.getMatriceLitere());
        ///MeineTimer();

        int ID = FindWordActivity.this.getResources().getIdentifier("Retry", "id", FindWordActivity.this.getPackageName());
        Retry = (Button) findViewById(ID);

        Retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GameState.getStareJoc() == 1) {
                    StartGame();
                }
            }
        });


        AfiseazaTimp(GameState.getMinute(),GameState.getSecunde());
        for(int i = 1 ; i <= GameState.getMinute()*60+GameState.getSecunde(); ++i) {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            if(GameState.getStareJoc() == 0) {
                                int Secunde = GameState.getSecunde();
                                int Minute = GameState.getMinute();
                                Secunde--;
                                if (Secunde < 0) {
                                    Minute--;
                                    Secunde += 60;
                                }
                                if (Minute == 0 && Secunde == 0) {
                                    GameState.SetStareJoc(1);
                                }
                                if(GameState.getStareJoc() == 1)
                                {
                                    GameState.SetMesajJoc(" GAME OVER");
                                    AfiseazaMesaj(GameState.getMesajJoc());
                                }
                                GameState.SetMinute(Minute);
                                GameState.SetSecunde(Secunde);
                                AfiseazaTimp(GameState.getMinute(),GameState.getSecunde());
                            }
                        }
                    }, 1000*i);
        }
    }
}