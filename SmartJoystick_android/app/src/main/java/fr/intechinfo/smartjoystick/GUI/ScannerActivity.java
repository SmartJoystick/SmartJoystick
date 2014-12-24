package fr.intechinfo.smartjoystick.GUI;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;

import net.sourceforge.zbar.Symbol;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.json.JSONException;

import java.util.Map;

import fr.intechinfo.smartjoystick.corelibrary.Helper;
import fr.intechinfo.smartjoystick.corelibrary.SJContext;
import fr.intechinfo.smartjoystick.localareanetwork.LAN;

/**
 * Created by Spraden on 24/11/2014.
 */
@EActivity
public class ScannerActivity extends Activity {

    @Extra
    SJContext sjc;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "Welcome " + sjc.currentUser.username, Toast.LENGTH_SHORT).show();
        launchQRScanner();
    }

    public void launchQRScanner() {
        if (isCameraAvailable()) {
            Intent intent = new Intent(this, ZBarScannerActivity.class);
            intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.QRCODE});
            startActivityForResult(intent, 1);
        } else {
            Toast.makeText(this, "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isCameraAvailable() {
        PackageManager pm = getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            //get data from the qr code which was scanned
            Map<String, String> list = null;
            try {
                list = Helper.JSONParser(data.getStringExtra(ZBarConstants.SCAN_RESULT), 0, sjc);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //set the address and the port for the socket connection
            LAN.getInstance().address = list.get("address");
            LAN.getInstance().port = Integer.parseInt(list.get("port"));

            CategoryListActivity_.intent(this).sjc(sjc).start();

        } else if (resultCode == RESULT_CANCELED && data != null) {
            String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
            if (!TextUtils.isEmpty(error)) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
