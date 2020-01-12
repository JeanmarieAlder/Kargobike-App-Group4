package deploy.example.kargobikeappg4.ui.user;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import deploy.example.kargobikeappg4.R;
import deploy.example.kargobikeappg4.db.entities.WorkDetails;
import deploy.example.kargobikeappg4.viewmodel.workDetails.WorkDetailsListViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MonthlyReportActivity extends AppCompatActivity {

    //Attributes
    private String userId;
    private String DateDuJour =  new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());
    private List<WorkDetails> wdList;
    private TextView DisplayStartDate;
    private TextView DisplayEndDate;
    private TextView DisplayDeliveries;
    private TextView WorkedHours;
    private String DateDebut = DateDuJour;
    private String DateFin = DateDuJour;
    private WorkDetailsListViewModel listViewModel;
    private TextView ClickedTv;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    //On create method, initialize all stuff
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_report);

        userId = getIntent().getStringExtra("userId");

        //Initialize the UI-elements
        DisplayStartDate = findViewById(R.id.monthlyReport_tv_calendar);
        DisplayEndDate = findViewById(R.id.monthlyReport_tv_calendar_2);
        DisplayDeliveries = findViewById(R.id.mr_tv_deliveries);
        DisplayStartDate.setText(DateDuJour);
        DisplayEndDate.setText(DateDuJour);
        WorkedHours = findViewById(R.id.mr_tv_hours);

        DisplayStartDate.setOnClickListener(v -> SelectDate(v));
        DisplayEndDate.setOnClickListener(v -> SelectDate(v));

        //Look for the selected date
        onDateSetListener = (view, year, month, day) -> {
            month = month+1;
            String date = day + "/" + month + "/" + year;
            ClickedTv.setText(date);

            if(ClickedTv == DisplayStartDate)
                DateDebut = date;
            else
                DateFin = date;

            CalculateWorkingHours();
        };

        CalculateWorkingHours();

    }

    //Datepicker
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

    //Getting the workinghours from the DB
    private void CalculateWorkingHours(){

        WorkDetailsListViewModel.Factory factory = new WorkDetailsListViewModel.Factory(
                getApplication(), userId
        );

        listViewModel = ViewModelProviders.of(this, factory)
                .get(WorkDetailsListViewModel.class);
        listViewModel.getAllWorkDetails().observe(this, workDetailsEntities -> {
            if (workDetailsEntities != null) {
                wdList = workDetailsEntities;
                CompareDate(wdList);
            }
        });

    }

    //Calculate the times depending on the date
    private void CompareDate(List<WorkDetails> list){

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        int totalHours = 0;
        int totalMinutes = 0;
        int totalDeliveries=0;

        try {

            Date startDate = format.parse(DateDebut);
            if(DateFin == null){
                DateFin = DateDuJour;
            }
            Date endDate = format.parse(DateFin);
            for (WorkDetails wd: list) {
                Date dateWd = format.parse(wd.getDate());

                if(startDate.compareTo(dateWd) * dateWd.compareTo(endDate) >= 0)
                {

                    String time = wd.getHours();
                    String hours = time.substring(0,2);
                    String minutes = time.substring(3);

                    int intHours = Integer.parseInt(hours);
                    int intMinutes = Integer.parseInt(minutes);

                    totalHours += intHours;
                    totalMinutes += intMinutes;
                    totalDeliveries+=wd.getDeliveries();
                }
            }

            totalHours += totalMinutes/60;
            totalMinutes = totalMinutes%60;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        WorkedHours.setText(totalHours + " heures " + totalMinutes + " minutes");
        DisplayDeliveries.setText(Integer.toString(totalDeliveries));
    }

}
