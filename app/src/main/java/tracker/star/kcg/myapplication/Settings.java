package tracker.star.kcg.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.textclassifier.TextClassification;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Settings extends BaseActivity {
    int Radius=0;
    int threshold=0;
    int percent=0;
    int res=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final Button buttonAF = findViewById(R.id.buttonAF);
        buttonAF.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // set Auto focus On /off
                startActivity(new Intent(getApplicationContext(), Settings.class));
            }
        });

        final TextView radiusTextView = (TextView) findViewById(R.id.textViewR);
        final TextView THTextView = (TextView) findViewById(R.id.textViewT);
        final TextView PTextView = (TextView) findViewById(R.id.textViewP);
        final TextView ResTextView = (TextView) findViewById(R.id.textViewRes);
        final EditText radiusEV = (EditText) findViewById(R.id.textEditR);
        final EditText THEV = (EditText) findViewById(R.id.textEditT);
        final EditText PTEV= (EditText) findViewById(R.id.textEditP);
        final EditText ResEV = (EditText) findViewById(R.id.textEditRes);

//radius button
        final Button buttonS = findViewById(R.id.save);
        buttonS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // set radius
                String r = radiusEV.getText().toString();
                Radius = Integer.parseInt(r);
                // set threshhold
                String t = THEV.getText().toString();
                threshold = Integer.parseInt(t);
                // set percent
                String p = PTEV.getText().toString();
                percent = Integer.parseInt(p);
                // set resolution
                String re = ResEV.getText().toString();
                res = Integer.parseInt(re);
            }
        });

    }

}
