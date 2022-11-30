package com.psmsdb.lr11_14;

public class ProgressTable {

    private int IDStudent;
    private int IDSubject;
    private String ExamDate;
    private int Mark;
    private String Teacher;

    public ProgressTable()
    {

    }

    public ProgressTable(int IDStudent, int IDSubject, String ExamDate, int Mark, String Teacher)
    {
        this.IDStudent = IDStudent;
        this.IDSubject = IDSubject;
        this.ExamDate = ExamDate;
        this.Mark = Mark;
        this.Teacher = Teacher;
    }

    @Override
    public String toString()
    {
        return String.format("IDStudent: %s\n IDSubject: %s\n ExamDate: %s\n Mark: %s\n Teacher: %s\n", IDStudent, IDSubject, ExamDate, Mark, Teacher);
    }

}
