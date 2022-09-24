package com.psdbms.lr4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public String path = "Base_Lab.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //проверка наличия файла в Internal памяти
    private boolean ExistBase(String fname)
    {
        boolean rc = false;
        File f = new File(super.getFilesDir(), fname);
        if (rc = f.exists())
        {
            Log.d("Log_02", "Файл " + fname + " существует");
            Toast t = Toast.makeText(getApplicationContext(), "Файл уже существует", Toast.LENGTH_SHORT);
            t.show();
        }
        else
        {
            Log.d("Log_02", "Файл " + fname + " не найден");
            Toast t = Toast.makeText(getApplicationContext(), "Файл не найден, идет создание...", Toast.LENGTH_SHORT);
            t.show();

            ShowMessage(path);
            CreateFile(path);
        }
        return  rc;
    }

    //диалоговое окно для сообщения о создании файла
    private void ShowMessage(String fname)
    {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Создается файл " + fname).setPositiveButton("Хорошо",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("Log_02", "Создание файла " + fname);
                    }
                });
        AlertDialog ad = b.create();
        ad.show();
    }

    //создание файла в Internal памяти
    private void CreateFile(String fname)
    {
        File f = new File(super.getFilesDir(), fname);
        try
        {
            f.createNewFile();
            Log.d("Log_02", "Файл " + fname + " создан");
        }
        catch (IOException e)
        {
            Log.d("Log_02", "Файл " + fname + " не создан");
        }
    }

//создаем поток для преобразования введеных данных в байты
    private void CreateStream(String fname)
    {
        FileOutputStream fos = null;
        try
        {
            Log.d("Log_02", "Файл " + fname + " открыт");

            EditText entsur = findViewById(R.id.entrfam);
            EditText entname = findViewById(R.id.entrname);

            String fam = entsur.getText().toString();
            String name = entname.getText().toString();
            String s = fam+";"+name+";"+"\r\n";

            fos = openFileOutput(path, MODE_APPEND);
            fos.write(s.getBytes());
            Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e)
        {
            Log.d("Log_02", "Файл " + fname + " не открыт");
        }

    }

//кнопка ввода
    public void EnterBtn(View view)
    {
        ExistBase(path);
        CreateStream(path);
    }

//кнопка открытия файла и показа данных в текстовом блоке
    public void OpenBtn(View view)
    {
        FileInputStream fin = null;
        TextView tv = findViewById(R.id.blf);
        try
        {
            fin = openFileInput(path);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text = new String (bytes);
            tv.setText(text);
        }
        catch (IOException exc)
        {
            Toast.makeText(this, "Файла не существует, открывать нечего!", Toast.LENGTH_SHORT).show();
        }
        finally
        {
            try
            {
                if(fin != null)
                    fin.close();
            }
            catch (IOException exc)
            {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

//кнопка удаления файла и данных из текстового блока
    public void DelBtn(View view)
    {
        File f = new File(super.getFilesDir(), path);
        f.delete();
        TextView tv = findViewById(R.id.blf);
        tv.setText("");
        Toast.makeText(this, "Файл удален", Toast.LENGTH_SHORT).show();
    }
}

