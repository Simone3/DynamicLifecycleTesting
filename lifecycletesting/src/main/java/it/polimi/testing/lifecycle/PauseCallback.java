package it.polimi.testing.lifecycle;

/**
 * Callback to test the component being paused (partially hidden to the user) and then resumed
 */
public interface PauseCallback
{
    /**
     * To perform actions and checks before the component is paused
     */
    void beforePause();

    /**
     * To perform checks during the pause
     */
    void whilePaused();

    /**
     * To perform actions and checks after the component is resumed
     */
    void afterResume();
}
