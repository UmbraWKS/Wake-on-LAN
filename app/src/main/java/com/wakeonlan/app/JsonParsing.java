package com.wakeonlan.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;

public class JsonParsing {
    public static JSONArray toJson(ArrayList<String> mac, ArrayList<String> name) throws JSONException {
        Iterator<String> macIterator = mac.iterator();
        Iterator<String> nameIterator = name.iterator();
        JSONArray jsonArray = new JSONArray();

        while (macIterator.hasNext() && nameIterator.hasNext()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mac", macIterator.next());
            jsonObject.put("name", nameIterator.next());
            jsonArray.put(jsonObject);
        }

        return jsonArray;
    }

    public static ArrayList<ArrayList<String>> fromJson(JSONArray jsonArray) throws JSONException {
        ArrayList<ArrayList<String>> data = new ArrayList<>(); // arraylist containing other arraylists (for potential data manipulation in the file and simplicity of handling)
        // content of the file
        ArrayList<String> mac = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();

        if (jsonArray == null)
            return null;

        // adding values
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            mac.add(jsonObject.getString("mac"));
            name.add(jsonObject.getString("name"));
        }

        // adding arraylists with data inside the container arraylist
        data.add(mac);
        data.add(name);

        return data;
    }
}
