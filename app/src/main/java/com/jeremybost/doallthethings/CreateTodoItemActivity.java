package com.jeremybost.doallthethings;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnFailureListener;
import com.jeremybost.doallthethings.models.TodoItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class CreateTodoItemActivity extends AppCompatActivity {
    private EditText itemName;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener dateListener;
    private TimePickerDialog.OnTimeSetListener timeListener;
    private Location lastLocation;
    TextView dueDateBtn;
    TextView dueTimeBtn;
    private EditText reminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo_item);

        Toolbar toolbar = findViewById(R.id.createToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        itemName = findViewById(R.id.itemName);
        dueDateBtn = findViewById(R.id.dueDateBtn);
        dueTimeBtn = findViewById(R.id.dueTimeBtn);
        reminder = findViewById(R.id.reminderMinutes);

        itemName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        myCalendar = Calendar.getInstance();

        dueDateBtn.setText(SimpleDateFormat.getDateInstance().format(myCalendar.getTime()));
        dueTimeBtn.setText(SimpleDateFormat.getTimeInstance().format(myCalendar.getTime()));

        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                dueDateBtn.setText(SimpleDateFormat.getDateInstance().format(myCalendar.getTime()));
            }
        };

        timeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                myCalendar.set(Calendar.HOUR_OF_DAY, i);
                myCalendar.set(Calendar.MINUTE, i1);

                dueTimeBtn.setText(SimpleDateFormat.getTimeInstance().format(myCalendar.getTime()));
            }
        };


        dueDateBtn.setOnClickListener(v -> {
            new DatePickerDialog(this, dateListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        dueTimeBtn.setOnClickListener(view -> new TimePickerDialog(this, timeListener,
                myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE),
                false).show());

        // get last location from Google Play API

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    lastLocation = location;
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println(e.getMessage());
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.create_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.action_add) {
            TodoItem item = new TodoItem();
            item.setName(itemName.getText().toString());
            item.setDueDate(myCalendar.getTime());
            if(lastLocation != null)
                item.setLocation(lastLocation.getLatitude(), lastLocation.getLongitude());

            if(!reminder.getText().toString().equals(""))
                item.setReminder(Integer.parseInt(reminder.getText().toString()));

            TodoItemRepository.getInstance().addItem(item);

            finish();

            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
