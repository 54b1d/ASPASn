package com.sabid.aspasn;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

public class AddOwnerActivity extends AppCompatActivity {
    String name;
    int accountId;
    AutoCompleteTextView editAutoName;
    Button btnConfirmAddOwner;
    ArrayList accounts;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_owner);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add Owner");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _v) {
                    onBackPressed();
                }
            });

        editAutoName = findViewById(R.id.editAutoName);
        btnConfirmAddOwner = findViewById(R.id.btnConfirmAddOwner);
        DB = new DBHelper(this);

        loadEditAutoName();

        btnConfirmAddOwner.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    addOwnerConfirmed();
                }
            });
    }

    private void loadEditAutoName() {
        accounts = new ArrayList<String>();
        Cursor res = DB.getAccounts();
        if (res.getCount() == 0) {
            accounts.add("No Available Account");
        } else {
            while (res.moveToNext()) {
                //take category name only from 2nd(1) column
                accounts.add(res.getString(1));

            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, accounts);
        editAutoName.setThreshold(1);
        editAutoName.setAdapter(adapter);
        res.close();
	}

    private void addOwnerConfirmed() {
        name = editAutoName.getText().toString();

        Cursor res = DB.getAccountId(name);
        while (res.moveToNext()) {
            accountId = res.getInt(0);
        }
        res.close();
        if (accountId == 0) {
            editAutoName.setText("");
            editAutoName.setHint("Invalid Client Name");
		}
        if (name.isEmpty() || accountId == 0) {
            Toast.makeText(this, "Input required fields", Toast.LENGTH_SHORT).show();
        } else {
            //todo insert into owner table
            if (DB.insertOwner(accountId)) {
                Toast.makeText(this, "Owner: " + name + " Added", Toast.LENGTH_SHORT).show();
                editAutoName.setText("");
            } else {
                Toast.makeText(this, "Failed to add new owner", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
