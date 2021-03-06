package com.sabid.aspasn.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;

import com.sabid.aspasn.R;

public class DebugButtonsActivity extends AppCompatActivity {
    Button btnActivityTransactions, btnActivityAccounts, btnActivityAddCashTransaction, btnActivityAddExpenseAccount, btnActivityAddInventoryItem, btnActivityAddInvoice, btnActivityAccountingPeriods, btnAddJournalEntry, btnAddOwner, btnAddCashBankAccountActivity;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_buttons);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Debug Buttons");
        //actionBar.setDisplayHomeAsUpEnabled(true);

        btnAddCashBankAccountActivity = findViewById(R.id.btnAddCashBankAccountActivity);
        btnActivityTransactions = findViewById(R.id.btnActivityTransactions);
        btnActivityAccounts = findViewById(R.id.btnActivityAccounts);
        btnActivityAddCashTransaction = findViewById(R.id.btnActivityAddCashTransaction);
        btnActivityAddInventoryItem = findViewById(R.id.btnActivityAddInventoryItem);
        btnActivityAddInvoice = findViewById(R.id.btnActivityAddInvoice);
        btnActivityAccountingPeriods = findViewById(R.id.btnActivityAccountingPeriods);
        btnAddJournalEntry = findViewById(R.id.btnAddJournalEntry);
        btnAddOwner = findViewById(R.id.btnAddOwner);

        btnAddJournalEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAddJournalTransaction();
            }
        });
        btnActivityTransactions.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openTransactionsActivity();
            }
        });
        btnActivityAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAccountsActivity();
            }
        });
        btnActivityAddCashTransaction.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openActivityAddCashTransaction();
            }
        });
        btnActivityAddInventoryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAddProduct();
            }
        });
        btnActivityAddInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAddInvoice();
            }
        });
        btnActivityAccountingPeriods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAccountingPeriods();
            }
        });
        btnAddOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddOwner();
            }
        });
        btnAddCashBankAccountActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAddCashBankAccount();
            }
        });
    }

    private void openActivityAddCashBankAccount() {
        Intent intent = new Intent(this, AddCashBankAccountActivity.class);
        startActivity(intent);
    }

    private void openAddOwner(){
        Intent Intent = new Intent(this, AddOwnerActivity.class);
        startActivity(Intent);
    }

    private void openActivityAddJournalTransaction() {
        Intent intent = new Intent(this, AddJournalTransactionActivity.class);
        startActivity(intent);
    }

    public void openTransactionsActivity(){
        Intent intent = new Intent(this, TransactionsActivity.class);
        startActivity(intent);
    }
    public void openAccountsActivity(){
        Intent intent = new Intent(this, AccountsActivity.class);
        startActivity(intent);
    }
    public void openActivityAddCashTransaction(){
        Intent intent = new Intent(this, AddCashTransactionActivity.class);
        startActivity(intent);
    }
	
	public void openActivityAddProduct() {
        Intent intent = new Intent(this, AddProductActivity.class);
        startActivity(intent);
    }
    
    public void openActivityAddInvoice() {
        Intent intent = new Intent(this, AddInvoiceActivity.class);
        startActivity(intent);
    }
    public void openAccountingPeriods(){
        Intent intent = new Intent(this, AccountingPeriodActivity.class);
        startActivity(intent);
    }
}
