package com.example.financeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

import static com.example.financeapplication.DBHelper.printCursor;


public class MainActivity extends AppCompatActivity {
    private final int REQ_CODE = 100;
    TextView textView;
    DBHelper mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = new DBHelper(this);
        new Query(Contract.Expenses.TABLE_NAME).execute();
    }


    public void toLog(View view) {
        Intent intent = new Intent(this, FinanceLog.class);
        startActivity(intent);
    }

    public void toBudget(View view) {
        Intent intent = new Intent(this, Budget.class);
        startActivity(intent);
    }

    public void toHistory(View view) {
        Intent intent = new Intent(this, History.class);
        startActivity(intent);
    }

    public static void log(String string) {
        Log.d("FINANCE_APP", string);
    }

    public class Query extends AsyncTask<Void, Void, Cursor> {
        String mTableName;

        public Query(String name) {
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
        protected Cursor doInBackground(Void... voids) {
            SQLiteDatabase db = mDatabase.getReadableDatabase();
//            String[] projection = null;
//            String selection = null;
//            String[] selectionArgs = null;
//            String sortOrder = null;
            return db.rawQuery("Select category, sum(amount) from " + Contract.Expenses.TABLE_NAME + " Group by " + Contract.Expenses.COLUMN_NAME_CATEGORY, null);
        }
    }

}
