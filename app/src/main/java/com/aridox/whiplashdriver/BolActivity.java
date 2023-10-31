package com.aridox.whiplashdriver;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.TimePickerDialog;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BolActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String PHONE_NUMBER = "2536064557";

    Button submitrossbtn;
    ImageView imageView2;
    EditText storeedit, driveredit, palletinedit, palletoutedit;
    TextView stoptxt, arrivaltxt, departuretxt, storecounttxt, drivercounttxt, palletintxt, palletouttxt;
    EditText arrivaltime, departureedit;
    Spinner runnumberspinner;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bol);

        runnumberspinner = findViewById(R.id.runnumberspinner);
        arrivaltime = findViewById(R.id.arrivaledit);
        departureedit = findViewById(R.id.departureedit);
        radioGroup = findViewById(R.id.radioGroup);
        storeedit = findViewById(R.id.storeedit);
        driveredit = findViewById(R.id.driveredit);
        palletinedit = findViewById(R.id.palletinedit);
        palletoutedit = findViewById(R.id.palletoutedit);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.run_number_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        runnumberspinner.setAdapter(adapter);

        departureedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(departureedit);
            }
        });

        arrivaltime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(arrivaltime);
            }
        });

        submitrossbtn = findViewById(R.id.submitrossbtn);
        submitrossbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    public void showTimePickerDialog(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String selectedTime = timeFormat.format(calendar.getTime());

                editText.setText(selectedTime);
            }
        }, hourOfDay, minute, false);
        timePickerDialog.show();
    }

    private void sendMessage() {
        if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            sendSMS();
        } else {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMS();
            }
        }
    }

    private void sendSMS() {
        String selectedRunNumber = runnumberspinner.getSelectedItem().toString();

        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);

        int radioButtonCount = radioGroup.getChildCount();
        int currentRadioButtonIndex = radioGroup.indexOfChild(selectedRadioButton);
        int nextRadioButtonIndex = (currentRadioButtonIndex + 1) % radioButtonCount;

        RadioButton nextRadioButton = (RadioButton) radioGroup.getChildAt(nextRadioButtonIndex);

        if (nextRadioButton != null) {
            nextRadioButton.setChecked(true);
        } else {
            RadioButton firstRadioButton = (RadioButton) radioGroup.getChildAt(0);
            firstRadioButton.setChecked(true);
        }

        String selectedArrivalTime = arrivaltime.getText().toString();
        String selectedDepartureTime = departureedit.getText().toString();
        String selectedDriverCount = driveredit.getText().toString();
        String selectedStore = storeedit.getText().toString();
        String selectedPalletIn = palletinedit.getText().toString();
        String selectedPalletOut = palletoutedit.getText().toString();

        String selectedRadioButtonText = ((RadioButton) findViewById(selectedRadioButtonId)).getText().toString();

        String message = "Ross " + selectedRunNumber + " Stop " + selectedRadioButtonText
                + "\nArrival: " + selectedArrivalTime
                + "\nDeparture: " + selectedDepartureTime
                + "\nStore: " + selectedStore
                + "\nDriver: " + selectedDriverCount
                + "\nPallet In: " + selectedPalletIn
                + "\nPallet Out: " + selectedPalletOut
                + "\nSent by the Whiplash Driver App";

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(PHONE_NUMBER, null, message, null, null);

        arrivaltime.setText("");
        departureedit.setText("");
        storeedit.setText("");
        driveredit.setText("");
        palletinedit.setText("");
        palletoutedit.setText("");
    }
}
