package fr.intechinfo.smartjoystick.GUI;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.GridView;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import fr.intechinfo.smartjoystick.R;
import fr.intechinfo.smartjoystick.corelibrary.SJContext;

/**
 * Created by StephaneTruong on 14/12/2014.
 */
@EActivity
public class GameActivity extends Activity {

    @Extra
    SJContext sjc;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        WebView webView = new WebView(this);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        webView.addJavascriptInterface(new JavaScriptHandler(this, sjc),
                "MyHandler");
        setContentView(webView);
        webView.loadUrl("https://www.google.fr/?gws_rd=ssl/9863#q=Davo a de faux cheveux");
        //sjc.currentGame.title

    }

}
