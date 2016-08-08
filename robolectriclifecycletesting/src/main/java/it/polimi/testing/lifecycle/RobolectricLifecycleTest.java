package it.polimi.testing.lifecycle;

import android.app.Activity;
import android.os.Bundle;

import org.junit.Ignore;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

/**
 * Implementation of {@link LifecycleTest} for the "Robolectric" framework
 * @param <T> the activity under test
 */
public abstract class RobolectricLifecycleTest<T extends Activity> extends LifecycleTest
{
    private final Class<T> activityClass;
    private T activity;
    private ActivityController<T> controller;

    /**
     * Constructor
     */
    protected RobolectricLifecycleTest()
    {
        activityClass = getActivityClass();
    }

    /**
     * Use this to pass the class of the activity to be tested
     * @return the activity to be tested
     */
    protected abstract Class<T> getActivityClass();

    /**
     * {@inheritDoc}
     */
    @Override
    void beforeLifecycleTest()
    {
        // Start activity before each test
        controller = Robolectric.buildActivity(activityClass)
                .create()
                .start()
                .resume()
                .visible();
        activity = controller.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void afterLifecycleTest()
    {
        controller = null;
        activity = null;
    }

    /**
     * Use this method to get the activity being currently tested. Use this only inside the lifecycle tests callbacks.
     * @return the activity under test
     */
    protected T getActivity()
    {
        if(activity==null) throw new AssertionError("RobolectricLifecycleTest#getActivity() is just a utility method to retrieve the activity during a lifecycle test, you cannot used it outside it!");
        return activity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void callOnCreate(final Bundle savedInstanceState)
    {
        controller.create();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean callOnRestart()
    {
        controller.restart();
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void callOnStart()
    {
        controller.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void callOnResume()
    {
        controller.resume();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void callOnPause()
    {
        controller.pause();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void callOnStop()
    {
        controller.stop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void callFinish()
    {
        controller.pause();
        controller.stop();
        controller.destroy();
    }

    /**
     * NOT SUPPORTED BY ROBOLECTRIC
     */
    @Override
    void callRotation()
    {
        /**
         * activity.setRequestedOrientation() does not work (activity is just a "shadow")
         *
         * If there's a way to rotate in Robolectric implement it here and remove the overridden empty
         * rotation test in this class
         */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void callRecreation()
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

    /**
     * {@inheritDoc}
     */
    @Override
    Bundle getSavedInstanceState()
    {
        final Bundle savedInstanceState = new Bundle();
        controller.saveInstanceState(savedInstanceState);
        return savedInstanceState;
    }


    /**
     * The rotation test is not supported for Robolectric
     */
    @Override
    public final RotationCallback testRotation()
    {
        return null;
    }
    @Ignore
    @Override
    public void lifecycleTestRotation()
    {

    }
}
