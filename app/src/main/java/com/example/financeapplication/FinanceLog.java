package com.example.financeapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.financeapplication.MainActivity.log;

public class FinanceLog extends AppCompatActivity {
    DBHelper mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        log("FinanceLog onCreate Called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_layout);
        mDatabase = new DBHelper(this);

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

        log(line.toString());

        if (!cursor.moveToNext()) {
            log("NOTHING TO SHOW");
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
            log(line.toString());


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
            log(line.toString());


        }
    }

}
