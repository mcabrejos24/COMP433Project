package com.example.financeapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    DBHelper mDatabase;

    public DBHelper(@Nullable Context context) {
        super(context, "financeapp.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        MainActivity.log("oncreate called");
        mDatabase = this;
        sqLiteDatabase.execSQL(Contract.CategoriesTable.CREATE);
        sqLiteDatabase.execSQL(Contract.TagsTable.CREATE);
        insertInitialCategories();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        MainActivity.log("onUpgrade called");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "
                + Contract.CategoriesTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "
                + Contract.TagsTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertInitialCategories() {
        MainActivity.log("insert initial categories");
        mDatabase = this;
        ContentValues p1 = new ContentValues();
        p1.put(Contract.CategoriesTable.COLUMN_NAME_NAME, "Transportation");
        p1.put(Contract.CategoriesTable.COLUMN_NAME_BUDGET_AMOUNT, 100);
        ContentValues p2 = new ContentValues();
        p2.put(Contract.CategoriesTable.COLUMN_NAME_NAME, "Groceries");
        p2.put(Contract.CategoriesTable.COLUMN_NAME_BUDGET_AMOUNT, 200);
        ContentValues p3 = new ContentValues();
        p3.put(Contract.CategoriesTable.COLUMN_NAME_NAME, "Restaurants/Bars");
        p3.put(Contract.CategoriesTable.COLUMN_NAME_BUDGET_AMOUNT, 50);
        ContentValues p4 = new ContentValues();
        p4.put(Contract.CategoriesTable.COLUMN_NAME_NAME, "Misc.");
        p4.put(Contract.CategoriesTable.COLUMN_NAME_BUDGET_AMOUNT, 14);
        ContentValues p5 = new ContentValues();
        p5.put(Contract.CategoriesTable.COLUMN_NAME_NAME, "Bills");
        p5.put(Contract.CategoriesTable.COLUMN_NAME_BUDGET_AMOUNT, 400);
        new Inserter(Contract.CategoriesTable.TABLE_NAME).execute(p1, p2, p3, p4, p5);
    }

    public void insertContent(ContentValues p1) {
        MainActivity.log("inserting new value");
        mDatabase = this;
        new Inserter(Contract.CategoriesTable.TABLE_NAME).execute(p1);

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

    public void printCategories() {
        mDatabase = this;
        new printCategoriesAsync().execute();
    }

    private class printCategoriesAsync extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected void onPostExecute(Cursor cursor) {
            printCursor(cursor);

        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            SQLiteDatabase db = mDatabase.getReadableDatabase();
            String sql = "SELECT * FROM " + Contract.CategoriesTable.TABLE_NAME;
            Cursor cursor = db.rawQuery(sql, null);
            return cursor;
        }
    }

    public static void printCursor(Cursor cursor) {
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

}

