package poq.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        //Button playButton = (Button) findViewById(R.id.play_button);
    }

    public void play(View view) {
        Intent intent = new Intent(this, Poq.class);
        startActivity(intent);
    }

   /* public void holdOneSecond(){
        Calendar c = Calendar.getInstance();
        int beginning = c.get(Calendar.MILLISECOND);
        int end = beginning;
        while(end - beginning < 1000){
            end = c.get(Calendar.MILLISECOND);
        }
    }*/
}


