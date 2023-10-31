package com.aridox.whiplashdriver;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RossPayroll extends AppCompatActivity {
    EditText arrivaltime, startedit, endedit, dateedit;
    Spinner runnumberspinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ross_payroll);

        arrivaltime = findViewById(R.id.payedit);
        runnumberspinner = findViewById(R.id.runnumberspinner);
        startedit = findViewById(R.id.startedit);
        endedit = findViewById(R.id.endedit);
        dateedit = findViewById(R.id.dateedit);

        startedit.setOnClickListener(v -> showTimePickerDialog(startedit));
        endedit.setOnClickListener(v -> showTimePickerDialog(endedit));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.run_number_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        runnumberspinner.setAdapter(adapter);

        arrivaltime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No implementation needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No implementation needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (!text.startsWith("$")) {
                    arrivaltime.setText("$" + text);
                    arrivaltime.setSelection(arrivaltime.getText().length());
                }
            }
        });

        // Set default date to today
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(Calendar.getInstance().getTime());
        dateedit.setText(currentDate);

        dateedit.setOnClickListener(v -> showDatePickerDialog());
    }

    public void showTimePickerDialog(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hour, minute1) -> {
            String selectedTime = convertTo12HourFormat(hour, minute1);
            editText.setText(selectedTime);
        }, hourOfDay, minute, false);
        timePickerDialog.show();
    }

    public void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth1) -> {
            String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", month1 + 1, dayOfMonth1, year1);
            dateedit.setText(selectedDate);
        }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private String convertTo12HourFormat(int hourOfDay, int minute) {
        String amPm;
        if (hourOfDay >= 12) {
            amPm = "PM";
            if (hourOfDay > 12) {
                hourOfDay -= 12;
            }
        } else {
            amPm = "AM";
            if (hourOfDay == 0) {
                hourOfDay = 12;
            }
        }
        return String.format(Locale.getDefault(), "%02d:%02d %s", hourOfDay, minute, amPm);
    }
}
