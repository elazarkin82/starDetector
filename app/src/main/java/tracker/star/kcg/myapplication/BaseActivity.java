package tracker.star.kcg.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * Created by boaz on 18/07/2017.
 */

public class BaseActivity extends Activity
{
    protected static final String PROPERTY_FOCUS_KEY = "focus";
    protected static final String PROPERTY_RADIUS_KEY = "radius";
    protected static final String PROPERTY_THRESHHOLD_KEY = "threshhold";
    protected static final String PROPERTY_PERCENT_KEY = "percent";
    protected static final String PROPERTY_RESOLUTION_KEY = "resolution";

    private static File propertyFile = null;
    private static Properties properties = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if(properties == null) {
            //propertyFile = new File(getFilesDir(), "settings.xml");
            propertyFile = new File(Environment.getExternalStorageDirectory(), "settings.xml");
            properties = new Properties();

            try {
                properties.loadFromXML(new FileInputStream(propertyFile));
            } catch (Exception e) {
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
