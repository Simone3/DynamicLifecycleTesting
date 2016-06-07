package it.polimi.testing.sample_app;

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
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

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
    public PauseCallback testPause()
    {
        return new PauseCallback()
        {
            private String textViewText;

            @Override
            public void beforePause()
            {
                assertEquals("Wrong myLifecycleVariable!", MainActivity.RESUMED, getActivity().myLifecycleVariable);

                getActivity().myButton.performClick();

                textViewText = getActivity().myTextView.getText().toString();
            }

            @Override
            public void whilePaused()
            {
                assertEquals("Wrong myLifecycleVariable!", MainActivity.PAUSED, getActivity().myLifecycleVariable);
            }

            @Override
            public void afterResume()
            {
                assertEquals("Wrong myLifecycleVariable!", MainActivity.RESUMED, getActivity().myLifecycleVariable);

                assertEquals("Wrong myTextView text!", textViewText, getActivity().myTextView.getText().toString());
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
                assertEquals("Wrong myLifecycleVariable!", MainActivity.RESUMED, getActivity().myLifecycleVariable);

                getActivity().myButton.performClick();

                textViewText = getActivity().myTextView.getText().toString();
            }

            @Override
            public void whileStopped()
            {
                assertEquals("Wrong myLifecycleVariable!", MainActivity.STOPPED, getActivity().myLifecycleVariable);
            }

            @Override
            public void afterRestart()
            {
                assertEquals("Wrong myLifecycleVariable!", MainActivity.RESUMED, getActivity().myLifecycleVariable);

                assertEquals("Wrong myTextView text!", textViewText, getActivity().myTextView.getText().toString());
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
                assertEquals("Wrong myLifecycleVariable!", MainActivity.RESUMED, getActivity().myLifecycleVariable);
            }

            @Override
            public void afterDestroy()
            {
                assertTrue("Wrong myLifecycleVariable!", getActivity().myLifecycleVariable==MainActivity.DESTROYED || getActivity().myLifecycleVariable==MainActivity.STOPPED);
            }
        };
    }

    @Override
    public RecreateCallback testRecreation()
    {
        return new RecreateCallback()
        {
            private String textViewText;
            private int bundleVar;

            @Override
            public void beforeRecreation()
            {
                assertEquals("Wrong myLifecycleVariable!", MainActivity.RESUMED, getActivity().myLifecycleVariable);

                getActivity().myButton.performClick();

                textViewText = getActivity().myTextView.getText().toString();

                bundleVar = getActivity().myBundleVariable;
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
                assertEquals("Wrong myLifecycleVariable!", MainActivity.RESUMED, getActivity().myLifecycleVariable);

                assertEquals("Wrong myTextView text!", textViewText, getActivity().myTextView.getText().toString());
            }
        };
    }
}
