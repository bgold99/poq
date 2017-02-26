package poq.game;

import android.graphics.Path;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.TextView;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Timer;


/**
 * Created by Brittany Goldstein on 2/10/2017.
 */

public class Poq extends AppCompatActivity {
    TextView second; //holds the number of seconds remaining in the game
    int startTime = 91; //the starttime of the countdown clock
    long timeRemaining;
    int score = 0;
    public MyView[] boxes = new MyView[64];
    public GridLayout gridLayout;
    private float x1 = 0, y1 = 0, rX1 = 0, rY1 = 0;
    private float x2, y2;
    static final int MIN_DISTANCE = 150;


    //Changes the screen to activity_main (the main game)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridLayout = (GridLayout) findViewById(R.id.GridLayout);

        boxes[0] = new MyView(this, 0, 0, 0);   //initializing to avoid null object reference error
        for (int i=0; i<8; i++) {
            for (int j = 0; j < 8; j++) {
                //i is the x coordinate
                //j is the y coordinate of the box in the grid

                int colorsLength = boxes[0].getColorsLength();    //Getting the number of colors in the grid
                int tColor = (int) (Math.random()*colorsLength);    //Setting random color for box
                MyView tView = new MyView(this, i, j, tColor);      //Temporary MyView
                boxes[j*8+i] = tView;

            }
        }

        //Eliminate matches in random grid
        HashSet<Integer> matches = getMatches();
        while(matches.size()>0){
            eliminateMatchesInit(matches);
            matches = getMatches();
        }

        //Add the squares to the grid layout
        for (int i=0; i<8; i++) {
            for (int j = 0; j < 8; j++) {
                gridLayout.addView(boxes[j*8+i]);
            }
        }

        //Code modified from code found at http://android-er.blogspot.com/2014/09/insert-view-to-gridlayout-dynamically.html
        //displaying squares in grid
        gridLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener(){

                    @Override
                    public void onGlobalLayout() {

                        final int MARGIN = 5;

                        int pWidth = gridLayout.getWidth();
                        int pHeight = gridLayout.getHeight();
                        int w = pWidth/8;
                        int h = pHeight/8;

                        for(int yPos=0; yPos<8; yPos++){
                            for(int xPos=0; xPos<8; xPos++){
                                GridLayout.LayoutParams params =
                                        (GridLayout.LayoutParams)boxes[yPos*8 + xPos].getLayoutParams();
                                params.width = w - 2*MARGIN;
                                params.height = h - 2*MARGIN;
                                params.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
                                boxes[yPos*8 + xPos].setLayoutParams(params);
                            }
                        }

                    }
                }
        );

        Intent intentIn = getIntent();
        startTime = 1000*intentIn.getIntExtra("EXTRA_STARTTIME", 91);
        second = (TextView) findViewById(R.id.seconds);
        startCountdown(startTime);
    }

    //pauses the game and changes the screen when the pause button is pressed
    public void pause(View view){
        startTime = (int)timeRemaining;
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
                second.setText(" Time: " + millisUntilFinished / 1000);
            }
            public void onFinish() {
                second.setText(" Time's Up!");
                Intent intentEndGame = new Intent(Poq.this, GameOver.class);
                intentEndGame.putExtra("EXTRA_SCORE", score);
                startActivity(intentEndGame);
            }
        }.start();
    }

    public HashSet<Integer> getMatches(){
        HashSet<Integer> matches = new HashSet<>();      //Array of coordinate of squares with matches

        //Check for matches in rows
        for (int xPos=1; xPos<7; xPos++){
            for (int yPos=0; yPos<8; yPos++){

                //If the current box is in the center of a match of three horizontally
                if (boxes[yPos*8 + xPos].getIdColor()==boxes[yPos*8 + xPos-1].getIdColor()
                        && boxes[yPos*8 + xPos].getIdColor()==boxes[yPos*8 + xPos+1].getIdColor()){
                    //Add the match of three to the set
                    matches.add(yPos*8 + xPos -1);
                    matches.add(yPos*8 + xPos);
                    matches.add(yPos*8 + xPos +1);
                }
            }
        }

        //Check for matches in columns
        for (int xPos=0; xPos<8; xPos++){
            for (int yPos=1; yPos<7; yPos++){

                //If the current box is in the center of a match of three vertically
                if (boxes[yPos*8 + xPos].getIdColor()==boxes[(yPos-1)*8 + xPos].getIdColor()
                        && boxes[yPos*8 + xPos].getIdColor()==boxes[(yPos+1)*8 + xPos].getIdColor()){
                    //Add the match of three to the set
                    matches.add((yPos-1)*8 + xPos);
                    matches.add(yPos*8 + xPos);
                    matches.add((yPos+1)*8 + xPos);
                }
            }
        }

        return matches;
    }

    /**
     * Remove the matches at the beginning of the board generation.
     * Replace the matches with random colors.
     * @param matches Hashset of indices of the locations of matches in the grid.
     */
    public void eliminateMatchesInit(HashSet matches){
        int colorsLength = boxes[0].getColorsLength();    //Getting the number of colors in the grid

        for (int i=0; i<8; i++){
            for (int j=0; j<8; j++){

                //reset the color of the matched square
                if (matches.contains((Integer) (i*8+j))){
                    int newColor = (int) (Math.random()*colorsLength);
                    boxes[j*8+i] = new MyView(this, i, j, newColor);
                }
            }
        }
    }

    /*Checks which direction the user swiped
    Code altered from code retrieved from StackOverflow user2999943
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){
        TextView score = (TextView) findViewById(R.id.score);
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                rX1 = event.getRawX();
                rY1 = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                float deltaX = x2 - x1;
                float deltaY = y2 - y1;

                if (Math.abs(deltaX) > Math.abs(deltaY) && Math.abs(deltaX)>MIN_DISTANCE) {
                    // Left to Right swipe action
                    if (x2 > x1) {
                        score.setText("Right");
                    }

                    // Right to left swipe action
                    else {
                        score.setText("Left");
                    }
                }

                if (Math.abs(deltaY)>Math.abs(deltaX) && Math.abs(deltaY)>MIN_DISTANCE){
                    //Up swipe action
                    if(y1>y2){
                        score.setText("Up");
                    }

                    //Down swipe action
                    else {
                        score.setText("down");
                    }

                }
                else
                {
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}

