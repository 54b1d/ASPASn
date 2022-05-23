package com.sabid.aspasn;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class ExpenseAccountsActivity extends AppCompatActivity {
    Button btnActivityAddExpenseAccount;
    ListView listExpenseAccounts;
    ArrayList expenseAccounts;
    ArrayAdapter adapter;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_accounts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Expense Accounts");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _v) {
                onBackPressed();
            }
        });

        btnActivityAddExpenseAccount = findViewById(R.id.btnActivityAddExpenseAccount);
        listExpenseAccounts = findViewById(R.id.listExpenseAccounts);
        DB = new DBHelper(this);
        loadListAccounts();

        btnActivityAddExpenseAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddExpenseAccountActivity();
            }
        });
    }

    public void loadListAccounts() {
        expenseAccounts = new ArrayList<String>();
        Cursor res = DB.getExpenseAccounts();
        if (res.getCount() == 0) {
            expenseAccounts.add("No Account Found");
        } else {
            while (res.moveToNext()) {
                // expenseId 0, expenseTitle 1, accountTypeId 2
                String a = res.getString(0) + ", " + res.getString(1) + ", " + res.getString(2);
                expenseAccounts.add(a);
            }
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, expenseAccounts);
        listExpenseAccounts.setAdapter(adapter);
    }

    public void openAddExpenseAccountActivity() {
        Intent intent = new Intent(this, AddExpenseAccountActivity.class);
        startActivity(intent);
    }
}
