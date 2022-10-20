package com.psmsdb.lr7;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class json
{
    public static String FILE_NAME = "LR7.json";

    //из java в json
    static boolean exportToJSON(Context context, List<Note> dataList)
    {
        Gson gson = new Gson();
        DataItems dataItems = new DataItems();
        dataItems.setNotes(dataList);
        String jsonString = gson.toJson(dataItems);

        FileOutputStream fos = null;
        try
        {
            fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(jsonString.getBytes());
            return true;
        }
        catch(Exception exc)
        {
            Toast.makeText(context, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

//из json в java
    static List<Note> importFromJSON(Context context)
    {
        InputStreamReader isr = null;
        FileInputStream fis = null;
        try
        {
            fis = context.openFileInput(FILE_NAME);
            isr = new InputStreamReader(fis);
            Gson gson = new Gson();
            DataItems dataItems = gson.fromJson(isr, DataItems.class);
            return dataItems.getNotes();
        }
        catch(IOException ioexc)
        {
            Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private static class DataItems
    {
        private List<Note> notes;
        List<Note> getNotes()
        {
            return notes;
        }
        void setNotes(List<Note> notes)
        {
            this.notes = notes;
        }
    }
}
