package tracker.star.kcg.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.view.textclassifier.TextClassification;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Settings extends BaseActivity {
    int Radius=0;
    int threshold=0;
    int percent=0;
    int res=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final Button buttonAF = findViewById(R.id.buttonAF);
        buttonAF.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // set Auto focus On /off
                startActivity(new Intent(getApplicationContext(), Settings.class));
            }
        });

        final EditText radiusEV = (EditText) findViewById(R.id.textEditR);
        final EditText THEV = (EditText) findViewById(R.id.textEditT);
        final EditText PTEV= (EditText) findViewById(R.id.textEditP);
        final Spinner ResEV = (Spinner) findViewById(R.id.resolution_spinner);

        Properties prop = getProperties();

        radiusEV.setText(prop.getProperty(PROPERTY_RADIUS_KEY, "0.8"));
        THEV.setText(prop.getProperty(PROPERTY_THRESHHOLD_KEY, "80"));
        PTEV.setText(prop.getProperty(PROPERTY_PERCENT_KEY, "80"));

        try
        {
            CameraManager manager  = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            CameraCharacteristics characteristics = manager.getCameraCharacteristics("0");
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            List<Size> sizes = Arrays.asList(map.getOutputSizes(ImageFormat.YUV_420_888));
            ArrayAdapter<String> adapter = null;

            List<String> list = new ArrayList<String>();

            for(Size s:sizes)
            {
                list.add(s.getWidth() + "x" + s.getHeight());
            }

            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);

            ResEV.setAdapter(adapter);

            if(getProperties().getProperty(PROPERTY_RESOLUTION_KEY) != null)
            {
                Log.d("resolution", "" + getProperties().getProperty(PROPERTY_RESOLUTION_KEY) + " index = " + adapter.getPosition(getProperties().getProperty(PROPERTY_RESOLUTION_KEY)));
                ResEV.setSelection(adapter.getPosition(getProperties().getProperty(PROPERTY_RESOLUTION_KEY)));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

//radius button
        final Button buttonS = findViewById(R.id.save);
        buttonS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // set radius
                getProperties().setProperty(PROPERTY_RADIUS_KEY, "" + radiusEV.getText().toString());
                // set threshhold
                getProperties().setProperty(PROPERTY_THRESHHOLD_KEY, "" + THEV.getText().toString());
                // set percent
                getProperties().setProperty(PROPERTY_PERCENT_KEY, "" + PTEV.getText().toString());
                // set resolution
                getProperties().setProperty(PROPERTY_RESOLUTION_KEY, ResEV.getSelectedItem().toString());

                Log.d("resolution", "" + getProperties().getProperty(PROPERTY_RESOLUTION_KEY));

                saveProperties();

                finish();
            }
        });

    }

}
