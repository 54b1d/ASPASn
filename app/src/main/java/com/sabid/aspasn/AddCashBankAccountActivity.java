package com.sabid.aspasn;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddCashBankAccountActivity extends AppCompatActivity {
    String cashBankTitle;
    int cashBankId;
    double openingBalance;
    EditText editName, editOpeningBalance;
    Button btnConfirmAddCashBankAccount;

    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cashbank_account);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add Cash Bank Account");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _v) {
                onBackPressed();
            }
        });

        editName = findViewById(R.id.editName);
        editOpeningBalance = findViewById(R.id.editOpeningBalance);
        btnConfirmAddCashBankAccount = findViewById(R.id.btnConfirmAddCashBankAccount);

        DB = new DBHelper(this);
        
        btnConfirmAddCashBankAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cashBankTitle = editName.getText().toString().trim();


                if (cashBankTitle.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter Cash/Bank Name", Toast.LENGTH_SHORT).show();
                } else {
                    String a = editOpeningBalance.getText().toString().trim();
                    if (a.isEmpty()) {
                        openingBalance = 0;
                    } else {
                        openingBalance = Double.parseDouble(a);
                    }
                    boolean checkinsert = DB.insertCashBankAccount(cashBankTitle, 1);
                    if (checkinsert) { // 1 is static for assets accountType
                        // get id for CashBank name
                        Cursor res = DB.getCashBankAccountIdFor(cashBankTitle);
                        res.moveToNext();
                        cashBankId = res.getInt(0);
                        res.close();
                        DB.insertAccountsBalance("cashBankAccountsBalance", "cashBankId", cashBankId, 1, openingBalance, 0);
                        Toast.makeText(getApplicationContext(), "Cash Bank Account: " + cashBankTitle + " Created with balance " + openingBalance, Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(getApplicationContext(), "Failed To add Account", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
