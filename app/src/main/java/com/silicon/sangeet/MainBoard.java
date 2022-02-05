package com.silicon.sangeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MainBoard extends AppCompatActivity {

    @Override
    // on closing the app media player gets stopped.
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        Seek.interrupt();
    }

    TextView songName;
    ImageView previous, play,next;
    ArrayList<File> Songs;
    MediaPlayer mediaPlayer;
    String SongName;
    int position;
    SeekBar SongSeekBar;
    Thread Seek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_board);

        songName  = findViewById(R.id.SongName);
        previous = findViewById(R.id.imageView3);
        play = findViewById(R.id.imageView4);
        next = findViewById(R.id.imageView5);
        SongSeekBar = findViewById(R.id.seekBar);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Songs = (ArrayList) bundle.getParcelableArrayList("mysong");
        SongName = intent.getStringExtra("currentSong");

        songName.setText(SongName);
        songName.setSelected(true);
        position = intent.getIntExtra("position",0);

        Uri uri = Uri.parse(Songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();
        SongSeekBar.setMax(mediaPlayer.getDuration());

        SongSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(SongSeekBar.getProgress());
            }
        });


        Seek = new Thread() {
            @Override
            public void run() {
                int currentSeek = 0;

                try {

                    while (currentSeek < mediaPlayer.getDuration()) {
                        currentSeek = mediaPlayer.getCurrentPosition();
                        SongSeekBar.setProgress(currentSeek);
                        Seek.sleep(500);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
        Seek.start();





        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying())
                {
                    play.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    mediaPlayer.pause();
                }
                else
                {
                    play.setImageResource(R.drawable.ic_baseline_pause_24);
                    mediaPlayer.start();
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=Songs.size()-1)
                {
                    position = position+1;
                }
                else
                {
                    position =0;
                }
                SongName = Songs.get(position).getName().toString();
                songName.setText(SongName);
                Uri uri = Uri.parse(Songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.ic_baseline_pause_24);


            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=0)
                {
                    position = position-1;
                }
                else
                {
                    position = Songs.size()-1;
                }
                SongName = Songs.get(position).getName().toString();
                songName.setText(SongName);
                Uri uri = Uri.parse(Songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.ic_baseline_pause_24);



            }
        });





    }
}