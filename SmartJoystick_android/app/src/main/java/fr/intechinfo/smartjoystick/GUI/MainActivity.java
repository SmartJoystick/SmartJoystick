package fr.intechinfo.smartjoystick.GUI;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;

import fr.intechinfo.smartjoystick.R;
import fr.intechinfo.smartjoystick.corelibrary.Repository;
import fr.intechinfo.smartjoystick.corelibrary.SJContext;
import fr.intechinfo.smartjoystick.corelibrary.SJContextServices;
import fr.intechinfo.smartjoystick.localstorage.BinaryFileRepository;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    @ViewById(R.id.myInput)
    EditText myInput;

    private static Repository repo;
    private SJContextServices sjcs;
    private SJContext sjc;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //create the services
        repo = new BinaryFileRepository();
        sjcs = new SJContextServices(repo);

        //check if data.sj exists
        File file = new File(this.getFilesDir().toString(), this.getString(R.string.filename));

        //if it exists, the context is loaded
        if(file.exists()) {
            try {
                sjc = SJContext.Load(sjcs, this);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            CategoryListActivity_.intent(this).sjc(sjc).start();
            //ScannerActivity_.intent(this).sjc(sjc).start();
        }
    }

    @Click
    void registerButton() {

        //create the new context, if not created, which is inherited from all the app objects
        sjc = new SJContext();

        //initialize the services (repository etc.)
        sjc.Initialize(sjcs);

        //create a new user and save in the data.js file
        sjc.currentUser = sjc.CreateUser(myInput.getText().toString());
        try {
            sjc.Save(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        CategoryListActivity_.intent(this).sjc(sjc).start();
        //ScannerActivity_.intent(this).sjc(sjc).start();
    }
}
