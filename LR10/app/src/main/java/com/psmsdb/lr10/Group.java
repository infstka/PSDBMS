package com.psmsdb.lr10;

public class Group
{
    public int IDGroup;
    public String Faculty;
    public int Course;
    public String Name;
    public String Head;

    public Group() {}

    public Group(int IDGroup, String Faculty, int Course, String Name, String Head)
    {
        this.IDGroup = IDGroup;
        this.Faculty = Faculty;
        this.Course = Course;
        this.Name = Name;
        this.Head = Head;
    }

    @Override
    public String toString()
    {
        return Name;
    }

    public String getInf()
    {
        return String.format("Group: %s\n" + "\tFaculty: %s\n" + "\tCourse: %s\n" + "\tGroupName: %s\t" + "\tMainStudent: %s\n", IDGroup, Faculty, Course, Name, Head);
    }
}
