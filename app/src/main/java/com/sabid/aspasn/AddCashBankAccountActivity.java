package com.sabid.aspasn;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

public class AddCashBankAccountActivity extends AppCompatActivity {
    String accountTypeName;
    double openingBalance, openingAssetAmount, openingLiabilityAmount;
    
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cashbank_account);
        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Add Cash Bank Account");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View _v) {
                    onBackPressed();
                }});
    }
}
