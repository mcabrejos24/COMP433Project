package com.example.financeapplication;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FinanceLog extends AppCompatActivity {
    private final int REQ_CODE = 100;
    DBHelper mDatabase;
    TextView dateEntry;
    final Calendar myCalendar = Calendar.getInstance();
    EditText edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MainActivity.log("FinanceLog onCreate Called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_layout);
        ImageView speak = findViewById(R.id.speak);
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        "How much did you spend? For example: \n" +
                                "\"I spent {amount} dollars at {location} on {date}.\"");
                try {
                    startActivityForResult(intent, REQ_CODE);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(), "device not supported",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDatabase = new DBHelper(this);
//        mDatabase.printCategories();

        Spinner categories = (Spinner) findViewById(R.id.categoryTitle);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FinanceLog.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.categories));
        categories.setAdapter(adapter);
       // dateEntry = findViewById(R.id.date);
       edittext = findViewById(R.id.date);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar.getTime());
            }

        };
        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(FinanceLog.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



    }
    private void updateLabel(Date d) {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edittext.setText(sdf.format(d));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    parseText(result.get(0).toString());
                    //textView.setText(result.get(0));
                }
                break;
            }
        }
    }



    public void parseText(String result) {
        Log.d("sirine", "full string: " + result);
        String[] words = result.split(" ");
        int atIndex = 0;
        int spentIndex = 0;
        boolean today = false;
        boolean yesterday = false;
        Date dateobj = new Date();

        for (int i = 0; i < words.length; i++) {
            if (words[i].equals("at")) {
                atIndex = i;
            } else if (words[i].equals("spent")) {
                spentIndex = i;
            } else if (words[i].equals("today")) {
                today = true;
            } else if (words[i].equals("yesterday")) {
                yesterday = true;
            }
        }
        TextView expense = findViewById(R.id.expense);
        TextView amount = findViewById(R.id.amount);
        expense.setText(words[atIndex + 1]);
        amount.setText(words[spentIndex + 1]);

        if(yesterday) {
            dateobj = yesterday();
        } else if(today) {
            dateobj = today();
        } else {
            dateobj = parseDate(words);
        }
        updateLabel(dateobj);
    }
    public Date parseDate(String[] words) {
        String month = words[words.length - 2];
        int m = 0;
        switch (month) {
            case "January":
                m = 0;
                break;
            case "February":
                m = 1;
                break;
            case "March":
                m = 2;
                break;
            case "April":
                m = 3;
                break;
            case "May":
                m = 4;
                break;
            case "June":
                m = 5;
                break;
            case "July":
                m = 6;
                break;
            case "August":
                m = 7;
                break;
            case "September":
                m = 8;
                break;
            case "October":
                m = 9;
                break;
            case "November":
                m = 10;
                break;
            case "December":
                m = 11;
                break;
        }
        String dayStr = words[words.length-1];
        dayStr = dayStr.substring(0, dayStr.length() - 2);
        int day = Integer.parseInt(dayStr);
        Calendar cal = Calendar.getInstance();
        Date d = new Date();
        cal.set(d.getYear(), m, day, 0, 0);
        return cal.getTime();

    }
    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    private Date today() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        return cal.getTime();
    }

    public void onSubmit(View view) {
        Spinner categories = (Spinner) findViewById(R.id.categoryTitle);
        String Category = categories.getSelectedItem().toString();
        TextView expense = findViewById(R.id.expense);
        Log.d("sirine","expense: " + expense.getText().toString());
        TextView amount = findViewById(R.id.amount);
        Log.d("sirine","amount: " + amount.getText().toString());
        TextView date = findViewById(R.id.date);
        Log.d("sirine","date: " + date.getText().toString());
    }

    public void toHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
