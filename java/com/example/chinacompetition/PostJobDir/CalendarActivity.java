package com.example.chinacompetition.PostJobDir;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.example.chinacompetition.R;


import java.util.Calendar;

public class CalendarActivity extends Activity {

    private String TAG = "CalendarActivity";

    DateRangeCalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        Log.i(TAG, "onCreate");

        calendarView = findViewById(R.id.calendar);

        // 달력객체를 생성하고, 리스너를 달아준다. 날짜 한개를 선택했을때와, 두개를 선택했을때 실행되는 메소드가 다르다.

        calendarView.setCalendarListener(new DateRangeCalendarView.CalendarListener() {
            @Override
            public void onFirstDateSelected(Calendar startDate) {
                Log.i(TAG, "onFirstDateSelected");
            }

            @Override
            public void onDateRangeSelected(Calendar startDate, Calendar endDate) {
                Log.i(TAG, "onDateRangeSelected");
                int sYear = startDate.get(Calendar.YEAR);
                int sMonth = startDate.get(Calendar.MONTH);
                int sDay = startDate.get(Calendar.DAY_OF_MONTH);
                String sDate = sYear+"-"+sMonth+"-"+sDay;

                int eYear = endDate.get(Calendar.YEAR);
                int eMonth = endDate.get(Calendar.MONTH);
                int eDay = endDate.get(Calendar.DAY_OF_MONTH);
                String eDate = eYear+"-"+eMonth+"-"+eDay;

                Log.i(TAG, "result: "+sDate+"/"+eDate);

                Intent intent = new Intent();
                intent.putExtra("startDate", sDate);
                intent.putExtra("endDate", eDate);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}
