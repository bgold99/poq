package poq.game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;


public class grid extends AppCompatActivity {
    public MyView[] boxes = new MyView[64];
    public GridLayout gridLayout;

    @Override
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
                gridLayout.addView(tView);

            }
        }

        //Code modified from code found at http://android-er.blogspot.com/2014/09/insert-view-to-gridlayout-dynamically.html
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


}
