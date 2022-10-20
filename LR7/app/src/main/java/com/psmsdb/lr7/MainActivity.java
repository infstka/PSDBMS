package com.psmsdb.lr7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String FILE_NAME = "LR7.json";
    List<Note> notesList;
    Note note;
    EditText enternote;
    Button add, change, delete;
    CalendarView calendar;
    public String selectedCD = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = findViewById(R.id.calendar);
        enternote = findViewById(R.id.enternote);
        add = findViewById(R.id.add);
        change = findViewById(R.id.change);
        delete = findViewById(R.id.delete);
        note = new Note();
        notesList = new ArrayList<>();

        //перезапись файла, если он существует
        File file = new File(super.getFilesDir(), FILE_NAME);
        if(!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch(IOException ioexc)
            {
                ioexc.printStackTrace();
            }
        }
        try
        {
            notesList = json.importFromJSON(this);
        }
        catch(Exception exc)
        {
            Toast.makeText(this, "В файле отсутствуют заметки", Toast.LENGTH_SHORT).show();
        }

        //получить дату
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int y, int m, int d) {
                try {
                    //к строковому значению
                    selectedCD = String.valueOf(d) + "." + String.valueOf(m) + "." + String.valueOf(y);
                    boolean ifUnvailable = checkForBinding(y, m, d);
                    for (Note note : notesList)
                    {
                        if (note.noteDate.equals(selectedCD))
                        {
                            enternote.setText(note.noteText);
                            break;
                        }
                        else
                            enternote.setText("");
                    }
                    changeVisibility(ifUnvailable);
                }
                catch (Exception exc)
                {
                    Toast.makeText(MainActivity.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //проверяет выбранную дату на наличие в файле
    public boolean checkForBinding(int y, int m, int d)
    {
        for (Note note : notesList)
        {
            if (note.noteDate.equals(selectedCD))
                return true;
        }
        return false;
    }

    //видимость кнопок
    public void changeVisibility(boolean ifUnvailable)
    {
        if(ifUnvailable)
        {
            add.setVisibility(View.INVISIBLE);
            change.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
        }
        else
        {
            add.setVisibility(View.VISIBLE);
            change.setVisibility(View.INVISIBLE);
            delete.setVisibility(View.INVISIBLE);
        }
    }

    //добавление заметки
    public void add(View view)
    {
        //если заметок больше 10 - выведет сообщение
        if(notesList.size() >= 10)
        {
            Toast.makeText(this, "Превышено количество записей", Toast.LENGTH_LONG).show();
            return;
        }
        //если в поле заметки пусто - вывод сообщения
        else if(enternote.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Заметка пустая", Toast.LENGTH_LONG).show();
            return;
        }

        try
        {
            notesList = json.importFromJSON(this);
        }
        catch (Exception exc)
        {
        }

        try
        {
            note.noteText = enternote.getText().toString();
            note.noteDate = selectedCD;
            notesList.add(note);
            json.exportToJSON(this, notesList);
            changeVisibility(true);
            Toast.makeText(this, "Добавлено", Toast.LENGTH_SHORT).show();
        }
        catch (Exception exc)
        {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //изменение заметки
    public void change(View view)
    {
        try
        {
            if(enternote.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Заметка пустая", Toast.LENGTH_LONG).show();
                return;
            }

            try
            {
                notesList = json.importFromJSON(this);
            }
            catch (Exception exc)
            {
            }

            //если выбранная дата совпадает с датой в файле и написан новый текст - заметка изменится
            for(Note note : notesList)
            {
                if(note.noteDate.equals(selectedCD))
                {
                    notesList.remove(note);
                    notesList.add(new Note(enternote.getText().toString(), selectedCD));
                    json.exportToJSON(this, notesList);
                    Toast.makeText(this, "Изменено", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
        catch(Exception exc)
        {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //удаление заметки
    public void delete(View view)
    {
        try
        {
            notesList = json.importFromJSON(this);
        }
        catch(Exception exc)
        {
        }

        //если выбранная дата совпадает с датой в файле - удаление заметки
        for(Note note : notesList)
        {
            if(note.noteDate.equals(selectedCD))
            {
                notesList.remove(note);
                json.exportToJSON(this, notesList);
                changeVisibility(false);
                Toast.makeText(this, "Удалено", Toast.LENGTH_SHORT).show();
                enternote.setText("");
                break;
            }
        }
    }
}