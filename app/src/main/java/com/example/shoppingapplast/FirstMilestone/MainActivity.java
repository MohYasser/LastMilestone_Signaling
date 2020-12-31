package com.example.shoppingapplast.FirstMilestone;


import com.example.shoppingapplast.ProductList;
import com.example.shoppingapplast.R;
import com.example.shoppingapplast.helper.SQLiteHandler;
import com.example.shoppingapplast.helper.SessionManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


public class MainActivity extends Activity {

    private SQLiteHandler db;

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnMaps = findViewById(R.id.btnMaps);
        Button btnList = findViewById(R.id.btnLinkToList);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Logout button click event
        btnLogout.setOnClickListener(v -> logoutUser());

        // Map button click event
        btnMaps.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(),
                    MapsActivity.class);
            startActivity(i);
            finish();
        });

        // List button click event
        btnList.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(),
                    ProductList.class);
            startActivity(i);
            finish();
        });
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}