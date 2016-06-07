package it.polimi.testing.lifecycle;

import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;

public class ActivityRuleLifecycleTest_Test extends ActivityRuleLifecycleTest<ControlActivity>
{
    @Override
    protected ActivityTestRule<ControlActivity> getActivityTestRule()
    {
        return new ActivityTestRule<>(ControlActivity.class);
    }

    @Override
    public PauseCallback testPause()
    {
        return LifecycleTest_TestUtils.getTestPauseCallback(activityTestRule.getActivity());
    }

    @Override
    public StopCallback testStop()
    {
        return LifecycleTest_TestUtils.getTestStopCallback(activityTestRule.getActivity());
    }

    @Override
    public DestroyCallback testDestroy()
    {
        return LifecycleTest_TestUtils.getTestDestroyCallback(activityTestRule.getActivity());
    }

    @Override
    public RecreateCallback testRotation()
    {
        return new RecreateCallback()
        {
            private ControlActivity oldActivity;

            @Override
            public void beforeRecreation()
            {
                oldActivity = activityTestRule.getActivity();
                LifecycleTest_TestUtils.recreateCallback_beforeRecreation(activityTestRule.getActivity());
            }

            @Override
            public void checkSavedInstance(Bundle savedInstanceState)
            {
                LifecycleTest_TestUtils.recreateCallback_checkSavedInstance(activityTestRule.getActivity(), savedInstanceState);
            }

            @Override
            public void afterRecreation()
            {
                LifecycleTest_TestUtils.recreateCallback_afterRecreation(oldActivity, activityTestRule.getActivity());
            }
        };
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
                oldActivity = activityTestRule.getActivity();
                LifecycleTest_TestUtils.recreateCallback_beforeRecreation(activityTestRule.getActivity());
            }

            @Override
            public void checkSavedInstance(Bundle savedInstanceState)
            {
                LifecycleTest_TestUtils.recreateCallback_checkSavedInstance(activityTestRule.getActivity(), savedInstanceState);
            }

            @Override
            public void afterRecreation()
            {
                LifecycleTest_TestUtils.recreateCallback_afterRecreation(oldActivity, activityTestRule.getActivity());
            }
        };
    }
}
