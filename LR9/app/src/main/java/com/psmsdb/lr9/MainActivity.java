package com.psmsdb.lr9;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DB(MainActivity.this);

        enterID = findViewById(R.id.enterID);
        enterFloat = findViewById(R.id.enterFloat);
        enterText = findViewById(R.id.enterText);
    }

    DB db;
    EditText enterID, enterFloat, enterText;

    //проверка -> работа с данными
    public void insertTable(View view) throws NumberFormatException, NullPointerException {

        //если в какое-то поле (или все сразу) пустое - вывод тоста
        if(enterID.getText().toString().isEmpty() ||
           enterFloat.getText().toString().isEmpty() ||
           enterText.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Необходимо заполнить все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        Integer id = enterID.getText().toString().matches("") ? null : Integer.parseInt(String.valueOf(enterID.getText()));
        Float f = enterFloat.getText().toString().matches("") ? null : Float.parseFloat(String.valueOf(enterFloat.getText()));
        String t = enterText.getText().toString().matches("") ? null : enterText.getText().toString();

        if (db.addTable(id, f, t))
        {
            Toast toast = Toast.makeText(this, "Значение добавлено", Toast.LENGTH_LONG);
            toast.show();
            enterText.setText("");
            enterID.setText("");
            enterFloat.setText("");
        }
        else
        {
            Toast toast = Toast.makeText(this, "Ошибка", Toast.LENGTH_LONG);
            toast.show();
        }

    }

    public void selectTable(View view)
    {
        try
        {
            //если поле айди пустое - вывод тоста
            if(enterID.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Заполните ID", Toast.LENGTH_SHORT).show();
                return;
            }

            //если введенного id нет в бд - заполнит float 0.0 и text null
            SimpleTable st = db.getTable(enterID.getText().toString());
            if(String.valueOf(st.f) != "0.0" && st.t == null)
            {
                Toast.makeText(this, "Элемент отсутствует", Toast.LENGTH_SHORT).show();
                enterFloat.setText("");
                enterText.setText("");
                return;
            }
            enterFloat.setText(String.valueOf(st.f));
            enterText.setText(st.t);
            Toast.makeText(this, "Элемент получен", Toast.LENGTH_SHORT).show();
        }
        catch(Exception exc)
        {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void selectRawTable(View view)
    {
        try
        {
            if(enterID.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Заполните ID", Toast.LENGTH_SHORT).show();
                return;
            }

            SimpleTable st = db.getTablesRaw(enterID.getText().toString());
            if(String.valueOf(st.f) != "0.0" && st.t == null)
            {
                Toast.makeText(this, "Элемент отсутствует", Toast.LENGTH_SHORT).show();
                enterFloat.setText("");
                enterText.setText("");
                return;
            }
            enterFloat.setText(String.valueOf(st.f));
            enterText.setText(st.t);
            Toast.makeText(this, "Элемент получен", Toast.LENGTH_SHORT).show();
        }
        catch(Exception exc)
        {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void updateTable(View view)
    {
        try
        {
            if(enterID.getText().toString().isEmpty() ||
                    enterFloat.getText().toString().isEmpty() ||
                    enterText.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Необходимо заполнить все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            db.updateTable(enterID.getText().toString(), enterFloat.getText().toString(), enterText.getText().toString());
            Toast.makeText(this, "Значение обновлено", Toast.LENGTH_SHORT).show();
            enterID.setText("");
            enterFloat.setText("");
            enterText.setText("");
        }
        catch(Exception exc)
        {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteTable (View view)
    {
        try
        {
            if(enterID.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Заполните ID", Toast.LENGTH_SHORT).show();
                return;
            }

            db.deleteTable(enterID.getText().toString());
            Toast.makeText(this, "Значение удалено", Toast.LENGTH_SHORT).show();
            enterID.setText("");
            enterFloat.setText("");
            enterText.setText("");
        }
        catch(Exception exc)
        {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}