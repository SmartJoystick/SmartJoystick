package fr.intechinfo.smartjoystick.GUI;

import android.app.Activity;

import fr.intechinfo.smartjoystick.corelibrary.SJContext;

/**
 * Created by StephaneTruong on 16/12/2014.
 */

public class JavaScriptHandler extends Activity {

    GameActivity parentActivity;
    SJContext sjc;

    public JavaScriptHandler(GameActivity activity, SJContext sjc) {
        parentActivity = activity;
        this.sjc = sjc;
    }

    @android.webkit.JavascriptInterface
    public void home(){

        //THERE ARE NOT ANY OTHER ACTIVITIES
    	/*
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        */

    }

}
