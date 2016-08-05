package it.polimi.testing.sample_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import it.polimi.testing.lifecycle.ActivityRuleLifecycleTest;
import it.polimi.testing.lifecycle.DestroyCallback;
import it.polimi.testing.lifecycle.PauseCallback;
import it.polimi.testing.lifecycle.RecreateCallback;
import it.polimi.testing.lifecycle.RotationCallback;
import it.polimi.testing.lifecycle.StopCallback;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Example of lifecycle tests for the ActivityTestRule unit tests
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class MainActivityUnitTest extends ActivityRuleLifecycleTest<MainActivity>
{
    @Override
    protected ActivityTestRule<MainActivity> initializeActivityTestRule()
    {
        return new ActivityTestRule<>(MainActivity.class);
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
                assertTrue("BroadcastReceiver not active", activityTestRule.getActivity().broadcastReceiverRegistered);
            }

            @Override
            public void whileStopped()
            {
                // BroadcastReceiver is disabled
                assertFalse("BroadcastReceiver active while stopped", activityTestRule.getActivity().broadcastReceiverRegistered);
            }

            @Override
            public void afterRestart()
            {
                // BroadcastReceiver is enabled
                assertTrue("BroadcastReceiver not active", activityTestRule.getActivity().broadcastReceiverRegistered);
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
                write(activityTestRule.getActivity().name, "MyName");
                for(int i=0; i<4; i++) click(activityTestRule.getActivity().button);
            }

            @Override
            public void afterDestroy()
            {
                // Check that when the app is killed the shared preferences are always updated
                SharedPreferences sharedPref = activityTestRule.getActivity().getPreferences(Context.MODE_PRIVATE);
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
                write(activityTestRule.getActivity().name, "MyName");
                for(int i=0; i<2; i++) click(activityTestRule.getActivity().button);

                // Check counter before the recreation
                assertEquals("Counter not working",
                        2,
                        activityTestRule.getActivity().clicks);
                assertEquals("Counter text not working",
                        activityTestRule.getActivity().getString(R.string.counter, 2),
                        activityTestRule.getActivity().counter.getText().toString());
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
                        activityTestRule.getActivity().name.getText().toString());

                // Check that the counter is correctly restored
                assertEquals("Counter not restored",
                        2,
                        activityTestRule.getActivity().clicks);
                assertEquals("Counter text not restored",
                        activityTestRule.getActivity().getString(R.string.counter, 2),
                        activityTestRule.getActivity().counter.getText().toString());
            }
        };
    }

    @Override
    public RotationCallback testRotation()
    {
        return new RotationCallback()
        {
            @Override
            public void beforeRotation()
            {
                // Before, the layout should be vertical
                LinearLayout container = (LinearLayout) activityTestRule.getActivity().findViewById(R.id.container);
                assertNotNull("Container null", container);
                assertEquals("Wrong orientation before", container.getOrientation(), LinearLayout.VERTICAL);
            }

            @Override
            public void afterRotation()
            {
                // After, the layout should be horizontal
                LinearLayout container = (LinearLayout) activityTestRule.getActivity().findViewById(R.id.container);
                assertNotNull("Container null", container);
                assertEquals("Wrong orientation after", container.getOrientation(), LinearLayout.HORIZONTAL);
            }
        };
    }

    /**
     * Can also define custom lifecycle tests
     */
    @Test
    public void myCustomLifecycleTest()
    {
        // Actions and checks
        write(activityTestRule.getActivity().name, "MyName");
        click(activityTestRule.getActivity().button);
        assertEquals("Counter not working", 1, activityTestRule.getActivity().clicks);

        // Pause
        callOnPause();

        // Resume
        callOnResume();

        // Actions and checks
        click(activityTestRule.getActivity().button);
        assertEquals("Counter not working", 2, activityTestRule.getActivity().clicks);

        // Stop
        callOnPause();
        callOnStop();

        // Restart
        boolean alsoCallsOnStart = callOnRestart();
        if(!alsoCallsOnStart) callOnStart();
        callOnResume();

        // Actions and checks
        click(activityTestRule.getActivity().button);
        assertEquals("Counter not working", 3, activityTestRule.getActivity().clicks);
    }






    // Some internal helpers:

    private void click(final Button button)
    {
        final CountDownLatch latch = new CountDownLatch(1);
        activityTestRule.getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                button.performClick();
                latch.countDown();
            }
        });
        try
        {
            latch.await(20, TimeUnit.SECONDS);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private void write(final EditText editText, final String text)
    {
        final CountDownLatch latch = new CountDownLatch(1);
        activityTestRule.getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                editText.setText(text);
                latch.countDown();
            }
        });
        try
        {
            latch.await(20, TimeUnit.SECONDS);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
