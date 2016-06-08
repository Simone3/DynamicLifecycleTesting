package it.polimi.testing.sample_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Simple activity that has an EditText for the user name, a Button and a TextView that counts the clicks
 *
 * Also has a (useless) BroadcastReceiver registered in onStart() and unregistered in onStop()
 *
 * Before activity is destroyed it saves the max number of clicks in SharedPreferences
 */
public class MainActivity extends AppCompatActivity
{
    public final static String CLICKS = "CLICKS";
    public final static String MAX_CLICKS_FOR = "MAX_CLICKS_FOR";

    @VisibleForTesting
    int clicks;

    @VisibleForTesting
    Button button;

    @VisibleForTesting
    TextView counter;

    @VisibleForTesting
    EditText name;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Log.v("SampleApp", intent.toString());
        }
    };

    @VisibleForTesting
    boolean broadcastReceiverRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState!=null)
        {
            clicks = savedInstanceState.getInt(CLICKS);
        }
        else
        {
            clicks = 0;
        }

        button = (Button) findViewById(R.id.button);
        counter = (TextView) findViewById(R.id.counter);
        name = (EditText) findViewById(R.id.name);

        if(button!=null) button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                clicks++;
                counter.setText(getString(R.string.counter, clicks));
            }
        });
        counter.setText(getString(R.string.counter, clicks));
    }

    @Override
    public void onStart()
    {
        super.onStart();

        if(!broadcastReceiverRegistered)
        {
            registerReceiver(broadcastReceiver, new IntentFilter());
            broadcastReceiverRegistered = true;
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onStop()
    {
        super.onStop();

        if(broadcastReceiverRegistered)
        {
            unregisterReceiver(broadcastReceiver);
            broadcastReceiverRegistered = false;
        }

        if(name.getText()!=null && name.getText().length()>0)
        {
            System.out.println("ok");
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            int maxClicks = sharedPref.getInt(MAX_CLICKS_FOR+name, 0);
            if(clicks>maxClicks)
            {System.out.println("ok2");
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(MAX_CLICKS_FOR+name.getText().toString(), clicks);
                editor.apply();
            }
            System.out.println("res"+sharedPref.getInt(MainActivity.MAX_CLICKS_FOR+"MyName", 0));
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putInt(CLICKS, clicks);
        super.onSaveInstanceState(savedInstanceState);
    }
}
