package com.sabid.aspasn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DebugButtonsActivity extends AppCompatActivity {
    Button btnActivityTransactions, btnActivityAccounts, btnActivityAddCashTransaction, btnActivityAddExpenseAccount, btnActivityAddInventoryItem, btnActivityAddInvoice, btnActivityAccountingPeriods, btnAddJournalEntry;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_buttons);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Debug Buttons");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _v) {
                    onBackPressed();
                }
            });
        
        btnActivityTransactions = findViewById(R.id.btnActivityTransactions);
        btnActivityAccounts = findViewById(R.id.btnActivityAccounts);
        btnActivityAddCashTransaction = findViewById(R.id.btnActivityAddCashTransaction);
		btnActivityAddInventoryItem = findViewById(R.id.btnActivityAddInventoryItem);
        btnActivityAddInvoice = findViewById(R.id.btnActivityAddInvoice);
        btnActivityAccountingPeriods = findViewById(R.id.btnActivityAccountingPeriods);
        btnAddJournalEntry = findViewById(R.id.btnAddJournalEntry);

        btnAddJournalEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAddJournalTransaction();
            }
        });
        btnActivityTransactions.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    openTransactionsActivity();
                }
            });
        btnActivityAccounts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    openAccountsActivity();
                }
            });
        btnActivityAddCashTransaction.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
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
