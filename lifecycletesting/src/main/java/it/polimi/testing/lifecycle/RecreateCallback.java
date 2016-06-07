package it.polimi.testing.lifecycle;

import android.os.Bundle;

public interface RecreateCallback
{
    void beforeRecreation();

    void checkSavedInstance(Bundle savedInstanceState);

    void afterRecreation();
}
