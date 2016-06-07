package it.polimi.testing.lifecycle;

import android.app.Activity;
import android.os.Bundle;

import org.junit.Ignore;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

public abstract class RobolectricLifecycleTest<T extends Activity> extends LifecycleTest
{
    private Class<T> activityClass;
    private T activity;
    private ActivityController<T> controller;

    protected RobolectricLifecycleTest()
    {
        activityClass = getActivityClass();
    }

    protected abstract Class<T> getActivityClass();

    @Override
    void beforeLifecycleTest()
    {
        controller = Robolectric.buildActivity(activityClass)
                .create()
                .start()
                .resume()
                .visible();
        activity = controller.get();
    }

    @Override
    void afterLifecycleTest()
    {
        controller = null;
        activity = null;
    }

    protected T getActivity()
    {
        if(activity==null) throw new AssertionError("RobolectricLifecycleTest#getActivity() is just a utility method to retrieve the activity during a lifecycle test, you cannot used it outside it!");
        return activity;
    }

    @Override
    void callOnCreate(final Bundle savedInstanceState)
    {
        controller.create();
    }

    @Override
    boolean callOnRestart()
    {
        controller.restart();
        return true;
    }

    @Override
    void callOnStart()
    {
        controller.start();
    }

    @Override
    void callOnResume()
    {
        controller.resume();
    }

    @Override
    void callOnPause()
    {
        controller.pause();
    }

    @Override
    void callOnStop()
    {
        controller.stop();
    }

    @Override
    void callFinish()
    {
        controller.pause();
        controller.stop();
        controller.destroy();
    }

    @Override
    void rotateDevice()
    {
        /**
         * activity.setRequestedOrientation() does not work (activity is just a "shadow")
         *
         * If there's a way to rotate in Robolectric implement it here and remove the overridden empty
         * rotation test in this class
         */
    }

    @Override
    void recreateActivity()
    {
        Bundle savedInstanceState = new Bundle();

        controller
                .pause()
                .saveInstanceState(savedInstanceState)
                .stop()
                .destroy();

        controller = Robolectric.buildActivity(activityClass)
                .create(savedInstanceState)
                .start()
                .restoreInstanceState(savedInstanceState)
                .resume()
                .visible();
        activity = controller.get();
    }

    @Override
    Bundle getSavedInstanceState()
    {
        final Bundle savedInstanceState = new Bundle();
        controller.saveInstanceState(savedInstanceState);
        return savedInstanceState;
    }







    @Ignore
    @Override
    public void lifecycleTestRotation()
    {

    }
    @Override
    public final RecreateCallback testRotation()
    {
        return null;
    }
}
