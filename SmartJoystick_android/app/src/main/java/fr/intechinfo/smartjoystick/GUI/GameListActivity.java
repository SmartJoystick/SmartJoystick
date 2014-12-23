package fr.intechinfo.smartjoystick.GUI;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import java.io.IOException;

import fr.intechinfo.smartjoystick.R;
import fr.intechinfo.smartjoystick.corelibrary.SJContext;


/**
 * Created by Spraden on 05/11/2014.
 */
@EActivity
public class GameListActivity extends Activity {

    @Extra
    SJContext sjc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        final GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(this, sjc, sjc.currentCategory));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageAdapter.Item item = (ImageAdapter.Item) gridView.getAdapter().getItem(position);
                sjc.currentGame = sjc.getCurrentGame(item.name);
                GameActivity_.intent(GameListActivity.this).sjc(sjc).start();
            }
        });

    }
}
