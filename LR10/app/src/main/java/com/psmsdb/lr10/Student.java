package com.psmsdb.lr10;

public class Student
{
    public int IDGroup;
    public int IDStudent;
    public String Name;

    public Student() {}

    public Student(int IDGroup, int IDStudent, String Name)
    {
        this.IDGroup = IDGroup;
        this.IDStudent = IDStudent;
        this.Name = Name;
    }

    @Override
    public String toString()
    {
        return String.format("%s\n", Name);
    }

    public String getInf()
    {
        return String.format("\t%s\n" + "\t\t\t* GroupID: %s\n" + "\t\t\t* StudentID: %s\n", Name, IDGroup, IDStudent);
    }
}
