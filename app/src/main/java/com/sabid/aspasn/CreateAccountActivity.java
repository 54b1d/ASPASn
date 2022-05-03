package com.sabid.aspasn;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.util.FindAddress;
import android.content.Intent;

public class CreateAccountActivity extends AppCompatActivity {
    EditText name, address, mobile;
    Button confirmAddAccount, viewAccounts, btnAddAccountCategory;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Toolbar toolbar=findViewById(R.id.toolbar);
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

        confirmAddAccount = findViewById(R.id.btnConfirmAddAccount);
        viewAccounts = findViewById(R.id.btnViewAccounts);
        btnAddAccountCategory = findViewById(R.id.btnAddAccountCategory);

        DB = new DBHelper(this);

        confirmAddAccount.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String nameTXT = name.getText().toString();
                    String addressTXT = address.getText().toString();
                    String mobileTXT = mobile.getText().toString();

                    Boolean checkinsertdata = DB.insertuserdata(nameTXT, addressTXT, mobileTXT);
                    if (checkinsertdata) {
                        Toast.makeText(CreateAccountActivity.this, "New Account Created", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CreateAccountActivity.this, "Not Inserted", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        viewAccounts.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Cursor res = DB.getdata();
                    if (res.getCount() == 0) {
                        Toast.makeText(CreateAccountActivity.this, "No Account Exists", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    StringBuffer buffer = new StringBuffer();
                    while (res.moveToNext()) {
                        buffer.append("Name: " + res.getString(0) + "\n");
                        buffer.append("Address: " + res.getString(1) + "\n");
                        buffer.append("Mobile: " + res.getString(2) + "\n\n");
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccountActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Account List");
                    builder.setMessage(buffer.toString());
                    builder.show();
                }
            });

        btnAddAccountCategory.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    openAddAccountCategoryActivity();
                }
            });

    }
    public void openAddAccountCategoryActivity() {
        Intent i = new Intent(this, AddAccountCategoryActivity.class);
        startActivity(i);
    }

}
