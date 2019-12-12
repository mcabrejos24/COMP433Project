package com.example.financeapplication;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
            List<String> list = new ArrayList<>();
            int columnCount = cursor.getColumnCount();


            final TableLayout tableLayout = findViewById(R.id.tableLayout);
//            int j = 0;

            while (cursor.moveToNext()) {
                TableRow row = new TableRow(History.this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);
                row.setId(cursor.getInt(0));
                MainActivity.log("ID: " + row.getId());
                tableLayout.addView(row);
                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                lp2.weight = 1;
                for (int i = 1; i < columnCount; ++i) {
                    TextView tv = new TextView(History.this);
                    tv.setLayoutParams(lp2);
                    switch (cursor.getType(i)) {
                        case Cursor.FIELD_TYPE_INTEGER:
                            //  Log.d("plswork", "" + cursor.getInt(i));
                            tv.setText(cursor.getInt(i) + "");
                            break;
                        case Cursor.FIELD_TYPE_STRING:
                            //  Log.d("plswork", "" + cursor.getString(i));
                            tv.setText(cursor.getString(i));
                            break;
                        default:
                            // do nothing
                            break;
                    }
                    Log.d("plswork", tv.getText().toString());
                    row.addView(tv);
                }
                Button button = new Button(History.this);
                button.setLayoutParams(lp2);
                button.setText("Delete");
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        TableRow tr = (TableRow) v.getParent();
                        MainActivity.log("ID: " + tr.getId());
                        deleteEntry(tr.getId());
//                        for (int i = 0; i < 4; i++) {
//                            TextView t = (TextView) tr.getChildAt(i);
//                            Log.d("delete", t.getText().toString());
//                        }
                        TableLayout table = findViewById(R.id.tableLayout);
                        table.removeView(tr);

                    }
                });
                row.addView(button);
//                j++;
            }
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            SQLiteDatabase db = mDatabase.getReadableDatabase();
            return db.rawQuery("Select * from " + Contract.Expenses.TABLE_NAME + " Order by " + Contract.Expenses.COLUMN_NAME_DATE + "  DESC", null);
        }
    }

    public Integer deleteEntry (Integer id) {
        SQLiteDatabase db = mDatabase.getWritableDatabase();
        return db.delete(Contract.Expenses.TABLE_NAME,
                "_id = ? ",
                new String[] { Integer.toString(id) });
    }


}
