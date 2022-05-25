package com.sabid.aspasn;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;

public class AddProductActivity extends AppCompatActivity {
    private String accountTypeName = "Inventory"; // literal from database table accountTypesEntity
    EditText productName, address, mobile;
    Button confirmAddProduct, viewProducts, btnAddProductType;
    Spinner spinnerProductType;
    ArrayList categories;
    ArrayAdapter adapter;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Add New Product");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View _v) {
                    onBackPressed();
                }});

        productName = findViewById(R.id.editProductName);
        spinnerProductType = findViewById(R.id.spinnerProductType);

        confirmAddProduct = findViewById(R.id.btnConfirmAddProduct);
        viewProducts = findViewById(R.id.btnViewProducts);
        btnAddProductType = findViewById(R.id.btnAddProductType);

        DB = new DBHelper(this);


        loadSpinnerProductType();

        confirmAddProduct.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    String productTypeTitleText = spinnerProductType.getSelectedItem().toString();
                    int accountTypeId = 1;
                    Cursor res = DB.getProductTypeFor(productTypeTitleText);
                    if (res.getCount() == 0) {
                        Log.v("AddClientData", "No data in getAccountTypeId cursor");
                        Toast.makeText(getApplicationContext(), "Invalid Group Name Selected", Toast.LENGTH_SHORT).show();
                    } else {
                        res.moveToNext();
                        int productTypeId = res.getInt(0);
                        String productNameText = productName.getText().toString().trim();
                        if (!productNameText.isEmpty()) {
                            Boolean checkInsertData = DB.insertProductType(productNameText, productTypeId, accountTypeId);
                            if (checkInsertData) {
                                Toast.makeText(getApplicationContext(), productNameText + " Client Account Created", Toast.LENGTH_SHORT).show();
                                // clear editText fields
                                productName.setText("");
                            } else {
                                Toast.makeText(getApplicationContext(), "Not Inserted", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Enter Name", Toast.LENGTH_SHORT).show();
                        }}
                    res.close();
                }
            });
        viewProducts.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    openActivityInventory();
                }
            });

        btnAddProductType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openAddAccountTypeActivity();
                }
            });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSpinnerProductType();
    }

    public void loadSpinnerProductType() {
        categories = new ArrayList<String>();
        Cursor res = DB.getAccountTypeFor(accountTypeName);
        if (res.getCount() == 0) {
            categories.add("Add Categories");
        } else {
            while (res.moveToNext()) {
                //take category name only from 2nd(1) column
                categories.add(res.getString(1));
            }
        }
        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerProductType.setAdapter(adapter);
        res.close();
    }

    public void openActivityInventory() {
        Intent intent = new Intent(this, InventoryActivity.class);
        startActivity(intent);
    }

    public void openAddAccountTypeActivity() {
        Intent i = new Intent(this, AddAccountTypeActivity.class);
        startActivity(i);
    }
    
}
