package com.sabid.aspasn.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sabid.aspasn.Adapters.ClientsAdapter;
import com.sabid.aspasn.DBHelper;
import com.sabid.aspasn.DataModels.Clients;
import com.sabid.aspasn.R;

import java.util.ArrayList;
import java.util.List;

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
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Accounts");
		
		DB = new DBHelper(this);

        btnActivityAddAccount = findViewById(R.id.btnActivityAddAccount);
        //Spinner Account Type
        spinnerAccountType = findViewById(R.id.spinnerAccountType);
        loadSpinnerAccountType();

        layoutManager = new LinearLayoutManager(this);
        rvClients = findViewById(R.id.rvClients);
        rvClients.setHasFixedSize(true);
        rvClients.setLayoutManager(layoutManager);
        // todo take account type value from intent
        // take currently selected item as account type filter


        spinnerAccountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                accountTypeName = spinnerAccountType.getSelectedItem().toString();
                clientsList.clear();
                clientsList = getClientsList(spinnerAccountType.getSelectedItem().toString());
                clientsAdapter = new ClientsAdapter(getApplicationContext(), clientsList, rvClients);
                rvClients.setAdapter(clientsAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        
        btnActivityAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountTypeName = spinnerAccountType.getSelectedItem().toString();
                openAddAccountActivity(accountTypeName);
            }
        });
    }

    public List<Clients> getClientsList(String accountTypeNameText) {
        Cursor res = DB.getAccountsOf(accountTypeNameText);
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

    public void openAddAccountActivity(String accountTypeName) {
        Intent intent = new Intent(this, AddAccountActivity.class);
        
        intent.putExtra("accountTypeName", accountTypeName);
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
