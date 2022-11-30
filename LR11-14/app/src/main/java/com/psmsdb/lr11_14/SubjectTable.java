package com.psmsdb.lr11_14;

public class SubjectTable {

    private int IDSubject;
    private String Subject;

    public SubjectTable()
    {

    }

    public SubjectTable(int IDSubject, String Subject)
    {
        this.IDSubject = IDSubject;
        this.Subject = Subject;
    }

    @Override
    public String toString()
    {
        return String.format("IDSubject: %s\n Subject: %s\n", IDSubject, Subject);
    }
}

