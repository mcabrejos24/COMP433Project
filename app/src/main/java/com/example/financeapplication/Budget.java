package com.example.financeapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class Budget extends AppCompatActivity {

    DBHelper mDatabase;

    //    private ViewGroup rootView;
    EditText editTrans;
    EditText editGroc;
    EditText editBills;
    EditText editRB;
    EditText editFun;
    Boolean editingTrans = false;
    Boolean editingGroc = false;
    Boolean editingBills = false;
    Boolean editingRB = false;
    Boolean editingFun = false;

    Button submitTrans;
    Button submitGroc;
    Button submitBills;
    Button submitRB;
    Button submitMisc;


    Integer budget = 0;
    Integer transBudget = 0;
    Integer grocBudget = 0;
    Integer billsBudget = 0;
    Integer rbBudget = 0;
    Integer funBudget = 0;

    InputMethodManager inputManager;
    TextView budgetView;
    Button budgetBtn;

    String changeType;
    Boolean categoryOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.budget_layout);
        mDatabase = new DBHelper(this);

        inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        budgetView = findViewById(R.id.budget);

        editTrans = (EditText) findViewById(R.id.editTrans);
        editTrans.setVisibility(View.GONE);
        editGroc = (EditText) findViewById(R.id.editGroc);
        editGroc.setVisibility(View.GONE);
        editBills = (EditText) findViewById(R.id.editBills);
        editBills.setVisibility(View.GONE);
        editRB = (EditText) findViewById(R.id.editRB);
        editRB.setVisibility(View.GONE);
        editFun = (EditText) findViewById(R.id.editFun);
        editFun.setVisibility(View.GONE);

        submitTrans = (Button) findViewById(R.id.submitTrans);
        submitTrans.setVisibility(View.GONE);
        submitGroc = (Button) findViewById(R.id.submitGroc);
        submitGroc.setVisibility(View.GONE);
        submitBills = (Button) findViewById(R.id.submitBills);
        submitBills.setVisibility(View.GONE);
        submitRB = (Button) findViewById(R.id.submitRB);
        submitRB.setVisibility(View.GONE);
        submitMisc = (Button) findViewById(R.id.submitFun);
        submitMisc.setVisibility(View.GONE);


//        rootView = (ViewGroup)findViewById(R.id.scroll);

        mDatabase = new DBHelper(this);
        new printCategoriesAsync().execute();
