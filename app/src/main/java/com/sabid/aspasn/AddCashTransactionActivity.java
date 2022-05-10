package com.sabid.aspasn;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.database.Cursor;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.ArrayList;

public class AddCashTransactionActivity extends AppCompatActivity {
    AutoCompleteTextView editAutoName;
    EditText editDate;
    ArrayList accounts;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cash_transaction);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("New Cash Transaction");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _v) {
                    onBackPressed();
                }
            });

        editAutoName = findViewById(R.id.editAutoName);
        editDate = findViewById(R.id.editDate);

        DB = new DBHelper(this);
        loadEditAutoName();

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        editDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(AddCashTransactionActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                month = month + 1;
                                String date = day + "/" + month + "/" + year;
                                editDate.setText(date);
                            }
                        }, year, month, day);

                    datePickerDialog.show();
                }
            });


    }

    public void loadEditAutoName() {
        accounts = new ArrayList<String>();
        Cursor res = DB.getAccounts();
        if (res.getCount() == 0) {
            accounts.add("No Available Account");
        } else {
            while (res.moveToNext()) {
                //take category name only from 2nd(1) column
                accounts.add(res.getString(1));

            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>  
        (this, android.R.layout.select_dialog_item, accounts);
        editAutoName.setThreshold(1);
        editAutoName.setAdapter(adapter);
        res.close();
    }

}
