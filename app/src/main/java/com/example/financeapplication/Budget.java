package com.example.financeapplication;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
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

public class Budget extends AppCompatActivity {

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

    double budget = 0;
    double transBudget = 0;
    double grocBudget = 0;
    double billsBudget = 0;
    double rbBudget = 0;
    double funBudget = 0;

    InputMethodManager inputManager;
    TextView budgetView;
    Button budgetBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.budget_layout);

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

//        rootView = (ViewGroup)findViewById(R.id.scroll);
    }


    public void changeBudget(View view) {
        budgetBtn = (Button) findViewById(view.getId());
//        Log.d("tag", budgetBtn.getText().toString());

        if (view.getId() == R.id.transBtn) {
            if(!editingTrans) {
                editingTrans = true;
                editTrans.setVisibility(View.VISIBLE);
            } else {
                if (editTrans.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please enter a budget.", Toast.LENGTH_SHORT).show();
                } else {
                    budget -= transBudget;
                    transBudget = Double.valueOf(editTrans.getText().toString());
                    budget += transBudget;
                    editingTrans = false;
                    budgetBtn.setText("TRANSPORTATION: $" + transBudget);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    editTrans.setVisibility(View.GONE);
                }
            }
        } else if (view.getId() == R.id.grocBtn) {
            if(!editingGroc) {
                editingGroc = true;
                editGroc.setVisibility(View.VISIBLE);
            } else {
                if (editGroc.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please enter a budget.", Toast.LENGTH_SHORT).show();
                } else {
                    budget -= grocBudget;
                    grocBudget = Double.valueOf(editGroc.getText().toString());
                    budget += grocBudget;
                    editingGroc = false;
                    budgetBtn.setText("GROCERIES: $" + grocBudget);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    editGroc.setVisibility(View.GONE);
                }
            }
        } else if (view.getId() == R.id.billsBtn) {
            if(!editingBills) {
                editingBills = true;
                editBills.setVisibility(View.VISIBLE);
            } else {
                if (editBills.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please enter a budget.", Toast.LENGTH_SHORT).show();
                } else {
                    budget -= billsBudget;
                    billsBudget = Double.valueOf(editBills.getText().toString());
                    budget += billsBudget;
                    editingBills = false;
                    budgetBtn.setText("BILLS: $" + billsBudget);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    editBills.setVisibility(View.GONE);
                }
            }
        } else if (view.getId() == R.id.rbBtn) {
            if(!editingRB) {
                editingRB = true;
                editRB.setVisibility(View.VISIBLE);
            } else {
                if (editRB.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please enter a budget.", Toast.LENGTH_SHORT).show();
                } else {
                    budget -= rbBudget;
                    rbBudget = Double.valueOf(editRB.getText().toString());
                    budget += rbBudget;
                    editingRB = false;
                    budgetBtn.setText("RESTAURANTS/BARS: $" + rbBudget);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    editRB.setVisibility(View.GONE);
                }
            }
        } else if (view.getId() == R.id.funBtn) {
            if(!editingFun) {
                editingFun = true;
                editFun.setVisibility(View.VISIBLE);
            } else {
                if (editFun.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please enter a budget.", Toast.LENGTH_SHORT).show();
                } else {
                    budget -= funBudget;
                    funBudget = Double.valueOf(editFun.getText().toString());
                    budget += funBudget;
                    editingFun = false;
                    budgetBtn.setText("FUN: $" + funBudget);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    editFun.setVisibility(View.GONE);
                }
            }
        }
        budgetView.setText("Total Budget :$" + budget);


    }


    public void goHome(View view) {
        finish();
    }


//    public void add(View view) {
//
//        EditText edit = new EditText(Budget.this);
////        edit.setTransformationMethod();
//        edit.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
//
//        Button button = new Button(Budget.this);
//
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
//        params.addRule(RelativeLayout.BELOW, R.id.budgetbtn);
//
//        rootView.addView(edit);
//        rootView.addView(button);
//
//    }



}
