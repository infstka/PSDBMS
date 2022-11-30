package com.psmsdb.lr11_14;

public class StudentTable {

    private int IDStudent;
    private int IDGroup;
    private String Name;
    private String Birthdate;
    private String Address;

    public StudentTable()
    {

    }

    public StudentTable(int IDStudent, int IDGroup, String Name, String Birthdate, String Address)
    {
        this.IDStudent = IDStudent;
        this.IDGroup = IDGroup;
        this.Name = Name;
        this.Birthdate = Birthdate;
        this.Address = Address;
    }

    @Override
    public String toString()
    {
        return String.format("IDStudent: %s\n IDGroup: %s\n Name: %s\n Birthdate: %s\n Address: %s\n", IDStudent, IDGroup, Name, Birthdate, Address);
    }
}
