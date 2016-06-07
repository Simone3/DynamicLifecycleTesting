package it.polimi.testing.sample_app;

import android.os.Bundle;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import it.polimi.testing.lifecycle.ActivityRuleLifecycleTest;
import it.polimi.testing.lifecycle.DestroyCallback;
import it.polimi.testing.lifecycle.PauseCallback;
import it.polimi.testing.lifecycle.RecreateCallback;
import it.polimi.testing.lifecycle.StopCallback;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class MainActivityUnitTest extends ActivityRuleLifecycleTest<MainActivity>
{
    @Override
    protected ActivityTestRule<MainActivity> getActivityTestRule()
    {
        return new ActivityTestRule<>(MainActivity.class);
    }

    @Override
    public PauseCallback testPause()
    {
        return new PauseCallback()
        {
            private String textViewText;

            @Override
            public void beforePause()
            {
                assertEquals("Wrong myLifecycleVariable!", MainActivity.RESUMED, activityTestRule.getActivity().myLifecycleVariable);

                final CountDownLatch latch = new CountDownLatch(1);
                activityTestRule.getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        activityTestRule.getActivity().myButton.performClick();

                        textViewText = activityTestRule.getActivity().myTextView.getText().toString();

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

            @Override
            public void whilePaused()
            {
                assertEquals("Wrong myLifecycleVariable!", MainActivity.PAUSED, activityTestRule.getActivity().myLifecycleVariable);
            }

            @Override
            public void afterResume()
            {
                assertEquals("Wrong myLifecycleVariable!", MainActivity.RESUMED, activityTestRule.getActivity().myLifecycleVariable);

                assertEquals("Wrong myTextView text!", textViewText, activityTestRule.getActivity().myTextView.getText().toString());
            }
        };
    }

    @Override
    public StopCallback testStop()
    {
        return new StopCallback()
        {
            private String textViewText;

            @Override
            public void beforeStop()
            {
                assertEquals("Wrong myLifecycleVariable!", MainActivity.RESUMED, activityTestRule.getActivity().myLifecycleVariable);

                final CountDownLatch latch = new CountDownLatch(1);
                activityTestRule.getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        activityTestRule.getActivity().myButton.performClick();

                        textViewText = activityTestRule.getActivity().myTextView.getText().toString();

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

            @Override
            public void whileStopped()
            {
                assertEquals("Wrong myLifecycleVariable!", MainActivity.STOPPED, activityTestRule.getActivity().myLifecycleVariable);
            }

            @Override
            public void afterRestart()
            {
                assertEquals("Wrong myLifecycleVariable!", MainActivity.RESUMED, activityTestRule.getActivity().myLifecycleVariable);

                assertEquals("Wrong myTextView text!", textViewText, activityTestRule.getActivity().myTextView.getText().toString());
            }
        };
    }

    @Override
    public DestroyCallback testDestroy()
    {
        return new DestroyCallback()
        {
            @Override
            public void beforeDestroy()
            {
                assertEquals("Wrong myLifecycleVariable!", MainActivity.RESUMED, activityTestRule.getActivity().myLifecycleVariable);
            }

            @Override
            public void afterDestroy()
            {
                assertEquals("Wrong myLifecycleVariable!", MainActivity.DESTROYED, activityTestRule.getActivity().myLifecycleVariable);
            }
        };
    }

    @Override
    public RecreateCallback testRotation()
    {
        return new RecreateCallback()
        {
            private String textViewText;
            private int bundleVar;

            @Override
            public void beforeRecreation()
            {
                assertEquals("Wrong myLifecycleVariable!", MainActivity.RESUMED, activityTestRule.getActivity().myLifecycleVariable);

                final CountDownLatch latch = new CountDownLatch(1);
                activityTestRule.getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        activityTestRule.getActivity().myButton.performClick();

                        textViewText = activityTestRule.getActivity().myTextView.getText().toString();

                        bundleVar = activityTestRule.getActivity().myBundleVariable;

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

            @Override
            public void checkSavedInstance(Bundle savedInstanceState)
            {
                assertNotNull("Bundle null", savedInstanceState);
                assertTrue("Bundle does not save variable", savedInstanceState.containsKey(MainActivity.BUNDLE_VAR));
                assertEquals("Wrong bundle value", bundleVar, savedInstanceState.getInt(MainActivity.BUNDLE_VAR));
            }

            @Override
            public void afterRecreation()
            {
                assertEquals("Wrong myLifecycleVariable!", MainActivity.RESUMED, activityTestRule.getActivity().myLifecycleVariable);

                assertEquals("Wrong myTextView text!", textViewText, activityTestRule.getActivity().myTextView.getText().toString());
            }
        };
    }

    @Override
    public RecreateCallback testRecreation()
    {
        return testRotation();
    }
}
