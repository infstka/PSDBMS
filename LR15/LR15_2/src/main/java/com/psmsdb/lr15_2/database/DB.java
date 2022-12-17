package com.psmsdb.lr15_2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "LR15.db";
    private static final int DATABASE_VERSION = 1;

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS FACULTY_TABLE ( "
                + "IDFACULTY       INTEGER PRIMARY KEY, "
                + "FACULTY         TEXT    UNIQUE, "
                + "DEAN            TEXT    NOT NULL, "
                + "OFFICETIMETABLE TEXT    NOT NULL);"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS GROUP_TABLE ("
                + "IDGROUP INTEGER PRIMARY KEY, "
                + "FACULTY TEXT    NOT NULL, "
                + "COURSE  INTEGER CHECK (COURSE > 0 AND COURSE < 4), "
                + "NAME    TEXT    NOT NULL, "
                + "HEAD    TEXT    DEFAULT(0), "
                + "FOREIGN KEY(FACULTY) REFERENCES FACULTY_TABLE(FACULTY) "
                + "ON DELETE CASCADE ON UPDATE CASCADE);"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS STUDENT_TABLE ("
                + "IDGROUP   INTEGER NOT NULL, "
                + "IDSTUDENT INTEGER PRIMARY KEY, "
                + "NAME      TEXT    NOT NULL, "
                + "BIRTHDATE DATE    NOT NULL, "
                + "ADDRESS   TEXT    NOT NULL, "
                + "FOREIGN KEY(IDGROUP) REFERENCES GROUP_TABLE(IDGROUP) "
                + "ON DELETE CASCADE ON UPDATE CASCADE);"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS SUBJECT_TABLE ("
                + "IDSUBJECT INTEGER PRIMARY KEY, "
                + "SUBJECT   TEXT    NOT NULL);"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS PROGRESS_TABLE ("
                + "IDSTUDENT INTEGER NOT NULL, "
                + "IDSUBJECT INTEGER NOT NULL, "
                + "EXAMDATE  DATE    NOT NULL, "
                + "MARK      INTEGER CHECK (MARK >= 0 AND MARK <= 10), "
                + "TEACHER   TEXT    NOT NULL, "
                + "FOREIGN KEY(IDSUBJECT) REFERENCES SUBJECT_TABLE(IDSUBJECT) "
                + "ON DELETE CASCADE ON UPDATE CASCADE, "
                + "FOREIGN KEY(IDSTUDENT) REFERENCES STUDENT_TABLE(IDSTUDENT) "
                + "ON DELETE CASCADE ON UPDATE CASCADE);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE FACULTY_TABLE;");
        db.execSQL("DROP TABLE GROUP_TABLE");
        db.execSQL("DROP TABLE STUDENT_TABLE;");
        db.execSQL("DROP TABLE SUBJECT_TABLE");
        db.execSQL("DROP TABLE PROGRESS_TABLE;");
        onCreate(db);
    }


    public void addFaculty(SQLiteDatabase db, int idFaculty, String nameFaculty, String dean, String timetable ) {
        ContentValues cv = new ContentValues();
        cv.put("IDFACULTY", idFaculty);
        cv.put("FACULTY", nameFaculty);
        cv.put("DEAN", dean);
        cv.put("OFFICETIMETABLE", timetable);
        db.insert("FACULTY_TABLE", null, cv);
        cv.clear();
    }

    public void addGroup(SQLiteDatabase db, int idGroup, String faculty, int course, String name, String head) {
        ContentValues cv = new ContentValues();
        cv.put("IDGROUP", idGroup);
        cv.put("FACULTY", faculty);
        cv.put("COURSE", course);
        cv.put("NAME", name);
        cv.put("HEAD", head);
        db.insert("GROUP_TABLE", null, cv);
        cv.clear();
    }

    public void addStudent(SQLiteDatabase db, int idStudent, int idGroup, String name, String birthDate, String address) {
        ContentValues cv = new ContentValues();
        cv.put("IDSTUDENT", idStudent);
        cv.put("IDGROUP", idGroup);
        cv.put("NAME", name);
        cv.put("BIRTHDATE", birthDate);
        cv.put("ADDRESS", address);
        db.insert("STUDENT_TABLE", null, cv);
        cv.clear();
    }

    public void addSubject(SQLiteDatabase db, int idSubject, String name){
        ContentValues cv = new ContentValues();
        cv.put("IDSUBJECT", idSubject);
        cv.put("SUBJECT", name);
        db.insert("SUBJECT_TABLE", null, cv);
        cv.clear();
    }

    public void addProgress(SQLiteDatabase db, int idStudent, int idSubject ,String examDate, int mark, String teacher){
        ContentValues cv = new ContentValues();
        cv.put("IDSTUDENT", idStudent);
        cv.put("IDSUBJECT", idSubject);
        cv.put("EXAMDATE", examDate);
        cv.put("MARK", mark);
        cv.put("TEACHER", teacher);
        db.insert("PROGRESS_TABLE", null, cv);
        cv.clear();
    }

    public void initDatabase(SQLiteDatabase db) {
        addFaculty(db, 1,"Faculty1", "Dean1", "01:00-05:00");
        addFaculty(db, 2,"Faculty2", "Dean2", "02:00-06:00");
        addFaculty(db, 3,"Faculty3", "Dean3", "03:00-07:00");

        addGroup(db, 1,"Faculty1", 1, "Group1", "1");
        addGroup(db, 2,"Faculty1", 2, "Group2", "3");
        addGroup(db, 3,"Faculty2", 1, "Group3", "5");
        addGroup(db, 4,"Faculty2", 2, "Group4", "7");
        addGroup(db, 5,"Faculty3", 1, "Group5", "9");
        addGroup(db, 6,"Faculty3", 2, "Group6", "11");

        addStudent(db, 1, 1, "Name1", "01.01.2001","Address1");
        addStudent(db, 2, 1, "Name2", "02.02.2001","Address2");
        addStudent(db, 3, 2, "Name3", "03.03.2001","Address3");
        addStudent(db, 4, 2, "Name4", "04.04.2001","Address4");
        addStudent(db, 5, 3, "Name5", "01.01.2002","Address5");
        addStudent(db, 6, 3, "Name6", "02.02.2002","Address6");
        addStudent(db, 7, 4, "Name7", "03.03.2002","Address7");
        addStudent(db, 8, 4, "Name8", "04.04.2002","Address8");
        addStudent(db, 9, 5, "Name9", "01.01.2003","Address9");
        addStudent(db, 10, 5, "Name10", "02.02.2003","Address10");
        addStudent(db, 11, 6, "Name11", "03.03.2003","Address11");
        addStudent(db, 12, 6, "Name12", "04.04.2003","Address12");

        addSubject(db, 1, "Subject1");
        addSubject(db, 2, "Subject2");
        addSubject(db, 3, "Subject3");
        addSubject(db, 4, "Subject4");
        addSubject(db, 5, "Subject5");
        addSubject(db, 6, "Subject6");

        addProgress(db, 1,1,"2022-01-01", 2, "Teacher1");
        addProgress(db, 2,1,"2022-01-01", 4, "Teacher1");
        addProgress(db, 1,2,"2022-01-02", 6, "Teacher2");
        addProgress(db, 2,2,"2022-01-02", 8, "Teacher2");
//
        addProgress(db, 3,2,"2022-01-03", 9, "Teacher2");
        addProgress(db, 4,2,"2022-01-03", 5, "Teacher2");
        addProgress(db, 3,1,"2022-01-04", 6, "Teacher1");
        addProgress(db, 4,1,"2022-01-04", 4, "Teacher1");
//
        addProgress(db, 5,3,"2022-01-05", 3, "Teacher3");
        addProgress(db, 6,3,"2022-01-05", 3, "Teacher3");
        addProgress(db, 5,4,"2022-01-06", 4, "Teacher4");
        addProgress(db, 6,4,"2022-01-06", 3, "Teacher4");
//
        addProgress(db, 7,4,"2022-01-07", 6, "Teacher4");
        addProgress(db, 8,4,"2022-01-07", 4, "Teacher4");
        addProgress(db, 7,3,"2022-01-08", 4, "Teacher3");
        addProgress(db, 8,3,"2022-01-08", 5, "Teacher3");
//
        addProgress(db, 9,5,"2022-01-09", 7, "Teacher5");
        addProgress(db, 10,5,"2022-01-09", 6, "Teacher5");
        addProgress(db, 9,6,"2022-01-10", 9, "Teacher6");
        addProgress(db, 10,6,"2022-01-10", 1, "Teacher6");
//
        addProgress(db, 11,6,"2022-01-11", 2, "Teacher6");
        addProgress(db, 12,6,"2022-01-11", 5, "Teacher6");
        addProgress(db, 11,5,"2022-01-12", 4, "Teacher5");
        addProgress(db, 12,5,"2022-01-12", 7, "Teacher5");
    }

    public void createViews(SQLiteDatabase db) {
        db.execSQL("drop view if exists studentProgressGroup; ");
        db.execSQL("create view if not exists studentProgressGroup as " +
                "select s.IDGROUP, g.FACULTY, g.NAME groupName, s.NAME strudentName, p.MARK, p.EXAMDATE from PROGRESS_TABLE p, STUDENT_TABLE s, GROUP_TABLE g " +
                "where s.IDSTUDENT =  p.IDSTUDENT and s.IDGROUP = g.IDGROUP; "
        );

        db.execSQL("drop view if exists subjectProgress; ");
        db.execSQL("create view if not exists subjectProgress as " +
                "select st.IDGROUP, s.SUBJECT, st.NAME, p.MARK, p.EXAMDATE from SUBJECT_TABLE s, PROGRESS_TABLE p, STUDENT_TABLE st " +
                "where s.IDSUBJECT =  p.IDSUBJECT and st.IDSTUDENT = p.IDSTUDENT; "
        );
    }

    public void createIndex(SQLiteDatabase db) {
        db.execSQL("create index if not exists idx_examdate " +
                "on PROGRESS_TABLE (EXAMDATE);"
        );

        db.execSQL("create index if not exists idx_mark  "+
                "on PROGRESS_TABLE (MARK);"
        );
    }

    public void createTriggers(SQLiteDatabase db) {
        db.execSQL("create trigger tr_Insert_Student " +
                "before insert on STUDENT_TABLE " +
                "when (select count(STUDENT_TABLE.IDSTUDENT) from STUDENT_TABLE where IDGROUP = new.IDGROUP) > 2 " +
                "begin " +
                "select raise(abort, 'Операция INSERT запрещена');" +
                "end;"
        );

        db.execSQL("create trigger tr_Delete_Student " +
                "before delete on STUDENT_TABLE " +
                "when (select count(STUDENT_TABLE.IDSTUDENT) from STUDENT_TABLE where IDGROUP = new.IDGROUP) < 3 " +
                "begin " +
                "select raise(abort, 'Операция DELETE запрещена');" +
                "end;"
        );

        db.execSQL("create trigger tr_Update_Student " +
                "instead of update on studentProgressGroup " +
                "when (select count(*) from STUDENT_TABLE) < 3 " +
                "begin " +
                "update STUDENT_TABLE set NAME = new.NAME where IDSTUDENT = old.IDSTUDENT;" +
                "end;"
        );
    }
}