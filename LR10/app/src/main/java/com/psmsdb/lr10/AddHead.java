package com.psmsdb.lr10;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddHead extends AppCompatActivity {

    Spinner chooseStudent;
    DB db;
    String studentName;
    int IDStudent;
    List<Student> ls;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_head);

        db = new DB(this);
        ls = new ArrayList<>();
        chooseStudent = findViewById(R.id.chooseStudent);
        //принимает значения с другого активити
        Intent intent = getIntent();
        studentName = intent.getStringExtra("HeadName");
        IDStudent = Integer.parseInt(intent.getStringExtra("IDGroup"));
        ls = db.getStudents();

        List<Student> students = new ArrayList<>();
        for (Student student : ls)
        {
            if (student.IDGroup == IDStudent) //сравнивает айди групп двух таблиц
            {
                students.add(student);
            }
        }

        //передача объектов в спиннер
        ArrayAdapter<Student> adapter = new ArrayAdapter<Student>(this, android.R.layout.simple_list_item_1, students);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseStudent.setAdapter(adapter);
    }

    //выбор старосты
    public void chooseHead(View view)
    {
        try
        {
            String HeadName = String.valueOf(chooseStudent.getSelectedItem());
            db.selectGroupHead(IDStudent, HeadName);
            Toast tst = Toast.makeText(this, "Head added", Toast.LENGTH_SHORT);
            tst.show();
            this.finish();
        }
        catch (Exception exc)
        {
            Toast tst = Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT);
            tst.show();
        }
    }
}