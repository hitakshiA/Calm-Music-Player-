package com.example.hitakshiarora;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Spinner songSpinner;
    private Button playButton;
    private Button pauseButton;
    private Button relaxButton;
    private Button backButton;
    private TextView timerText;
    private Handler handler = new Handler();
    private Runnable updateTimer;

    private int[] songResources = {
            R.raw.one, R.raw.second, R.raw.third,
            R.raw.fourth, R.raw.fifth, R.raw.sixth
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        songSpinner = findViewById(R.id.song_spinner);
        playButton = findViewById(R.id.play_button);
        pauseButton = findViewById(R.id.pause_button);
        relaxButton = findViewById(R.id.relax_button);
        backButton = findViewById(R.id.back_button);
        timerText = findViewById(R.id.timer_text);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.song_titles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        songSpinner.setAdapter(adapter);

        songSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Release current MediaPlayer if it's playing
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mediaPlayer = MediaPlayer.create(MainActivity.this, songResources[position]);


                updateTimerText(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        playButton.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.start();
                startTimer();
            } else {
                Toast.makeText(MainActivity.this, "Select a song first", Toast.LENGTH_SHORT).show();
            }
        });

        pauseButton.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
                stopTimer();
            } else {
                Toast.makeText(MainActivity.this, "No song is playing", Toast.LENGTH_SHORT).show();
            }
        });

        relaxButton.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, activity_focus_session.class);
            startActivity(intent);
        });

        backButton.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, activity_relaxation.class);
            startActivity(intent);
        });
    }

    private void startTimer() {
        updateTimer = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    updateTimerText(currentPosition);
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.post(updateTimer);
    }

    private void stopTimer() {
        if (updateTimer != null) {
            handler.removeCallbacks(updateTimer);
        }
    }

    private void updateTimerText(int milliseconds) {
        int seconds = (milliseconds / 1000) % 60;
        int minutes = (milliseconds / (1000 * 60)) % 60;
        timerText.setText(String.format("%02d:%02d", minutes, seconds));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        stopTimer();
    }
}
