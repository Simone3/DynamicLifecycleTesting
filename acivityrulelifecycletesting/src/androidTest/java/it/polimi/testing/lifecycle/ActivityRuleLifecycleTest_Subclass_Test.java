package it.polimi.testing.lifecycle;

import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;

import it.polimi.testing.lifecycle.utils.MyActivityTestRuleSubclass;

/**
 * Test for the tests. Use the "lifecycletesting_tests" module to see if this implementation correctly calls
 * all the lifecycle methods.
 *
 * Similar to ActivityRuleLifecycleTest_Test but passing a subclass of ActivityTestRule
 */
public class ActivityRuleLifecycleTest_Subclass_Test extends ActivityRuleLifecycleTest<ControlActivity>
{
    @Override
    protected ActivityTestRule<ControlActivity> initializeActivityTestRule()
    {
        return new MyActivityTestRuleSubclass<>(ControlActivity.class);
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
    public RotationCallback testRotation()
    {
        return new RotationCallback()
        {
            private ControlActivity oldActivity;

            @Override
            public void beforeRotation()
            {
                oldActivity = activityTestRule.getActivity();
                LifecycleTest_TestUtils.recreateCallback_beforeRecreation(activityTestRule.getActivity());
            }

            @Override
            public void afterRotation()
            {
                LifecycleTest_TestUtils.rotationCallback_afterRotation(oldActivity, activityTestRule.getActivity());
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
