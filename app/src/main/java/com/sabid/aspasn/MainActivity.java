package com.sabid.aspasn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button btnActivityDebugButtons;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        /**toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View _v) {
                    onBackPressed();
                }});*/


        btnActivityDebugButtons = findViewById(R.id.btnActivityDebugButtons);
        
        btnActivityDebugButtons.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    openDebugButtonsActivity();
                }
            });
        
        

        

    }
    
    public void openDebugButtonsActivity(){
        Intent intent = new Intent(this, DebugButtonsActivity.class);
        startActivity(intent);
    }
    

}
