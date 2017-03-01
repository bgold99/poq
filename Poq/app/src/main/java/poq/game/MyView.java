package poq.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
    public int idShape =0 ;//default
    Canvas canvas;

    //Set of colors in the grid
    public int colors[] = {
            Color.rgb(77, 195, 255),    //light blue
            Color.rgb(153, 51, 255),       //purple
            Color.rgb(0, 51, 204),       //dark blue
            Color.rgb(255, 0, 102),      //darker pink
    };

    public MyView(Context context, int x, int y, int color) {
        super(context);
        idX = x;
        idY = y;
        idShape = color;
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
    public void onDraw(Canvas c){
        canvas = c;
        Paint p = new Paint();
        if (idShape<colors.length){
            p.setColor(colors[idShape]);
            //checks which shape is being used and draws it to the canvas
            //circle:
            if(idShape == 0) canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, 50, p);
                //square:
            else if(idShape == 1) canvas.drawRect(canvas.getWidth()/2-45, canvas.getHeight()/2-45, canvas.getWidth()/2+45, canvas.getHeight()/2+45, p);
                //triangle:
            else if(idShape == 2){
                Path triangle = new Path();
                triangle.moveTo(canvas.getWidth()/2,canvas.getHeight()/2-45);
                triangle.lineTo(canvas.getWidth()/2+45,canvas.getHeight()/2+45);
                triangle.lineTo(canvas.getWidth()/2-45, canvas.getHeight()/2+45);
                triangle.lineTo(canvas.getWidth()/2,canvas.getHeight()/2-45);
                p.setStyle(Paint.Style.FILL);
                canvas.drawPath(triangle, p);
            }
            //diamond:
            else if(idShape==3) {
                Path diamond = new Path();
                diamond.moveTo(canvas.getWidth()/2,canvas.getHeight()/2-50);
                diamond.lineTo(canvas.getWidth()/2+50,canvas.getHeight()/2);
                diamond.lineTo(canvas.getWidth()/2,canvas.getHeight()/2+50);
                diamond.lineTo(canvas.getWidth()/2-50,canvas.getHeight()/2);
                diamond.lineTo(canvas.getWidth()/2,canvas.getHeight()/2-50);
                p.setStyle(Paint.Style.FILL);
                canvas.drawPath(diamond, p);
            }
            else{
                //draw nothing
            }
        }

    }

    /**
     * @return Number of colors possible in the grid
     */
    public int getColorsLength() {
        return colors.length;
    }

    public int getIdX() {
        return idX;
    }

    public int getIdY() {
        return idY;
    }

    /**
     * @return Location of the color of the current square in the array of possible colors
     */
    public int getIdColor() {
        return idShape;
    }

}
