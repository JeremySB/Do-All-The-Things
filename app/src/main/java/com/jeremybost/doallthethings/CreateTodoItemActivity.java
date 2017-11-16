package com.jeremybost.doallthethings;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.jeremybost.doallthethings.models.TodoItem;

import java.util.Calendar;
import java.util.Date;

public class CreateTodoItemActivity extends AppCompatActivity {
    private Button addItemBtn;
    private EditText itemName;
    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo_item);

        addItemBtn = (Button) findViewById(R.id.addItemBtn);
        itemName = (EditText) findViewById(R.id.itemName);
        datePicker = (DatePicker) findViewById(R.id.itemDatePicker);

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TodoItem item = new TodoItem();
                item.setName(itemName.getText().toString());
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, datePicker.getYear());
                cal.set(Calendar.MONTH, datePicker.getMonth());
                cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                Date date = cal.getTime();
                item.setDueDate(date);

                TodoItemRepository.getInstance().addItem(item);

                finish();
            }
        });
    }
}
