package com.sabid.aspasn;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class AddExpenseAccountActivity extends AppCompatActivity {
    EditText name;
    Button ConfirmAddExpenseAccount, viewExpenseAccounts;
    Spinner spinnerAccountType;
    ArrayList categories;
    ArrayAdapter adapter;
    DBHelper DB;
    private String accountTypeName = "Expenses"; // literal from database table accountTypesEntity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense_account);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add New Expense Account");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _v) {
                onBackPressed();
            }
        });

        name = findViewById(R.id.editExpenseName);
        spinnerAccountType = findViewById(R.id.spinnerAccountType);
        ConfirmAddExpenseAccount = findViewById(R.id.btnConfirmAddExpenseAccount);
        viewExpenseAccounts = findViewById(R.id.btnViewExpenseAccounts);

        DB = new DBHelper(this);


        loadSpinnerAccountType();

        ConfirmAddExpenseAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String accountTypeNameText = spinnerAccountType.getSelectedItem().toString();

                Cursor res = DB.getAccountTypeId(accountTypeNameText);
                if (res.getCount() == 0) {
                    Log.e("AddExpenseAccount", "No data in getAccountTypeId cursor");
                    Toast.makeText(getApplicationContext(), "Invalid Group Name Selected", Toast.LENGTH_SHORT).show();
                } else {
                    res.moveToNext();
                    int accountTypeId = res.getInt(0);
                    String expenseNameText = name.getText().toString().trim();
                    if (!expenseNameText.isEmpty()) {
                        Boolean checkInsertData = DB.insertExpenseAccount(expenseNameText, accountTypeId);
                        if (checkInsertData) {
                            Toast.makeText(getApplicationContext(), expenseNameText + " Expense Account Created", Toast.LENGTH_SHORT).show();
                            // clear editText fields
                            name.setText("");
                        } else {
                            Toast.makeText(getApplicationContext(), "Not Inserted", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Enter Expense Name", Toast.LENGTH_SHORT).show();
                    }
                }
                res.close();
            }
        });
        viewExpenseAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityExpenseAccounts();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSpinnerAccountType();
    }

    public void loadSpinnerAccountType() {
        categories = new ArrayList<String>();
        Cursor res = DB.getAccountTypeFor(accountTypeName);
        if (res.getCount() == 0) {
            categories.add("Add Categories");
        } else {
            while (res.moveToNext()) {
                //take category name only from 2nd(1) column
                categories.add(res.getString(1));
            }
        }
        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerAccountType.setAdapter(adapter);
        res.close();
    }

    public void openActivityExpenseAccounts() {
        Intent intent = new Intent(this, ExpenseAccountsActivity.class);
        startActivity(intent);
    }


}
