package com.psmsdb.lr11_14;

public class GroupTable {

    private int IDGroup;
    private int Faculty;
    private int Course;
    private String Name;
    private int Head;

    public GroupTable()
    {

    }

    public GroupTable(int IDGroup, int Faculty, int Course, String Name, int Head)
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
        return String.format("IDGroup: %s\n Faculty: %s\n Course: %s\n Name: %s\n Head: %s\n", IDGroup, Faculty, Course, Name, Head);
    }
}
