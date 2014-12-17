package fr.intechinfo.smartjoystick.corelibrary;

import android.content.Context;

import java.io.IOException;

/**
 * Created by Spraden on 17/11/2014.
 */
public interface Repository {
    void Save(Context c, SJContext sjc) throws IOException;
    SJContext LoadUnitializedContext(Context c) throws IOException, ClassNotFoundException;
}
