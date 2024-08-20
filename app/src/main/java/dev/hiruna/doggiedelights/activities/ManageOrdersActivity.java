package dev.hiruna.doggiedelights.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.hiruna.doggiedelights.R;
import dev.hiruna.doggiedelights.adapters.AdminOrderAdapter;
import dev.hiruna.doggiedelights.database.DatabaseHelper;
import dev.hiruna.doggiedelights.entities.Order;

public class ManageOrdersActivity extends AppCompatActivity {

    private RecyclerView rvOrders;
    private AdminOrderAdapter adminOrderAdapter;
    private DatabaseHelper dbHelper;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_orders);

        dbHelper = new DatabaseHelper(this);

        // Get all orders from the database
        orderList = dbHelper.getAllOrders();

        // Set up RecyclerView
        rvOrders = findViewById(R.id.rvOrders);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        adminOrderAdapter = new AdminOrderAdapter(orderList, this);
        rvOrders.setAdapter(adminOrderAdapter);
    }
}
