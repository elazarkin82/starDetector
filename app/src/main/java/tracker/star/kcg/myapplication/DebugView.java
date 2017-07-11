package tracker.star.kcg.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by elazar on 11/07/17.
 */

public class DebugView extends View
{
    private Bitmap screen = null;

    public DebugView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void setBitmap(Bitmap bit)
    {
        screen = bit;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if(screen != null)
        {
            canvas.rotate(90,canvas.getWidth() / 2, canvas.getHeight()/ 2);
            canvas.drawBitmap
            (
                screen,
                new Rect(0, 0, screen.getWidth(), screen.getHeight()),
                new Rect(0, 0, canvas.getWidth(), canvas.getHeight()),
                null
            );
        }
    }
}
