package fr.intechinfo.smartjoystick.GUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;

import net.sourceforge.zbar.Symbol;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.json.JSONException;

import java.io.IOException;

import fr.intechinfo.smartjoystick.R;
import fr.intechinfo.smartjoystick.corelibrary.Helper;
import fr.intechinfo.smartjoystick.corelibrary.SJContext;
import fr.intechinfo.smartjoystick.localareanetwork.LAN;

/**
 * Created by StephaneTruong on 16/12/2014.
 */
@EActivity
public class CategoryListActivity extends Activity {

    @Extra
    SJContext sjc;

    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //set a new handler to handle received data
        LAN.getInstance().handler = myHandler;

        //for test only
        LAN.getInstance().address = "192.168.0.12";
        LAN.getInstance().port = 5000;

        //set the receiver
        try {
            LAN.getInstance().SetReceiver();
        } catch (IOException e) {
            e.printStackTrace();
        }

        gridView = (GridView) findViewById(R.id.gridview);

        //executed when a category is clicked
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageAdapter.Item item = (ImageAdapter.Item) gridView.getAdapter().getItem(position);
                sjc.currentCategory = item.name;
                GameListActivity_.intent(CategoryListActivity.this).sjc(sjc).start();
            }
        });

    }
    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            try {
                //parse data sent by the server
                Helper.JSONParser(msg.getData().getString("message"),1, sjc);
                updateUI();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ZBarScannerActivity.class);
        intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.QRCODE});
        startActivityForResult(intent, 1);

        //close the socket and all the input/output streams.
        try {
            LAN.getInstance().Close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateUI(){
        gridView.setAdapter(new ImageAdapter(CategoryListActivity.this, sjc));
    }
}
