package com.psdbms.lr6_1;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class json {
    private static final String FILE_NAME = "contacts.json";

    //из java в json (internal storage)
    static boolean exportToJSONInternalStorage(Context context, List<contact> dataList)
    {
        Gson gson = new Gson();
        DataItems dataItems = new DataItems();
        dataItems.setContacts(dataList);
        String string = gson.toJson(dataItems);

        FileOutputStream fos = null;

        try
        {
            fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(string.getBytes());
            return true;
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
        }
        return false;
    }

    //из json в java (internal storage)
    static List<contact> importFromJSONInternalStorage(Context context)
    {
        InputStreamReader isr = null;
        FileInputStream fis = null;

        try
        {
         fis = context.openFileInput(FILE_NAME);
         isr = new InputStreamReader(fis);
         Gson gson = new Gson();
         DataItems dataItems = gson.fromJson(isr, DataItems.class);
         return dataItems != null ? dataItems.getContacts() : new ArrayList<contact>();
        }
        catch(IOException ioexc)
        {
            ioexc.printStackTrace();
        }
        return null;
    }

    //из java в json (external storage)
    static boolean exportToJSONExternalStorage(Context context, List<contact> dataList)
    {
        Gson gson = new Gson();
        DataItems dataItems = new DataItems();
        dataItems.setContacts(dataList);
        String string = gson.toJson(dataItems);

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), FILE_NAME);

        try
        {
            if(!file.getParentFile().exists())
                file.getParentFile().mkdir();

            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file, false);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(string);
            osw.close();
            return true;
        }
        catch(IOException ioexc)
        {
            Toast.makeText(context, ioexc.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    //из json в java (external storage)
    static List<contact> importFromJSONExternalStorage(Context context)
    {
        InputStreamReader isr = null;
        FileOutputStream fos = null;
        FileReader fr = null;
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), FILE_NAME);
        StringBuilder sb = new StringBuilder();

        try
        {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String rline = br.readLine();
            sb.append(rline);

            Gson gson = new Gson();
            DataItems dataItems = gson.fromJson(sb.toString(), DataItems.class);
            return dataItems != null ? dataItems.getContacts() : new ArrayList<contact>();
        }
        catch(IOException ioexc)
        {
            ioexc.printStackTrace();
        }
        return null;
    }

    private static class DataItems
    {
        private List<contact> contacts;

        List<contact> getContacts()
        {
            return contacts;
        }
        void setContacts(List<contact> contacts)
        {
            this.contacts = contacts;
        }
    }
}
