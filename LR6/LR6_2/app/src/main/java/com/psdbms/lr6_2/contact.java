package com.psdbms.lr6_2;

public class contact {
    public String Name;
    public String Surname;
    public String Phone;
    public String Birth;
    public Boolean IsOpen;

    public contact(String name, String surname, String phone, String birth, Boolean isopen)
    {
        Name = name;
        Surname = surname;
        Phone = phone;
        Birth = birth;
        IsOpen = isopen;
    }

    @Override
    public String toString()
    {
        return Name + " " + Surname + "\n" + Phone + "\n" + Birth;
    }
}
