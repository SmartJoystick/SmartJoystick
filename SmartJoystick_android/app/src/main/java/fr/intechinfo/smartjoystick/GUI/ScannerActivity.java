package fr.intechinfo.smartjoystick.GUI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;

import net.sourceforge.zbar.Symbol;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fr.intechinfo.smartjoystick.corelibrary.Helper;
import fr.intechinfo.smartjoystick.corelibrary.SJContext;
import fr.intechinfo.smartjoystick.corelibrary.User;

/**
 * Created by Spraden on 24/11/2014.
 */
@EActivity
public class ScannerActivity extends Activity {

    @Extra
    SJContext sjc;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(this, "Welcome "+sjc.currentUser.username, Toast.LENGTH_SHORT).show();
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

                Map<String, String> list = JSONParser(st, 1);
                //Map<String, String> list = JSONParser(data.getStringExtra(ZBarConstants.SCAN_RESULT), 0);
                //Toast.makeText(this, "address : "+list.get("address")+" port : "+list.get("port"), Toast.LENGTH_SHORT).show();


                /*
                Toast.makeText(this, list.get(0)+list.get(1), Toast.LENGTH_SHORT).show();
                list.add(user.username);
                server_.intent(this).data(list).start();*/

                Handler myHandler = new Handler() {
                    @Override
                    public void handleMessage (Message msg) {
                        try {
                            JSONParser((String) msg.obj, 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
               // sjc.InitializeLAN(list.get("address"),Integer.parseInt(list.get("port")), myHandler);
                CategoryListActivity_.intent(this).sjc(sjc).start();

            } catch (JSONException e) {
                Toast.makeText(this, "Can't connect, please try again !", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_CANCELED && data != null) {
            String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
            if (!TextUtils.isEmpty(error)) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        }
    }
    public Map JSONParser(String st, int mode) throws JSONException {
        Map<String,String> list = new HashMap<String, String>();

        JSONObject reader = null;
        try {
            reader = new JSONObject(st);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Iterator<String> iter = reader.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            Object value = reader.get(key);
            if ( mode == 1) {
                JSONObject obj = (JSONObject) value;
                Iterator<String> iter2 = obj.keys();

                List<String> list2 = new ArrayList<String>();
                sjc.categories.add(key);

                while (iter2.hasNext()) {
                    String game = iter2.next();
                    JSONObject value2 = (JSONObject) obj.get(game);
                    Iterator<String> iter3 = value2.keys();

                    list2.add(game);
                    sjc.categoryList.put(key,list2);
                    List<String> list3 = new ArrayList<String>();

                    while (iter3.hasNext()) {
                        String content = iter3.next();
                        Object value3 = value2.get(content);

                        list3.add(value3.toString());
                        sjc.gameInfo.put(game,list3);
                    }
                    sjc.gameList.add(sjc.CreateGame(sjc.gameInfo.get(game).get(0),sjc.gameInfo.get(game).get(1)));
                }
            }
            else {
                list.put(key,value.toString());
            }
        }
        return list;
    }
}
