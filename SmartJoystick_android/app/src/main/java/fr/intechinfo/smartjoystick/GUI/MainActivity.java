package fr.intechinfo.smartjoystick.GUI;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import fr.intechinfo.smartjoystick.R;
import fr.intechinfo.smartjoystick.corelibrary.LocalAreaNetwork;
import fr.intechinfo.smartjoystick.corelibrary.Repository;
import fr.intechinfo.smartjoystick.corelibrary.SJContext;
import fr.intechinfo.smartjoystick.corelibrary.SJContextServices;
import fr.intechinfo.smartjoystick.localareanetwork.LAN;
import fr.intechinfo.smartjoystick.localstorage.BinaryFileRepository;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    @ViewById(R.id.myInput)
    EditText myInput;

    private static Repository repo;
    private static LocalAreaNetwork lan;
    private SJContextServices sjcs;
    private SJContext sjc;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        repo = new BinaryFileRepository();
        lan = new LAN();
        sjcs = new SJContextServices(repo, lan);

        /*
        Intent intent = new Intent(this, activity.class);
        startActivity(intent);
        server_.intent(this).start();
        */

        File file = new File(this.getFilesDir().toString(), this.getString(R.string.filename));

        if(file.exists()) {
            try {
                sjc = SJContext.Load(sjcs, this);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            ScannerActivity_.intent(this).sjc(sjc).start();
        }
    }

    @Click
    void registerButton() {
        sjc = new SJContext();
        sjc.Initialize(sjcs);
        sjc.currentUser = sjc.CreateUser(myInput.getText().toString());
        try {
            sjc.Save(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ScannerActivity_.intent(this).sjc(sjc).start();
    }
}
