package com.sabid.aspasn;

import android.os.Bundle;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import android.database.Cursor;

public class TransactionsActivity extends AppCompatActivity {
	ListView listPurchases, listSales, listReceipts, listPayments;
	ArrayList purchases, sales, receipts, payments;
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
		listPayments = findViewById(R.id.listPeyments);
		DB = new DBHelper(this);
        loadListPurchases();
		loadListSales();
		loadListReceipts();
		loadListPayments();
		
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
		String tableName = "purchaseInvoiceEntity";
        Cursor res = DB.getTable(tableName);
        if (res.getCount() == 0) {
            purchases.add("No Purchase Invoice");
        } else {
            while (res.moveToNext()) {
                // clientId 0, name 1, address 2, mobile 3, accountTypeId 4
                String a = res.getString(0) + ", " + res.getString(1) + ", " + res.getString(2) + ", " + res.getString(3);
                purchases.add(a);
            }
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, purchases);
        listPurchases.setAdapter(adapter);
    }
	
	public void loadListSales() {
        purchases = new ArrayList<String>();
		String tableName = "salesInvoiceEntity";
        Cursor res = DB.getTable(tableName);
        if (res.getCount() == 0) {
            purchases.add("No Sales Invoice");
        } else {
            while (res.moveToNext()) {
                // clientId 0, name 1, address 2, mobile 3, accountTypeId 4
                String a = res.getString(0) + ", " + res.getString(1) + ", " + res.getString(2) + ", " + res.getString(3);
                purchases.add(a);
            }
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, purchases);
        listSales.setAdapter(adapter);
    }
	
	public void loadListReceipts() {
        purchases = new ArrayList<String>();
		String tableName = "receiptEntity";
        Cursor res = DB.getTable(tableName);
        if (res.getCount() == 0) {
            purchases.add("No Cash Received");
        } else {
            while (res.moveToNext()) {
                // clientId 0, name 1, address 2, mobile 3, accountTypeId 4
                String a = res.getString(0) + ", " + res.getString(1) + ", " + res.getString(2) + ", " + res.getString(3);
                purchases.add(a);
            }
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, purchases);
        listReceipts.setAdapter(adapter);
    }
	
	public void loadListPayments() {
        purchases = new ArrayList<String>();
        Cursor res = DB.getPayments();
        if (res.getCount() == 0) {
            purchases.add("No Cash Paid");
        } else {
            while (res.moveToNext()) {
                // date, Name, Address, Amount
                String a = res.getString(0) + ": " + res.getString(1) + ", " + res.getString(2) + " =" + res.getString(3)+"Tk";
                purchases.add(a);
            }
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, purchases);
        listPayments.setAdapter(adapter);
    }
}
