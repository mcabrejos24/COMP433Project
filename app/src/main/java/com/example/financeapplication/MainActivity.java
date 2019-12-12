package com.example.financeapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
//import android.support.*;
import android.*;

import static com.example.financeapplication.DBHelper.printCursor;


public class MainActivity extends AppCompatActivity {
    private final int REQ_CODE = 100;
    TextView textView;
    DBHelper mDatabase;
    List<SliceValue> pieData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = new DBHelper(this);
//        new Query(Contract.Expenses.TABLE_NAME).execute();




    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        PieChartView pieChartView = findViewById(R.id.chart);
        pieData = new ArrayList<>();
        PieChartData pieChartData = new PieChartData(pieData);
        pieChartView.setPieChartData(pieChartData);
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
            log("on post execute -- should update colors");
            if (cursor == null || cursor.getCount() == 0) {
//                Toast.makeText(FinanceLog.this, "NO DATA", Toast.LENGTH_SHORT).show();
                return;
            }
            PieChartView pieChartView = findViewById(R.id.chart);
            int[] colors = {Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED, Color.LTGRAY};
            int colorpicker = 0;
            while (cursor.moveToNext()) {
//                MainActivity.log(cursor.getString(0));
//                MainActivity.log("num " + cursor.getInt(1));
                pieData.add(new SliceValue(cursor.getInt(1), colors[colorpicker]).setLabel(cursor.getString(0) + ": $" + cursor.getInt(1)));
               colorpicker++;
            }
            PieChartData pieChartData = new PieChartData(pieData);
            pieChartData.setHasLabels(true);
            pieChartView.setPieChartData(pieChartData);
//            printCursor(cursor);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            SQLiteDatabase db = mDatabase.getReadableDatabase();
//            String[] projection = null;
//            String selection = null;
//            String[] selectionArgs = null;
//            String sortOrder = null;
            return db.rawQuery("Select category, sum(amount) from " + Contract.Expenses.TABLE_NAME + " Group by " + Contract.Expenses.COLUMN_NAME_CATEGORY, null);

//            return db.rawQuery("Select * from " + Contract.Expenses.TABLE_NAME + " Order by " + Contract.Expenses.COLUMN_NAME_DATE + " Desc", null);
        }
    }

}
