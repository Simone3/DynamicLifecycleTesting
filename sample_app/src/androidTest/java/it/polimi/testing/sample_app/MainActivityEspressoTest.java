package it.polimi.testing.sample_app;

import android.os.Bundle;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;

import it.polimi.testing.lifecycle.ActivityRuleLifecycleTest;
import it.polimi.testing.lifecycle.DestroyCallback;
import it.polimi.testing.lifecycle.PauseCallback;
import it.polimi.testing.lifecycle.RecreateCallback;
import it.polimi.testing.lifecycle.RotationCallback;
import it.polimi.testing.lifecycle.StopCallback;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.StringStartsWith.startsWith;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class MainActivityEspressoTest extends ActivityRuleLifecycleTest<MainActivity>
{
    @Override
    protected ActivityTestRule<MainActivity> getActivityTestRule()
    {
        return new ActivityTestRule<>(MainActivity.class);
    }

    @Override
    public PauseCallback testPause()
    {
        return new PauseCallback()
        {
            @Override
            public void beforePause()
            {
                onView(withId(R.id.myButton))
                        .perform(click());

                onView(withId(R.id.myTextView))
                        .check(matches(allOf(isDisplayed(), withText(startsWith("My value")))));
            }

            @Override
            public void whilePaused()
            {
                // ...
            }

            @Override
            public void afterResume()
            {
                onView(withId(R.id.myTextView))
                        .check(matches(allOf(isDisplayed(), withText(startsWith("My value")))));

                onView(withId(R.id.myButton))
                        .perform(click());

                onView(withId(R.id.myTextView))
                        .check(matches(allOf(isDisplayed(), withText(startsWith("My value")))));
            }
        };
    }

    @Override
    public StopCallback testStop()
    {
        return new StopCallback()
        {
            @Override
            public void beforeStop()
            {
                onView(withId(R.id.myButton))
                        .perform(click());

                onView(withId(R.id.myTextView))
                        .check(matches(allOf(isDisplayed(), withText(startsWith("My value")))));
            }

            @Override
            public void whileStopped()
            {
                // ...
            }

            @Override
            public void afterRestart()
            {
                onView(withId(R.id.myTextView))
                        .check(matches(allOf(isDisplayed(), withText(startsWith("My value")))));

                onView(withId(R.id.myButton))
                        .perform(click());

                onView(withId(R.id.myTextView))
                        .check(matches(allOf(isDisplayed(), withText(startsWith("My value")))));
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
                onView(withId(R.id.myButton))
                        .perform(click());

                onView(withId(R.id.myTextView))
                        .check(matches(allOf(isDisplayed(), withText(startsWith("My value")))));
            }

            @Override
            public void afterDestroy()
            {
                // ?
            }
        };
    }

    @Override
    public RecreateCallback testRecreation()
    {
        return new RecreateCallback()
        {
            @Override
            public void beforeRecreation()
            {
                onView(withId(R.id.myButton))
                        .perform(click());

                onView(withId(R.id.myTextView))
                        .check(matches(allOf(isDisplayed(), withText(startsWith("My value")))));
            }

            @Override
            public void checkSavedInstance(Bundle savedInstanceState)
            {
                assertNotNull("Bundle null", savedInstanceState);
                assertTrue("Bundle does not save variable", savedInstanceState.containsKey(MainActivity.BUNDLE_VAR));
            }

            @Override
            public void afterRecreation()
            {
                onView(withId(R.id.myTextView))
                        .check(matches(allOf(isDisplayed(), withText(startsWith("My value")))));

                onView(withId(R.id.myButton))
                        .perform(click());

                onView(withId(R.id.myTextView))
                        .check(matches(allOf(isDisplayed(), withText(startsWith("My value")))));
            }
        };
    }

    @Override
    public RotationCallback testRotation()
    {
        return null;
    }
}
