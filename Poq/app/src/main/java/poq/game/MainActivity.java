package poq.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
    }

    //Takes Player to Game(Poq Class) when Play button is pressed
    public void play(View view) {
        Intent intent = new Intent(this, Poq.class);
        startActivity(intent);
    }

    //Takes Player to Instructions Layout (Instructions Class) when Instructions button is pressed
    public void howTo(View view){
        Intent intent = new Intent(this, Instructions.class);
        startActivity(intent);
    }
}


