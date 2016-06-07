package it.polimi.testing.lifecycle;

/**
 * Callback to test the component being killed
 */
public interface DestroyCallback
{
    /**
     * To perform actions and checks before the component is destroyed
     */
    void beforeDestroy();

    /**
     * To perform checks after the component is destroyed
     */
    void afterDestroy();
}
