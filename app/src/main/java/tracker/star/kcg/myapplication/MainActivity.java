package tracker.star.kcg.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.SettingInjectorService;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

public class MainActivity extends BaseActivity
{
    private byte                 textOut[]      = null;
    private Camera2BasicFragment cameraFragment = null;
    private Bitmap               workBitmap     = null;
    private DebugView            debugView      = null;

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

        debugView = findViewById(R.id.debug_view);
        final Button button = findViewById(R.id.Settings);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // open activity settings
                startActivity(new Intent(getApplicationContext(), Settings.class));
            }
        });

        if (null == savedInstanceState)
        {
            getFragmentManager().beginTransaction().replace(R.id.container, (cameraFragment=Camera2BasicFragment.newInstance())).commit();

            textOut = new byte[65536];

            cameraFragment.setOnPreviewSizeSet(new Camera2BasicFragment.OnPreviewSizeSet()
            {
                @Override
                public Size chooseSize(List<Size> sizes)
                {
                    Size size = null;

                    if(sizes == null && sizes.size() <= 0)
                    {
                        return null;
                    }

                    if(getProperties().getProperty(PROPERTY_RESOLUTION_KEY) == null)
                    {
                        size = sizes.get(0);
                        getProperties().setProperty(PROPERTY_RESOLUTION_KEY, size.getWidth()+"x"+size.getHeight());
                        saveProperties();
                    }
                    else
                    {
                        String resolution[] = getProperties().getProperty(PROPERTY_RESOLUTION_KEY).split("x");
                        try
                        {
                            size = new Size(Integer.decode(resolution[0]), Integer.decode(resolution[1]));
                        }
                        catch (Exception x)
                        {
                            return null;
                        }

                    }
                    return size;
                }
            });

            cameraFragment.setOnFrameCallback(new Camera2BasicFragment.FrameCallback()
            {
                @Override
                public void onFrame(byte[] frame, int w, int h)
                {
                    if(workBitmap == null || workBitmap.getWidth() != w || workBitmap.getHeight() != h)
                    {
                        if(workBitmap != null) workBitmap.recycle();

                        workBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

                        debugView.setBitmap(workBitmap);
                    }

                    findStarsJNI(frame, w, h, textOut, workBitmap);

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run() {debugView.invalidate();}
                    });
                }
            });
        }
    }

    public native void findStarsJNI(byte[] ybuffer, int w, int h, byte[] textOut, Bitmap debug);
}
