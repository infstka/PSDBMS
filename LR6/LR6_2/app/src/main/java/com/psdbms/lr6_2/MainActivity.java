package com.psdbms.lr6_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<contact> contactList;
    EditText searchStr;
    ListView searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchList = findViewById(R.id.list);
        searchStr = findViewById(R.id.editsrch);
        contactList = new ArrayList<>();
        contactList = json.importFromJSONExternalStorage(this);

        searchStr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence CS, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence CS, int start, int count, int after) {
                if (CS.toString().equals("")) {
                    //обновление списка
                    initList();
                } else {
                    //выполнение поиска
                    searchItem(CS.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    //поиск контакта
    public void searchItem(String searchedtext) {
        List<contact> foundCntcts = new ArrayList<>();
        for (contact contact : contactList) {
            if (contact.Birth.contains(searchedtext)) {
                foundCntcts.add(contact);
            }
        }
        ArrayAdapter<contact> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, foundCntcts);
        ListView listview = findViewById(R.id.list);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //загрузка, проверка, обновление
    public void initList() {
        List<contact> contacts = json.importFromJSONExternalStorage(this);
        if (contacts == null) {
            contacts = new ArrayList<>();
        }
        ArrayAdapter<contact> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contacts);
        ListView listview = findViewById(R.id.list);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged(); //обновление списка
    }
}