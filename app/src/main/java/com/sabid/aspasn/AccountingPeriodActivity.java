package com.sabid.aspasn;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AccountingPeriodActivity extends AppCompatActivity {
    String date, startDate, endDate;
    EditText editStartDate, editEndDate;
    ListView listAccountingPeriods;
    Button btnConfirmAddAccountingPeriod;
    ArrayList accountingPeriods;
    ArrayAdapter adapter;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounting_period);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Accounting Period");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        editStartDate = findViewById(R.id.editStartDate);
        editEndDate = findViewById(R.id.editEndDate);
        listAccountingPeriods = findViewById(R.id.listAccountingPeriods);
        btnConfirmAddAccountingPeriod = findViewById(R.id.btnConfirmAddAccountingPeriod);

        DB = new DBHelper(this);
        loadListAccountingPeriods();

        DateTimeFormatter dateFormat =  DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime currentDateTime = LocalDateTime.now();
        date = currentDateTime.format(dateFormat);
        editEndDate.setText(date);
        final int year = currentDateTime.getYear();
        final int month = currentDateTime.getMonthValue();
        final int day = currentDateTime.getDayOfMonth();
        

        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AccountingPeriodActivity.this,
                        R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
						month = month +1;
                        String date = day + "/" + month + "/" + year;
                        editStartDate.setText(date);
                    }
                }, year, month -1, day);

                datePickerDialog.show();
            }
        });

        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AccountingPeriodActivity.this,
                        R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month +1;
						String date = day + "/" + month + "/" + year;
                        editEndDate.setText(date);
                    }
                }, year, month -1, day);

                datePickerDialog.show();
            }
        });

        btnConfirmAddAccountingPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDate = editStartDate.getText().toString();
                endDate = editEndDate.getText().toString();
                if(startDate.isEmpty() || endDate.isEmpty()){
                    Toast.makeText(AccountingPeriodActivity.this, "Select Dates Correctly", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AccountingPeriodActivity.this, "Implement Insert Logic", Toast.LENGTH_SHORT).show();
                    loadListAccountingPeriods(); // Refresh Accounting Period List
                }
            }
        });
    }

    public void loadListAccountingPeriods(){
        accountingPeriods = new ArrayList<String>();
        Cursor res = DB.getAccountingPeriods();
        if (res.getCount() == 0) {
            accountingPeriods.add("No Periods Created");
        } else {
            while (res.moveToNext()) {
                String a = res.getString(0) + ", " + res.getString(1) + ", " + res.getString(2);
                accountingPeriods.add(a);
            }
        }
        res.close();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, accountingPeriods);
        listAccountingPeriods.setAdapter(adapter);

    }
}
