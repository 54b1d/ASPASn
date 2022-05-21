package com.sabid.aspasn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DebugButtonsActivity extends AppCompatActivity {
    Button btnActivityCalc, btnActivityCreateAccount, btnActivityAddCashTransaction;
    
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
        btnActivityCreateAccount = findViewById(R.id.btnActivityCreateAccount);
        btnActivityAddCashTransaction = findViewById(R.id.btnActivityAddCashTransaction);

        btnActivityCalc.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    openCalculatorActivity();
                }
            });
        btnActivityCreateAccount.setOnClickListener(new View.OnClickListener(){
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
        
    }
    public void openCalculatorActivity(){
        Intent intent = new Intent(this, CalculatorActivity.class);
        startActivity(intent);
    }
    public void openCreateAccountActivity(){
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }
    public void openActivityAddCashTransaction(){
        Intent intent = new Intent(this, AddCashTransactionActivity.class);
        startActivity(intent);
    }
    
}
