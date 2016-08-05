package it.polimi.testing.lifecycle;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Looper;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;

import org.junit.Rule;

import java.lang.reflect.Field;
import java.util.Collection;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * Implementation of {@link LifecycleTest} for the "Activity Test Rule" framework, used for unit
 * testing or Espresso
 * @param <T> the activity under test
 */
public abstract class ActivityRuleLifecycleTest<T extends Activity> extends LifecycleTest
{
    @Rule
    public final ActivityTestRule<T> activityTestRule;

    /**
     * Constructor
     */
    protected ActivityRuleLifecycleTest()
    {
        this.activityTestRule = initializeActivityTestRule();
    }

    /**
     * Use this method to initialize the ActivityTestRule, i.e. do NOT define the @Rule
     * variable yourself
     * @return the test rule of the test suite
     */
    protected abstract ActivityTestRule<T> initializeActivityTestRule();

    /**
     * {@inheritDoc}
     */
    @Override
    void beforeLifecycleTest()
    {
        // Do nothing here
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void afterLifecycleTest()
    {
        // Do nothing here
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void callOnCreate(final Bundle savedInstanceState)
    {
        runInUIThreadSync(new Runnable()
        {
            @Override
            public void run()
            {
                getInstrumentation().callActivityOnCreate(activityTestRule.getActivity(), savedInstanceState);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean callOnRestart()
    {
        runInUIThreadSync(new Runnable()
        {
            @Override
            public void run()
            {
                getInstrumentation().callActivityOnRestart(activityTestRule.getActivity());
            }
        });
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void callOnStart()
    {
        runInUIThreadSync(new Runnable()
        {
            @Override
            public void run()
            {
                getInstrumentation().callActivityOnStart(activityTestRule.getActivity());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void callOnResume()
    {
        runInUIThreadSync(new Runnable()
        {
            @Override
            public void run()
            {
                getInstrumentation().callActivityOnResume(activityTestRule.getActivity());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void callOnPause()
    {
        runInUIThreadSync(new Runnable()
        {
            @Override
            public void run()
            {
                getInstrumentation().callActivityOnPause(activityTestRule.getActivity());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void callOnStop()
    {
        runInUIThreadSync(new Runnable()
        {
            @Override
            public void run()
            {
                getInstrumentation().callActivityOnStop(activityTestRule.getActivity());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void callFinish()
    {
        runInUIThreadSync(new Runnable()
        {
            @Override
            public void run()
            {
                activityTestRule.getActivity().finish();
            }
        });

        // TODO this sleep here is very bad but I didn't find a better solution. Fix!
        // Problem is that finish() is async so when we get here it may still be in progress
        // Tried to wait for activity.isFinishing() to be true with Awaitility but does not solve the problem (it's finisING, not finishED)
        try
        {
            Thread.sleep(2000);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void rotateDevice()
    {
        int currentOrientation = activityTestRule.getActivity().getResources().getConfiguration().orientation;
        int newOrientation = currentOrientation==Configuration.ORIENTATION_LANDSCAPE ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        activityTestRule.getActivity().setRequestedOrientation(newOrientation);
        updateActivityReferenceAfterRecreation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void recreateActivity()
    {
        runInUIThreadSync(new Runnable()
        {
            @Override
            public void run()
            {
                activityTestRule.getActivity().recreate();
            }
        });
        updateActivityReferenceAfterRecreation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Bundle getSavedInstanceState()
    {
        final Bundle savedInstanceState = new Bundle();
        runInUIThreadSync(new Runnable()
        {
            @Override
            public void run()
            {
                getInstrumentation().callActivityOnSaveInstanceState(activityTestRule.getActivity(), savedInstanceState);
            }
        });
        return savedInstanceState;
    }

    /**
     * Helper to run some code on the UI Thread in a synchronous way (i.e. this method returns only after the
     * runnable has completed)
     * @param runnable the code to run
     */
    private void runInUIThreadSync(Runnable runnable)
    {
        // If we are already in the main thread, just run it
        if(Looper.myLooper() == Looper.getMainLooper())
        {
            runnable.run();
        }

        // Otherwise call the instrumentation method
        else
        {
            getInstrumentation().runOnMainSync(runnable);
        }
    }

    /**
     * During a recreation the activity object is destroyed and a new instance is created, i.e. the pointer
     * activityTestRule.getActivity() contains the old destroyed activity. This helper retrieves the new instance
     * of the activity and sets it in the activityTestRule.
     */
    private void updateActivityReferenceAfterRecreation()
    {
        // Retrieve the new instance using the ActivityLifecycleMonitorRegistry
        getInstrumentation().waitForIdleSync();
        class Wrapper {Activity activity;}
        final Wrapper wrapper = new Wrapper();
        runInUIThreadSync(new Runnable()
        {
            @Override
            public void run()
            {
                Collection<Activity> activities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                wrapper.activity = activities.iterator().next();
            }
        });
        Activity activity = wrapper.activity;

        // There is no setter in ActivityTestRule: need to use reflection to set the private field
        // This of course is not so great, relying on refection (bad) and on current implementation of ActivityTestRule (bad)
        try
        {
            Field f = activityTestRule.getClass().getDeclaredField("mActivity");
            f.setAccessible(true);
            f.set(activityTestRule, activity);
        }
        catch(Exception e)
        {
            throw new IllegalStateException("The implementation of ActivityTestRule has changed, check the ActivityRuleLifecycleTest#updateActivityReferenceAfterRecreation() method.");
        }
    }
}
