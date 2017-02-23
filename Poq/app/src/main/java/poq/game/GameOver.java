package poq.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Intent intentIn = getIntent();
        score = intentIn.getIntExtra("EXTRA_SCORE",0);
        TextView scoreDisplay = (TextView) findViewById(R.id.score_display);
        scoreDisplay.setText("Final Score: "+score);
    }

    public void exit(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
