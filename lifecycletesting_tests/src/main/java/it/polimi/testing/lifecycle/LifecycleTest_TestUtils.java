package it.polimi.testing.lifecycle;

import android.os.Bundle;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;

/**
 * Some utils to create the callbacks used during the tests
 */
class LifecycleTest_TestUtils
{
    public static PauseCallback getTestPauseCallback(final ControlActivity activity)
    {
        return new PauseCallback()
        {
            @Override
            public void beforePause()
            {
                activity.assertSingleTraversed();
            }

            @Override
            public void whilePaused()
            {
                activity.assertSingleTraversed(
                        ControlActivity.ON_PAUSE);
            }

            @Override
            public void afterResume()
            {
                activity.assertSingleTraversed(
                        ControlActivity.ON_PAUSE,
                        ControlActivity.ON_RESUME);
            }
        };
    }

    public static StopCallback getTestStopCallback(final ControlActivity activity)
    {
        return new StopCallback()
        {
            @Override
            public void beforeStop()
            {
                activity.assertSingleTraversed();
            }

            @Override
            public void whileStopped()
            {
                activity.assertSingleTraversed(
                        ControlActivity.ON_PAUSE,
                        ControlActivity.ON_STOP);
            }

            @Override
            public void afterRestart()
            {
                activity.assertSingleTraversed(
                        ControlActivity.ON_PAUSE,
                        ControlActivity.ON_STOP,
                        ControlActivity.ON_RESTART,
                        ControlActivity.ON_START,
                        ControlActivity.ON_RESUME);
            }
        };
    }

    public static DestroyCallback getTestDestroyCallback(final ControlActivity activity)
    {
        return new DestroyCallback()
        {
            @Override
            public void beforeDestroy()
            {
                activity.assertSingleTraversed();
            }

            @Override
            public void afterDestroy()
            {
                activity.assertMultipleTraversed(
                        Arrays.asList(
                                ControlActivity.ON_PAUSE,
                                ControlActivity.ON_STOP,
                                ControlActivity.ON_DESTROY),
                        Arrays.asList(
                                ControlActivity.ON_PAUSE,
                                ControlActivity.ON_STOP));
            }
        };
    }

    public static void recreateCallback_beforeRecreation(ControlActivity activity)
    {
        activity.assertSingleTraversed();
    }

    public static void recreateCallback_checkSavedInstance(ControlActivity activity, Bundle savedInstanceState)
    {
        activity.assertSingleTraversed(
                ControlActivity.ON_SAVE_INSTANCE_STATE);

        assertNotNull("Bundle null", savedInstanceState);
        assertTrue("No key in bundle", savedInstanceState.containsKey(ControlActivity.BUNDLE_KEY));
    }

    public static void recreateCallback_afterRecreation(ControlActivity oldActivity, ControlActivity newActivity)
    {
        assertNotSame("Activity is not recreated", oldActivity, newActivity);

        assertEquals("Restored bundle did not work", oldActivity.bundleValue, newActivity.bundleValue);

        oldActivity.assertSingleTraversed(
                ControlActivity.ON_SAVE_INSTANCE_STATE,
                ControlActivity.ON_PAUSE,
                ControlActivity.ON_SAVE_INSTANCE_STATE,
                ControlActivity.ON_STOP,
                ControlActivity.ON_DESTROY
        );

        newActivity.assertSingleTraversed();
    }

    public static void rotationCallback_afterRotation(ControlActivity oldActivity, ControlActivity newActivity)
    {
        assertNotSame("Activity is not recreated", oldActivity, newActivity);

        assertEquals("Restored bundle did not work", oldActivity.bundleValue, newActivity.bundleValue);

        oldActivity.assertSingleTraversed(
                ControlActivity.ON_PAUSE,
                ControlActivity.ON_SAVE_INSTANCE_STATE,
                ControlActivity.ON_STOP,
                ControlActivity.ON_DESTROY
        );

        newActivity.assertSingleTraversed();
    }
}
