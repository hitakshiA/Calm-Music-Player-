package com.example.hitakshiarora;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class RelaxationActivity extends AppCompatActivity {

    private ImageView breathingCircle;
    private Button startBreathingButton;
    private Button backButton;
    private MediaPlayer mediaPlayer;
    private boolean isBreathing = false;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relaxation);

        breathingCircle = findViewById(R.id.breathing_circle);
        startBreathingButton = findViewById(R.id.start_breathing_button);
        backButton = findViewById(R.id.back_button);

        startBreathingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isBreathing) {
                    startBreathingExercise();
                    isBreathing = true;
                } else {
                    stopBreathingExercise();
                    isBreathing = false;
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Go back to home screen
            }
        });
    }

    private void startBreathingExercise() {
        mediaPlayer = MediaPlayer.create(this, R.raw.relaxing_sound); // Add relaxing sound file to res/raw
        mediaPlayer.start();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Animation or breathing effect logic
                breathingCircle.setScaleX(1.5f);
                breathingCircle.setScaleY(1.5f);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        breathingCircle.setScaleX(1f);
                        breathingCircle.setScaleY(1f);
                        startBreathingExercise();
                    }
                }, 5000); // Adjust timing as needed
            }
        }, 5000); // Adjust timing as needed
    }

    private void stopBreathingExercise() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        handler.removeCallbacksAndMessages(null);
    }
}
