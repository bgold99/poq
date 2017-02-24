package poq.game;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;


/**
 * Created by Brittany Goldstein on 2/10/2017.
 */

public class Poq extends AppCompatActivity {
    TextView second; //holds the number of seconds remaining in the game
    int startTime = 91; //the starttime of the countdown clock
    long timeRemaining;
    int score = 0;
    boolean paused;

    //Changes the screen to activity_main (the main game)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intentIn = getIntent();
        startTime = 1000*intentIn.getIntExtra("EXTRA_STARTTIME", 91);

        second = (TextView) findViewById(R.id.seconds);
        paused = false;
        startCountdown(startTime);
    }

    //pauses the game and changes the screen when the pause button is pressed
    public void pause(View view){
        startTime = (int)timeRemaining;
        paused = true;
        Intent intent = new Intent(this, Paused.class);
        intent.putExtra("EXTRA_STARTTIME", startTime);
        startActivity(intent);
    }

    //returns the player to the main menu
    public void main(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //counts down the time left in the game from the start time
    private void startCountdown(int startTime){
        CountDownTimer timer = new CountDownTimer(startTime, 1000) {
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished/1000;
                second.setText("Time: " + millisUntilFinished / 1000);
            }
            public void onFinish() {
                if(paused==true){
                    second.setText("Time's Up!");
                    Intent intentEnd = new Intent(Poq.this, GameOver.class);
                    intentEnd.putExtra("EXTRA_SCORE", score);
                    startActivity(intentEnd);
                }
            }
        }.start();
    }
}
