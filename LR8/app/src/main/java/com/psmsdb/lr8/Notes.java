package com.psmsdb.lr8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class Notes extends AppCompatActivity {

    private String chsnDate;
    public static ArrayList<Note> currentNotes;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        //получение значений из предыдущих активити
        Bundle bundle = getIntent().getExtras();
        chsnDate = bundle.getString("ChoosenDate");

        TextView currentDate = findViewById(R.id.noteCurDate);
        currentDate.setText(chsnDate);

        Spinner fspinner = findViewById(R.id.fspinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Note.Categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fspinner.setAdapter(spinnerAdapter);

        ListView lv = findViewById(R.id.noteList);
        registerForContextMenu(lv);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                Note note = currentNotes.get(position);
                OpenNoteEditor(note);
            }
        });

        currentNotes = new ArrayList<>();
        for (Note note : Note.Notes) {
            if(note.Date.equals(chsnDate)){
                currentNotes.add(note);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, currentNotes);
        lv.setAdapter(adapter);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_category, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.del:
                deleteNote(acmi.position);
                FragmentManager manager = getSupportFragmentManager();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void add(View view)
    {
        //при попытке добавления задач больше 20
        if(Note.Notes.size() >= 20)
        {
            Toast.makeText(this, "Can't add notes more than 20", Toast.LENGTH_LONG).show();
        }
        //при попытке добавления задач больше 5 на одну дату
        else if(currentNotes.size() >= 5)
        {
            Toast.makeText(this, "Can't be notes more than 5 for this date", Toast.LENGTH_LONG).show();
        }
        //в противном случае - добавление
        else
        {
            Note newnote = new Note();
            newnote.Date = chsnDate;
            EditText noteInput = findViewById(R.id.enterZad);
            newnote.Name = String.valueOf(noteInput.getText());
            //имя задачи пустое
            if(newnote.Name.length() <= 0)
            {
                Toast.makeText(this, "Enter name", Toast.LENGTH_LONG).show();
            }
            else
            {
                Spinner spinner = findViewById(R.id.fspinner);
                newnote.Category = String.valueOf(spinner.getSelectedItem());
                Note.Notes.add(newnote);
                currentNotes.add(newnote);

                ListView lv = findViewById(R.id.noteList);
                ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, currentNotes);
                lv.setAdapter(adapter);
                Note.NotesSerialization(getApplicationContext());
            }
        }
    }

    public void deleteNote(int index){
        Note note = currentNotes.get(index);
        currentNotes.remove(note);
        Note.Notes.remove(note);
        ListView lv = findViewById(R.id.noteList);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, currentNotes);
        lv.setAdapter(adapter);
        Note.NotesSerialization(getApplicationContext());
        Toast.makeText(this, "Deleted", Toast.LENGTH_LONG).show();
    }

    //редактирование задачи и передача значений активити
    public void OpenNoteEditor(Note note){
        Intent intent = new Intent(this, EditNote.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra("Name", note.Name);
        intent.putExtra("Category", note.Category);
        intent.putExtra("Date", note.Date);
        startActivity(intent);
    }

    //формирование хслт
    public void xslt(View view) {
        try{
            String xslt =
                    "<?xml version=\"1.0\"?>\n" +
                            "<xsl:stylesheet version=\"1.0\" exclude-result-prefixes=\"xsl\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" +
                            "\n" +
                            "<xsl:key name=\"group\" match=\"Note\" use=\"@Category\"/>\n" +
                            "\n" +
                            "<xsl:template match=\"Notes\">\n" +
                            "    <Notes> \n" +
                            "        <xsl:apply-templates select=\"Note[generate-id(.) = generate-id(key('group',@Category))]\" />\n" +
                            "    </Notes>\n" +
                            "</xsl:template>\n" +
                            "\n" +
                            "<xsl:template match=\"Note\">\n" +
                            "    <Category>" +
                            "           <xsl:attribute name=\"value\">" +
                            "               <xsl:value-of select = \"@Category\"/>\n" +
                            "           </xsl:attribute>" +
                            "        <xsl:for-each select=\"key('group',@Category)\">\n" +
                            "            <xsl:if test=\"@Date='"+ chsnDate + "'\">\n" +
                            "            <Name><xsl:value-of select=\"@Name\"/></Name>\n" +
                            "            <Date><xsl:value-of select=\"@Date\"/></Date>\n" +
                            "</xsl:if>" +
                            "        </xsl:for-each>\n" +
                            "    </Category>\n" +
                            "</xsl:template>\n" +
                            "\n" +
                            "</xsl:stylesheet>";

            File file = new File(getFilesDir(), "LR8.xslt");
            FileWriter fw = new FileWriter(file, false);
            fw.write(xslt);
            fw.close();

            //формирование остальных файлов (задачи, категория с датой, задачи и категории)
            FileInputStream xmlf = openFileInput("Notes.xml");
            FileInputStream xslf = openFileInput("LR8.xslt");
            FileOutputStream txt = openFileOutput("AllTogether.xml", MODE_PRIVATE);
            TransformerFactory tf = TransformerFactory.newInstance();

            Source xsltsrc = new StreamSource(xslf);
            Source xmlsrc = new StreamSource(xmlf);
            Transformer transformer = tf.newTransformer(xsltsrc);
            transformer.transform(xmlsrc, new StreamResult(txt));
            Toast.makeText(this, "XSLT is ready", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}