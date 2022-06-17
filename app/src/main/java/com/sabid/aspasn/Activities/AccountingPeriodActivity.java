package com.sabid.aspasn.Activities;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.sabid.aspasn.DBHelper;
import com.sabid.aspasn.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AccountingPeriodActivity extends AppCompatActivity {
    String date, startDate, endDate, previousStartDate, previousEndDate;
    EditText editStartDate, editEndDate;
    boolean noPeriod;
    ListView listAccountingPeriods;
    Button btnConfirmAddAccountingPeriod;
    ArrayList accountingPeriods;
    ArrayAdapter adapter;
    DBHelper DB;
    ActionBar actionBar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounting_period);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Accounting Period");

        editStartDate = findViewById(R.id.editStartDate);
        editEndDate = findViewById(R.id.editEndDate);
        listAccountingPeriods = findViewById(R.id.listAccountingPeriods);
        btnConfirmAddAccountingPeriod = findViewById(R.id.btnConfirmAddAccountingPeriod);

        DB = new DBHelper(this);
        loadListAccountingPeriods();

        final DateTimeFormatter dateFormat =  DateTimeFormatter.ofPattern("yyyy-M-d");
		LocalDate currentDate = LocalDate.now();
        date = currentDate.format(dateFormat);
        editEndDate.setText(date);
        final int year = currentDate.getYear();
        final int month = currentDate.getMonthValue();
        final int day = currentDate.getDayOfMonth();
        
        Cursor res = DB.getLastAccountingClosingDate();
        if(res.getCount()==0){ // no period found
            editStartDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(AccountingPeriodActivity.this,
                            R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int day) {
                                    month = month +1;
                                    String date = year + "-" + month + "-" + day;
                                    editStartDate.setText(date);
                                }
                            }, year, month -1, day);

                        datePickerDialog.show();
                    }
                });
        } else{
            while(res.moveToNext()){
                // get last date plus 1 day
                editStartDate.setText(LocalDate.parse(res.getString(0),dateFormat).plusDays(1).format(dateFormat));
                
            }
        }
        
        

        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AccountingPeriodActivity.this,
                        R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month +1;
						String date = year + "-" + month + "-" + day;
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
                    /*if(noPeriod==true){
                    previousStartDate = calculateDate(startDate, dateFormat, 30);
                    previousEndDate = calculateDate(startDate, dateFormat, 1);
                    Toast.makeText(getApplicationContext(), previousStartDate +" To "+ previousEndDate, Toast.LENGTH_SHORT).show();
                        if (DB.insertAccountingPeriod(previousStartDate, previousEndDate)){
                            noPeriod = false;
                        }
                    }*/
                    //todo make sure new period is not between older periods
                    
					boolean checkInsert = DB.insertAccountingPeriod(startDate, endDate);
					if (checkInsert){
                    loadListAccountingPeriods(); // Refresh Accounting Period List
					} else {
						Toast.makeText(AccountingPeriodActivity.this, "Failed to insert period", Toast.LENGTH_SHORT).show();
					}
                }
            }
        });
    }
    
    private String calculateDate(String date, DateTimeFormatter dateFormat, long days){
        LocalDate d = LocalDate.parse(date, dateFormat).minusDays(days);
        String dateString = d.format(dateFormat);
        return dateString;
    }

    private void loadListAccountingPeriods(){
        accountingPeriods = new ArrayList<String>();
        Cursor res = DB.getAccountingPeriods();
        if (res.getCount() == 0) {
            accountingPeriods.add("No Periods Created");
            // set noPeriod true for previous period creation
            noPeriod = true;
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
