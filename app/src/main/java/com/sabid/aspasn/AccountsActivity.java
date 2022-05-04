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

public class AccountsActivity extends AppCompatActivity {
    Button btnActivityCreateAccount;
    ListView listAccounts;
    ArrayList accounts;
    ArrayAdapter adapter;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Accounts");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _v) {
                onBackPressed();
            }
        });

        btnActivityCreateAccount = findViewById(R.id.btnActivityCreateAccount);
        listAccounts = findViewById(R.id.listAccounts);

        loadListAccounts();

        btnActivityCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateAccountActivity();
            }
        });
    }

    public void loadListAccounts() {
        accounts = new ArrayList<String>();
        Cursor res = DB.getAccounts();
        if (res.getCount() == 0) {
            accounts.add("No Account Found");
        } else {
            while (res.moveToNext()) {
                //take category name only from 2nd(1) column
                accounts.add(res.getString(0));
            }
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, accounts);
        listAccounts.setAdapter(adapter);
    }

    public void openCreateAccountActivity() {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }
}
