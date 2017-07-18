package tracker.star.kcg.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
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
            canvas.drawBitmap
            (
                rotateBitmap(screen, 90),
                null,
                new Rect(0, 0, canvas.getWidth(), canvas.getHeight()),
                null
            );
        }
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
