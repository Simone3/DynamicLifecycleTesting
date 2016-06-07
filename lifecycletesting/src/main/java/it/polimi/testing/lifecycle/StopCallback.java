package it.polimi.testing.lifecycle;

public interface StopCallback
{
    void beforeStop();

    void whileStopped();

    void afterRestart();
}
