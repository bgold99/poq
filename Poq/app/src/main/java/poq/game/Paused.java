package poq.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Paused extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paused);
    }

    public void resume(View view){
        Intent intent = new Intent(this, Poq.class);
        startActivity(intent);
    }
}
