package com.psdbms.lr5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;

public class MainActivity extends AppCompatActivity {

    private RandomAccessFile raf;
    private String FILE_NAME = "LR5.txt";
    EditText key1, key2, value1, value2;
    String example = "     :          :  ;\n";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        key1 = findViewById(R.id.enterkey1);
        key2 = findViewById(R.id.enterkey2);
        value1 = findViewById(R.id.entervalue1);
        value2 = findViewById(R.id.entervalue2);
        checkFile();
    }

    //проверка существования файла
    private void checkFile()
    {
        File file = new File(super.getFilesDir(), FILE_NAME);

        if (file.exists())
        {
        }
        else
        {
            try
            {
                file.createNewFile();
                exampleFill(10);
            }
            catch (Exception exc)
            {
                Log.d("LR5", exc.getMessage());
            }
        }
    }

    public void exampleFill(int count)
    {
        File file = new File(super.getFilesDir(), FILE_NAME);
        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            for (int i = 0; i < count; i++)
            {
                bw.write(example);
            }
            bw.close();
        }

        catch (Exception exc)
        {
            Log.d("LR5", exc.getMessage());
        }
    }

    //запись значения
    public void save(View view)
    {
        if (key1.getText().toString().equals("") || value1.getText().toString().equals(""))
        {
            Toast toast = Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        String firstKey = key1.getText().toString();
        String firstValue = value1.getText().toString();

        int Hash = Hash(firstKey);
        Log.d("LR5", "key: " + firstKey);
        Log.d("LR5", "hash: " + String.valueOf(Hash));
        stringReader(Hash, firstKey, firstValue);
    }

    public void stringReader(int Hash, String firstKey, String firstValue)
    {
        String buf;
        String keyBuf;
        try
        {
            File file = new File(super.getFilesDir(), FILE_NAME);
            raf = new RandomAccessFile(file, "rw");
            raf.seek(21 + Hash);
            buf = raf.readLine();
            keyBuf = buf.substring(0, 5);
            if (firstKey.trim().equals(keyBuf.trim()) || keyBuf.trim().equals("")) {
                Writer(Hash, firstKey, firstValue);
            } else {
                newHash(Hash, firstKey, firstValue);
            }
        }
        catch (Exception exc)
        {
            Log.d("LR5", exc.getMessage());
        }
    }

    public void newHash(int Hash, String firstKey, String firstValue)
    {

    }

    public int Hash(String firstKey)
    {

        return firstKey.hashCode()%9;
    }

    public void addExample(int newhash)
    {

    }

    public void Writer(int hashPos, String firstkey, String firstvalue)
    {

    }

    public void get(View view)
    {

    }
}
