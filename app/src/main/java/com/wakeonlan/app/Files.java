package com.wakeonlan.app;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Files {
    private static final String SAVEDMACADDR = "savedMacs.json";

    public static void saveFile(JSONArray jsonArray, Context context) {
       checkFileExists(context);
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;

        try {
            // opening an output stream
            fileOutputStream = context.openFileOutput(SAVEDMACADDR, Context.MODE_PRIVATE);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream);

            // writing the json content
            outputStreamWriter.write(jsonArray.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // closing the output stream
            try {
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static JSONArray readFile(Context context){
        checkFileExists(context);

        // stringBuilder object to properly read the file
        StringBuilder jsonStringBuilder = new StringBuilder();
        try {
            FileInputStream fis = context.openFileInput(SAVEDMACADDR);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);

            // reading and appending
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonStringBuilder.append(line);
            }

            bufferedReader.close();
            isr.close();
            fis.close();

            // creation of new JsonObject
            return new JSONArray(jsonStringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null; // return null if there is an error
    }

    // removing an item from the .json file at a determined location
    public static void removeItemAtLocation(int location, Context context){
        JSONArray jsonArray = readFile(context);

        for(int i = 0; i < jsonArray.length(); i++){
            if(i == location) {
                jsonArray.remove(i);
                break; // exits the for loop when item removed
            }
        }
        saveFile(jsonArray, context);
    }

    // checks if file exists
    private static void checkFileExists(Context context) {
        File file = new File(context.getFilesDir(), SAVEDMACADDR);
        if(!file.exists())
            createNewFile(context); // if it doesn't exists it is created
    }

    private static void createNewFile(Context context){
        File file = new File(context.getFilesDir(), SAVEDMACADDR);
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
