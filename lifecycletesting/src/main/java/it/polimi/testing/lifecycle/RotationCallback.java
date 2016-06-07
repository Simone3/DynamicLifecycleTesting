package it.polimi.testing.lifecycle;

/**
 * Callback to test the device being rotated (landscape->portrait or vice-versa)
 */
public interface RotationCallback
{
    /**
     * To perform actions and checks before the device is rotated
     */
    void beforeRotation();

    /**
     * To perform actions and checks after the device is rotated
     */
    void afterRotation();
}
