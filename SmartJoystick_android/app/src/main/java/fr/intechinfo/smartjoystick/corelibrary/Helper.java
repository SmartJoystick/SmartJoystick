package fr.intechinfo.smartjoystick.corelibrary;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Spraden on 24/11/2014.
 */
public abstract class Helper {

    public static HashMap JSONParser(String st) throws JSONException {

        /*
        for int keys
        SparseArray<String> list = new SparseArray<String>();
        */

        HashMap<String, String> list = new HashMap<String, String>();
        List<String> categoryList = new ArrayList<String>();
        HashMap<String, String> gameList = new HashMap<String, String>();
        HashMap<String, HashMap<String, String>> gameInfo = new HashMap<String, HashMap<String, String>>();
        HashMap<String, String> gameDetails = new HashMap<String, String>();

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
            System.out.println("key = " + key + " value = " + value.toString());

            if (value instanceof JSONObject) {
                JSONObject obj = (JSONObject) value;
                Iterator<String> iter2 = obj.keys();

                //add category
                categoryList.add(key);

                while (iter2.hasNext()) {
                    String game = iter2.next();
                    JSONObject value2 = (JSONObject) obj.get(game);
                    System.out.println("key = " + game + " value = " + value2.toString());
                    gameList.put(key,game);

                    Iterator<String> iter3 = value2.keys();
                    while (iter3.hasNext()) {
                        String content = iter3.next();
                        Object value3 = value2.get(content);
                        System.out.println("key = " + content + " value = " + value3.toString());
                        gameDetails.put(content,value3.toString());
                        gameInfo.put(game,gameDetails);
                    }
                }
            }
            else {
                list.put(key,value.toString());
            }
        }

        /*
        List<String> list = new ArrayList<String>();
        JSONObject reader = new JSONObject(st);
        JSONObject data  = reader.getJSONObject("data");

        list.add(data.getString("address"));
        list.add(data.getString("port"));

        Iterator it=list.iterator();

        while(it.hasNext())
        {
            String value=(String)it.next();

            System.out.println("Value :"+value);
        }
        */
        return list;
    }
}
