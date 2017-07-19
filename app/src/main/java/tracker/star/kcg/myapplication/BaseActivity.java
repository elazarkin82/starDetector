package tracker.star.kcg.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

/**
 * Created by boaz on 18/07/2017.
 */

public class BaseActivity extends Activity
{
    public static final String PROPERTY_FOCUS_KEY      = "focus";
    public static final String PROPERTY_USE_EXPOSE_ISO = "expose_iso";
    public static final String PROPERTY_RADIUS_KEY     = "radius";
    public static final String PROPERTY_THRESHHOLD_KEY = "threshhold";
    public static final String PROPERTY_PERCENT_KEY    = "percent";
    public static final String PROPERTY_RESOLUTION_KEY = "resolution";
    public static final String PROPERTY_ISO            = "iso";
    public static final String PROPERTY_EXPOSE         = "expose";

    private static File       propertyFile = null;
    private static Properties properties   = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (properties == null)
        {
            propertyFile = new File(getFilesDir(), "settings.xml");
//            propertyFile = new File(Environment.getExternalStorageDirectory(), "settings.xml");
            properties = new Properties();

            try
            {
                Set<String> names;
                properties.load(new FileInputStream(propertyFile));
                names = properties.stringPropertyNames();

                for(String name:names)
                {
                    Log.d("property", "key=" + name + " value=" + properties.getProperty(name));
                }
            }
            catch (Exception e)
            {
            }
        }
    }

    public Properties getProperties()
    {
        return properties;
    }

    public void saveProperties()
    {
        try
        {
            properties.store(new FileOutputStream(propertyFile), "" + new Date().toString());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Log.d("resolution", "error save prop!" + e.toString());
        }
    }
}
