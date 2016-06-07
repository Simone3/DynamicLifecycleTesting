package it.polimi.testing.sample_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;


public class MainActivity extends AppCompatActivity
{
    final static int CREATED = 0;
    final static int STARTED = 1;
    final static int RESUMED = 2;
    final static int PAUSED = 3;
    final static int STOPPED = 4;
    final static int DESTROYED = 5;

    int myLifecycleVariable = -1;

    final static String BUNDLE_VAR = "BUNDLE_VAR";

    int myBundleVariable = 0;

    TextView myTextView;
    TextView mySecondTextView;
    Button myButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState!=null)
        {
            myBundleVariable = savedInstanceState.getInt(BUNDLE_VAR);
        }
        else
        {
            myBundleVariable = 100 + (new Random()).nextInt(101);
        }

        myTextView = (TextView) findViewById(R.id.myTextView);
        mySecondTextView = (TextView) findViewById(R.id.mySecondTextView);

        myButton = (Button) findViewById(R.id.myButton);
        if(myButton!=null) myButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(myTextView!=null) myTextView.setText(getString(R.string.my_value, 500 + (new Random()).nextInt(101)));
            }
        });

        myLifecycleVariable = CREATED;
        System.out.println("[-MAIN ACTIVITY LIFECYCLE] "+this+"---created");
    }

    @Override
    public void onStart()
    {
        super.onStart();
        myLifecycleVariable = STARTED;
        mySecondTextView.setText(String.valueOf(myLifecycleVariable));
        System.out.println("[-MAIN ACTIVITY LIFECYCLE] "+this+"---started");
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        System.out.println("[-MAIN ACTIVITY LIFECYCLE] "+this+"---REstarted");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        myLifecycleVariable = RESUMED;
        mySecondTextView.setText(String.valueOf(myLifecycleVariable));
        System.out.println("[-MAIN ACTIVITY LIFECYCLE] "+this+"---resumed");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        myLifecycleVariable = PAUSED;
        mySecondTextView.setText(String.valueOf(myLifecycleVariable));
        System.out.println("[-MAIN ACTIVITY LIFECYCLE] "+this+"---paused");
    }

    @Override
    public void onStop()
    {
        super.onStop();
        myLifecycleVariable = STOPPED;
        mySecondTextView.setText(String.valueOf(myLifecycleVariable));
        System.out.println("[-MAIN ACTIVITY LIFECYCLE] "+this+"---stopped");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        myLifecycleVariable = DESTROYED;
        mySecondTextView.setText(String.valueOf(myLifecycleVariable));
        System.out.println("[-MAIN ACTIVITY LIFECYCLE] "+this+"---destroyed");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putInt(BUNDLE_VAR, myBundleVariable);
        super.onSaveInstanceState(savedInstanceState);
        System.out.println("[-MAIN ACTIVITY LIFECYCLE] "+this+"---save instance");
    }
}
