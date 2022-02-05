package com.silicon.sangeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread background = new Thread()
        {
            public void run (){
                try {
                    sleep((1*1000));
                    Intent i = new Intent(MainActivity.this,MusicList.class);
                    startActivity(i);

                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        background.start();


    }
}