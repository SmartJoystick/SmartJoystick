package fr.intechinfo.smartjoystick.localstorage;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import fr.intechinfo.smartjoystick.R;
import fr.intechinfo.smartjoystick.corelibrary.Repository;
import fr.intechinfo.smartjoystick.corelibrary.SJContext;

/**
 * Created by Spraden on 17/11/2014.
 */
public class BinaryFileRepository implements Repository {

    @Override
    public void Save(Context c, SJContext sjc) throws IOException {
        FileOutputStream fos = c.openFileOutput(c.getString(R.string.filename), Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(sjc);
        os.close();
    }

    @Override
    public SJContext LoadUnitializedContext(Context c) throws IOException,
            ClassNotFoundException {
        FileInputStream fis = c.openFileInput(c.getString(R.string.filename));
        ObjectInputStream is = new ObjectInputStream(fis);
        SJContext sjc = (SJContext) is.readObject();
        is.close();
        return sjc;
    }
}
