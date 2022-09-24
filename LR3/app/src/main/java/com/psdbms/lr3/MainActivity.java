package com.psdbms.lr3;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    public TextView abs, nm, pth, rdwrt, extrnl;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        gfd = findViewById(R.id.gfd);
//        gcd = findViewById(R.id.gcd);
//        gefd = findViewById(R.id.gefd);
//        gecd = findViewById(R.id.gecd);
//        gesd = findViewById(R.id.gesd);
//        gespd = findViewById(R.id.gespd);

    }
    public void btngfd(View view){
          File file = getFilesDir();

          abs = findViewById(R.id.abs);
          nm = findViewById(R.id.nm);
          pth = findViewById(R.id.pth);
          rdwrt = findViewById(R.id.rdwrt);
          extrnl = findViewById(R.id.extrnl);

          abs.setText(String.format("Absolute: %s",file.getAbsolutePath()));
          nm.setText(String.format("Name: %s", file.getName()));
          pth.setText(String.format("Path: %s", file.getPath()));
          rdwrt.setText(String.format("Read/Write: %s", file.canRead(), file.canWrite()));
          extrnl.setText(String.format("External: %s", Environment.getExternalStorageState()));
    }

    public void btngcd(View view){
        File file = getCacheDir();

        abs = findViewById(R.id.abs);
        nm = findViewById(R.id.nm);
        pth = findViewById(R.id.pth);
        rdwrt = findViewById(R.id.rdwrt);
        extrnl = findViewById(R.id.extrnl);

        abs.setText(String.format("Absolute: %s", file.getAbsolutePath()));
        nm.setText(String.format("Name: %s", file.getName()));
        pth.setText(String.format("Path: %s", file.getPath()));
        rdwrt.setText(String.format("Read/Write: %s", file.canRead(), file.canWrite()));
        extrnl.setText(String.format("External: %s", Environment.getExternalStorageState()));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void btngefd(View view){
        File file = super.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        abs = findViewById(R.id.abs);
        nm = findViewById(R.id.nm);
        pth = findViewById(R.id.pth);
        rdwrt = findViewById(R.id.rdwrt);
        extrnl = findViewById(R.id.extrnl);

        abs.setText(String.format("Absolute: %s", file.getAbsolutePath()));
        nm.setText(String.format("Name: %s", file.getName()));
        pth.setText(String.format("Path: %s", file.getPath()));
        rdwrt.setText(String.format("Read/Write: %s", file.canRead(), file.canWrite()));
        extrnl.setText(String.format("External: %s", Environment.getExternalStorageState()));
    }

    public void btngecd(View view){
        File file = getExternalCacheDir();

        abs = findViewById(R.id.abs);
        nm = findViewById(R.id.nm);
        pth = findViewById(R.id.pth);
        rdwrt = findViewById(R.id.rdwrt);
        extrnl = findViewById(R.id.extrnl);

        abs.setText(String.format("Absolute: %s", file.getAbsolutePath()));
        nm.setText(String.format("Name: %s", file.getName()));
        pth.setText(String.format("Path: %s", file.getPath()));
        rdwrt.setText(String.format("Read/Write: %s", file.canRead(), file.canWrite()));
        extrnl.setText(String.format("External: %s", Environment.getExternalStorageState()));
    }

    public void btngesd(View view){
        File file = Environment.getExternalStorageDirectory();

        String m = Environment.getExternalStorageState();

        if(Environment.MEDIA_MOUNTED.equals(m))
        {
            Log.d("LR1", "mounted");
        }
        else
        {
            Log.d("LR1", m);
        }

        abs = findViewById(R.id.abs);
        nm = findViewById(R.id.nm);
        pth = findViewById(R.id.pth);
        rdwrt = findViewById(R.id.rdwrt);
        extrnl = findViewById(R.id.extrnl);

        abs.setText(String.format("Absolute: %s", file.getAbsolutePath()));
        nm.setText(String.format("Name: %s", file.getName()));
        pth.setText(String.format("Path: %s", file.getPath()));
        rdwrt.setText(String.format("Read/Write: %s", file.canRead(), file.canWrite()));
        extrnl.setText(String.format("External: %s", Environment.getExternalStorageState()));
    }

    public void btngespd(View view){
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        String m = Environment.getExternalStorageState();

        if(Environment.MEDIA_MOUNTED.equals(m))
        {
            Log.d("LR1", "mounted");
        }
        else
        {
            Log.d("LR1", m);
        }

        abs = findViewById(R.id.abs);
        nm = findViewById(R.id.nm);
        pth = findViewById(R.id.pth);
        rdwrt = findViewById(R.id.rdwrt);
        extrnl = findViewById(R.id.extrnl);

        abs.setText(String.format("Absolute: %s", file.getAbsolutePath()));
        nm.setText(String.format("Name: %s", file.getName()));
        pth.setText(String.format("Path: %s", file.getPath()));
        rdwrt.setText(String.format("Read/Write: %s", file.canRead(), file.canWrite()));
        extrnl.setText(String.format("External: %s", Environment.getExternalStorageState()));
    }
}