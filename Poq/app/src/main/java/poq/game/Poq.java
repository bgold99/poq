package poq.game;

import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
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
    public LinearLayout linearLayout;
    private float x1 = 0, y1 = 0, rX1 = 0, rY1 = 0;
    private float x2, y2;
    static final int MIN_DISTANCE = 150;
    boolean paused=true;

    //Changes the screen to activity_main (the main game)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridLayout = (GridLayout) findViewById(R.id.GridLayout);
        linearLayout = (LinearLayout) findViewById(R.id.LinearLayout);

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
        paused = false;
        startCountdown(startTime);
    }

    //pauses the game and changes the screen when the pause button is pressed
    public void pause(View view){
        startTime = (int)timeRemaining;
        paused = true;
        Intent intent = new Intent(this, Paused.class);
        intent.putExtra("EXTRA_STARTTIME", startTime);
        startActivity(intent);
    }

    //returns the player to the main menu
    public void main(View view){
        paused = true;
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
                if(paused!=true){
                    second.setText("Time's Up!");
                    Intent intentEnd = new Intent(Poq.this, GameOver.class);
                    intentEnd.putExtra("EXTRA_SCORE", score);
                    startActivity(intentEnd);}
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
                        score.setText("Down");
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

    /**
     * Given a pixel location clicked within the grid, this method returns the index of the
     * box in the grid. This indices go from 0 to 7 from left to right in the first row and
     * increase going down.
     * @param xPix The x coordinate in pixels of the pixel clicked
     * @param yPix The y coordinate in pixels of the pixel clicked
     * @return index of the box clicked within the grid
     */
    public int getGridIndex(float xPix, float yPix){
        int xPos = (int) (xPix/gridLayout.getWidth()*8);
        int yPos = (int) ((yPix-linearLayout.getHeight()-getSupportActionBar().getHeight())/gridLayout.getHeight()*8);
        return yPos*8+xPos;
    }

    public void animateSwap(int id1, int id2){
        //TODO
    }

    public int[] findAdjacentMatch(int xPos, int yPos){
        ArrayList<Integer> matchPos = new ArrayList<Integer>();   //to be filled with adjacent positions (1-64) of the same shape
        int i=1;
        //keeps going left until not the same color or hits the border
        while(xPos-i!=-1 && boxes[yPos*8 + xPos - (i-1)].getIdColor()==boxes[yPos*8 + xPos - i].getIdColor()) {
            matchPos.add(yPos * 8 + xPos - i);
            i++;
        }
        i=1;
        //keeps going right until not the same color or hits the border
        while(xPos+i!=8 && boxes[yPos*8 + xPos + (i-1)].getIdColor()==boxes[yPos*8 + xPos + i].getIdColor()){
            matchPos.add(yPos*8+xPos+i);
            i++;
        }
        i=1;
        //keeps going up until not the same color or hits the border
        while (yPos-i!=-1 && boxes[(yPos-i)*8 + xPos].getIdColor()==boxes[(yPos-i+1)*8 + xPos].getIdColor()){
            matchPos.add((yPos-i)*8+xPos);
            i++;
        }
        i=1;
        //keeps going down until not the same color or hits the border
        while (yPos+i!=8 && boxes[(yPos+i)*8 + xPos].getIdColor()==boxes[(yPos+i-1)*8 + xPos].getIdColor()){
            matchPos.add((yPos+i)*8+xPos);
            i++;
        }
        //convert ArrayList to integer array for returning
        int[] intArr = new int[matchPos.size()];
        for (int j=0; j < intArr.length; j++){
            intArr[j] = matchPos.get(j).intValue();
        }
        return intArr;
    }

    public void animateGravity(int[] deletedBoxes){
        for (int i = 0; i < deletedBoxes.length; i++){
            int subtract = 8;
            boxes[deletedBoxes[i]] = boxes[deletedBoxes[i]-subtract];
            subtract += 8;
            while (deletedBoxes[i] - subtract >= 0){
                boxes[deletedBoxes[i]-subtract+8] = boxes[deletedBoxes[i]-subtract];
                subtract += 8;
            }

            int colorsLength = boxes[0].getColorsLength();
            int newColor = (int) (Math.random()*colorsLength);
            boxes[deletedBoxes[i%8]] = new MyView(this, i%8, 0, newColor);
        }

    }
}

