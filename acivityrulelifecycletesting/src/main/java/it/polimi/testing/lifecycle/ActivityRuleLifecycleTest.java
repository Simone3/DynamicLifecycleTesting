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

public abstract class ActivityRuleLifecycleTest<T extends Activity> extends LifecycleTest
{
    @Rule
    public ActivityTestRule<T> activityTestRule;

    protected ActivityRuleLifecycleTest()
    {
        this.activityTestRule = getActivityTestRule();
    }

    protected abstract ActivityTestRule<T> getActivityTestRule();

    @Override
    void beforeLifecycleTest()
    {
        // Do nothing here
    }

    @Override
    void afterLifecycleTest()
    {
        // Do nothing here
    }

    @Override
    void callOnCreate(final Bundle savedInstanceState)
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

    @Override
    boolean callOnRestart()
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

    @Override
    void callOnStart()
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

    @Override
    void callOnResume()
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

    @Override
    void callOnPause()
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

    @Override
    void callOnStop()
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

    @Override
    void callFinish()
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

    @Override
    void rotateDevice()
    {
        int currentOrientation = activityTestRule.getActivity().getResources().getConfiguration().orientation;
        int newOrientation = currentOrientation==Configuration.ORIENTATION_LANDSCAPE ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        activityTestRule.getActivity().setRequestedOrientation(newOrientation);
        updateActivityReferenceAfterRecreation();
    }

    @Override
    void recreateActivity()
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

    private void updateActivityReferenceAfterRecreation()
    {
        // The activity changed, retrieve it using the ActivityLifecycleMonitorRegistry
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

        // The activity changed, but there is no setter in ActivityTestRule: need to use reflection to set the private field
        // This of course is not so great, relying on refection (bad) and on current implementation of ActivityTestRule (bad)
        try
        {
            Field f = activityTestRule.getClass().getDeclaredField("mActivity");
            f.setAccessible(true);
            f.set(activityTestRule, activity);
        }
        catch(IllegalAccessException | NoSuchFieldException e)
        {
            throw new IllegalStateException("The implementation of ActivityTestRule has changed, check the ActivityRuleLifecycleTest#updateActivityReferenceAfterRecreation() method.");
        }
    }

    @Override
    Bundle getSavedInstanceState()
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



    private void runInUIThreadSync(Runnable runnable)
    {
        if(Looper.myLooper() == Looper.getMainLooper())
        {
            runnable.run();
        }
        else
        {
            getInstrumentation().runOnMainSync(runnable);
        }
    }
}
