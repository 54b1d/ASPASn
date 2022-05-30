package com.sabid.aspasn;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sabid.aspasn.Adapters.ClientsAdapter;
import com.sabid.aspasn.DataModels.Clients;

import java.util.ArrayList;
import java.util.List;

public class AccountsActivity extends AppCompatActivity {
    Button btnActivityAddAccount;
    ListView listAccounts;
    ArrayList accounts;
    ArrayAdapter adapter;
    DBHelper DB;
    ClientsAdapter clientsAdapter;
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

        btnActivityAddAccount = findViewById(R.id.btnActivityAddAccount);
        DB = new DBHelper(this);
        rvClients = findViewById(R.id.rvClients);
        rvClients.setHasFixedSize(true);
        clientsList = getClientsList();
        layoutManager = new LinearLayoutManager(this);
        rvClients.setLayoutManager(layoutManager);
        clientsAdapter = new ClientsAdapter(this, clientsList, rvClients);
        rvClients.setAdapter(clientsAdapter);

        btnActivityAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddAccountActivity();
            }
        });
    }

    public List<Clients> getClientsList() {
        Cursor res = DB.getAccounts();
        if (res.getCount() == 0) {
            accounts.add("No Account Found");
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
}
