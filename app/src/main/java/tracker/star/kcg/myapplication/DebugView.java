package tracker.star.kcg.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by elazar on 21/06/17.
 */

public class DebugView extends View
{
    private Bitmap bit = null;
    private String starsList;

    public DebugView(Context context)
    {
        super(context);

        Log.d("elazarkin", "DebugViewConstructor");
    }

    public DebugView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        Log.d("elazarkin", "DebugViewConstructor");
    }

    public void setBit(Bitmap bit)
    {
        this.bit = bit;
        Log.d("elazarkin", "DebugView setBit");
    }

    @Override
    protected void onDraw(Canvas c)
    {
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.RED);
        p.setTextSize(64);

        Log.d("elazarkin", "bit = " + bit + " txt=" + starsList);

        if(bit != null)
        {
            int radius = bit.getWidth()/50;
            Canvas cbit = new Canvas(bit);
            if(starsList != null)
            {
                String starsPosition[] = starsList.split("\n");
                for(String s:starsPosition)
                {
                    String xy[] = s.split(" ");

                    if(xy.length == 2)
                    {
                        int x = Integer.parseInt(xy[0]);
                        int y = Integer.parseInt(xy[1]);

                        cbit.drawCircle(x-radius/4, y-radius/4, radius, p);

                    }

                }
            }
            else cbit.drawText("No Result!", cbit.getWidth()/2 - 50, bit.getHeight()/2, p);

            Bitmap rotated = rotateBitmap(bit, 270);

            c.drawBitmap
                    (
                            rotated,
                            null,
                            new Rect(0, 0, getWidth() - 1, getHeight() - 1),
                            //new Rect(0, 0, bit.getWidth(), bit.getHeight()),
                            //new Rect(getWidth()-1, getHeight()-1, 0, 0),
                            null
                    );

            rotated.recycle();

//            c.drawBitmap
//            (
//                    bit,
//                    null,
//                    new Rect(0, 0, getWidth() - 1, getHeight() - 1),
//                    //new Rect(0, 0, bit.getWidth(), bit.getHeight()),
//                    //new Rect(getWidth()-1, getHeight()-1, 0, 0),
//                    null
//            );
        }
        else
        {
            c.drawText("Bit = null!", c.getWidth()/2 - 50, c.getHeight()/2, p);
        }
    }

    public void setStarsList(String starsList)
    {
        this.starsList = starsList;
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
