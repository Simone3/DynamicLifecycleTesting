package it.polimi.testing.lifecycle;

public interface PauseCallback
{
    void beforePause();

    void whilePaused();

    void afterResume();
}
