package tracker.star.kcg.myapplication;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class MainActivity extends Activity
{

    private Camera2BasicFragment cameraFragment;
    //private Camera2RawFragment cameraFragment = null;
    // Used to load the 'native-lib' library on application startup.
    static
    {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (null == savedInstanceState)
        {
            getFragmentManager().beginTransaction().replace(R.id.container, (cameraFragment=Camera2BasicFragment.newInstance())).commit();
            cameraFragment.setOnFrameCallback(new Camera2BasicFragment.FrameCallback()
            {
                @Override
                public void onFrame(byte[] frame, int w, int h)
                {
                    Log.d("elazarkin", "onFrame " + w + "x" + h);
                }
            });
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
