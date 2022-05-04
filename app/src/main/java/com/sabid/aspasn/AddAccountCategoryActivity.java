package com.sabid.aspasn;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.database.Cursor;
import java.util.ArrayList;

public class AddAccountCategoryActivity extends AppCompatActivity {
    EditText editCategoryName;
    Button btnConfirmAddAccountCategory;
    ListView listAccountCategory;
    ArrayAdapter adapter;
    ArrayList<String> categories;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account_category);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View _v) {
                    onBackPressed();
                }});
        editCategoryName = findViewById(R.id.editCategoryName);
        btnConfirmAddAccountCategory = findViewById(R.id.btnConfirmAddAccountCategory);
        listAccountCategory = findViewById(R.id.listAccountCategory);
        DB = new DBHelper(this);
        
        loadCategoryList();
        
        btnConfirmAddAccountCategory.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String categoryNameTXT = editCategoryName.getText().toString().trim();
                    if (!categoryNameTXT.isEmpty()){
                        Boolean checkinsertdata = DB.insertAccountCategory(categoryNameTXT);
                        if (checkinsertdata) {
                            Toast.makeText(AddAccountCategoryActivity.this, "New Account Created", Toast.LENGTH_SHORT).show();
                            // Refresh List after new cr
                            loadCategoryList();
                        } else {
                            Toast.makeText(AddAccountCategoryActivity.this, "Not Inserted", Toast.LENGTH_SHORT).show();}
                    } else{
                        Toast.makeText(AddAccountCategoryActivity.this, "Type Category Name", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
    
    public void loadCategoryList(){
        categories = new ArrayList<String>();
        Cursor res = DB.getAccountCategories();
        if (res.getCount()==0){
            categories.add("Add Categories");
        } else {
            while(res.moveToNext()){
                //take category name only from 2nd(1) column
                categories.add(res.getString(1));
            }
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,categories);
        listAccountCategory.setAdapter(adapter);
        
    }
    
    
    
}
