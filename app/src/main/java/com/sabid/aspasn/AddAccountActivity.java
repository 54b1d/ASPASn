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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class AddAccountActivity extends AppCompatActivity {
    String accountTypeName;
    double openingBalance, openingAssetAmount, openingLiabilityAmount;
    EditText editName, editAddress, editMobile, editOpeningLiability, editOpeningAsset;
    Button confirmAddAccount, viewAccounts, btnAddAccountType;
    Spinner spinnerAccountType;
    ArrayList categories;
    ArrayAdapter adapter;
    DBHelper DB;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Account");
        
        accountTypeName = getIntent().getExtras().getString("accountTypeName");
        editName = findViewById(R.id.editName);
        editAddress = findViewById(R.id.editAddress);
        editMobile = findViewById(R.id.editMobile);
        editOpeningLiability = findViewById(R.id.editOpeningLiability);
        editOpeningAsset = findViewById(R.id.editOpeningAsset);
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
                        String nameText = editName.getText().toString().trim();
                        String addressText = editAddress.getText().toString().trim();
                        String mobileText = editMobile.getText().toString().trim();
                        
                        //parse opening balance from ui & set negative or positive
                        String a = editOpeningAsset.getText().toString().trim();
                        String b = editOpeningLiability.getText().toString().trim();
                        if (a.isEmpty() && b.isEmpty()) {
                            openingBalance = 0;
                        } else if (b.isEmpty()) {
                            openingBalance = Double.parseDouble(a) - 0;
                        } else if (a.isEmpty()) {
                            openingBalance = 0 - Double.parseDouble(b);
                        }
                        //Toast.makeText(getApplicationContext(), Double.toString(openingBalance), Toast.LENGTH_LONG).show();

                        if (!nameText.isEmpty()) {
                            Boolean checkInsertData = DB.insertAccountData(nameText, addressText, mobileText, accountTypeId);
                            if (checkInsertData) {
                                Toast.makeText(getApplicationContext(), accountTypeNameText + ": " + nameText + " Created", Toast.LENGTH_SHORT).show();
                                // get newly created account id
                                res = DB.getAccountId(nameText);
                                res.moveToNext();
                                int accountId = res.getInt(0);
                                res.close();
                                //get first accounting period id which should be 1
                                int accountingPeriodId = 1;
                                //db insert opening balance
                                checkInsertData = DB.insertAccountsBalance("accountsBalance", "accountId", accountId, accountingPeriodId, openingBalance, 0);

                                // clear editText fields
                                editName.setText("");
                                editAddress.setText("");
                                editMobile.setText("");
                                editOpeningLiability.setText("");
                                editOpeningAsset.setText("");
                            } else {
                                Toast.makeText(getApplicationContext(), "Not Inserted", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Enter Name", Toast.LENGTH_SHORT).show();
                        }
                    }
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
