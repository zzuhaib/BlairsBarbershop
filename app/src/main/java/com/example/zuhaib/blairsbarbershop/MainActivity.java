package com.example.zuhaib.blairsbarbershop;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView textViewDate;
    private TextView textViewTime;
    private DatePickerDialog.OnDateSetListener mDisplaySetListener;
    private EditText editTextName;
    private Button submitButton;
    String name, date, time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewDate = (TextView) findViewById(R.id.textview_date);
        textViewTime = (TextView) findViewById(R.id.textview_time);
        editTextName = (EditText) findViewById(R.id.edittext_name);
        submitButton = (Button) findViewById(R.id.button_schedule);

        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDisplaySetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        textViewTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog
                        (MainActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener(){
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute){
                                String am_pm;
                                if (selectedHour > 12) {
                                    selectedHour -= 12;
                                    am_pm = "PM";
                                } else if (selectedHour == 0) {
                                    selectedHour += 12;
                                    am_pm = "AM";
                                } else if (selectedHour == 12){
                                    am_pm = "PM";
                                }else{
                                    am_pm = "AM";
                                }
                                String time = selectedHour + ":" + selectedMinute + " " + am_pm;
                                textViewTime.setText(time);
                            }
                        }, hour, minute, false);
                mTimePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mTimePicker.show();

            }
        });

        mDisplaySetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                String date = month + "/" + dayOfMonth + "/" + year;
                textViewDate.setText(date);
            }
        };

        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                name = editTextName.getText().toString();
                date = textViewDate.getText().toString();
                time = textViewTime.getText().toString();
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"email"});
                i.putExtra(Intent.EXTRA_SUBJECT, name + " needs to get fresh");
                i.putExtra(Intent.EXTRA_TEXT   , name + date + time);
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showToast(String text){
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}