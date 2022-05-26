package com.sabid.aspasn;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.database.Cursor;
import android.content.Intent;

public class InventoryActivity extends AppCompatActivity {
    Button btnActivityAddProduct;
    ListView listProducts;
    ArrayList products;
    ArrayAdapter adapter;
    DBHelper DB;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Inventory");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _v) {
                onBackPressed();
            }
        });
        btnActivityAddProduct = findViewById(R.id.btnActivityAddProduct);
        listProducts = findViewById(R.id.listProducts);
        DB = new DBHelper(this);
        loadListProducts();

        btnActivityAddProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openAddProductActivity();
                }
            });
    }

    public void loadListProducts() {
        products = new ArrayList<String>();
        Cursor res = DB.getProducts();
        if (res.getCount() == 0) {
            products.add("No Product Found");
        } else {
            while (res.moveToNext()) {
                // clientId 0, name 1, address 2, mobile 3, accountTypeId 4
                String a = res.getString(0) + ", " + res.getString(1) + ", " + res.getString(2) + ", " + res.getString(3);
                products.add(a);
            }
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, products);
        listProducts.setAdapter(adapter);
    }

    public void openAddProductActivity() {
        Intent intent = new Intent(this, AddProductActivity.class);
        startActivity(intent);
    }
}
