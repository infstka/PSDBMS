package com.psmsdb.lr11_14;

import static java.lang.String.format;

public class FacultyTable {

    private int IDFaculty;
    private String Faculty;
    private String Dean;
    private String OfficeTimetable;

    public FacultyTable() {

    }

    public FacultyTable(int IDFaculty, String Faculty, String Dean, String OfficeTimetable) {
        this.IDFaculty = IDFaculty;
        this.Faculty = Faculty;
        this.Dean = Dean;
        this.OfficeTimetable = OfficeTimetable;
    }

    @Override
    public String toString()
    {
        return String.format("IDFaculty: %s\n Faculty: %s\n Dean: %s\n OfficeTimetable: %s\n", IDFaculty, Faculty, Dean, OfficeTimetable);
    }
}
