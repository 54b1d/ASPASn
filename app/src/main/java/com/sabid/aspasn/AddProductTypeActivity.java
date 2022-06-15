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

public class AddProductTypeActivity extends AppCompatActivity {
    String baseUnitName, interpretedUnitName, productTypeTitleText, minWeightToInterpretUnitText;
    int productTypeId, minWeightToInterpretUnit;
    EditText editProductTypeTitle, editBaseUnitName, editInterpretedUnitName, editMinWeightToInterpretUnit;
    Button btnConfirmAddProductType;
    ListView listProductTypes;
    ArrayAdapter adapter;
    ArrayList<String> productTypes;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_type);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Product Type");
        
        editProductTypeTitle = findViewById(R.id.editProductTypeTitle);
        editBaseUnitName = findViewById(R.id.editBaseUnitName);
        editInterpretedUnitName = findViewById(R.id.editInterpretedUnitName);
        editMinWeightToInterpretUnit = findViewById(R.id.editMinWeightToInterpretUnit);
        btnConfirmAddProductType = findViewById(R.id.btnConfirmAddProductType);
        listProductTypes = findViewById(R.id.listProductTypes);
        DB = new DBHelper(this);

        loadProductTypesList();

        btnConfirmAddProductType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productTypeTitleText = editProductTypeTitle.getText().toString().trim();
                    baseUnitName = editBaseUnitName.getText().toString().trim();
                    interpretedUnitName = editInterpretedUnitName.getText().toString().trim();
                    minWeightToInterpretUnitText = editMinWeightToInterpretUnit.getText().toString().trim();
                    minWeightToInterpretUnit = Integer.parseInt(minWeightToInterpretUnitText);
                    if (!productTypeTitleText.isEmpty()) {
                        Boolean checkInsertData = DB.insertProductType(productTypeTitleText, baseUnitName, interpretedUnitName, minWeightToInterpretUnit);
                        if (checkInsertData) {
                            Toast.makeText(AddProductTypeActivity.this, "New Product Type Created", Toast.LENGTH_SHORT).show();
                            editProductTypeTitle.setText("");
                            // Refresh List after new cr
                            loadProductTypesList();
                        } else {
                            Toast.makeText(AddProductTypeActivity.this, "Not Inserted", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AddProductTypeActivity.this, "Enter required values", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    public void loadProductTypesList() {
        productTypes = new ArrayList<String>();
        Cursor res = DB.getProductTypes();
        if (res.getCount() == 0) {
            productTypes.add("Add Product Type");
        } else {
            while (res.moveToNext()) {
                //take product type title only from 2nd(1) column
                productTypes.add(res.getString(1));
            }
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, productTypes);
        listProductTypes.setAdapter(adapter);
        res.close();
    }


}
