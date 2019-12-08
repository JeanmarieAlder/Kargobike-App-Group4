package com.example.kargobikeappg4.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.kargobikeappg4.R;

import java.lang.reflect.Field;
import java.util.Calendar;

public class MonthlyReportActivity extends AppCompatActivity {

    private TextView DisplayStartDate;
    private TextView DisplayEndDate;
    private TextView ClickedTv;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_report);
        DisplayStartDate = findViewById(R.id.monthlyReport_tv_calendar);
        DisplayEndDate = findViewById(R.id.monthlyReport_tv_calendar_2);

        DisplayStartDate.setOnClickListener(v -> SelectDate(v));
        DisplayEndDate.setOnClickListener(v -> SelectDate(v));

        onDateSetListener = (view, year, month, day) -> {
            month = month+1;
            String date = day + "/" + month + "/" + year;
            ClickedTv.setText(date);
        };

    }

    private void SelectDate(View v) {
        ClickedTv = findViewById(v.getId());
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                android.R.style.Theme_DeviceDefault_Dialog_MinWidth,
                onDateSetListener,
                year,month, day);


        dialog.show();
    }
}
