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

import java.util.concurrent.Semaphore;

/**
 * Created by elazar on 21/06/17.
 */

public class DebugView extends View
{
    private Bitmap bit = null;
    private Canvas cbit = null;
    private String starsList;
    private Paint p;
    private float RAD_AS_PERCENT_OF_W = 0.02f;
    private long algorithmTime = 0L;

    private Semaphore bitSemaphore = new Semaphore(1);

    public DebugView(Context context)
    {
        super(context);

        init();

        Log.d("elazarkin", "DebugViewConstructor");
    }

    public DebugView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init();
        Log.d("elazarkin", "DebugViewConstructor");
    }

    private void init()
    {
        p = new Paint();
    }

    public void setBit(Bitmap b)
    {
        try
        {
            bitSemaphore.acquire();
        }
        catch (InterruptedException e)
        {
            bitSemaphore.release();
            return;
        }
        if(bit != null && (bit.getWidth() != b.getWidth() || bit.getHeight() != b.getWidth()))
        {
            bit.recycle();
            bit = null;
        }

        if(bit == null)
        {
            bit = b.copy(Bitmap.Config.ARGB_8888, true);
            cbit = new Canvas(bit);
        }
        else
        {
            cbit.drawBitmap(b, 0, 0, null);
        }

        bitSemaphore.release();
    }

    public void setAlgorithmTime(long ms)
    {
        algorithmTime = ms;
    }

    public void setRadiusAsPercentofWidth(float percent)
    {
        RAD_AS_PERCENT_OF_W = percent/100.0f;
    }

    @Override
    protected void onDraw(Canvas c)
    {
        try
        {
            bitSemaphore.acquire();
        }
        catch (InterruptedException e) {bitSemaphore.release();return;}

        if(bit != null)
        {
            boolean USE_ROTATE = true;
            int radius = (int) (bit.getWidth()*RAD_AS_PERCENT_OF_W);

            if(starsList != null)
            {
                String starsPosition[] = starsList.split("\n");

                p.setStyle(Paint.Style.STROKE);
                p.setColor(Color.RED);

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

            if(USE_ROTATE)
            {
                Bitmap rotated = rotateBitmap(bit, 270);
                c.drawBitmap
                (
                        rotated,
                        new Rect(0, 0, rotated.getWidth(), rotated.getHeight()),
                        new Rect(0, 0, getWidth(), getHeight()),
                        null
                );

                rotated.recycle();
            }
            else
            {
                c.drawBitmap
                (
                        bit,
                        new Rect(0, 0, bit.getWidth(), bit.getHeight()),
                        new Rect(0, 0, getWidth() - 1, getHeight() - 1),
                        null
                );
            }

            p.setColor(Color.BLUE);
            p.setTextSize(c.getHeight()*0.02f);
            c.drawText(String.format("Algorithm time take %d ms", algorithmTime), 50, 50, p);
        }
        else
        {
            c.drawText("Bit = null!", c.getWidth()/2 - 50, c.getHeight()/2, p);
        }

        bitSemaphore.release();
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
