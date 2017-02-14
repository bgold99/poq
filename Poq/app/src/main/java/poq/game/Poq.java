package poq.game;

import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;

/**
 * Created by Brittany Goldstein on 2/10/2017.
 */

public class Poq extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();

        TextView second = (TextView) findViewById(R.id.seconds);

        for(int i = 500000000; i >= 0; i--) {
            String text = ""+i;
            second.setText(text);
            //holdOneSecond();
        }

    }


    public void holdOneSecond() {
        //Calendar c = Calendar.getInstance();
        long beginning = System.currentTimeMillis();
        long end = beginning;
        while(end - beginning < 1000){
            end = System.currentTimeMillis();
        }
    }
}
