package com.psdbms.lr6_2;

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
