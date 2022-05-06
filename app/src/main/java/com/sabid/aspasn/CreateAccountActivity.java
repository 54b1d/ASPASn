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

public class CreateAccountActivity extends AppCompatActivity {
    EditText name, address, mobile;
    Button confirmAddAccount, viewAccounts, btnAddAccountCategory;
    Spinner spinnerAccountType;
    ArrayList categories;
    ArrayAdapter adapter;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Create Account");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View _v) {
                    onBackPressed();
                }});

        name = findViewById(R.id.editName);
        address = findViewById(R.id.editAddress);
        mobile = findViewById(R.id.editMobile);
        spinnerAccountType = findViewById(R.id.spinnerAccountType);

        confirmAddAccount = findViewById(R.id.btnConfirmAddAccount);
        viewAccounts = findViewById(R.id.btnViewAccounts);
        btnAddAccountCategory = findViewById(R.id.btnAddAccountCategory);

        DB = new DBHelper(this);


        loadSpinnerAccountType();

        confirmAddAccount.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    
                    String accountGroupTXT = spinnerAccountType.getSelectedItem().toString();
                    
                    Cursor res = DB.getGroupId(accountGroupTXT);
                    if (res.getCount() == 0) {
                        Log.d("CreateAccount", "No data in getGroupId cursor");
                        Toast.makeText(getApplicationContext(), "Invalid Group Name Selected", Toast.LENGTH_SHORT).show();
                    } else {
                        res.moveToNext();
                        int accountGroupId = res.getInt(0);
                        String nameTXT = name.getText().toString().trim();
                        String addressTXT = address.getText().toString().trim();
                        String mobileTXT = mobile.getText().toString().trim();
                        if (!nameTXT.isEmpty()) {
                            Boolean checkinsertdata = DB.insertuserdata(nameTXT, addressTXT, mobileTXT, accountGroupId);
                            if (checkinsertdata) {
                                Toast.makeText(getApplicationContext(), nameTXT+" Account Created", Toast.LENGTH_SHORT).show();
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
                }
            });
        viewAccounts.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    openActivityAddCashTransaction();
                }
            });

        btnAddAccountCategory.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    openAddAccountCategoryActivity();
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
        Cursor res = DB.getAccountCategories();
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
    }

    public void openActivityAddCashTransaction() {
        Intent intent = new Intent(this, AccountsActivity.class);
        startActivity(intent);
    }

    public void openAddAccountCategoryActivity() {
        Intent i = new Intent(this, AddAccountCategoryActivity.class);
        startActivity(i);
    }

}
