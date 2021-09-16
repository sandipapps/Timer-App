package com.sandipbhattacharya.timerapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    // Declare object references
    SeekBar timer_sb;
    TextView timer_tv;
    Button start_btn;
    CountDownTimer countDownTimer;
    MediaPlayer mediaPlayer;
    // Define a boolean to store counter active state
    Boolean counterIsActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get handles for views
        timer_sb = findViewById(R.id.timer_sb);
        timer_tv = findViewById(R.id.timer_tv);
        start_btn = findViewById(R.id.start_btn);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alarm);
        // I am setting the upper range of the SeekBar with 300, since I
        // want the max limit to be 5 minutes or 300 seconds. Change
        // this as per your need.
        timer_sb.setMax(300);
        // I am setting the current default progress with 30, for 30 seconds
        timer_sb.setProgress(30);
        // Attach setOnSeekBarChangeListener to get notified of the user's actions
        timer_sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // onProgressChanged() method is invoked if the progress level is changed.
                // Call a method here and pass progress as parameter
                // to update the TextView.
                update(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void start_timer(View view) {
        // If the flag counterIsActive is false, we'll make it true,
        // disable the SeekBar, change the button text from "START" to "STOP"
        // and start the countDownTimer
        if(counterIsActive == false){
            counterIsActive = true;
            timer_sb.setEnabled(false);
            start_btn.setText("STOP");
            // Next, instantiate the countDownTimer object.
            // Android CountDownTimer class is used to schedule a countdown
            // until a time in the future defined by the value you specify,
            // with regular notifications on intervals along the way.
            // In the constructor for CountDownTimer you need to specify two things:
            // 1. The number of millis in the future from the call to
            // start() until the countdown is done and onFinish() is called.
            // And,
            // 2. The interval along the way to receive onTick() callbacks.
            // You'll override onTick() and onFinish() methods.
            countDownTimer = new CountDownTimer(timer_sb.getProgress() * 1000, 1000) {
                // In this case, onTick() callback method is fired on regular intervals of
                // 1 second and onFinish() callback method is fired when the timer finishes.
                @Override
                public void onTick(long millisUntilFinished) {
                    // Call update() method here to update the TextView and SeekBar every 1 second.
                    update((int) millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    // Here call reset() method
                    reset();
                    // Optionally, you can play a sound to indicate that the timer has
                    // completed.
                    if (mediaPlayer != null)
                        mediaPlayer.start();
                }
            }.start();
        }else{
            // Here call a method to reset all the variables, views and cancel countDownTimer
            reset();
        }
    }

    // Define reset() method
    private void reset() {
        timer_tv.setText("0:30");
        timer_sb.setProgress(30);
        countDownTimer.cancel();
        start_btn.setText("START");
        timer_sb.setEnabled(true);
        counterIsActive = false;
    }

    // Define update() method
    private void update(int progress) {
        // Divide progress by 60 to get the minutes
        int minutes = progress / 60;
        // Divide progress by 60 and get the remainder for seconds
        int seconds = progress % 60;
        String secondsFinal = "";
        // If the value of seconds is less than or equal to 9,
        // print a leading zero if you want to show seconds in 2 digits format
        if(seconds <= 9){
            secondsFinal = "0" + seconds;
        }else{
            secondsFinal = "" + seconds;
        }
        // Update the SeekBar and TextView
        timer_sb.setProgress(progress);
        timer_tv.setText("" + minutes + ":" + secondsFinal);
    }

    // In onPause() and onDestroy(), if the counterIsActive flag is true,
    // cancel countDownTimer.
    @Override
    protected void onPause() {
        super.onPause();
        if(counterIsActive){
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(counterIsActive){
            countDownTimer.cancel();
        }
    }
}