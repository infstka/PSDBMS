package com.psmsdb.lr8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Categories extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        ListView clv = findViewById(R.id.noteList);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Note.Categories);
        clv.setAdapter(adapter);
        registerForContextMenu(clv);

        //для листвью
        clv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NoteCategory(i);
            }
        });
    }

    //меню
    @Override
    public void onCreateContextMenu(ContextMenu cm, View view, ContextMenu.ContextMenuInfo cmi)
    {
        super.onCreateContextMenu(cm, view, cmi);
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_category, cm);
    }

    //действия
    @Override
    public boolean onContextItemSelected(MenuItem menuItem)
    {
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();

        switch (menuItem.getItemId())
        {
            case R.id.del:
                delete(acmi.position);
                FragmentManager fm = getSupportFragmentManager();
                return true;
            default:
                return super.onContextItemSelected(menuItem);
        }
    }

    //удаление
    public void delete(int position)
    {
        Note.Categories.remove(position);
        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
        ListView lv = findViewById(R.id.noteList);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Note.Categories);
        lv.setAdapter(adapter);
        Note.CategoriesSerialization(getApplicationContext());
    }

    //добавление категории
    public void addCat(View view)
    {
        EditText enterCategory = findViewById(R.id.enterCategory);
        String newCategory = String.valueOf(enterCategory.getText());
        int temp = 0;

        //не больше пяти категорий
        if(Note.Categories.size() >= 5)
        {
            Toast.makeText(this, "Can't add more than 5 categories", Toast.LENGTH_SHORT).show();
        }

        else
        {
            for (String cat : Note.Categories)
            {
                if(cat.equals(newCategory))
                {
                    temp++;
                }
            }

            //при попытке добавить одинаковые категории
            if(temp > 0)
            {
                Toast.makeText(this, "This category is already exists", Toast.LENGTH_SHORT).show();
            }

            else
            {
                //при пустом поле имени категории
                if(newCategory.length() <= 0)
                {
                    Toast.makeText(this, "Enter category", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    //добавление, передача в список
                    Note.Categories.add(newCategory);
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                    ListView lv = findViewById(R.id.noteList);
                    ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Note.Categories);
                    lv.setAdapter(adapter);
                    Note.CategoriesSerialization(getApplicationContext());
                }
            }
        }
    }

    //передача значения в другое активити
    public void NoteCategory(int position)
    {
        Intent intnt = new Intent(this, NoteCategory.class);
        intnt.putExtra("extra", position);
        startActivity(intnt);
    }
}