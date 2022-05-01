package com.sabid.aspasn;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.database.Cursor;
import android.app.AlertDialog;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final EditText name, contact,  dob;
        Button insert, update, delete, view, btnActivityCalc;
        final DBHelper DB;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View _v) {
                    onBackPressed();
                }});

        name = findViewById(R.id.edit_name);
        contact = findViewById(R.id.edit_contact);
        dob = findViewById(R.id.edit_dob);

        insert = findViewById(R.id.btn_insert);
        update = findViewById(R.id.btn_update);
        delete = findViewById(R.id.btn_delete);
        view = findViewById(R.id.btn_view);
        btnActivityCalc = findViewById(R.id.btnActivityCalc);
        DB = new DBHelper(this);
        
        btnActivityCalc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openCalculatorActivity();
            }
        });

        insert.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String nameTXT = name.getText().toString();
                    String contactTXT = contact.getText().toString();
                    String dobTXT = dob.getText().toString();

                    Boolean checkinsertdata = DB.insertuserdata(nameTXT, contactTXT, dobTXT);
                    if (checkinsertdata) {
                        Toast.makeText(MainActivity.this, "New Entry Inserted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Not Inserted", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        update.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String nameTXT = name.getText().toString();
                    String contactTXT = contact.getText().toString();
                    String dobTXT = dob.getText().toString();

                    Boolean checkupdatedata = DB.updateuserdata(nameTXT, contactTXT, dobTXT);
                    if (checkupdatedata) {
                        Toast.makeText(MainActivity.this, "Entry Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Not Updated", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        delete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String nameTXT = name.getText().toString();


                    Boolean checkdeletedata = DB.deletedata(nameTXT);
                    if (checkdeletedata) {
                        Toast.makeText(MainActivity.this, "Entry Deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Not Deleted", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Cursor res = DB.getdata();
                    if (res.getCount() == 0) {
                        Toast.makeText(MainActivity.this, "No Entry Exists", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    StringBuffer buffer = new StringBuffer();
                    while (res.moveToNext()) {
                        buffer.append("Name: " + res.getString(0) + "\n");
                        buffer.append("Contact: " + res.getString(1) + "\n");
                        buffer.append("Date of Birth: " + res.getString(2) + "\n\n");
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("user Entries");
                    builder.setMessage(buffer.toString());
                    builder.show();
                }
            });

    }
    public void openCalculatorActivity(){
        Intent intent = new Intent(this, CalculatorActivity.class);
        startActivity(intent);
    }

}
