package fr.intechinfo.smartjoystick;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import fr.intechinfo.smartjoystick.GUI.ImageAdapter;

/**
 * Created by StephaneTruong on 15/12/2014.
 */
public class testClass extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try {
            testJsonParser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testJsonParser() throws Exception
    {
        String st = "{\"action\":{\"game1\":{\"title\":\"game1\",\"description\":\"description game 1\"},\"game3\":{\"title\":\"game3\",\"description\":\"description game 3\"}},\"cards\":{\"game2\":{\"title\":\"game2\",\"description\":\"description game 2\"}}}";
        HashMap<String, String> list =new HashMap<String, String>();
        JSONObject reader = new JSONObject(st);
        Iterator<String> iter = reader.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                Object value = reader.get(key);
                System.out.println("key = "+key+" value = "+value.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
