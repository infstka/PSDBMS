package com.psmsdb.lr8;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditNote extends AppCompatActivity {

    private String Name;
    private String Category;
    private String Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        //получение значений из предыдущего активити
        Bundle bundle = getIntent().getExtras();
        Name = bundle.getString("Name");
        Category = bundle.getString("Category");
        Date = bundle.getString("Date");

        TextView currentDate = findViewById(R.id.currentDate);
        currentDate.setText(Date);

        EditText enterNewNote = findViewById(R.id.enterNewNote);
        enterNewNote.setText(Name);

        Spinner spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Note.Categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        int position = Note.Categories.indexOf(Category);
        spinner.setSelection(position, false);
    }

    public void edit(View view)
    {
        EditText enterNewNote = findViewById(R.id.enterNewNote);
        Spinner spinner = findViewById(R.id.spinner);
        String newName = String.valueOf(enterNewNote.getText());
        String newCategory = String.valueOf(spinner.getSelectedItem());

        //если поле имени категории пустое
        if(newName.length() <= 0)
        {
            Toast.makeText(this, "Enter new name", Toast.LENGTH_SHORT).show();
        }
        else
        {
            for(Note note : Note.Notes)
            {
                //добавление нового имени/категории если совпадают условия
                if(note.Name.equals(Name) && note.Category.equals(Category) && note.Date.equals(Date))
                {
                    note.Name = newName;
                    note.Category = newCategory;
                    break;
                }
            }

            Toast.makeText(this, "Note is updated", Toast.LENGTH_SHORT).show();
            Intent intnt = new Intent(this, Notes.class);
            intnt.putExtra("ChoosenDate", Date);
            startActivity(intnt);
        }
    }
}