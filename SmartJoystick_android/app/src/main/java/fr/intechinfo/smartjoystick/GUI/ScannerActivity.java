package fr.intechinfo.smartjoystick.GUI;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;

import net.sourceforge.zbar.Symbol;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.json.JSONException;

import java.io.IOException;
import java.util.Map;

import fr.intechinfo.smartjoystick.corelibrary.Helper;
import fr.intechinfo.smartjoystick.corelibrary.SJContext;

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
            try {

                String st = "{\n" +
                        "  \"action\": {\n" +
                        "    \"game1\": {\n" +
                        "      \"title\": \"game1\",\n" +
                        "      \"description\": \"description game 1\"\n" +
                        "    },\n" +
                        "    \"game3\": {\n" +
                        "      \"title\": \"game3\",\n" +
                        "      \"description\": \"description game 3\"\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"cards\": {\n" +
                        "    \"game2\": {\n" +
                        "      \"title\": \"game2\",\n" +
                        "      \"description\": \"description game 2\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}";

                //Map<String, String> list = Helper.JSONParser(st, 1, sjc);
                Map<String, String> list = Helper.JSONParser(data.getStringExtra(ZBarConstants.SCAN_RESULT), 0, sjc);
                sjc.InitializeLAN(list.get("address"), Integer.parseInt(list.get("port")), handler);
                Helper.JSONParser(st,1,sjc);
                CategoryListActivity_.intent(this).sjc(sjc).start();

            } catch (JSONException e) {
                Toast.makeText(this, "Can't connect, please try again !", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_CANCELED && data != null) {
            String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
            if (!TextUtils.isEmpty(error)) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            /*
            try {
                Helper.JSONParser(msg.getData().getString("message"), 1, sjc);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            */
        }
    };
}
