package com.example.financeapplication;

import android.provider.BaseColumns;

public final class Contract {

    private Contract(){}

    public static class CategoriesTable implements BaseColumns {
        public static final String TABLE_NAME = "categories";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_BUDGET_AMOUNT = "budget_amount";

        public static final String CREATE = "CREATE TABLE " + TABLE_NAME + " ( "
                + _ID + " INTEGER PRIMARY KEY, "
                + COLUMN_NAME_NAME + " TEXT, "
                + COLUMN_NAME_BUDGET_AMOUNT + " INTEGER) ";
    }
    public static class Expenses implements BaseColumns {
        public static final String TABLE_NAME = "expenses";
        public static final String COLUMN_NAME_EXPENSE = "expense_name";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_AMOUNT = "amount";


        public static final String CREATE = "CREATE TABLE " + TABLE_NAME + " ( "
                + _ID + " INTEGER PRIMARY KEY, "
                + COLUMN_NAME_EXPENSE + " TEXT, "
                + COLUMN_NAME_CATEGORY + " TEXT, "
                + COLUMN_NAME_DATE + " TEXT, "
                + COLUMN_NAME_AMOUNT + " TEXT) ";
    }

}
