package com.example.financeapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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


import java.util.ArrayList;
import java.util.Locale;

public class FinanceLog extends AppCompatActivity {
    private final int REQ_CODE = 100;
    DBHelper mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MainActivity.log("FinanceLog onCreate Called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_layout);
        mDatabase = new DBHelper(this);
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

    }

    private class Inserter extends AsyncTask<ContentValues, Void, Cursor> {
        String mTableName;

        public Inserter(String name) {
            mTableName = name;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor == null || cursor.getCount() == 0) {
                Toast.makeText(FinanceLog.this, "NO DATA", Toast.LENGTH_SHORT).show();
                return;
            }

            printCursor(cursor);
        }

        @Override
        protected Cursor doInBackground(ContentValues... contentValues) {
            SQLiteDatabase db = mDatabase.getWritableDatabase();
//            db.execSQL("Delete From " + mTableName);
            for (ContentValues contentValues1 : contentValues) {
                db.insert(mTableName, null, contentValues1);
            }

            db = mDatabase.getReadableDatabase();
            String[] projection = null;
            String selection = null;
            String[] selectionArgs = null;
            String sortOrder = null;
            return db.query(mTableName,
                    projection, selection, selectionArgs,
                    null, null, sortOrder);
        }
    }

    private void printCursor(Cursor cursor) {
        int columnCount = cursor.getColumnCount();
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < columnCount; ++i) {
            line.append(cursor.getColumnName(i));
            if (i < columnCount - 1) {
                line.append("\t");
            }
        }

        MainActivity.log(line.toString());

        if (!cursor.moveToNext()) {
            MainActivity.log("NOTHING TO SHOW");
        } else {
            line = new StringBuilder();
            for (int i = 0; i < columnCount; ++i) {
                switch (cursor.getType(i)) {
                    case Cursor.FIELD_TYPE_INTEGER:
                        line.append(cursor.getInt(i));
                        break;
                    case Cursor.FIELD_TYPE_STRING:
                        line.append(cursor.getString(i));
                        break;
                    default:
                        // do nothing
                        break;
                }
                if (i < columnCount - 1) {
                    line.append("\t");
                }
            }
            MainActivity.log(line.toString());


        }

        while (cursor.moveToNext()) {
            line = new StringBuilder();
            for (int i = 0; i < columnCount; ++i) {
                switch (cursor.getType(i)) {
                    case Cursor.FIELD_TYPE_INTEGER:
                        line.append(cursor.getInt(i));
                        break;
                    case Cursor.FIELD_TYPE_STRING:
                        line.append(cursor.getString(i));
                        break;
                    default:
                        // do nothing
                        break;
                }
                if (i < columnCount - 1) {
                    line.append("\t");
                }
            }
            MainActivity.log(line.toString());


        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQ_CODE: {
                if(resultCode == RESULT_OK && null != data) {
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

        for(int i = 0; i < words.length; i++) {
            if(words[i].equals("at")) {
                atIndex = i;
            } else if (words[i].equals("spent")) {
                spentIndex = i;
            } else if(words[i].equals("today")) {
                today = true;
            } else if(words[i].equals("yesterday")) {
                yesterday = true;
            }
        }
        TextView expense = findViewById(R.id.expense);
        TextView amount = findViewById(R.id.amount);
        expense.setText(words[atIndex + 1]);
        amount.setText(words[spentIndex + 1]);



    }

}
