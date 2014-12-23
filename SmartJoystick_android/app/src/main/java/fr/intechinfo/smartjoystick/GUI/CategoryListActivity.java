package fr.intechinfo.smartjoystick.GUI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;

import net.sourceforge.zbar.Symbol;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import java.io.IOException;

import fr.intechinfo.smartjoystick.R;
import fr.intechinfo.smartjoystick.corelibrary.SJContext;

/**
 * Created by StephaneTruong on 16/12/2014.
 */
@EActivity
public class CategoryListActivity extends Activity {

    @Extra
    SJContext sjc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        final GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(this, sjc, ""));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageAdapter.Item item = (ImageAdapter.Item) gridView.getAdapter().getItem(position);
                sjc.currentCategory = item.name;
                GameListActivity_.intent(CategoryListActivity.this).sjc(sjc).start();
            }
        });

    }
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, ZBarScannerActivity.class);
        intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.QRCODE});
        startActivityForResult(intent, 1);
    }


}
