package com.psmsdb.lr15_2;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.psmsdb.lr15_2.database.DB;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DB DB;
    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.task_1).setOnClickListener(this);
        findViewById(R.id.task_2).setOnClickListener(this);
        findViewById(R.id.task_3).setOnClickListener(this);
        findViewById(R.id.task_4).setOnClickListener(this);
        findViewById(R.id.task_5).setOnClickListener(this);

        DB = new DB(this);
        db = DB.getWritableDatabase();
        DB.createViews(db);
        DB.initDatabase(db);
        DB.createIndex(db);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.task_1:
                startActivity(new Intent(this, BestStudentsActivity.class));
                break;
            case R.id.task_2:
                startActivity(new Intent(this, BadStudentsActivity.class));
                break;
            case R.id.task_3:
                startActivity(new Intent(this, GroupComparisonActivity.class));
                break;
            case R.id.task_4:
                startActivity(new Intent(this, FacultyComparisonActivity.class));
                break;
            case R.id.task_5:
                startActivity(new Intent(this, GroupComparison2Activity.class));
                break;
        }
    }
}