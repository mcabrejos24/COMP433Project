package com.example.financeapplication;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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
import java.util.List;
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

        Spinner categories = (Spinner) findViewById(R.id.categoryTitle);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FinanceLog.this,
                R.layout.spinner_item,
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
        MainActivity.log("full string: " + result);
        String[] words = result.split(" ");
        int atIndex = 0;
        int spentIndex = 0;
        boolean today = false;
        boolean yesterday = false;
        Date dateobj = new Date();

        for (int i = 0; i < words.length; i++) {
            if (words[i].equals("at") || words[i].equals("on")) {
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
        setSpinner(words[atIndex + 1]);

        if (yesterday) {
            dateobj = yesterday();
        } else if (today) {
            dateobj = today();
        } else {
            dateobj = parseDate(words);
        }
        updateLabel(dateobj);

    }

    public void setSpinner(String entry) {
        Options options = new Options();
        Map<String, String> map = options.getMap();
        Spinner spin = findViewById(R.id.categoryTitle);
        String category = map.get(entry);
        Log.d("sirine", "setSpinner: " + category);
        Log.d("sirine", "at 0: " + spin.getItemAtPosition(0).toString());
        Log.d("sirine", "at 1: " + spin.getItemAtPosition(1).toString());
        Log.d("sirine", "at 2: " + spin.getItemAtPosition(2).toString());
        Log.d("sirine", "at 3: " + spin.getItemAtPosition(3).toString());
        Log.d("sirine", "at 4: " + spin.getItemAtPosition(4).toString());
        if (category != null) {
            MainActivity.log(category);
            switch (category) {
                case "Transportation":
                    spin.setSelection(1);
                    break;
                case "Groceries":
                    spin.setSelection(2);
                    break;
                case "Restaurants/Bars":
                    spin.setSelection(3);
                    break;
                case "Bills":
                    spin.setSelection(4);
                    break;
                case "Misc":
                    spin.setSelection(0);
                    break;
            }
        } else {
            spin.setSelection(0);
        }
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
        String dayStr = words[words.length - 1];
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
        Log.d("sirine", "category: " + Category);
        TextView expense = findViewById(R.id.expense);
        String expenseName = expense.getText().toString();
        Log.d("sirine", "expense: " + expenseName);
        TextView amount = findViewById(R.id.amount);
        Integer amt = 0;
        if (!amount.getText().toString().isEmpty()) {
            amt = (int) Double.parseDouble(amount.getText().toString().replace("$", ""));
        }
        Log.d("sirine", "amount: " + amt);
        TextView date = findViewById(R.id.date);
        String date_string = date.getText().toString();
        Log.d("sirine", "date: " + date_string);

//        Toast.makeText(this, expenseName, Toast.LENGTH_SHORT).show();
        if (expenseName.equals("") || amt == 0 || date_string.equals("")) {
            Toast.makeText(this, "Please Fill in All Boxes", Toast.LENGTH_SHORT).show();
        } else {
            ContentValues p1 = new ContentValues();
            p1.put(Contract.Expenses.COLUMN_NAME_EXPENSE, expenseName);
            p1.put(Contract.Expenses.COLUMN_NAME_CATEGORY, Category);
            p1.put(Contract.Expenses.COLUMN_NAME_DATE, date_string);
            p1.put(Contract.Expenses.COLUMN_NAME_AMOUNT, amt);
            new Inserter(Contract.Expenses.TABLE_NAME).execute(p1);


        }
    }

    public void toHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public class Inserter extends AsyncTask<ContentValues, Void, Cursor> {
        String mTableName;

        public Inserter(String name) {
            mTableName = name;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor == null || cursor.getCount() == 0) {
//                Toast.makeText(FinanceLog.this, "NO DATA", Toast.LENGTH_SHORT).show();
                return;
            }
            printCursor(cursor);
        }

        @Override
        protected Cursor doInBackground(ContentValues... contentValues) {
            SQLiteDatabase db = mDatabase.getWritableDatabase();
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

    public static List<String> printCursor(Cursor cursor) {
        List<String> list = new ArrayList<>();
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
            list.add(line.toString());
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
            list.add(line.toString());
        }
        return list;
    }

}
