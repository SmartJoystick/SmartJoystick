package fr.intechinfo.smartjoystick;

import android.test.InstrumentationTestCase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by StephaneTruong on 15/12/2014.
 */
public class ExampleTest extends InstrumentationTestCase {

    public void testJsonParser() throws Exception {
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
        HashMap<String, String> categoriesAndGame = new HashMap<String, String>();
        List<String> categoriesList = new ArrayList<String>();
        JSONObject reader = null;
        try {
            reader = new JSONObject(st);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Iterator<String> iter = reader.keys();
        while (iter.hasNext()) {
            String categories = iter.next();
            categoriesList.add(categories);
            JSONObject value = (JSONObject) reader.get(categories);
            System.out.println("key = " + categories + " value = " + value.toString());
            Iterator<String> iter2 = value.keys();

            while (iter2.hasNext()) {
                String game = iter2.next();
                categoriesAndGame.put(categories,game);
                JSONObject value2 = (JSONObject) value.get(game);
                System.out.println("key = " + game + " value = " + value2.toString());

                Iterator<String> iter3 = value2.keys();
                while (iter3.hasNext()) {
                    String content = iter3.next();
                    Object value3 = value2.get(content);
                    System.out.println("key = " + content + " value = " + value3.toString());
                }
            }
        }
    }

    public void JsonParser(Iterator<String> iter) {
        while (iter.hasNext()) {

        }
    }
}
