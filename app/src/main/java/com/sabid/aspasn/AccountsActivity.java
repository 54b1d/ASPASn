package com.sabid.aspasn;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sabid.aspasn.Adapters.ClientsAdapter;
import com.sabid.aspasn.DataModels.Clients;

import java.util.ArrayList;
import java.util.List;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

public class AccountsActivity extends AppCompatActivity {
    String accountTypeName;
    Button btnActivityAddAccount;
    ArrayList accounts, accountTypes;
    DBHelper DB;
    ClientsAdapter clientsAdapter;
	Spinner spinnerAccountType;
	ArrayAdapter accountTypeAdapter;
    RecyclerView rvClients;
    RecyclerView.LayoutManager layoutManager;
    List<Clients> clientsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Accounts");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _v) {
                onBackPressed();
            }
        });
		
		DB = new DBHelper(this);
		layoutManager = new LinearLayoutManager(this);
        btnActivityAddAccount = findViewById(R.id.btnActivityAddAccount);
		spinnerAccountType = findViewById(R.id.spinnerAccountType);
        rvClients = findViewById(R.id.rvClients);
        rvClients.setHasFixedSize(true);
        rvClients.setLayoutManager(layoutManager);
        //todo get accounttypename from spinner
        accountTypeName = "Accounts";
        clientsList = getClientsList(accountTypeName);
        clientsAdapter = new ClientsAdapter(getApplicationContext(), clientsList, rvClients);
        rvClients.setAdapter(clientsAdapter);
        
		loadSpinnerAccountType();
        
        
        btnActivityAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddAccountActivity();
            }
        });
    }

    public List<Clients> getClientsList(String accountTypeName) {
        Cursor res = DB.getAccountsOf(accountTypeName);
        if (res.getCount() == 0) {
				String name = "No Account";
                String address = "-";
                String mobile = "-";
                Clients emptyClient = new Clients(name, address, mobile);
                clientsList.add(emptyClient);
        } else {
            while (res.moveToNext()) {
                //clientId, clientId, name, address, mobile, accountTypeId
                String name = res.getString(1);
                String address = res.getString(2);
                String mobile = res.getString(3);
                Clients client = new Clients(name, address, mobile);
                clientsList.add(client);
            }
        }
        return clientsList;
    }

    public void openAddAccountActivity() {
        Intent intent = new Intent(this, AddAccountActivity.class);
        startActivity(intent);
    }
	
	public void loadSpinnerAccountType() {
        accountTypes = new ArrayList<String>();
        Cursor res = DB.getAccountTypes();
        if (res.getCount() == 0) {
            accountTypes.add("Add Categories");
        } else {
            while (res.moveToNext()) {
                //take category name only from 2nd(1) column
                accountTypes.add(res.getString(1));
            }
        }
        accountTypeAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, accountTypes);
        spinnerAccountType.setAdapter(accountTypeAdapter);
        res.close();
    }
}
