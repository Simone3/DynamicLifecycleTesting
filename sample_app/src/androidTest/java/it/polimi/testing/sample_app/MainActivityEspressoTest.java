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
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Example of lifecycle tests for the Espresso framework
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class MainActivityEspressoTest extends ActivityRuleLifecycleTest<MainActivity>
{
    @Override
    protected ActivityTestRule<MainActivity> initializeActivityTestRule()
    {
        return new ActivityTestRule<>(MainActivity.class);
    }

    @Override
    public PauseCallback testPause()
    {
        // Return null if you are not interested in this lifecycle test
        return null;
    }

    @Override
    public StopCallback testStop()
    {
        // Return null if you are not interested in this lifecycle test
        return null;
    }

    @Override
    public DestroyCallback testDestroy()
    {
        // Return null if you are not interested in this lifecycle test
        return null;
    }

    @Override
    public RecreateCallback testRecreation()
    {
        return new RecreateCallback()
        {
            @Override
            public void beforeRecreation()
            {
                // Perform some actions
                onView(withId(R.id.name))
                        .perform(typeText("MyName"));
                for(int i=0; i<3; i++)
                {
                    onView(withId(R.id.button))
                            .perform(click());
                }

                // Check counter
                onView(withId(R.id.counter))
                        .check(matches(allOf(isDisplayed(), withText(equalTo("3 clicks")))));
            }

            @Override
            public void checkSavedInstance(Bundle savedInstanceState)
            {
                // Do nothing here
            }

            @Override
            public void afterRecreation()
            {
                // Check name input is still there
                onView(withId(R.id.name))
                        .check(matches(allOf(isDisplayed(), withText(equalTo("MyName")))));

                // Check that the counter is correctly restored
                onView(withId(R.id.counter))
                        .check(matches(allOf(isDisplayed(), withText(equalTo("3 clicks")))));
            }
        };
    }

    @Override
    public RotationCallback testRotation()
    {
        return new RotationCallback()
        {
            @Override
            public void beforeRotation()
            {
                // Check that all UI elements are visible in the vertical layout
                onView(withId(R.id.button))
                    .check(matches(isDisplayed()));
                onView(withId(R.id.counter))
                        .check(matches(isDisplayed()));
                onView(withId(R.id.name))
                        .check(matches(isDisplayed()));
            }

            @Override
            public void afterRotation()
            {
                // Check that all UI elements are visible in the horizontal layout
                onView(withId(R.id.button))
                        .check(matches(isDisplayed()));
                onView(withId(R.id.counter))
                        .check(matches(isDisplayed()));
                onView(withId(R.id.name))
                        .check(matches(isDisplayed()));
            }
        };
    }
}
