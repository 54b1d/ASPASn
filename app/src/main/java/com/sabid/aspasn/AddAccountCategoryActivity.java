package com.sabid.aspasn;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class AddAccountCategoryActivity extends AppCompatActivity {
    EditText editCategoryName;
    Button btnConfirmAddAccountCategory;
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
        DB = new DBHelper(this);
        btnConfirmAddAccountCategory.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String categoryNameTXT = editCategoryName.getText().toString().trim();
                    if (categoryNameTXT != ""){
                        Boolean checkinsertdata = DB.insertAccountCategory(categoryNameTXT);
                        if (checkinsertdata) {
                            Toast.makeText(AddAccountCategoryActivity.this, "New Account Created", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddAccountCategoryActivity.this, "Not Inserted", Toast.LENGTH_SHORT).show();}
                    } else{
                        Toast.makeText(AddAccountCategoryActivity.this, "Type Category Name", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
    
    
    
}
