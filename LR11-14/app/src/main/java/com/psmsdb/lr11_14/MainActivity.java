package com.psmsdb.lr11_14;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DB db;
    ListView ResultLV;
    Spinner spinner;
    EditText dateStart, dateEnd;
    String start, end, spinString;
    Button addStudent, removeStudent;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DB(this);

        //если значений нет - заполняем таблицу
        if(db.getFaculty().size() == 0 && db.getGroup().size() == 0 && db.getStudent().size() == 0 && db.getSubject().size() == 0 && db.getProgress().size() == 0)
        {
            db.FillTables();
        }

        ResultLV = findViewById(R.id.ResultLV);
        spinner = findViewById(R.id.spinner);
        dateStart = findViewById(R.id.dateStart);
        dateEnd = findViewById(R.id.dateEnd);
        addStudent = findViewById(R.id.addStudent);
        removeStudent = findViewById(R.id.removeStudent);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinString = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner.setSelection(0);

        addStudent.setOnClickListener(view ->
        {
            try
            {
                db.triggerMoreThanThree();
            }

            catch(Exception exc)
            {
                Toast.makeText(this, "Can't add more than 3 students in 1 group!", Toast.LENGTH_SHORT).show();
            }
        });

        removeStudent.setOnClickListener(view ->
        {
            try
            {
                db.triggerLessThanTwo();
            }

            catch(Exception exc)
            {
                Toast.makeText(this, "Can't be less than 2 students in 1 group!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //проверка введенной даты
    public boolean DateCheck()
    {
        start = dateStart.getText().toString();
        end = dateEnd.getText().toString();

        if(start.isEmpty() || end.isEmpty() || start.length() != 10 || end.length() != 10)
        {
            Toast.makeText(this, "Incorrect date", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void apply(View view)
    {
        ArrayAdapter<String> arrayAdapter;
        switch(spinString)
        {
            case ("Средняя оценка (для каждого студента по предмету)"):
            arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, db.avgMarkStudentSubject());
            ResultLV.setAdapter(arrayAdapter);
            break;

            case ("Средняя оценка (для каждого студента)"):
            arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, db.avgMarkStudent());
            ResultLV.setAdapter(arrayAdapter);
            break;

            case ("Средняя оценка (для группы)"):
            if (!DateCheck())
            {
                break;
            }
            arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, db.avgGroup(start, end));
            ResultLV.setAdapter(arrayAdapter);
            break;

            case ("Наилучшие студенты"):
                if (!DateCheck())
                {
                    break;
                }
                arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, db.bestStudents(start, end));
                ResultLV.setAdapter(arrayAdapter);
                break;

            case ("Оценки ниже 4"):
                if (!DateCheck())
                {
                    break;
                }
                arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, db.negativeMark(start, end));
                ResultLV.setAdapter(arrayAdapter);
                break;

            case ("Сравнение групп по предметам"):
                if (!DateCheck())
                {
                    break;
                }
                arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, db.groupComp(start, end));
                ResultLV.setAdapter(arrayAdapter);
                break;

            case ("Сравнение по факультетам"):
                if (!DateCheck())
                {
                    break;
                }
                arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, db.facComp(start, end));
                ResultLV.setAdapter(arrayAdapter);
                break;
        }
    }

}