package com.sabid.aspasn;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class TransactionsActivity extends AppCompatActivity {
    ListView listPurchases, listSales, listReceipts, listPayments, listJournalEntries;
    ArrayList purchases, sales, receipts, payments, journalEntries;
	ArrayAdapter adapter;
	DBHelper DB;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Transactions");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _v) {
                onBackPressed();
            }
        });
		listPurchases = findViewById(R.id.listPurchases);
		listSales = findViewById(R.id.listSales);
		listReceipts = findViewById(R.id.listReceipts);
        listPayments = findViewById(R.id.listPayments);
        listJournalEntries = findViewById(R.id.listJournalEntries);

		DB = new DBHelper(this);
        loadListPurchases();
		loadListSales();
		loadListReceipts();
		loadListPayments();
        loadListJournalEntries();
    }

    @Override
	protected void onResume() {
		super.onResume();
		loadListPurchases();
		loadListSales();
		loadListReceipts();
		loadListPayments();
	}
	
	public void loadListPurchases() {
        purchases = new ArrayList<String>();
        Cursor res = DB.getPurchases();
        if (res.getCount() == 0) {
            purchases.add("No Purchase Invoice");
        } else {
            while (res.moveToNext()) {
                // date, Name, Address, Amount
				String a = res.getString(0) + ": " + res.getString(1) + ", " + res.getString(2) + " =" + res.getString(3)+"Tk";
				purchases.add(a);
            }
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, purchases);
        listPurchases.setAdapter(adapter);
    }
	
	public void loadListSales() {
        sales = new ArrayList<String>();
        Cursor res = DB.getSales();
        if (res.getCount() == 0) {
            sales.add("No Sales Invoice");
        } else {
            while (res.moveToNext()) {
                // date, Name, Address, Amount
				String a = res.getString(0) + ": " + res.getString(1) + ", " + res.getString(2) + " =" + res.getString(3)+"Tk";
				sales.add(a);
            }
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, sales);
        listSales.setAdapter(adapter);
    }
	
	public void loadListReceipts() {
        receipts = new ArrayList<String>();
        Cursor res = DB.getReceipts();
        if (res.getCount() == 0) {
            receipts.add("No Cash Received");
        } else {
            while (res.moveToNext()) {
                // date, Name, Address, Amount
				String a = res.getString(0) + ": " + res.getString(1) + ", " + res.getString(2) + " =" + res.getString(3)+"Tk";
				receipts.add(a);
            }
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, receipts);
        listReceipts.setAdapter(adapter);
    }
	
	public void loadListPayments() {
        payments = new ArrayList<String>();
        Cursor res = DB.getPayments();
        if (res.getCount() == 0) {
            payments.add("No Cash Paid");
        } else {
            while (res.moveToNext()) {
                // date, Name, Address, Amount
                String a = res.getString(0) + ": " + res.getString(1) + ", " + res.getString(2) + " =" + res.getString(3)+"Tk";
                payments.add(a);
            }
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, payments);
        listPayments.setAdapter(adapter);
    }

    public void loadListJournalEntries() {
        journalEntries = new ArrayList<String>();
        Cursor res = DB.getPayments();
        if (res.getCount() == 0) {
            journalEntries.add("No Jounal Entry");
        } else {
            while (res.moveToNext()) {
                // date, Name, Address, Amount
                String a = res.getString(0) + ": " + res.getString(1) + ", " + res.getString(2) + " =" + res.getString(3) + "Tk";
                journalEntries.add(a);
            }
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, journalEntries);
        listPayments.setAdapter(adapter);
    }
}
