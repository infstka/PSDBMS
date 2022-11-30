package com.psmsdb.lr11_14;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DB extends SQLiteOpenHelper {

    private static final String DB_NAME = "LR11-14.db";
    private static final int DB_VERSION = 1;

    private static final String FACULTY_TABLE = "FacultyTable";
    private static final String GROUP_TABLE = "GroupTable";
    private static final String STUDENT_TABLE = "StudentTable";
    private static final String SUBJECT_TABLE = "SubjectTable";
    private static final String PROGRESS_TABLE = "ProgressTable";

    /**Faculty table**/
    private static final String IDFACULTY = "IDFaculty";
    private static final String FACULTY = "Faculty";
    private static final String DEAN = "Dean";
    private static final String OFFICETIMETABLE = "OfficeTimetable";

    /**Groups table**/
    private static final String IDGROUP = "IDGroup";
    private static final String COURSE = "Course";
    private static final String NAME = "Name";
    private static final String HEAD = "Head";

    /**Student table**/
    private static final String IDSTUDENT = "IDStudent";
    private static final String BIRTHDATE = "Birthdate";
    private static final String ADDRESS = "Address";

    /**Subject table**/
    private static final String IDSUBJECT = "IDSubject";
    private static final String SUBJECT = "Subject";

    /**Progress table**/
    private static final String EXAMDATE = "ExamDate";
    private static final String MARK = "Mark";
    private static final String TEACHER = "Teacher";

    public DB(Context context)
    {

        super(context, DB_NAME, null, DB_VERSION);

    }

    //создание таблиц
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE IF EXISTS " + FACULTY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + GROUP_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SUBJECT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PROGRESS_TABLE);

        String query_Faculty = "CREATE TABLE " + FACULTY_TABLE + " (" +
                IDFACULTY + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FACULTY + " TEXT NOT NULL," +
                DEAN + " TEXT NOT NULL," +
                OFFICETIMETABLE + " TEXT)";

        String query_Group = "CREATE TABLE " + GROUP_TABLE + " (" +
                IDGROUP + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FACULTY + " INTEGER NOT NULL," +
                COURSE + " INTEGER NOT NULL," +
                NAME + " TEXT NOT NULL," +
                HEAD + " INTEGER, " +
                " CONSTRAINT GroupHeadConstr FOREIGN KEY (Head) REFERENCES StudentTable(IDStudent) ON DELETE CASCADE," +
                " CONSTRAINT GroupFacConstr FOREIGN KEY (Faculty) REFERENCES FacultyTable(IDFaculty) ON DELETE CASCADE )";


        String query_Student = "CREATE TABLE " + STUDENT_TABLE + " (" +
                IDSTUDENT + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IDGROUP + " INTEGER, " +
                NAME + " TEXT, " +
                BIRTHDATE + " TEXT UNIQUE, " +
                ADDRESS + " TEXT, " +
                " CONSTRAINT StudentConstr FOREIGN KEY (IDGroup) REFERENCES GroupTable(IDGroup) ON DELETE CASCADE )";

        String query_Subject = "CREATE TABLE " + SUBJECT_TABLE + " (" +
                IDSUBJECT + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SUBJECT + " TEXT NOT NULL)";

        String query_Progress = "CREATE TABLE " + PROGRESS_TABLE + " (" +
                IDSTUDENT + " INTEGER, " +
                IDSUBJECT + " INTEGER, " +
                EXAMDATE + " TEXT, " +
                MARK + " INTEGER, " +
                TEACHER + " TEXT NOT NULL, " +
                " CONSTRAINT StudProgressConstr FOREIGN KEY (IDStudent) REFERENCES StudentTable(IDStudent) ON DELETE CASCADE, " +
                " CONSTRAINT SubjProgressConstr FOREIGN KEY (IDSubject) REFERENCES SubjectTable(IDSubject) ON DELETE CASCADE )";

        db.execSQL(query_Faculty);
        db.execSQL(query_Group);
        db.execSQL(query_Student);
        db.execSQL(query_Subject);
        db.execSQL(query_Progress);

        /**Indexes**/
        db.execSQL("CREATE INDEX IF NOT EXISTS " + "INDEX_PROGRESS_EXAMDATE " + "ON " + PROGRESS_TABLE + " (" + "ExamDate" + ")");
        db.execSQL("CREATE INDEX IF NOT EXISTS " + "INDEX_GROUP_FACULTY " + "ON " + GROUP_TABLE + " (" + "Faculty" + ")");

        /**Triggers**/
        //тригер, запрещающий добавление больше 3 студентов в 1 группу
        db.execSQL("CREATE TRIGGER IF NOT EXISTS " + "TRIGGER_THREE_STUDENTS " +
                "BEFORE INSERT ON " + STUDENT_TABLE +
                " BEGIN SELECT RAISE (ABORT, 'Нельзя добавить больше 3 студентов!') " +
                "WHERE (SELECT COUNT(*) FROM StudentTable CURRENT WHERE NEW.IDGroup = CURRENT.IDGroup) >= 3;" +
                " END;");

        //триггер, запрещающий оставление в 1 группе меньше 2 студентов
        db.execSQL("CREATE TRIGGER IF NOT EXISTS " + "TRIGGER_TWO_STUDENTS " +
                "BEFORE DELETE ON " + STUDENT_TABLE +
                " BEGIN SELECT RAISE (ABORT, 'Нельзя оставить в 1 группе меньше 2 студентов!') " +
                "WHERE (SELECT COUNT(*) FROM StudentTable CURRENT WHERE OLD.IDGroup = CURRENT.IDGroup) <= 2;" +
                " END;");

        //триггеры, обновляющиe значения старост групп
        db.execSQL("CREATE TRIGGER IF NOT EXISTS " + "TRIGGER_UPDATE_GROUP1_HEAD " +
                " AFTER INSERT ON " + STUDENT_TABLE +
                " BEGIN " + "UPDATE " + GROUP_TABLE + " SET Head = 1" + " WHERE GroupTable.IDGroup = 1; " +
                " END;");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS " + "TRIGGER_UPDATE_GROUP2_HEAD " +
                " AFTER INSERT ON " + STUDENT_TABLE +
                " BEGIN " + "UPDATE " + GROUP_TABLE + " SET Head = 3" + " WHERE GroupTable.IDGroup = 2; " +
                " END;");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS " + "TRIGGER_UPDATE_GROUP3_HEAD " +
                " AFTER INSERT ON " + STUDENT_TABLE +
                " BEGIN " + "UPDATE " + GROUP_TABLE + " SET Head = 5" + " WHERE GroupTable.IDGroup = 3; " +
                " END;");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS " + "TRIGGER_UPDATE_GROUP4_HEAD " +
                " AFTER INSERT ON " + STUDENT_TABLE +
                " BEGIN " + "UPDATE " + GROUP_TABLE + " SET Head = 7" + " WHERE GroupTable.IDGroup = 4; " +
                " END;");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS " + "TRIGGER_UPDATE_GROUP5_HEAD " +
                " AFTER INSERT ON " + STUDENT_TABLE +
                " BEGIN " + "UPDATE " + GROUP_TABLE + " SET Head = 9" + " WHERE GroupTable.IDGroup = 5; " +
                " END;");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS " + "TRIGGER_UPDATE_GROUP6_HEAD " +
                " AFTER INSERT ON " + STUDENT_TABLE +
                " BEGIN " + "UPDATE " + GROUP_TABLE + " SET Head = 11" + " WHERE GroupTable.IDGroup = 6; " +
                " END;");
    }

    //заполнение таблицы факультетов
    private static final String fillFaculty = "INSERT INTO " + FACULTY_TABLE + "(Faculty, Dean, OfficeTimetable)" + "VALUES " +
            "('Faculty1', 'Dean1', '01:00-05:00'), " +
            "('Faculty2', 'Dean2', '02:00-06:00'), " +
            "('Faculty3', 'Dean3', '03:00-07:00');";

    //заполнение таблицы групп (старосты имеют значение нулл)
    private static final String fillGroup = "INSERT INTO " + GROUP_TABLE + "(Faculty, Course, Name)" + "VALUES " +
            "(1, 1, 'Group1'), " +
            "(1, 2, 'Group2'), " +
            "(2, 1, 'Group3'), " +
            "(2, 2, 'Group4'), " +
            "(3, 1, 'Group5'), " +
            "(3, 2, 'Group6');";

    //заполнение таблицы студентов
    private static final String fillStudent = "INSERT INTO " + STUDENT_TABLE + "(IDGroup, Name, Birthdate, Address)" + "VALUES " +
            "(1, 'Name1', '01.01.2001', 'Address1'), " +
            "(1, 'Name2', '02.02.2001', 'Address2'), " +
            "(2, 'Name3', '03.03.2001', 'Address3'), " +
            "(2, 'Name4', '04.04.2001', 'Address4'), " +
            "(3, 'Name5', '01.01.2002', 'Address5'), " +
            "(3, 'Name6', '02.02.2002', 'Address6'), " +
            "(4, 'Name7', '03.03.2002', 'Address7'), " +
            "(4, 'Name8', '04.04.2002', 'Address8'), " +
            "(5, 'Name9', '01.01.2003', 'Address9'), " +
            "(5, 'Name10', '02.02.2003', 'Address10'), " +
            "(6, 'Name11', '03.03.2003', 'Address11'), " +
            "(6, 'Name12', '04.04.2003', 'Address12');";

    //заполнение таблицы предметов
    private static final String fillSubject = "INSERT INTO " + SUBJECT_TABLE + "(Subject)" + "VALUES " +
            "('Subject1'), " +
            "('Subject2'), " +
            "('Subject3'), " +
            "('Subject4'), " +
            "('Subject5'), " +
            "('Subject6')";

    //заполнение таблицы прогресса
    private static final String fillProgress = "INSERT INTO " + PROGRESS_TABLE + "(IDStudent, IDSubject, ExamDate, Mark, Teacher)" + "VALUES " +
            "(1, 1, '01.01.2022', 2, 'Teacher1'), " +
            "(2, 1, '01.01.2022', 4, 'Teacher1'), " +
            "(1, 2, '02.01.2022', 6, 'Teacher2'), " +
            "(2, 2, '02.01.2022', 8, 'Teacher2'), " +

            "(3, 2, '03.01.2022', 9, 'Teacher2'), " +
            "(4, 2, '03.01.2022', 5, 'Teacher2'), " +
            "(3, 1, '04.01.2022', 8, 'Teacher1'), " +
            "(4, 1, '04.01.2022', 4, 'Teacher1'), " +

            "(5, 3, '05.01.2022', 3, 'Teacher3'), " +
            "(6, 3, '05.01.2022', 3, 'Teacher3'), " +
            "(5, 4, '06.01.2022', 4, 'Teacher4'), " +
            "(6, 4, '06.01.2022', 3, 'Teacher4'), " +

            "(7, 4, '07.01.2022', 6, 'Teacher4'), " +
            "(8, 4, '07.01.2022', 4, 'Teacher4'), " +
            "(7, 3, '08.01.2022', 4, 'Teacher3'), " +
            "(8, 3, '08.01.2022', 5, 'Teacher3'), " +

            "(9, 5, '09.01.2022', 7, 'Teacher5'), " +
            "(10, 5, '09.01.2022', 6, 'Teacher5'), " +
            "(9, 6, '10.01.2022', 9, 'Teacher6'), " +
            "(10, 6, '10.01.2022', 1, 'Teacher6'), " +

            "(11, 6, '11.01.2022', 2, 'Teacher6'), " +
            "(12, 6, '11.01.2022', 5, 'Teacher6'), " +
            "(11, 5, '12.01.2022', 4, 'Teacher5'), " +
            "(12, 5, '12.01.2022', 7, 'Teacher5');";


    //обновление таблиц
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldTable, int newTable)
    {
        db.execSQL("DROP TABLE IF EXISTS " + FACULTY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + GROUP_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SUBJECT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PROGRESS_TABLE);
        onCreate(db);
    }

    //заполнение таблиц
    public void FillTables()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(fillFaculty);
        db.execSQL(fillGroup);
        db.execSQL(fillStudent);
        db.execSQL(fillSubject);
        db.execSQL(fillProgress);
        db.close();
    }

    //триггер, запрещающий добавить больше трех студентов в группу
    public void triggerMoreThanThree()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO " + STUDENT_TABLE + "(IDGroup, Name, Birthdate, Address)" + "VALUES " +
                "(1, 'Name0', '01.01.2000', 'Address0');");
    }

    //триггер, запрещающий добавить больше трех студентов в группу
    public void triggerLessThanTwo()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + STUDENT_TABLE + " WHERE (Name = 'Name0')");
        db.execSQL("DELETE FROM " + STUDENT_TABLE + " WHERE (Name = 'Name1')");
        db.execSQL("DELETE FROM " + STUDENT_TABLE + " WHERE (Name = 'Name2')");
    }

    //вторичные ключи
    @Override
    public void onConfigure(SQLiteDatabase db)
    {
        super.onConfigure(db);
        db.execSQL("PRAGMA foreign_keys = ON");
    }

    //получение данных из таблицы факультетов
    public List<FacultyTable> getFaculty()
    {
        ArrayList<FacultyTable> ft = new ArrayList<>();
        try
        {
            SQLiteDatabase sql = this.getReadableDatabase();
            Cursor cursor = sql.rawQuery("SELECT * FROM " + FACULTY_TABLE, null);

            if(cursor.moveToFirst())
            {
                do
                {
                    ft.add(new FacultyTable(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            return ft;
        }
        catch (Exception exc)
        {
            Log.d("LR11", exc.getMessage());
        }
        return ft;
    }

    //получение данных из таблицы групп
    public List<GroupTable> getGroup()
    {
        ArrayList<GroupTable> gt = new ArrayList<>();
        try
        {
            SQLiteDatabase sql = this.getReadableDatabase();
            Cursor cursor = sql.rawQuery("SELECT * FROM " + GROUP_TABLE, null);

            if(cursor.moveToFirst())
            {
                do
                {
                    gt.add(new GroupTable(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getInt(4)));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            return gt;
        }
        catch (Exception exc)
        {
            Log.d("LR11", exc.getMessage());
        }
        return gt;
    }

    //получение данных из таблицы студентов
    public List<StudentTable> getStudent()
    {
        ArrayList<StudentTable> stt = new ArrayList<>();
        try
        {
            SQLiteDatabase sql = this.getReadableDatabase();
            Cursor cursor = sql.rawQuery("SELECT * FROM " + STUDENT_TABLE, null);

            if(cursor.moveToFirst())
            {
                do
                {
                    stt.add(new StudentTable(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            return stt;
        }
        catch (Exception exc)
        {
            Log.d("LR11", exc.getMessage());
        }
        return stt;
    }

    //получение данных из таблицы предметов
    public List<SubjectTable> getSubject()
    {
        ArrayList<SubjectTable> subt = new ArrayList<>();
        try
        {
            SQLiteDatabase sql = this.getReadableDatabase();
            Cursor cursor = sql.rawQuery("SELECT * FROM " + SUBJECT_TABLE, null);

            if(cursor.moveToFirst())
            {
                do
                {
                    subt.add(new SubjectTable(cursor.getInt(0), cursor.getString(2)));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            return subt;
        }
        catch (Exception exc)
        {
            Log.d("LR11", exc.getMessage());
        }
        return subt;
    }

    //получение данных из таблицы прогресса
    public List<ProgressTable> getProgress()
    {
        ArrayList<ProgressTable> pt = new ArrayList<>();
        try
        {
            SQLiteDatabase sql = this.getReadableDatabase();
            Cursor cursor = sql.rawQuery("SELECT * FROM " + PROGRESS_TABLE, null);

            if(cursor.moveToFirst())
            {
                do
                {
                    pt.add(new ProgressTable(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4)));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            return pt;
        }
        catch (Exception exc)
        {
            Log.d("LR11", exc.getMessage());
        }
        return pt;
    }

    /**Queries (add views)**/
    //средняя оценка (для каждого студента по предмету)
    public List<String> avgMarkStudentSubject()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> result = new ArrayList<>();
        db.execSQL("DROP VIEW IF EXISTS AMSSView");
        db.execSQL("CREATE VIEW AMSSView AS " + "SELECT Groups.Name, " + "StudentTable.Name, " + "Subject.Subject, " + "Progress.Mark " +
                "FROM StudentTable" +
                " INNER JOIN GroupTable Groups ON StudentTable.IDGroup = Groups.IDGroup" +
                " INNER JOIN ProgressTable Progress on StudentTable.IDStudent = Progress.IDStudent" +
                " INNER JOIN SubjectTable Subject on Progress.IDSubject = Subject.IDSubject" +
                " ORDER BY Progress.IDStudent");
        Cursor cursor = db.rawQuery("SELECT * " + " FROM AMSSView", null);

        if (cursor.moveToFirst())
        {
            do
            {
                result.add(String.format("Группа: %s\nСтудент: %s\nПредмет: %s\nСредняя оценка: %s", cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    //средняя оценка (для каждого студента)
    public List<String> avgMarkStudent()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> result = new ArrayList<>();
        db.execSQL("DROP VIEW IF EXISTS AMSView");
        db.execSQL("CREATE VIEW AMSView AS " + "SELECT Groups.Name, " + "StudentTable.Name, " + "AVG(Mark)" +
                "FROM StudentTable" +
                " INNER JOIN GroupTable Groups ON StudentTable.IDGroup = Groups.IDGroup" +
                " INNER JOIN ProgressTable Progress on StudentTable.IDStudent = Progress.IDStudent" +
                " GROUP BY StudentTable.Name" +
                " ORDER BY StudentTable.IDStudent");
        Cursor cursor = db.rawQuery("SELECT * " + " FROM AMSView", null);


        if (cursor.moveToFirst())
        {
            do
            {
                result.add(String.format("Группа: %s\nСтудент: %s\nСредняя оценка: %s", cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    //средняя оценка (для группы)
    public List<String> avgGroup(String start, String end)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> result = new ArrayList<>();
        db.execSQL("DROP VIEW IF EXISTS AGView");
        db.execSQL(String.format("CREATE VIEW AGView AS " + "SELECT Groups.Name, " + "AVG(Mark)" +
                " FROM StudentTable" +
                " INNER JOIN GroupTable Groups ON StudentTable.IDGroup = Groups.IDGroup" +
                " INNER JOIN ProgressTable Progress on StudentTable.IDStudent = Progress.IDStudent" +
                " GROUP BY Groups.IDGroup" +
                " HAVING ExamDate > '%s'" + " AND " + "ExamDate < '%s'" +
                " ORDER BY Groups.IDGroup", start, end));
        Cursor cursor = db.rawQuery("SELECT * " + " FROM AGView" , null);

        if (cursor.moveToFirst())
        {
            do
            {
                result.add(String.format("Группа: %s\nСредняя оценка: %s", cursor.getString(0), cursor.getString(1)));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    //наилучшие студенты
    public List<String> bestStudents(String start, String end)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> result = new ArrayList<>();
        db.execSQL("DROP VIEW IF EXISTS BSView");
        db.execSQL(String.format("CREATE VIEW BSView AS " + "SELECT StudentTable.Name, " + "AVG(Mark)" +
                " FROM StudentTable" +
                " INNER JOIN GroupTable Groups ON StudentTable.IDGroup = Groups.IDGroup" +
                " INNER JOIN ProgressTable Progress on StudentTable.IDStudent = Progress.IDStudent" +
                " GROUP BY StudentTable.Name" +
                " HAVING ExamDate > '%s'" + " AND " + "ExamDate < '%s'" + " AND " + "Mark > 6" +
                " ORDER BY Progress.IDStudent", start, end));
        Cursor cursor = db.rawQuery("SELECT * " + " FROM BSView", null);

        if (cursor.moveToFirst())
        {
            do
            {
                result.add(String.format("Студент: %s\nОценка: %S", cursor.getString(0), cursor.getString(1)));
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        return result;
    }

    //оценки ниже 4
    public List<String> negativeMark(String start, String end)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> result = new ArrayList<>();
        db.execSQL("DROP VIEW IF EXISTS NMView");
        db.execSQL(String.format("CREATE VIEW NMView AS " + "SELECT Groups.Name, " + "StudentTable.Name, " + "ExamDate, "  + "Mark" +
                " FROM StudentTable" +
                " INNER JOIN GroupTable Groups ON StudentTable.IDGroup = Groups.IDGroup" +
                " INNER JOIN ProgressTable Progress on StudentTable.IDStudent = Progress.IDStudent" +
                " WHERE ExamDate > '%s'" + " AND " + "ExamDate < '%s'" + " AND " + "Mark < 4" +
                " ORDER BY StudentTable.IDStudent ASC", start, end));
        Cursor cursor = db.rawQuery("SELECT * " + " FROM NMView", null);

        if (cursor.moveToFirst())
        {
            do
            {
                result.add(String.format("Группа: %s\nСтудент: %s\nДата экзамена: %S\nОценка: %s", cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    //сравнение групп по предметам
    public List<String> groupComp(String start, String end)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> result = new ArrayList<>();
        db.execSQL("DROP VIEW IF EXISTS GCView");
        db.execSQL(String.format("CREATE VIEW GCView AS " + "SELECT GroupTable.Name, " + "Students.Name, " + "Subject, " + "Mark" +
                " FROM GroupTable" +
                " INNER JOIN StudentTable Students ON GroupTable.IDGroup = Students.IDGroup" +
                " INNER JOIN ProgressTable Progress on Students.IDStudent = Progress.IDStudent" +
                " INNER JOIN SubjectTable Subjects ON Progress.IDSubject = Subjects.IDSubject" +
                " WHERE ExamDate > '%s'" + " AND " + "ExamDate < '%s'", start, end));
        Cursor cursor = db.rawQuery("SELECT * " + " FROM GCView", null);

        if (cursor.moveToFirst())
        {
            do
            {
                result.add(String.format("Группа: %s\nСтудент: %s\nПредмет: %s\nОценка: %s", cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    //сравнение по факультетам
    public List<String> facComp(String start, String end)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> result = new ArrayList<>();
        db.execSQL("DROP VIEW IF EXISTS FCView");
        db.execSQL(String.format("CREATE VIEW FCView AS " + "SELECT FacultyTable.Faculty, " +"AVG(Mark)" +
                " FROM FacultyTable" +
                " INNER JOIN GroupTable Groups ON FacultyTable.IDFaculty = Groups.Faculty" +
                " INNER JOIN StudentTable Students ON Groups.IDGroup = Students.IDGroup" +
                " INNER JOIN ProgressTable Progress on Students.IDStudent = Progress.IDStudent" +
                " GROUP BY FacultyTable.Faculty" +
                " HAVING ExamDate > '%s'" + " AND " + "ExamDate < '%s'" +
                " ORDER BY AVG(Mark) ASC", start, end));
        Cursor cursor = db.rawQuery("SELECT * FROM FCView", null);

        if (cursor.moveToFirst())
        {
            do
            {
                result.add(String.format("Факультет: %s\nСредняя оценка: %s", cursor.getString(0), cursor.getString(1)));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }
}
