package com.psmsdb.lr8;

import android.content.Context;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Note {

    public String Name;
    public String Category;
    public String Date;

    public Note(String name, String category, String date)
    {
        Name = name;
        Category = category;
        Date = date;
    }

    public Note()
    {

    }

    @Override
    public String toString()
    {
        return "Name: " + Name + "\n" + "Category: " + Category + "\n" + "Date: " + Date + "\n";
    }

    public static ArrayList<String> Categories = new ArrayList<>();
    public static ArrayList<Note> Notes = new ArrayList<>();

    public static void NotesSerialization(Context context)
    {
        try
        {
            //построение документа
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.newDocument();
            Element element = document.createElement("Notes");
            document.appendChild(element);

            for(Note note : Notes)
            {
                Element newElem = document.createElement("Note");
                newElem.setAttribute("Name", note.Name);
                newElem.setAttribute("Category", note.Category);
                newElem.setAttribute("Date", note.Date);
                element.appendChild(newElem);
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource doms = new DOMSource(document);

            FileOutputStream fos = context.openFileOutput("Notes.xml", Context.MODE_PRIVATE);
            StreamResult sr = new StreamResult(fos);
            transformer.transform(doms, sr);
            fos.close();
        }
        catch (Exception exc)
        {

        }
    }

    public static void CategoriesSerialization(Context context)
    {
        try
        {
            //построение документа
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.newDocument();
            Element element = document.createElement("Categories");
            document.appendChild(element);

            for(String cat : Categories)
            {
                Element newCategory = document.createElement("Category");
                newCategory.setTextContent(cat);
                element.appendChild(newCategory);
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource doms = new DOMSource(document);

            FileOutputStream fos = context.openFileOutput("Categories.xml", Context.MODE_PRIVATE);
            StreamResult sr = new StreamResult(fos);
            transformer.transform(doms, sr);
            fos.close();
        }
        catch (Exception exc)
        {

        }
    }

    public static void NotesDeserialization(File file)
    {
        try
        {
            //получение данных из файла
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            File f = new File(file, "Notes.xml");
            Document document = db.parse(f);
            ArrayList<Note> newNotes = new ArrayList<>();
            NodeList nl = document.getElementsByTagName("Note");

            for (int i = 0; i < nl.getLength(); i++)
            {
                Note note = new Note();
                NamedNodeMap nnm = nl.item(i).getAttributes();
                note.Name = nnm.getNamedItem("Name").getNodeValue();
                note.Category = nnm.getNamedItem("Category").getNodeValue();
                note.Date = nnm.getNamedItem("Date").getNodeValue();
                newNotes.add(note);
            }

            Notes = newNotes;
        }

        catch (Exception exc)
        {
            Log.d("LR8", exc.getMessage());
        }
    }

    public static void CategoriesDeserialization(File file)
    {
        try
        {
            //получение данных из файла
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            File f = new File(file, "Categories.xml");
            Document document = db.parse(f);
            ArrayList<String> newCategories = new ArrayList<>();
            NodeList nl = document.getElementsByTagName("Category");

            for (int i = 0; i < nl.getLength(); i++)
            {
                String str = nl.item(i).getTextContent();
                newCategories.add(str);
            }

            Categories = newCategories;
        }

        catch (Exception exc)
        {
            Log.d("LR8", exc.getMessage());
        }
    }
}
