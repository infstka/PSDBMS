package com.psdbms.lr5;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MainActivity extends AppCompatActivity {

    String FILE_NAME = "LR5.txt";
    EditText key1, key2, value1, value2;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        key1 = findViewById(R.id.enterkey1);
        value1 = findViewById(R.id.entervalue1);
        key2 = findViewById(R.id.enterkey2);
        value2 = findViewById(R.id.entervalue2);

        fileCheck();
    }

    //проверка существования файла
    public void fileCheck()
    {
        file = new File(super.getFilesDir(), FILE_NAME);
        if (!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException ioexc)
            {
                Toast.makeText(this, ioexc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //сохранение ключа и значения (метод)
    public static void save(String firstkey, String firstvalue, File file)
    {
        try
        {
            while(firstvalue.length() != 10)
            {
                firstvalue += " ";
            }

            while(firstkey.length() != 5)
            {
                firstkey += " ";
            }

            RandomAccessFile raf = new RandomAccessFile(file, "rw");

            int readHash = 0, position = 0, hash = getHash(firstkey);

            do
            {
                raf.seek(position);
                readHash = raf.read() - 48;

                if(readHash == -49)
                {
                    raf.seek(raf.length());
                    raf.writeBytes(String.valueOf(hash) + firstkey + firstvalue + "     ");
                }

                if(!(readHash == hash))
                {
//                    position += 21;
                }

                else
                {
                    byte[] readKey = new byte[5];
                    raf.read(readKey);
                    String readKeyStr = new String(readKey);

                    //перезапись значения ключа
                    if(readKeyStr.equals(firstkey))
                    {
                        raf.writeBytes(firstvalue);
                        break;
                    }

                    else
                    {
                        raf.skipBytes(10);
                        byte[] readLink = new byte[5];
                        raf.read(readLink);
                        String readLinkStr = new String(readLink);

                        if(readLinkStr.equals("     "))
                        {
                            long link = raf.length();
                            String linkStr = String.valueOf(link);

                            while(linkStr.length() != 5)
                            {
                                linkStr += " ";
                            }

                            raf.seek(raf.getFilePointer() - 5);
                            raf.writeBytes(linkStr);
                            raf.seek(raf.length());
                            String message = String.valueOf(hash) + firstkey + firstvalue + "     ";
                            raf.writeBytes(message);
                            break;
                        }

                        else
                        {
                            readLinkStr = readLinkStr.replace(" ", "");
                            position = Integer.parseInt(readLinkStr);
                        }
                    }
                }
            }
            while(readHash != -49);

            raf.close();
        }

        catch(Exception exс)
        {
            Log.d("save: ", exс.getMessage());
        }
    }

    //получение значения по ключу
    public static String getValue(String secondkey, File file)
    {
        String value = "";
        try
        {
            while(secondkey.length() != 5)
            {
                secondkey += " ";
            }

            RandomAccessFile raf = new RandomAccessFile(file, "rw");

            int readHash = 0, hash = getHash(secondkey), position = 0;

            do
            {
                raf.seek(position);
                readHash = raf.read() - 48;

                if(!(readHash == hash))
                {
                    //position = position + 21;
                }

                else
                {
                    byte[] readKey = new byte[5];
                    raf.read(readKey);
                    String readKeyStr = new String(readKey);

                    if(readKeyStr.equals(secondkey))
                    {
                        byte[] readValue = new byte[10];
                        raf.read(readValue);
                        value = new String(readValue);
                        break;
                    }

                    else
                    {
                        raf.skipBytes(10);
                        byte[] readLink = new byte[5];
                        raf.read(readLink);
                        String readLinkStr = new String(readLink);

                        if(readLinkStr.equals("     "))
                        {
                            value = "";
                            break;
                        }

                        else
                        {
                            readLinkStr = readLinkStr.replace(" ", "");
                            position = Integer.parseInt(readLinkStr);
                        }
                    }
                }
            }

            while(readHash != -49);
            raf.close();
        }
        catch(Exception exс)
        {
            Log.d("get: ", exс.getMessage());
        }

        value = value.replace(" ", "");
        return value;
    }

    //получение последнего символа
    private static int getLastChar(int number)
    {
        if(number / 10 == 0)
        {
            return number;
        }

        return getLastChar(number / 10);
    }

    //получение хэша
    private static int getHash(String input)
    {
        int hashCode = input.hashCode();
        int output = getLastChar(hashCode);

        if(output < 0)
        {
            output *= -1;
        }

        return output;
    }

    //добавление ключа и значения
    public void add(View view)
    {
        try
        {
            if (value1.getText().toString().isEmpty() || key1.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Enter key & value", Toast.LENGTH_SHORT).show();
                return;
            }

            String firstvalue = String.valueOf(value1.getText());
            String firstkey = String.valueOf(key1.getText());

            save(firstkey, firstvalue, file);
            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
        }

        catch (Exception exc)
        {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //получение значения
    public void get(View view)
    {
        try
        {
            if (key2.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Enter key", Toast.LENGTH_SHORT).show();
                return;
            }

            String secondkey = String.valueOf(key2.getText());
            String secondvalue = getValue(secondkey, file);

            if (secondvalue == "")
            {
                Toast.makeText(this, "Value not found", Toast.LENGTH_SHORT).show();
            }

            else
            {
                Toast.makeText(this, "Value found", Toast.LENGTH_SHORT).show();
            }

            value2.setText(secondvalue);
        }

        catch (Exception exc)
        {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}