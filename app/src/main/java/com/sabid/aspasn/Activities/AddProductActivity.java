package com.sabid.aspasn.Activities;

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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.sabid.aspasn.DBHelper;
import com.sabid.aspasn.R;

import java.util.ArrayList;

public class AddProductActivity extends AppCompatActivity {
    double openingBalance, openingWeight;
    EditText productName, editOpeningWeight, editOpeningBalance;
    Button confirmAddProduct, viewProducts, btnAddProductType;
    Spinner spinnerProductType;
    ArrayList categories;
    ArrayAdapter adapter;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Product");

        productName = findViewById(R.id.editProductName);
        editOpeningWeight = findViewById(R.id.editOpenigWeight);
        editOpeningBalance = findViewById(R.id.editOpeningBalance);
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
                        Log.v("AddProductData", "No data in getProductTypeId cursor");
                        Toast.makeText(getApplicationContext(), "Invalid Product Type Selected", Toast.LENGTH_SHORT).show();
                    } else {
                        res.moveToNext();
                        int productTypeId = res.getInt(0);
                        String productNameText = productName.getText().toString().trim();
                        String a = editOpeningBalance.getText().toString().trim();
                        String b = editOpeningWeight.getText().toString().trim();
                        if (a.isEmpty() && b.isEmpty()) {
                            openingWeight = 0;
                            openingBalance = 0;
                        } else {
                            openingBalance = Double.parseDouble(a);
                            openingWeight = Double.parseDouble(b);
                        }
                        if (!productNameText.isEmpty()) {
                            Boolean checkInsertData = DB.insertProduct(productNameText, productTypeId, accountTypeId);
                            if (checkInsertData) {
                                res = DB.getProductIdOf(productNameText);
                                res.moveToNext();
                                int productId = res.getInt(0);
                                res.close();
                                DB.insertProductsBalance("productsBalance", productId, 1, openingWeight, 0, openingBalance, 0);
                                Toast.makeText(getApplicationContext(), productNameText + " product Account Created", Toast.LENGTH_SHORT).show();
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
                    openAddProductTypeActivity();
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
        Cursor res = DB.getProductTypes();
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
        Intent intent = new Intent(this, ProductsActivity.class);
        startActivity(intent);
    }

    public void openAddProductTypeActivity() {
        Intent i = new Intent(this, AddProductTypeActivity.class);
        startActivity(i);
    }
    
}
