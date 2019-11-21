package com.example.financeapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context) {
        super(context, "financeapp.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        MainActivity.log("oncreate called");
        sqLiteDatabase.execSQL(Contract.PhotosTable.CREATE);
        sqLiteDatabase.execSQL(Contract.TagsTable.CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        MainActivity.log("onUpgrade called");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "
                + Contract.PhotosTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "
                + Contract.TagsTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

