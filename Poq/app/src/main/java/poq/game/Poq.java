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
    TextView second;
    int startTime = 91;
    long timeRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intentIn = getIntent();
        startTime = 1000*intentIn.getIntExtra("EXTRA_STARTTIME", 91);

        second = (TextView) findViewById(R.id.seconds);
        startCountdown(startTime);
    }

    public void pause(View view){
        startTime = (int)timeRemaining;
        Intent intent = new Intent(this, Paused.class);
        intent.putExtra("EXTRA_STARTTIME", startTime);
        startActivity(intent);
    }

    public void main(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void startCountdown(int startTime){
        CountDownTimer timer = new CountDownTimer(startTime, 1000) {
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished/1000;
                second.setText("Time: " + millisUntilFinished / 1000);
            }
            public void onFinish() {
                second.setText("Time's Up!");
                //**********************Should call GaveOver***************
                //?? is the context
                //Intent intentEnd = new Intent(??, GameOver.class);
                //startActivity(intentEnd);
            }
        }.start();
    }
}
