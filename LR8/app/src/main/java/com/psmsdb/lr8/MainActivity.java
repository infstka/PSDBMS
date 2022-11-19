package com.psmsdb.lr8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Note.NotesDeserialization(getFilesDir());
        Note.CategoriesDeserialization(getFilesDir());

        CalendarView cv = findViewById(R.id.calendarView);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                int Year = year;
                int Month = month;
                int Day = day;
                String choosenDate = "";

                //добавление 0 перед числом, меньше чем 10 (01.хх.хххх - 09.хх.хххх)
                if(day / 10 == 0)
                {
                    choosenDate = new StringBuilder().append("0").append(Day).append(".").append(Month+1).append(".").append(Year).toString();
                }

                //остальные дни
                else
                {
                    choosenDate = new StringBuilder().append(Day).append(".").append(Month+1).append(".").append(Year).toString();
                }

                chsnDate = choosenDate;
            }
        });
    }

    private String chsnDate;

    public void zad(View view)
    {
        Intent intnt = new Intent(this, Notes.class);
        intnt.putExtra("ChoosenDate", chsnDate);
        startActivity(intnt);
    }

    public void category(View view)
    {
        Intent intnt = new Intent(this, Categories.class);
        startActivity(intnt);
    }
}



