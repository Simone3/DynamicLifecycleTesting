package it.polimi.testing.lifecycle;

import android.os.Bundle;

/**
 * Callback to test the component being destroyed and then rebuilt (e.g. configuration change)
 */
public interface RecreateCallback
{
    /**
     * To perform actions and checks before the component is destroyed
     */
    void beforeRecreation();

    /**
     * Allows to check the Bundle used to pass data from the old instance to the new one
     */
    void checkSavedInstance(Bundle savedInstanceState);

    /**
     * To perform actions and checks after the component is recreated
     */
    void afterRecreation();
}
