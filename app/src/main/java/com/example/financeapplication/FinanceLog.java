package com.example.financeapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FinanceLog extends AppCompatActivity {
    private final int REQ_CODE = 100;
    DBHelper mDatabase;

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
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to Speak");
                try {
                    startActivityForResult(intent, REQ_CODE);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(), "device not supported",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDatabase = new DBHelper(this);
        mDatabase.printCategories();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d("sirine", result.get(0).toString());
                    parseText(result.get(0).toString());
                    //textView.setText(result.get(0));
                }
                break;
            }
        }
    }

    public void parseText(String result) {
        String[] words = result.split(" ");
        int atIndex = 0;
        int spentIndex = 0;
        boolean today = false;
        boolean yesterday = false;
        String date;

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
        TextView dateView = findViewById(R.id.date);
        if(yesterday) {
            date = getDateString(yesterday()).split(" ")[0];
            dateView.setText(date);
        } else if(today) {
            date = getDateString(today()).split(" ")[0];
            dateView.setText(date);
        }


    }
    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
    private String getDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(date);
    }
    private Date today() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        return cal.getTime();
    }




}
