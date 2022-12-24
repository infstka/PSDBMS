package com.psmsdb.lr17;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {



    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //включает все правила в политике
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
        SQLDB sqldb = new SQLDB();
        sqldb.execute("");

        Button getData = findViewById(R.id.button7);
        getData.setOnClickListener(view ->
                {
                    textView = findViewById(R.id.output);
                    textView.setText(sqldb.Query());
                });

        Button procedure = findViewById(R.id.button2);
        procedure.setOnClickListener(view ->
        {
            textView = findViewById(R.id.output);
            textView.setText(sqldb.Procedure());
        });
        Button function = findViewById(R.id.button3);
        function.setOnClickListener(view ->
        {
            textView = findViewById(R.id.output);
            textView.setText(sqldb.Function());
        });
        Button batch = findViewById(R.id.button4);
        batch.setOnClickListener(view ->
        {
            textView = findViewById(R.id.output);
            textView.setText(sqldb.Batch());
        });
        Button update = findViewById(R.id.button5);
        update.setOnClickListener(view ->
        {
            textView = findViewById(R.id.output);
            textView.setText(sqldb.Update());
        });
        Button delete = findViewById(R.id.button6);
        delete.setOnClickListener(view ->
        {
            textView = findViewById(R.id.output);
            textView.setText(sqldb.Delete());
        });

        Button param = findViewById(R.id.buttonextra);
        param.setOnClickListener(view ->
        {
            textView = findViewById(R.id.output);
            textView.setText(sqldb.QueryParam());
        });
    }
}