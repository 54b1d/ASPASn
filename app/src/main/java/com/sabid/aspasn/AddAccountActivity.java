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

public class AddAccountActivity extends AppCompatActivity {
    String accountTypeName;
    EditText name, address, mobile;
    Button confirmAddAccount, viewAccounts, btnAddAccountType;
    Spinner spinnerAccountType;
    ArrayList categories;
    ArrayAdapter adapter;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Add New Client Account");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View _v) {
                    onBackPressed();
                }});
        accountTypeName = getIntent().getExtras().getString("accountTypeName");
        name = findViewById(R.id.editName);
        address = findViewById(R.id.editAddress);
        mobile = findViewById(R.id.editMobile);
        spinnerAccountType = findViewById(R.id.spinnerAccountType);

        confirmAddAccount = findViewById(R.id.btnConfirmAddAccount);
        viewAccounts = findViewById(R.id.btnViewAccounts);
        btnAddAccountType = findViewById(R.id.btnAddAccountType);

        DB = new DBHelper(this);


        loadSpinnerAccountType();
        
        confirmAddAccount.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    String accountTypeNameText = spinnerAccountType.getSelectedItem().toString();

                    Cursor res = DB.getAccountTypeId(accountTypeNameText);
                    if (res.getCount() == 0) {
                        Log.v("AddClientData", "No data in getAccountTypeId cursor");
                        Toast.makeText(getApplicationContext(), "Invalid Group Name Selected", Toast.LENGTH_SHORT).show();
                    } else {
                        res.moveToNext();
                        int accountTypeId = res.getInt(0);
                        String nameText = name.getText().toString().trim();
                        String addressText = address.getText().toString().trim();
                        String mobileText = mobile.getText().toString().trim();
                        if (!nameText.isEmpty()) {
                            Boolean checkInsertData = DB.insertAccountData(nameText, addressText, mobileText, accountTypeId);
                            if (checkInsertData) {
                                Toast.makeText(getApplicationContext(), accountTypeNameText + ": " + nameText + " Created", Toast.LENGTH_SHORT).show();
                                // clear editText fields
                                name.setText("");
                                address.setText("");
                                mobile.setText("");
                            } else {
                                Toast.makeText(getApplicationContext(), "Not Inserted", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Enter Name", Toast.LENGTH_SHORT).show();
                        }}
                    res.close();
                }
            });
        viewAccounts.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

        btnAddAccountType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openAddAccountTypeActivity();
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
        Cursor res = DB.getAccountTypes();
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
        spinnerAccountType.setSelection(((ArrayAdapter<String>)spinnerAccountType.getAdapter()).getPosition(accountTypeName));
        res.close();
    }

    public void openAddAccountTypeActivity() {
        Intent i = new Intent(this, AddAccountTypeActivity.class);
        startActivity(i);
    }

}
