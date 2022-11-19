package com.psmsdb.lr8;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

public class NoteCategory extends AppCompatActivity {

    private int extra;
    private String Category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_category);

        //получение значений предыдущих активити
        Bundle bundle = getIntent().getExtras();
        extra = bundle.getInt("extra");

        getNoteByCtgry();
    }

    public void getNoteByCtgry() {
        try
        {
            //получение данных из файла
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            File file = new File(getFilesDir(), "Notes.xml");
            Document document = db.parse(file);
            ArrayList<Note> chooseNotes = new ArrayList<>();

            //получение категории
            XPathFactory xpf = XPathFactory.newInstance();
            XPath path = xpf.newXPath();
            XPathExpression xpe = path.compile("/Notes/Note[@Category = '" + Category + "']");

            NodeList nl = (NodeList) xpe.evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nl.getLength(); i++)
            {
                Note note = new Note();
                note.Name = nl.item(i).getAttributes().getNamedItem("Name").getNodeValue();
                note.Category = nl.item(i).getAttributes().getNamedItem("Category").getNodeValue();
                note.Date = nl.item(i).getAttributes().getNamedItem("Date").getNodeValue();
                chooseNotes.add(note);
            }

            ListView lv = findViewById(R.id.tbc);
            ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, chooseNotes);
            lv.setAdapter(adapter);
        }
        catch (Exception exc)
        {
            Log.d("LR8", exc.getMessage());
        }
    }
}