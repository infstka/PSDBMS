package com.psdbms.lr6_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<contact> contactList;
    EditText searchStr;
    ListView searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);
        loadList();

        searchList = findViewById(R.id.list);
        searchStr = findViewById(R.id.editsrch);
        contactList = new ArrayList<>();
        contactList = json.importFromJSONExternalStorage(this);

        searchStr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence CS, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence CS, int start, int count, int after) {
                if (CS.toString().equals(""))
                {
                    //обновление списка
                    initList();
                }
                else
                {
                    //выполнение поиска
                    searchItem(CS.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
            }
        });
    }

    //поиск контакта
    public void searchItem(String searchedtext)
    {
        List<contact> foundCntcts = new ArrayList<>();
        for(contact contact:contactList)
        {
            if(contact.Surname.contains(searchedtext))
            {
                foundCntcts.add(contact);
            }
            if(contact.Name.contains(searchedtext) && !foundCntcts.contains(contact))
            {
                foundCntcts.add(contact);
            }
        }
        ArrayAdapter<contact> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, foundCntcts);
        ListView listview = findViewById(R.id.list);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //загрузка, проверка, обновление
    public void initList()
    {
        List<contact> contacts = json.importFromJSONInternalStorage(this);
        if(contacts == null)
        {
            contacts = new ArrayList<>();
        }
        ArrayAdapter<contact> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contacts);
        ListView listview = findViewById(R.id.list);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged(); //обновление списка
    }

    //загрузка данных из json
    private void loadList()
    {
        contactList = json.importFromJSONInternalStorage(this);
        contactList = json.importFromJSONExternalStorage(this);
        if(contactList == null)
        {
            contactList = new ArrayList<>();
        }
        ArrayAdapter<contact> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactList);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
    }

    //кнопка "добавить контакт"
    public void newcontact (View view)
    {
        Intent intent = new Intent(this, second.class);
        startActivity(intent);
    }
}