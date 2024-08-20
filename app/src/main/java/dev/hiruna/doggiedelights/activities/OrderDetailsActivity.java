package dev.hiruna.doggiedelights.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import dev.hiruna.doggiedelights.R;
import dev.hiruna.doggiedelights.adapters.OrderAdapter;
import dev.hiruna.doggiedelights.database.DatabaseHelper;
import dev.hiruna.doggiedelights.entities.Order;

public class OrderDetailsActivity extends AppCompatActivity {

    private RecyclerView rvOrderDetails;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;
    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private ImageView btnBack;

    private static final String SHARED_PREF_NAME = "user_pref";
    private static final String KEY_USER_ID = "user_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        btnBack = findViewById(R.id.btnBack);

        // Set up back button click listener
        btnBack.setOnClickListener(view -> onBackPressed());

        // Initialize views and helpers
        rvOrderDetails = findViewById(R.id.rvOrderDetails);
        rvOrderDetails.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        // Get logged-in user's ID from SharedPreferences
        int userId = sharedPreferences.getInt(KEY_USER_ID, -1);

        if (userId != -1) {
            // Load orders for the logged-in user from the database
            orderList = dbHelper.getOrdersByUserId(userId);

            // Set up the adapter
            orderAdapter = new OrderAdapter(orderList);
            rvOrderDetails.setAdapter(orderAdapter);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
