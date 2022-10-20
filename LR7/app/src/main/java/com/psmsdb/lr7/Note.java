package com.psmsdb.lr7;

import java.io.Serializable;

public class Note implements Serializable
{
    public String noteText;
    public String noteDate;

    public Note()
    {
    }

    public Note(String noteText, String noteDate)
    {
        this.noteText = noteText;
        this.noteDate = noteDate;
    }

    public String toString()
    {
        return String.format("Текст заметки: %s\n Дата: s\n\n", noteText, noteDate);
    }
}
