package com.example.kargobikeappg4.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.kargobikeappg4.R;

import java.util.Calendar;

public class MonthlyReportActivity extends AppCompatActivity {

    private TextView DisplayDate;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_report);
        DisplayDate = findViewById(R.id.monthlyReport_tv_calendar);

        DisplayDate.setOnClickListener(v -> SelectDate());

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d("slt","Hello");
            }
        };

    }

    private void SelectDate() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Dialog_MinWidth,
                onDateSetListener,
                month,day, year);

        dialog.show();
    }
}
