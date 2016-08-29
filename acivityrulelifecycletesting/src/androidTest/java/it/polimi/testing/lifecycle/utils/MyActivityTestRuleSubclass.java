package it.polimi.testing.lifecycle.utils;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;

public class MyActivityTestRuleSubclass<T extends Activity> extends ActivityTestRule<T>
{
    public MyActivityTestRuleSubclass(Class<T> activityClass) {
        super(activityClass);
    }

    public MyActivityTestRuleSubclass(Class<T> activityClass, boolean initialTouchMode) {
        super(activityClass, initialTouchMode);
    }

    public MyActivityTestRuleSubclass(Class<T> activityClass, boolean initialTouchMode, boolean launchActivity) {
        super(activityClass, initialTouchMode, launchActivity);
    }
}
