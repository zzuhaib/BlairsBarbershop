package com.example.zuhaib.blairsbarbershop;

import android.app.AlertDialog;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;

import org.json.JSONObject;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.text.DateFormatSymbols;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView textViewDate;
    private TextView textViewTime;
    private DatePickerDialog.OnDateSetListener mDisplaySetListener;
    private EditText editTextName;
    private TextView textViewCalendarEvent;
    private Button submitButton;
    String name, date, time;
    private DateTimeRequest dateTimeRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Initialization
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewDate = (TextView) findViewById(R.id.textview_date);
        textViewTime = (TextView) findViewById(R.id.textview_time);
        editTextName = (EditText) findViewById(R.id.edittext_name);
        submitButton = (Button) findViewById(R.id.button_schedule);
        textViewCalendarEvent = (TextView) findViewById(R.id.textView_GoogleCalendar);

        //Get Date Value
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

        mDisplaySetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                String nameMonth = new DateFormatSymbols().getMonths()[month-1];

                String date = year + "-" + month + "-" + dayOfMonth;
                textViewDate.setText(date);
            }
        };

        //Get Time Value
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
                                String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                                textViewTime.setText(time + " " + am_pm);
                            }
                        }, hour, minute, false);
                mTimePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mTimePicker.show();

            }
        });

        //Submit button to send email to notify barber
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                name = editTextName.getText().toString();
                date = textViewDate.getText().toString();
                time = textViewTime.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            Boolean success = jsonResponse.getBoolean("success");
                            if (success){
                                new SimpleMail().sendEmail("blairsbarbershop@gmail.com", "Hey Blair, " + name +
                                        " scheduled an appointment for " + time + " on " + date, "Be sure to give " + name + " the best haircut possible!");
                                showToast("Appointment scheduled!");
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage("This time has already been booked!")
                                        .setNegativeButton("Please try another time", null)
                                        .create()
                                        .show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                DateTimeRequest dateTimeRequest = new DateTimeRequest(name, date, time, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(dateTimeRequest);
            }
        });

        //Set Google Calendar
        textViewCalendarEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = textViewDate.getText().toString();
                time = textViewTime.getText().toString();

                try {
                    Date fullDate = new SimpleDateFormat("yyyy-MM-dd-HH:mm a").parse(date+"-"+time);
                    Intent intent = new Intent(Intent.ACTION_EDIT);
                    intent.setType("vnd.android.cursor.item/event");
                    intent.putExtra("beginTime", fullDate.getTime());
                    intent.putExtra("allDay", false);
                    intent.putExtra("title", "Haircut");
                    intent.putExtra("endTime", fullDate.getTime()+60*60*1000);
                    startActivity(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        });
    }

    private void showToast(String text){
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}