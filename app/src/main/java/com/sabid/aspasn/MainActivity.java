package com.sabid.aspasn;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;


public class MainActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    Button btnDebugButtonsActivity;
    ActionBar actionBar;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        // drawer layout instance to toggle the menu icon to open 

        // drawer and back button to close drawer

        drawerLayout = findViewById(R.id.my_drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);


        // pass the Open and Close toggle for the drawer layout listener

        // to toggle the button

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();


        // to make the Navigation drawer icon always appear on the action bar
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Home");

        btnDebugButtonsActivity = findViewById(R.id.btnDebugButtonsActivity);
        btnDebugButtonsActivity.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), DebugButtonsActivity.class);
                    startActivity(intent);
                }
            });
    }

    // override the onOptionsItemSelected()

    // function to implement 

    // the item click listener callback

    // to open and close the navigation 

    // drawer when the icon is clicked

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            int id = item.getItemId();
            if (id == R.id.nav_debug) {
                startActivity(new Intent(this, DebugButtonsActivity.class));
                return true;
            } else if (id == R.id.nav_exit) {
                finishAffinity();
                return true;
            }
            return(false);
        }
        return(super.onOptionsItemSelected(item));
    }
}
