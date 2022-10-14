package com.psdbms.lr6_1;

import java.io.Serializable;

public class contact implements Serializable {
    public String Name;
    public String Surname;
    public String Phone;
    public String Birth;

    public contact(String name, String surname, String phone, String birth)
    {
        Name = name;
        Surname = surname;
        Phone = phone;
        Birth = birth;
    }

    @Override
    public String toString()
    {
        return Name + " " + Surname + "\n" + Phone + "\n" + Birth;
    }
}