//        new updateCategoryValueAsync("Groceries", 250.0).execute();
    }

    public void submit(View view) {
        if (changeType == "trans") {
            if (editTrans.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter a budget.", Toast.LENGTH_SHORT).show();
            } else {
                budget -= transBudget;
                transBudget = Integer.valueOf(editTrans.getText().toString());
                budget += transBudget;
                editingTrans = false;
                budgetBtn.setText("TRANSPORTATION: $" + transBudget);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                editTrans.setVisibility(View.GONE);
                submitTrans.setVisibility(View.GONE);
                new updateCategoryValueAsync("Transportation", (int) transBudget).execute();
            }
        } else if (changeType == "groc") {
            if (editGroc.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter a budget.", Toast.LENGTH_SHORT).show();
            } else {
                budget -= grocBudget;
                grocBudget = Integer.valueOf(editGroc.getText().toString());
                budget += grocBudget;
                editingGroc = false;
                budgetBtn.setText("GROCERIES: $" + grocBudget);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                editGroc.setVisibility(View.GONE);
                submitGroc.setVisibility(View.GONE);
                new updateCategoryValueAsync("Groceries", (int) grocBudget).execute();
            }
        } else if (changeType == "bills") {
            if (editBills.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter a budget.", Toast.LENGTH_SHORT).show();
            } else {
                budget -= billsBudget;
                billsBudget = Integer.valueOf(editBills.getText().toString());
                budget += billsBudget;
                editingBills = false;
                budgetBtn.setText("BILLS: $" + billsBudget);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                editBills.setVisibility(View.GONE);
                submitBills.setVisibility(View.GONE);
                new updateCategoryValueAsync("Bills", (int) billsBudget).execute();
            }
        } else if (changeType == "rb") {
            if (editRB.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter a budget.", Toast.LENGTH_SHORT).show();
            } else {
                budget -= rbBudget;
                rbBudget = Integer.valueOf(editRB.getText().toString());
                budget += rbBudget;
                editingRB = false;
                budgetBtn.setText("RESTAURANTS/BARS: $" + rbBudget);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                editRB.setVisibility(View.GONE);
                submitRB.setVisibility(View.GONE);
                new updateCategoryValueAsync("Restaurants/Bars", (int) rbBudget).execute();
            }
        } else if (changeType == "misc") {
            if (editFun.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter a budget.", Toast.LENGTH_SHORT).show();
            } else {
                budget -= funBudget;
                funBudget = Integer.valueOf(editFun.getText().toString());
                budget += funBudget;
                editingFun = false;
                budgetBtn.setText("Miscellaneous: $" + funBudget);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                editFun.setVisibility(View.GONE);
                submitMisc.setVisibility(View.GONE);
                new updateCategoryValueAsync("misc.", (int) funBudget).execute();
            }
        }
        budgetView.setText("Total Budget: $" + budget);
        categoryOpen = false;
    }


    public void changeBudget(View view) {
        if (categoryOpen) {
            Toast.makeText(this, "Please close the current category to open a new one.", Toast.LENGTH_SHORT).show();
        } else {
            categoryOpen = true;
            budgetBtn = (Button) findViewById(view.getId());
            if (view.getId() == R.id.transBtn) {
                if (!editingTrans) {
                    editingTrans = true;
                    editTrans.setVisibility(View.VISIBLE);
                    submitTrans.setVisibility(View.VISIBLE);
                    changeType = "trans";
                    editTrans.requestFocus();
                } else {
                    submit(view);
                }
            } else if (view.getId() == R.id.grocBtn) {
                if (!editingGroc) {
                    editingGroc = true;
                    editGroc.setVisibility(View.VISIBLE);
                    submitGroc.setVisibility(View.VISIBLE);
                    changeType = "groc";
                    editGroc.requestFocus();
                } else {
                    submit(view);
                }
            } else if (view.getId() == R.id.billsBtn) {
                if (!editingBills) {
                    editingBills = true;
                    editBills.setVisibility(View.VISIBLE);
                    submitBills.setVisibility(View.VISIBLE);
                    changeType = "bills";
                    editBills.requestFocus();
                } else {
                    submit(view);
                }
            } else if (view.getId() == R.id.rbBtn) {
                if (!editingRB) {
                    editingRB = true;
                    editRB.setVisibility(View.VISIBLE);
                    submitRB.setVisibility(View.VISIBLE);
                    changeType = "rb";
                    editRB.requestFocus();
                } else {
                    submit(view);
                }
            } else if (view.getId() == R.id.funBtn) {
                if (!editingFun) {
                    editingFun = true;
                    editFun.setVisibility(View.VISIBLE);
                    submitMisc.setVisibility(View.VISIBLE);
                    changeType = "misc";
                    editFun.requestFocus();
                } else {
                    submit(view);
                }
            }
        }
    }


    public void goHome(View view) {
        finish();
    }


    private class printCategoriesAsync extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected void onPostExecute(Cursor cursor) {
            List<String> list = DBHelper.printCursor(cursor);
            Log.d("this", list.toString());
            for (int i = 0; i < list.size(); i++) {

                String[] str = list.get(i).split("\t");
                if (list.get(i).contains("Trans")) {
                    MainActivity.log(str.toString());
                    transBudget = Integer.valueOf(str[2]);
                    editTrans.setText(str[2]);
                    budget += transBudget;
                    Button budgetBtn = (Button) findViewById(R.id.transBtn);
                    budgetBtn.setText("TRANSPORTATION: $" + transBudget);
                } else if (list.get(i).contains("Groc")) {
                    grocBudget = Integer.valueOf(str[2]);
                    editGroc.setText(str[2]);
                    budget += grocBudget;
                    Button budgetBtn = (Button) findViewById(R.id.grocBtn);
                    budgetBtn.setText("Groceries: $" + grocBudget);
                } else if (list.get(i).contains("Rest")) {
                    rbBudget = Integer.valueOf(str[2]);
                    editRB.setText(str[2]);
                    budget += rbBudget;
                    Button budgetBtn = (Button) findViewById(R.id.rbBtn);
                    budgetBtn.setText("Restaurants/Bars: $" + rbBudget);
                } else if (list.get(i).contains("Misc")) {
                    funBudget = Integer.valueOf(str[2]);
                    editFun.setText(str[2]);
                    budget += funBudget;
                    Button budgetBtn = (Button) findViewById(R.id.funBtn);
                    budgetBtn.setText("Miscellaneous: $" + funBudget);
                } else if (list.get(i).contains("Bills")) {
                    billsBudget = Integer.valueOf(str[2]);
                    editBills.setText(str[2]);
                    budget += billsBudget;
                    Button budgetBtn = (Button) findViewById(R.id.billsBtn);
                    budgetBtn.setText("Bills: $" + billsBudget);
                }
                budgetView.setText("Total Budget: $" + budget);
            }

        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            SQLiteDatabase db = mDatabase.getReadableDatabase();
            String sql = "SELECT * FROM " + Contract.CategoriesTable.TABLE_NAME;
            Cursor cursor = db.rawQuery(sql, null);
            return cursor;
        }
    }


    private class updateCategoryValueAsync extends AsyncTask<Void, Void, Integer> {
        String category;
        Integer amount;

        updateCategoryValueAsync(String category, Integer amt) {
            MainActivity.log("update " + category + " to $" + amt);
            this.category = category;
            if (amt < 0) {
                this.amount = 5;
            } else {
                this.amount = amt;
            }
        }

        @Override
        protected void onPostExecute(Integer changes) {
            super.onPostExecute(changes);
            MainActivity.log(changes + " rows changed");
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            SQLiteDatabase db = mDatabase.getWritableDatabase();
            // New value for one column
            ContentValues values = new ContentValues();
            values.put(Contract.CategoriesTable.COLUMN_NAME_BUDGET_AMOUNT, this.amount);

            String selection = Contract.CategoriesTable.COLUMN_NAME_NAME + " LIKE ?";
            String[] selectionArgs = {this.category};

            return db.update(
                    Contract.CategoriesTable.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }
    }
}


