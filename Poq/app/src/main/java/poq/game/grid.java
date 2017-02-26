package poq.game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Displaying the gridlayout.
 * Creating the contents of the grid and changing them.
 */
public class grid extends AppCompatActivity {
    public MyView[] boxes = new MyView[64];
    public GridLayout gridLayout;

    @Override
    /**
     * Draw a grid of random colored squares
     * Makes sure there are no pre-existing matches of three of the same squares in the grid
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.griddesign);

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

    }

    /**
     * Get a hash set of the indices of the squares in the grid that are members of matches.
     * Matches are rows are columns of three or more squares of the same color
     * @return match indices
     */
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


}
