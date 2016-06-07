package it.polimi.testing.lifecycle;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static junit.framework.Assert.assertEquals;

/**
 * Test activity used to see if the lifecycle tests work. It simply adds a callback value in the
 * "traversedCallbacks" list and offers some utility methods to assert on it.
 */
public class ControlActivity extends Activity
{
    public final static String ON_CREATE = "ON_CREATE";
    public final static String ON_RESTART = "ON_RESTART";
    public final static String ON_START = "ON_START";
    public final static String ON_RESUME = "ON_RESUME";
    public final static String ON_PAUSE = "ON_PAUSE";
    public final static String ON_STOP = "ON_STOP";
    public final static String ON_DESTROY = "ON_DESTROY";
    public final static String ON_SAVE_INSTANCE_STATE = "ON_SAVE_INSTANCE_STATE";

    private final List<String> traversedCallbacks = new ArrayList<>();

    public final static String BUNDLE_KEY = "BUNDLE_KEY";
    public int bundleValue;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        traversedCallbacks.add(ON_CREATE);
        if(savedInstanceState==null)
        {
            bundleValue = (new Random()).nextInt(10000);
        }
        else
        {
            bundleValue = savedInstanceState.getInt(BUNDLE_KEY);
        }
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        traversedCallbacks.add(ON_RESTART);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        traversedCallbacks.add(ON_START);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        traversedCallbacks.add(ON_RESUME);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        traversedCallbacks.add(ON_PAUSE);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        traversedCallbacks.add(ON_STOP);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        traversedCallbacks.add(ON_DESTROY);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putInt(BUNDLE_KEY, bundleValue);
        super.onSaveInstanceState(outState);
        traversedCallbacks.add(ON_SAVE_INSTANCE_STATE);
    }


    /**
     * Checks if the current callbacks are equal to the given ones
     * @param addCommonCallbacks if true the current list is common (create, start, resume) + expectedCallbacks, if false only expectedCallbacks
     * @param expectedCallbacks the expected callbacks
     */
    public void assertSingleTraversed(boolean addCommonCallbacks, String... expectedCallbacks)
    {
        List<String> allCallbacks = addCommonCallbacks ? addCommonCallbacks(Arrays.asList(expectedCallbacks)) : Arrays.asList(expectedCallbacks);
        assertEquals("Wrong traversed callbacks!", allCallbacks, traversedCallbacks);
    }

    /**
     * Checks if the current callbacks are equal to the given ones. It adds create-start-resume before the given callbacks.
     * @param expectedCallbacks the expected callbacks
     */
    public void assertSingleTraversed(String... expectedCallbacks)
    {
        assertSingleTraversed(true, expectedCallbacks);
    }

    /**
     * Checks if the current callbacks are one of the provided lists
     * @param possibleListsOfExpectedCallbacks possible lists of callbacks
     */
    @SafeVarargs
    public final void assertMultipleTraversed(List<String>... possibleListsOfExpectedCallbacks)
    {
        boolean foundOne = false;
        for(List<String> possible: possibleListsOfExpectedCallbacks)
        {
            try
            {
                assertSingleTraversed(possible.toArray(new String[possible.size()]));
            }
            catch(AssertionError e)
            {
                continue;
            }
            foundOne = true;
            break;
        }
        if(!foundOne)
        {
            String possibilities = "";
            for(List<String> possible: possibleListsOfExpectedCallbacks)
            {
                possibilities += ("".equals(possibilities) ? "" : " | ")+addCommonCallbacks(possible);
            }
            throw new AssertionError("Wrong traversed callbacks! Expected one among "+possibilities+" but was "+traversedCallbacks);
        }
    }

    /**
     * Add common callbacks to the list
     * @param specificCallbacks the callbacks
     * @return common callbacks + specificCallbacks
     */
    private List<String> addCommonCallbacks(List<String> specificCallbacks)
    {
        List<String> allCallbacks = new ArrayList<>();
        List<String> commonCallbacks = Arrays.asList(
                ControlActivity.ON_CREATE,
                ControlActivity.ON_START,
                ControlActivity.ON_RESUME);
        allCallbacks.addAll(commonCallbacks);
        allCallbacks.addAll(specificCallbacks);
        return allCallbacks;
    }
}
