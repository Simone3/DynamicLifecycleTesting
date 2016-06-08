package it.polimi.testing.sample_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import it.polimi.testing.lifecycle.DestroyCallback;
import it.polimi.testing.lifecycle.PauseCallback;
import it.polimi.testing.lifecycle.RecreateCallback;
import it.polimi.testing.lifecycle.RobolectricLifecycleTest;
import it.polimi.testing.lifecycle.StopCallback;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Example of lifecycle tests for the Robolectric framework
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MainActivityRobolectricTest extends RobolectricLifecycleTest<MainActivity>
{
    @Override
    protected Class<MainActivity> getActivityClass()
    {
        return MainActivity.class;
    }

    @Override
    public StopCallback testStop()
    {
        // Return null if you are not interested in this lifecycle test
        return new StopCallback()
        {
            @Override
            public void beforeStop()
            {
                // BroadcastReceiver is enabled
                assertTrue("BroadcastReceiver not active", getActivity().broadcastReceiverRegistered);
            }

            @Override
            public void whileStopped()
            {
                // BroadcastReceiver is disabled
                assertFalse("BroadcastReceiver active while stopped", getActivity().broadcastReceiverRegistered);
            }

            @Override
            public void afterRestart()
            {
                // BroadcastReceiver is enabled
                assertTrue("BroadcastReceiver not active", getActivity().broadcastReceiverRegistered);
            }
        };
    }

    @Override
    public PauseCallback testPause()
    {
        // Return null if you are not interested in this lifecycle test
        return null;
    }

    @Override
    public DestroyCallback testDestroy()
    {
        return new DestroyCallback()
        {
            @Override
            public void beforeDestroy()
            {
                // Perform some actions
                getActivity().name.setText("MyName");
                for(int i=0; i<4; i++) getActivity().button.performClick();
            }

            @Override
            public void afterDestroy()
            {
                // Check that when the app is killed the shared preferences are always updated
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                int savedMaxClicks = sharedPref.getInt(MainActivity.MAX_CLICKS_FOR+"MyName", 0);
                assertTrue("Max clicks not saved", savedMaxClicks>=4);
            }
        };
    }

    @Override
    public RecreateCallback testRecreation()
    {
        return new RecreateCallback()
        {
            @Override
            public void beforeRecreation()
            {
                // Perform some actions
                getActivity().name.setText("MyName");
                for(int i=0; i<2; i++) getActivity().button.performClick();

                // Check counter before the recreation
                assertEquals("Counter not working",
                        2,
                        getActivity().clicks);
                assertEquals("Counter text not working",
                        getActivity().getString(R.string.counter, 2),
                        getActivity().counter.getText().toString());
            }

            @Override
            public void checkSavedInstance(Bundle savedInstanceState)
            {
                // Check that the bundle contains the right number of clicks
                assertNotNull("Bundle null", savedInstanceState);
                assertTrue("Bundle does not save variable", savedInstanceState.containsKey(MainActivity.CLICKS));
                assertEquals("Wrong number of clicks", 2, savedInstanceState.getInt(MainActivity.CLICKS));
            }

            @Override
            public void afterRecreation()
            {
                // Check name input is still there
                assertEquals("Name lost with recreation",
                        "MyName",
                        getActivity().name.getText().toString());

                // Check that the counter is correctly restored
                assertEquals("Counter not restored",
                        2,
                        getActivity().clicks);
                assertEquals("Counter text not restored",
                        getActivity().getString(R.string.counter, 2),
                        getActivity().counter.getText().toString());
            }
        };
    }
}
