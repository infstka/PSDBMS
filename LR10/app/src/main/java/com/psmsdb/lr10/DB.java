package com.psmsdb.lr10;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DB extends SQLiteOpenHelper
{
    private static final String DB_NAME = "University";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME_STUDENTS = "Students";
    private static final String TABLE_NAME_GROUPS = "Groups";

    /**Groups table**/
    private static final String IDGROUP_COL = "IDGroup";
    private static final String FACULTY_COL = "Faculty";
    private static final String COURSE_COL = "Course";
    private static final String NAME_COL = "Name";
    private static final String HEAD_COL = "Head";

    /**Stuednts table**/
    private static final String IDSTUDENT_COL = "IDstudent";

    public DB(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //создание таблиц Group и Students
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query_Groups = "CREATE TABLE " + TABLE_NAME_GROUPS + " (" +
                IDGROUP_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FACULTY_COL + " TEXT," +
                COURSE_COL + " INTEGER," +
                NAME_COL + " TEXT UNIQUE," +
                HEAD_COL + " TEXT)";

        String query_Students = "CREATE TABLE " + TABLE_NAME_STUDENTS + " (" +
                IDGROUP_COL + " INTEGER, " +
                IDSTUDENT_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME_COL + " TEXT," +
                " CONSTRAINT studCnstrnt FOREIGN KEY (IDGroup) REFERENCES Groups(IDGroup) ON DELETE CASCADE)";
    //выше каскадное удаление
        db.execSQL(query_Groups);
        db.execSQL(query_Students);
    }

    //обновление таблиц
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldTable, int newTable)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_GROUPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STUDENTS);
        onCreate(db);
    }

    //связь
    @Override
    public void onConfigure(SQLiteDatabase db)
    {
        super.onConfigure(db);
        db.execSQL("PRAGMA foreign_keys = ON");
    }

    //добавление группы
    public void addGroup(Group grp)
    {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put(FACULTY_COL, grp.Faculty);
            cv.put(COURSE_COL, grp.Course);
            cv.put(NAME_COL, grp.Name);

            db.insert(TABLE_NAME_GROUPS, null, cv);
            db.close();
        }

        catch (Exception exc)
        {
            Log.d("addGroup: ", exc.getMessage());
        }
    }

    //добавление студента
    public void addStudent(Student stdnt)
    {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put(IDGROUP_COL, stdnt.IDGroup);
            cv.put(NAME_COL, stdnt.Name);

            db.insert(TABLE_NAME_STUDENTS, null, cv);
            db.close();
        }

        catch (Exception exc)
        {
            Log.d("addStudent: ", exc.getMessage());
        }
    }

    //получение группы
    public List<Group> getGroups()
    {
        ArrayList<Group> grpList = new ArrayList<>();
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursorGrp = db.rawQuery("SELECT * FROM " + TABLE_NAME_GROUPS, null);

            if (cursorGrp.moveToFirst())
            {
                do
                {
                    grpList.add(new Group(
                                    cursorGrp.getInt(0),
                                    cursorGrp.getString(1),
                                    cursorGrp.getInt(2),
                                    cursorGrp.getString(3),
                                    cursorGrp.getString(4)
                    ));
                }

                while (cursorGrp.moveToNext());
            }

            cursorGrp.close();
            return grpList;
        }
        catch (Exception exc)
        {
            Log.d("getGroup: ", exc.getMessage());
        }
        return grpList;
    }

    //получение студентов
    public List<Student> getStudents()
    {
        ArrayList<Student> stdntsList = new ArrayList<>();
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursorStdnts = db.rawQuery("SELECT * FROM " + TABLE_NAME_STUDENTS,null);

            if(cursorStdnts.moveToFirst())
            {
                do
                {
                    stdntsList.add(new Student(
                            cursorStdnts.getInt(0),
                            cursorStdnts.getInt(1),
                            cursorStdnts.getString(2)
                    ));
                }
                while (cursorStdnts.moveToNext());
            }
            cursorStdnts.close();
            return stdntsList;
        }

        catch (Exception exc)
        {
            Log.d("getStudents: ", exc.getMessage());
        }
        return stdntsList;
    }

    //получение старост
    public List<String> getHeads(String GroupName)
    {
        ArrayList<String> headsList = new ArrayList<>();
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursorHeads = db.rawQuery("SELECT Head " +
                    "FROM Students as stdudents " +
                    "INNER JOIN Groups as groups " +
                    "ON students.IDGroup = groups.IDGroup " +
                    "WHERE groups.Name = ?",
                    new String[]{GroupName});

            if (cursorHeads.moveToFirst())
            {
                do
                {
                    headsList.add(cursorHeads.getString(0));
                }

                while (cursorHeads.moveToNext());
            }

            cursorHeads.close();
            return headsList;
        }

        catch (Exception exc)
        {
            Log.d("getHeads: ", exc.getMessage());
        }

        return headsList;
    }

    public int getIDGroup(String GroupName)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursorHeads = db.rawQuery("SELECT IDGroup FROM Groups WHERE Name=?" , new String[]{GroupName});
            int id = 0;

            if (cursorHeads.moveToFirst())
            {
                id = cursorHeads.getInt(0);
            }
            cursorHeads.close();
            return id;
        }

        catch (Exception exc)
        {
            Log.d("getIDGroup: ", exc.getMessage());
        }

        return -1;
    }

    public void selectGroupHead(int IDGroup, String HeadName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Log.d("IDGroup: ", String.valueOf(IDGroup));
        Log.d("HeadName: ", HeadName);

        cv.put(HEAD_COL, HeadName);

        db.update(TABLE_NAME_GROUPS, cv, "IDGroup=?", new String[]{String.valueOf(IDGroup)});
        db.close();
    }

    public void deleteGroup(String GroupName)
    {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME_GROUPS, "Name = ?", new String[]{GroupName});
            db.close();
        }

        catch (Exception exc)
        {
            Log.d("deleteGroup: ", exc.getMessage());
        }
    }
}
