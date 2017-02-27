package poq.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

/**
 * Created by Brittany Goldstein on 2/15/2017.
 */

public class Instructions extends AppCompatActivity{

    //Changes the screen to the instructions layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions);
        getSupportActionBar().setTitle("Instructions");
        Intent intent = getIntent();
    }

    //Returns Viewer to Main Menu when Main Menu Button is Pressed
    public void mainMenu(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
