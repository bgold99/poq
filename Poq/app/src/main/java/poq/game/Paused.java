package poq.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Paused extends AppCompatActivity {
    private int startTime;
    private int score;

    //Changes the screen to the pause screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paused);

        Intent intentIn = getIntent();
        startTime = intentIn.getIntExtra("EXTRA_STARTTIME", 0);
        score = intentIn.getIntExtra("EXTRA_SCORE", 0);
    }

    //Returns to the game when the resume button is pressed
    public void resume(View view){
        Intent intent = new Intent(this, Poq.class);
        intent.putExtra("EXTRA_STARTTIME", startTime+1); //how much time is left in the game
        intent.putExtra("EXTRA_SCORE", score);
        startActivity(intent);
    }
}
