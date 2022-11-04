package com.psmsdb.lr10;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddGroup extends AppCompatActivity {

    EditText enterGroupName, enterFaculty, enterCourse;
    Group grp;
    DB db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        db = new DB(this);

        enterGroupName = findViewById(R.id.enterGroupName);
        enterFaculty = findViewById(R.id.enterFaculty);
        enterCourse = findViewById(R.id.enterCourse);
        grp = new Group();
    }

    //добавить старосту
    public void addHead(View view)
    {
        try
        {
            if (enterGroupName.getText().toString().isEmpty())
            {
                Toast tst = Toast.makeText(this, "Enter group name", Toast.LENGTH_SHORT);
                tst.show();
                return;
            }

            Intent headIntent = new Intent(this, AddHead.class);
            headIntent.putExtra("HeadName", enterGroupName.getText().toString());
            headIntent.putExtra("IDGroup", String.valueOf(db.getIDGroup(enterGroupName.getText().toString())));
            startActivity(headIntent);
            this.finish();
        }
        catch (Exception exc)
        {
            Toast tst = Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT);
            tst.show();
        }
    }

    //добавление группы
    public void addGroup(View view)
    {
        try
        {
            grp.Name = enterGroupName.getText().toString();
            grp.Faculty = enterFaculty.getText().toString();
            grp.Course = Integer.parseInt(enterCourse.getText().toString());

            db.addGroup(grp);
            Toast.makeText(this, "Group added", Toast.LENGTH_SHORT).show();
            enterGroupName.setText("");
            enterFaculty.setText("");
            enterCourse.setText("");
        }

        catch (Exception exc)
        {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    //удаление
    public void deleteGroup(View view)
    {
        try
        {
            db.deleteGroup(enterGroupName.getText().toString());
            Toast.makeText(this, "Group deleted", Toast.LENGTH_SHORT).show();
            enterGroupName.setText("");
        }
        catch (Exception exc)
        {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}