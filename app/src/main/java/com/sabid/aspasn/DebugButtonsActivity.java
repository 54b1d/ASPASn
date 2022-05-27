package com.sabid.aspasn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DebugButtonsActivity extends AppCompatActivity {
    Button btnActivityCalc, btnActivityAddAccount, btnActivityAddCashTransaction, btnActivityAddExpenseAccount
    , btnActivityAddInventoryItem, btnActivityAddInvoice, btnActivityAccountingPeriods;
    
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
        
        btnActivityCalc = findViewById(R.id.btnActivityCalc);
        btnActivityAddAccount = findViewById(R.id.btnActivityAddAccount);
        btnActivityAddCashTransaction = findViewById(R.id.btnActivityAddCashTransaction);
        btnActivityAddExpenseAccount = findViewById(R.id.btnActivityAddExpenseAccount);
		btnActivityAddInventoryItem = findViewById(R.id.btnActivityAddInventoryItem);
        btnActivityAddInvoice = findViewById(R.id.btnActivityAddInvoice);
        btnActivityAccountingPeriods = findViewById(R.id.btnActivityAccountingPeriods);

        btnActivityCalc.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    openCalculatorActivity();
                }
            });
        btnActivityAddAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    openCreateAccountActivity();
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
		btnActivityAddExpenseAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAddExpenseAccount();
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
    public void openCalculatorActivity(){
        Intent intent = new Intent(this, CalculatorActivity.class);
        startActivity(intent);
    }
    public void openCreateAccountActivity(){
        Intent intent = new Intent(this, AddAccountActivity.class);
        startActivity(intent);
    }
    public void openActivityAddCashTransaction(){
        Intent intent = new Intent(this, AddCashTransactionActivity.class);
        startActivity(intent);
    }

    public void openActivityAddExpenseAccount() {
        Intent intent = new Intent(this, AddExpenseAccountActivity.class);
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
