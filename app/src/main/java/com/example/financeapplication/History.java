package com.example.financeapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.financeapplication.DBHelper.printCursor;

public class History extends AppCompatActivity {
    DBHelper mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);
        mDatabase = new DBHelper(this);
        new History.Query(Contract.Expenses.TABLE_NAME).execute();
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
           // printCursor(cursor);



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
