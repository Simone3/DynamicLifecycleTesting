package it.polimi.testing.lifecycle;

/**
 * Callback to test the component being paused (completely hidden to the user) and then restarted
 */
public interface StopCallback
{
    /**
     * To perform actions and checks before the component is stopped
     */
    void beforeStop();

    /**
     * To perform checks during the stop
     */
    void whileStopped();

    /**
     * To perform actions and checks after the component is restarted
     */
    void afterRestart();
}
