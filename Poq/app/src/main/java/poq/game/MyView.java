package poq.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

/**
 * Class for an individual colored square within the grid.
 * Has color and index characteristics.
 * Gets drawn.
 *
 * Created by arielfromm on 2/24/17.
 */

public class MyView extends View {
    int idX = 0; //default
    int idY = 0; //default
    int idColor =0 ;//default

    //Set of colors in the grid
    public int colors[] = {
            Color.rgb(77, 195, 255),    //light blue
            Color.rgb(255, 102, 217),     //light pink
            Color.rgb(153, 51, 255),       //purple
            //Color.rgb(0, 51, 204),       //dark blue
            //4Color.rgb(102, 255, 153),        //light green
            Color.rgb(89, 0, 179),      //dark purple
            Color.rgb(255, 51, 0),       //dark orange
            Color.rgb(255, 0, 102)      //darker pink
    };

    public MyView(Context context, int x, int y, int color) {
        super(context);
        idX = x;
        idY = y;
        idColor = color;
    }

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onDraw(Canvas canvas){
        //We can do more things here if we want
        canvas.drawColor(colors[idColor]);
    }

    /**
     * @return Number of colors possible in the grid
     */
    public int getColorsLength() {
        return colors.length;
    }

    /**
     * @return Location of the color of the current square in the array of possible colors
     */
    public int getIdColor() {
        return idColor;
    }


}
