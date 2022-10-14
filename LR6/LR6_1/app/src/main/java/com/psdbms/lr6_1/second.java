package com.psdbms.lr6_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class second extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        verifyStoragePermissions(this);

        name = findViewById(R.id.editname);
        surname = findViewById(R.id.editsurname);
        phone = findViewById(R.id.editphone);
        date = findViewById(R.id.editdate);
        rb_yes = findViewById(R.id.yes);
        rb_no = findViewById(R.id.no);

        File privateFile = new File(super.getFilesDir(), FILE_NAME);
        File publicFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), FILE_NAME);
        //перезапись приватного файла
        if(!privateFile.exists())
        {
            try
            {
                privateFile.createNewFile();
            }
            catch(IOException ioexc)
            {
                ioexc.printStackTrace();
            }
        }
        //перезапись публичного файла
        if(!publicFile.exists())
        {
            try
            {
                publicFile.createNewFile();
            }
            catch(IOException ioexc)
            {
                ioexc.printStackTrace();
            }
        }

        contactList = new ArrayList<contact>();
    }

    EditText name;
    EditText surname;
    EditText phone;
    EditText date;
    RadioButton rb_yes;
    RadioButton rb_no;
    String FILE_NAME = "contacts.json";
    List<contact> contactList;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE =
            {
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

//подтвердить разрешения хранилища
    public static void verifyStoragePermissions(Activity activity)
    {
        //вид разрешения
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //выдаем разрешение
        if(permission != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

   //добавление контакта
    public void next(View view)
    {
        //проверка
        String sname = name.getText().toString();
        if(sname.length() < 1)
        {
            Toast.makeText(this, "Длина имени слишком короткая! Введите заново!", Toast.LENGTH_SHORT).show();
            name.setText("");
            return;
        }

        //тоже проверка
        String ssurname = surname.getText().toString();
        if(ssurname.length() < 1)
        {
            Toast.makeText(this, "Длина фамилии слишком короткая! Введите заново!", Toast.LENGTH_SHORT).show();
            surname.setText("");
            return;
        }

        //все еще проверка...
        String sphone = phone.getText().toString();
        try
        {
            if(sphone.length() < 3) {
                Toast.makeText(this, "Длина номера слишком короткая! Введите заново!", Toast.LENGTH_SHORT).show();
                phone.setText("");
                return;
            }
        }
        //если условие не удовлетворено, очистит строку и выведет тост
        catch (Exception exc)
        {
            Toast.makeText(this, "Введите номер", Toast.LENGTH_SHORT).show();
            phone.setText("");
            return;
        }

        //последняя проверка
        String sdate = date.getText().toString();
        if(sdate.length() < 7)
        {
            Toast.makeText(this, "Длина даты рождения слишком короткая! Введите заново!", Toast.LENGTH_SHORT).show();
            date.setText("");
            return;
        }
        //передаем в строковые значения
        contact contact = new contact(name.getText().toString(), surname.getText().toString(), phone.getText().toString(), date.getText().toString());

        //радиокнопки
        if(rb_yes.isChecked())
        {
            contactList = json.importFromJSONExternalStorage(this);
            assert contactList != null;
            contactList.add(contact);
            boolean extResult = json.exportToJSONExternalStorage(this, contactList);
            if(extResult)
                Toast.makeText(this, "Сохранено в External Storage", Toast.LENGTH_SHORT).show();
        }
        else if(rb_no.isChecked())
        {
            contactList = json.importFromJSONInternalStorage(this);
            contactList.add(contact);
            boolean intResult = json.exportToJSONInternalStorage(this, contactList);
            if(intResult)
                Toast.makeText(this, "Сохранено в Internal Storage", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "Ошибка записи", Toast.LENGTH_SHORT).show();

        this.finish();
    }

    //возврат на главную
    public void back(View view)
    {
        this.finish();
    }
}