package poq.game;

import android.os.Bundle;
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

        for(int i = 90; i >= 0; i--) {
            TextView view1 = (TextView) findViewById(R.id.seconds);
            view1.setText(""+i);
            holdOneSecond();
        }

    }


    public void holdOneSecond(){
        Calendar c = Calendar.getInstance();
        int beginning = c.get(Calendar.MILLISECOND);
        int end = beginning;
        while(end - beginning < 1000){
            end = c.get(Calendar.MILLISECOND);
        }
    }
}
