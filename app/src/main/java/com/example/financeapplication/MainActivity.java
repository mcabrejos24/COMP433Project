package com.example.financeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toLog(View view) {
        Intent intent = new Intent(this, Log.class);

    }
    public void toBudget(View view){

    }

    public static void log(String string) {
        Log.d("FINANCE_APP", string);
    }

}
