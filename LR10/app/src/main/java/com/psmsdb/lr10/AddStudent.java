package com.psmsdb.lr10;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class AddStudent extends AppCompatActivity {

    EditText enterStudentName;
    Spinner chooseGroup;
    DB db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        db = new DB(this);
        enterStudentName = findViewById(R.id.enterStudentName);
        chooseGroup = findViewById(R.id.chooseGroup);

        List<Group> grps = db.getGroups();
        ArrayAdapter<Group> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, grps);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseGroup.setAdapter(adapter);
    }

    public void addNewStudent(View view)
    {
        try
        {
            if(enterStudentName.getText().toString().isEmpty())
            {
                Toast tst = Toast.makeText(this, "Enter student name", Toast.LENGTH_SHORT);
                tst.show();
                return;
            }

            Student stdnt = new Student();
            stdnt.Name = enterStudentName.getText().toString();
            String grpName = chooseGroup.getSelectedItem().toString();
            stdnt.IDGroup = db.getIDGroup(grpName);
            db.addStudent(stdnt);
            Toast tst = Toast.makeText(this, "Student added", Toast.LENGTH_SHORT);
            tst.show();
            enterStudentName.setText("");
        }
        catch (Exception exc)
        {
            Toast tst = Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT);
            tst.show();
        }
    }
}