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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.TimeUnit;


/**
 * Created by Brittany Goldstein on 2/10/2017.
 */

public class Poq extends AppCompatActivity {
    TextView second; //holds the number of seconds remaining in the game
    int startTime = 91; //the starttime of the countdown clock
    long timeRemaining;
    int score = 0;
    public MyView[] boxes = new MyView[64];
    public int[] colorLayout = new int[64];
    public GridLayout gridLayout;
    public LinearLayout linearLayout;
    private float x1 = 0, y1 = 0, rX1 = 0, rY1 = 0;
    private float x2, y2;
    static final int MIN_DISTANCE = 75;
    boolean paused=true;

    //Changes the screen to activity_main (the main game)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridLayout = (GridLayout) findViewById(R.id.GridLayout);
        linearLayout = (LinearLayout) findViewById(R.id.LinearLayoutTop);

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
                gridLayout.addView(boxes[i*8+j], i*8+j);
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
        score = intentIn.getIntExtra("EXTRA_SCORE", 0);
        TextView scoreText = (TextView) findViewById(R.id.score);
        scoreText.setText("Score: "+score);
        colorLayout = intentIn.getIntArrayExtra("EXTRA_COLORLAYOUT");
        startCountdown(startTime);
    }

    //pauses the game and changes the screen when the pause button is pressed
    public void pause(View view){
        startTime = (int)timeRemaining;
        paused = true;
        Intent intent = new Intent(this, Paused.class);
        intent.putExtra("EXTRA_STARTTIME", startTime);
        intent.putExtra("EXTRA_SCORE", score);
        for(int i=0; i<boxes.length; i++){
            colorLayout[i] = boxes[i].getIdColor();
        }
        intent.putExtra("EXTRA_COLORLAYOUT", colorLayout);
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

    public void printBoxes(){
        for (int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                System.out.print(boxes[i*8+j].getIdColor()+" ");
            }
            System.out.println();
        }
    }

    /*Checks which direction the user swiped
    Code altered from code retrieved from StackOverflow user2999943
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){
        TextView scoreText = (TextView) findViewById(R.id.score);
        Handler swapHandler = new Handler();
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
                        int id1 = getGridIndex(x1, y1);
                        int id2 = id1+8;

                        if ((id1-id1%8)/8<7 && id1>=0) {
                            animateSwap(id1, id2);

                            int[] delete1 = disappearingMatch(id1);
                            int[] delete2 = disappearingMatch(id2);
                            deleteMatch(delete1);
                            deleteMatch(delete2);
                            scoreText.setText("Score: "+score);

                            //int[] colorGrid = returnBoxesColor();

                            if (delete1.length == 0 && delete2.length == 0) {
                                animateSwap(id2, id1);
                            } else {
                                HashSet<Integer> commonDelete = new HashSet<Integer>();

                                for (int i = 0; i < delete1.length; i++) {
                                    commonDelete.add(delete1[i]);
                                }
                                for (int i = 0; i < delete2.length; i++) {
                                    commonDelete.add(delete2[i]);
                                }

                                List<Integer> delete = new ArrayList<Integer>(commonDelete);
                                animateGravity(delete);

                                HashSet<Integer> setDelete = getMatches();
                                delete = toArrayList(setDelete);

                                //Keep iterating through if falling created more matches
                                while (delete.size()!=0){
                                    //animatePause();
                                    animateGravity(delete);
                                    setDelete = getMatches();
                                    delete = toArrayList(setDelete);
                                }
                            }
                        }

                        // Right to left swipe action
                    }else {
                        int id1 = getGridIndex(x1, y1);
                        int id2 = id1-8;

                        if ((id1-id1%8)/8>0 && id1>=0) {
                            animateSwap(id1, id2);

                            int[] delete1 = disappearingMatch(id1);
                            int[] delete2 = disappearingMatch(id2);
                            deleteMatch(delete1);
                            deleteMatch(delete2);
                            scoreText.setText("Score: "+score);

                            //int[] colorGrid = returnBoxesColor();

                            if (delete1.length == 0 && delete2.length == 0) {
                                animateSwap(id2, id1);
                            } else {
                                HashSet<Integer> commonDelete = new HashSet<Integer>();

                                for (int i = 0; i < delete1.length; i++) {
                                    commonDelete.add(delete1[i]);
                                }
                                for (int i = 0; i < delete2.length; i++) {
                                    commonDelete.add(delete2[i]);
                                }

                                List<Integer> delete = new ArrayList<Integer>(commonDelete);
                                animateGravity(delete);

                                HashSet<Integer> setDelete = getMatches();
                                delete = toArrayList(setDelete);

                                //Keep iterating through if falling created more matches
                                while (delete.size()!=0){
                                    //animatePause();
                                    animateGravity(delete);
                                    setDelete = getMatches();
                                    delete = toArrayList(setDelete);
                                }
                            }
                        }
                    }
                }

                if (Math.abs(deltaY)>Math.abs(deltaX) && Math.abs(deltaY)>MIN_DISTANCE){
                    //Up swipe action
                    if(y1>y2){
                        int id1 = getGridIndex(x1, y1);
                        int id2 = id1-1;

                        if (id1%8>0 && id1>=0) {     //if not in top row
                            animateSwap(id1, id2);

                            int[] delete1 = disappearingMatch(id1);
                            int[] delete2 = disappearingMatch(id2);
                            deleteMatch(delete1);
                            deleteMatch(delete2);
                            scoreText.setText("Score: "+score);

                            //int[] colorGrid = returnBoxesColor();

                            if (delete1.length == 0 && delete2.length == 0){
                                animateSwap(id2, id1);
                            }else {
                                HashSet<Integer> commonDelete = new HashSet<Integer>();

                                for(int i = 0; i < delete1.length; i++){
                                    commonDelete.add(delete1[i]);
                                }
                                for(int i = 0; i < delete2.length; i++){
                                    commonDelete.add(delete2[i]);
                                }

                                List<Integer> delete = new ArrayList<Integer>(commonDelete);
                                animateGravity(delete);

                                HashSet<Integer> setDelete = getMatches();
                                delete = toArrayList(setDelete);

                                //Keep iterating through if falling created more matches
                                while (delete.size()!=0){
                                    //animatePause();
                                    animateGravity(delete);
                                    setDelete = getMatches();
                                    delete = toArrayList(setDelete);
                                }
                            }
                        }
                    }

                    //Down swipe action
                    else {
                        int id1 = getGridIndex(x1, y1);
                        int id2 = id1+1;

                        if (id1%8<7 && id1>=0) {     //if not in bottom row
                            animateSwap(id1, id2);

                            int[] delete1 = disappearingMatch(id1);
                            int[] delete2 = disappearingMatch(id2);
                            deleteMatch(delete1);
                            deleteMatch(delete2);
                            scoreText.setText("Score: " + score);

                            //int[] colorGrid = returnBoxesColor();

                            if (delete1.length == 0 && delete2.length == 0) {
                                animateSwap(id2, id1);
                            } else {
                                HashSet<Integer> commonDelete = new HashSet<Integer>();

                                for (int i = 0; i < delete1.length; i++) {
                                    commonDelete.add(delete1[i]);
                                }
                                for (int i = 0; i < delete2.length; i++) {
                                    commonDelete.add(delete2[i]);
                                }

                                //Initial use of gravity
                                List<Integer> delete = new ArrayList<Integer>(commonDelete);
                                animateGravity(delete);

                                HashSet<Integer> setDelete = getMatches();
                                delete = toArrayList(setDelete);

                                //Keep iterating through if falling created more matches
                                while (delete.size()!=0){
                                    //animatePause();
                                    animateGravity(delete);
                                    setDelete = getMatches();
                                    delete = toArrayList(setDelete);
                                }


                            }
                        }
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
     * Converst a hash set of ints to an arraylist with no repeats.
     * @param hash Hashset of integers
     * @return arraylist of integers
     */
    public ArrayList<Integer> toArrayList(HashSet<Integer> hash){
        ArrayList<Integer> list = new ArrayList<Integer>();

        //I know this code is inefficient. I'm having trouble finding a better way due to Object and Integer conflicts
        for (int i=0; i<64; i++){
            if (hash.contains(i) && !list.contains(i)){
                list.add(i);
            }
        }
        return list;
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
        int column = (int) (xPix/gridLayout.getWidth()*8);
        int row = (int) ((yPix-linearLayout.getHeight()-getSupportActionBar().getHeight()-50)*8/gridLayout.getHeight());
        return column*8+row;
    }

    public void animateSwap(int id1, int id2){
        gridLayout = (GridLayout) findViewById(R.id.GridLayout);
        System.out.println("ID " + id1+" "+id2);
        printBoxes();
        int colorId1 = boxes[id1].getIdColor();
        int colorId2 = boxes[id2].getIdColor();
        System.out.println("Color " + colorId1 + " " + colorId2);

        int x1 = id1%8;
        int y1 = (id1-x1)/8;
        int x2 = id2%8;
        int y2 = (id2-x2)/8;

        //gridLayout.removeViewAt(id1);
        //gridLayout.removeViewAt(id2);
        gridLayout.removeViewsInLayout(id1, 1);
        boxes[id1] = new MyView(this, x1, y1, colorId2);
        gridLayout.addView(boxes[id1], id1);

        gridLayout.removeViewsInLayout(id2, 1);
        boxes[id2] = new MyView(this, x2, y2, colorId1);
        gridLayout.addView(boxes[id2], id2);
        printBoxes();
    }

    /**
     * Returns an array of all the indices of the shapes which are the same color and above, below,
     * or to the side of the selected index, so long as they are connected by blocks of the same color
     * @param index the index of the selected box (achieved from vertical indexing)
     * @return
     */
    public int[] findAdjacentMatch(int index){
        int[] rc = indexToRC(index);
        int row = rc[0];
        int col = rc[1];
        ArrayList<Integer> matchPos = new ArrayList<Integer>();   //to be filled with adjacent positions (1-64) of the same shape
        matchPos.add(index);
        int i=1;
        //keeps going left until not the same color or hits the border
        while(col-i!=-1 && boxes[index-8*i].getIdColor()==boxes[index].getIdColor()) {
            matchPos.add(index-i*8);
            i++;
        }
        i=1;
        //keeps going right until not the same color or hits the border
        while(col+i!=8 && boxes[index+i*8].getIdColor()==boxes[index].getIdColor()){
            matchPos.add(index+i*8);
            i++;
        }
        i=1;
        //keeps going up until not the same color or hits the border
        while (row-i!=-1 && boxes[index-i].getIdColor()==boxes[index].getIdColor()){
            matchPos.add(index-i);
            i++;
        }
        i=1;
        //keeps going down until not the same color or hits the border
        while (row+i!=8 && boxes[index+i].getIdColor()==boxes[index].getIdColor()){
            matchPos.add(index+i);
            i++;
        }
        //convert ArrayList to integer array for returning
        int[] intArr = new int[matchPos.size()];
        for (int j=0; j < intArr.length; j++){
            intArr[j] = matchPos.get(j).intValue();
        }
        return intArr;
    }

    /**
     * Returns an array of the indices of blocks which should disappear (becuase they are part of a 3+ row/col
     * @param index vertical index in the grid which the search is radiating from
     * @return
     */
    public int[] disappearingMatch(int index) {
        int[] shape = findAdjacentMatch(index);
        Arrays.sort(shape);
        ArrayList<Integer> delete = new ArrayList<Integer>();

        if(shape.length>2){
            //check for vertical matches
            int i = 0;
            int j;
            while (i < shape.length-2) {
                if (shape[i] + 1 == shape[i + 1] && shape[i + 1] + 1 == shape[i + 2]) { //if three positions are sequential //TODO caused crash
                    delete.add(shape[i]);
                    delete.add(shape[i + 1]);
                    delete.add(shape[i + 2]);
                    j = i + 2;
                    if (i+3 < shape.length && shape[i + 2] + 1 == shape[i + 3]) {   //if the fourth is also sequential
                        delete.add(shape[i + 3]);
                        j = i + 3;
                        if (i+4 < shape.length && shape[i + 3] + 1 == shape[i + 4]) {  //if the fifth is also sequential
                            delete.add(shape[i + 4]);
                            j = i + 4;
                        }
                    }
                } else {
                    j = i + 1;
                }
                i = j;
            }

            //check for horizontal matches
            i = 0;
            while(i<shape.length-2){
                if(shape[i]!=-1){
                    boolean plus8 = includes(shape, shape[i]+8);
                    boolean plus16 = includes(shape, shape[i]+16);
                    boolean plus24 = includes(shape, shape[i]+24);
                    boolean plus32 = includes(shape, shape[i]+32);
                    if(plus8 && plus16){
                        delete.add(shape[i]);
                        delete.add(shape[i]+8);
                        delete.add(shape[i]+16);
                        shape[indexOf(shape, shape[i]+8)]=-1;
                        shape[indexOf(shape, shape[i]+16)]=-1;
                        if(plus24){
                            delete.add(shape[i]+24);
                            shape[indexOf(shape, shape[i]+24)]=-1;
                            if(plus32){
                                delete.add(shape[i]+32);
                                shape[indexOf(shape, shape[i]+32)]=-1;
                            }
                        }
                        shape[indexOf(shape, shape[i])]=-1;
                    }
                }
                i++;
            }
        }

        //convert ArrayList to integer array for returning
        int[] deleteArr = new int[delete.size()];
        for (int k = 0; k < deleteArr.length; k++) {
            deleteArr[k] = delete.get(k).intValue();
        }
        return deleteArr;
    }

    public boolean includes(int[] arr, int val){
        for(int i=0;i<arr.length;i++){
            if(arr[i]==val) return true;
        }
        return false;
    }

    public int indexOf(int[] arr, int val){
        for(int i=0;i<arr.length;i++){
            if(arr[i]==val) return i;
        }
        return -1;
    }

    public void deleteMatch(int[] boxesToDel){
        score = score+boxesToDel.length;
        for(int i=0;i<boxesToDel.length;i++){
            int[] rc = indexToRC(boxesToDel[i]);
            int x = rc[1];
            int y = rc[0];
            gridLayout.removeViewsInLayout(boxesToDel[i], 1);
            boxes[boxesToDel[i]] = new MyView(this, x, y, 4);
            gridLayout.addView(boxes[boxesToDel[i]], boxesToDel[i]);

        }
    }

    /**
     * Put indices in order from left to right and then top to bottom
     * @param boxes indices to order
     * @return ordered list of indices
     */
    public List<Integer> orderByGravity(List<Integer> boxes) {
        List<Integer> orderedBoxes = new ArrayList<Integer>();
        for (int row=0; row<8; row++){
            for (int col=0; col<8; col++) {
                int index = col*8 + row;
                if (boxes.contains(index)){
                    orderedBoxes.add(index);
                }
            }
        }
        return orderedBoxes;
    }

    /**
     * Allow above boxes to fall into empty boxes. Generate new boxes to fall in on top.
     * @param deletedBoxes List of boxes in the grid that are currently unoccupied (before gravity)
     */
    public void animateGravity(List<Integer> deletedBoxes){

        //deleteBoxes needs to be going in order from top to bottom
        deletedBoxes = orderByGravity(deletedBoxes);

        for (int i = 0; i < deletedBoxes.size(); i++){
            //animatePause();
            int counter = 0;

            //moving boxes down
            while ((deletedBoxes.get(i)-counter)%8 > 0){
                gridLayout.removeViewsInLayout(deletedBoxes.get(i)-counter, 1);

                //Making a new view that is a copy of the box above to replace the current box
                boxes[deletedBoxes.get(i)-counter] = new MyView(this, boxes[deletedBoxes.get(i)-counter-1].getIdX(), boxes[deletedBoxes.get(i)-counter-1].getIdY(), boxes[deletedBoxes.get(i)-counter-1].getIdColor());
                    //boxes[deletedBoxes.get(i)-counter-1];

                gridLayout.addView(boxes[deletedBoxes.get(i)-counter], deletedBoxes.get(i)-counter);
                counter += 1;
            }

            //generating new random boxes
            int colorsLength = boxes[0].getColorsLength();
            int newColor = (int) (Math.random()*colorsLength);
            gridLayout.removeViewsInLayout(deletedBoxes.get(i)-counter, 1);
            int x = boxes[deletedBoxes.get(i)-counter].getIdX();
            int y = boxes[deletedBoxes.get(i)-counter].getIdY();
            boxes[deletedBoxes.get(i)-counter] = new MyView(this, x, y, newColor);
            gridLayout.addView(boxes[deletedBoxes.get(i)-counter], deletedBoxes.get(i)-counter);
        }
    }

    public void animatePause(){
        try {
            TimeUnit.MILLISECONDS.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int[] returnBoxesColor(){
        int[] boxesColor = new int[boxes.length];
        for(int i = 0; i<boxes.length;i++){
            boxesColor[i] = boxes[i].getIdColor();
        }
        return boxesColor;
    }

    /**
     * Given an index from 0-63 it will return an array containing the row and column values from 0-7
     * --- WARNING --- only use if you KNOW everything is indexed VERTICALLY
     */
    public int[] indexToRC(int index){
        int[] rc = new int[2];
        rc[0] = index%8; //row
        rc[1] = index/8; //column
        return rc;
    }

}

