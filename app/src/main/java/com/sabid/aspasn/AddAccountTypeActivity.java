package com.sabid.aspasn;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class AddAccountTypeActivity extends AppCompatActivity {
    EditText editAccountTypeName;
    Button btnConfirmAddAccountType;
    ListView listAccountTypes;
    ArrayAdapter adapter;
    ArrayList<String> accountTypes;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account_category);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Account Type");
        
        editAccountTypeName = findViewById(R.id.editCategoryName);
        btnConfirmAddAccountType = findViewById(R.id.btnConfirmAddAccountType);
        listAccountTypes = findViewById(R.id.listAccountCategory);
        DB = new DBHelper(this);

        loadAccountTypesList();

        btnConfirmAddAccountType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountTypeNameText = editAccountTypeName.getText().toString().trim();
                if (!accountTypeNameText.isEmpty()) {
                    Boolean checkInsertData = DB.insertAccountCategory(accountTypeNameText);
                    if (checkInsertData) {
                        Toast.makeText(AddAccountTypeActivity.this, "New Account Type Created", Toast.LENGTH_SHORT).show();
                        editAccountTypeName.setText("");
                        // Refresh List after new cr
                        loadAccountTypesList();
                    } else {
                        Toast.makeText(AddAccountTypeActivity.this, "Not Inserted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddAccountTypeActivity.this, "Type Account Type Name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void loadAccountTypesList() {
        accountTypes = new ArrayList<String>();
        Cursor res = DB.getAccountTypes();
        if (res.getCount() == 0) {
            accountTypes.add("Add Account Type");
        } else {
            while (res.moveToNext()) {
                //take account type name only from 2nd(1) column
                accountTypes.add(res.getString(1));
            }
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, accountTypes);
        listAccountTypes.setAdapter(adapter);
        res.close();
    }


}
