package it.polimi.testing.lifecycle;

import android.os.Bundle;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RobolectricLifecycleTest_Test extends RobolectricLifecycleTest<ControlActivity>
{
    @Override
    protected Class<ControlActivity> getActivityClass()
    {
        return ControlActivity.class;
    }

    @Override
    public PauseCallback testPause()
    {
        return LifecycleTest_TestUtils.getTestPauseCallback(getActivity());
    }

    @Override
    public StopCallback testStop()
    {
        return LifecycleTest_TestUtils.getTestStopCallback(getActivity());
    }

    @Override
    public DestroyCallback testDestroy()
    {
        return LifecycleTest_TestUtils.getTestDestroyCallback(getActivity());
    }

    @Override
    public RecreateCallback testRecreation()
    {
        return new RecreateCallback()
        {
            private ControlActivity oldActivity;

            @Override
            public void beforeRecreation()
            {
                oldActivity = getActivity();
                LifecycleTest_TestUtils.recreateCallback_beforeRecreation(getActivity());
            }

            @Override
            public void checkSavedInstance(Bundle savedInstanceState)
            {
                LifecycleTest_TestUtils.recreateCallback_checkSavedInstance(getActivity(), savedInstanceState);
            }

            @Override
            public void afterRecreation()
            {
                LifecycleTest_TestUtils.recreateCallback_afterRecreation(oldActivity, getActivity());
            }
        };
    }
}
