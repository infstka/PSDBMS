package com.psmsdb.lr10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

    public class MainActivity extends AppCompatActivity {

        Spinner chooseCourse;
        ListView dataListView;
        DB db;
        Group grp;
        List<Group> grps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DB(this);

        chooseCourse = findViewById(R.id.chooseCourse);
        dataListView = findViewById(R.id.dataListView);
        grp = new Group();

        grps = db.getGroups();
        ArrayAdapter<Group> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, grps);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseCourse.setAdapter(adapter);

        AdapterView.OnItemSelectedListener isl = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                grp = grps.get(i);
                LoadData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        };
        chooseCourse.setOnItemSelectedListener(isl);
    }

    //загрузка данных
    public void LoadData()
    {
        try
        {
            List<Student> first = new ArrayList<>();
            List<Student> last = new ArrayList<>();

            first = db.getStudents();

            for (Student stdnt : first)
            {
                if (stdnt.IDGroup == grp.IDGroup)
                {
                    last.add(stdnt);
                }

                ArrayAdapter<Student> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, last);
                dataListView.setAdapter(adapter);
            }
        }
        catch (Exception exc)
        {
            Log.d("load: ", exc.getMessage());
        }
    }

    //добавить группу
    public void addGroup(View view)
    {
        Intent grpIntent = new Intent(this, AddGroup.class);
        startActivity(grpIntent);
    }

    //добавить студента
    public void addStudent(View view)
    {
        Intent stdntIntent = new Intent(this, AddStudent.class);
        startActivity(stdntIntent);
    }
}