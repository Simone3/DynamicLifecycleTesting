package it.polimi.testing.lifecycle;

import android.os.Bundle;

import org.junit.Test;

public abstract class LifecycleTest
{
    abstract void beforeLifecycleTest();

    abstract void afterLifecycleTest();


    abstract void callOnCreate(Bundle savedInstanceState);

    abstract boolean callOnRestart();

    abstract void callOnStart();

    abstract void callOnResume();

    abstract void callOnPause();

    abstract void callOnStop();

    abstract void callFinish();

    abstract void rotateDevice();

    abstract void recreateActivity();

    abstract Bundle getSavedInstanceState();






    public abstract PauseCallback testPause();

    public abstract StopCallback testStop();

    public abstract DestroyCallback testDestroy();

    // TODO not necessarily a recreation, add RotationCallback...
    public abstract RecreateCallback testRotation();

    public abstract RecreateCallback testRecreation();










    @Test
    public void lifecycleTestPauseResume()
    {
        beforeLifecycleTest();

        final PauseCallback callback = testPause();
        if(callback==null) return;

        callback.beforePause();

        callOnPause();

        callback.whilePaused();

        callOnResume();

        callback.afterResume();

        afterLifecycleTest();
    }

    @Test
    public void lifecycleTestStopStart()
    {
        beforeLifecycleTest();

        final StopCallback callback = testStop();
        if(callback==null) return;

        callback.beforeStop();

        callOnPause();
        callOnStop();

        callback.whileStopped();

        boolean alsoCallsOnStart = callOnRestart();
        if(!alsoCallsOnStart) callOnStart();
        callOnResume();

        callback.afterRestart();

        afterLifecycleTest();
    }

    @Test
    public void lifecycleTestNormalDestroy()
    {
        beforeLifecycleTest();

        final DestroyCallback callback = testDestroy();
        if(callback==null) return;

        callback.beforeDestroy();

        callFinish();

        callback.afterDestroy();

        afterLifecycleTest();
    }

    @Test
    public void lifecycleTestDestroyWithoutOnDestroy()
    {
        beforeLifecycleTest();

        final DestroyCallback callback = testDestroy();
        if(callback==null) return;

        callback.beforeDestroy();

        callOnPause();
        callOnStop();

        callback.afterDestroy();

        afterLifecycleTest();
    }

    @Test
    public void lifecycleTestRotation()
    {
        beforeLifecycleTest();

        final RecreateCallback callback = testRotation();
        if(callback==null) return;

        callback.beforeRecreation();

        callback.checkSavedInstance(getSavedInstanceState());

        rotateDevice();

        callback.afterRecreation();

        afterLifecycleTest();
    }

    @Test
    public void lifecycleTestRecreation()
    {
        beforeLifecycleTest();

        final RecreateCallback callback = testRecreation();
        if(callback==null) return;

        callback.beforeRecreation();

        callback.checkSavedInstance(getSavedInstanceState());

        recreateActivity();

        callback.afterRecreation();

        afterLifecycleTest();
    }
}
