package fr.intechinfo.smartjoystick.corelibrary;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Spraden on 24/11/2014.
 */
public class Helper {

    //TO REDO, not optimal
    public static Map JSONParser(String st, int mode, SJContext sjc) throws JSONException {
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
            if (mode == 1) {
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
                    sjc.gameList.add(sjc.CreateGame(sjc.gameInfo.get(game).get(1),sjc.gameInfo.get(game).get(0)));
                }
            }
            else {
                list.put(key,value.toString());
            }
        }
        return list;
    }
    public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
        Set<T> keys = new HashSet<T>();
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }
}
