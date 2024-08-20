package dev.hiruna.doggiedelights.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import dev.hiruna.doggiedelights.R;
import dev.hiruna.doggiedelights.database.DatabaseHelper;
import dev.hiruna.doggiedelights.entities.Product;
import dev.hiruna.doggiedelights.entities.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    private TextView tvCheckoutSummary, tvCheckout;
    private Button btnCompleteOrder;
    private double totalPrice;
    private List<Product> cartList;
    private ImageView btnBack;
    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "user_pref";
    private static final String KEY_USER_ID = "user_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Initialize views and helpers
        tvCheckoutSummary = findViewById(R.id.tvCheckoutSummary);
        tvCheckout = findViewById(R.id.tvCheckout);
        btnCompleteOrder = findViewById(R.id.btnCompleteOrder);
        btnBack = findViewById(R.id.btnBack);
        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        // Set up back button click listener
        btnBack.setOnClickListener(view -> onBackPressed());

        // Get cart list from intent
        Intent intent = getIntent();
        cartList = intent.getParcelableArrayListExtra("cart");

        // Calculate total price and generate summary
        totalPrice = 0.0;
        StringBuilder summary = new StringBuilder();
        for (Product product : cartList) {
            double productTotal = product.getPrice() * product.getQuantity();
            totalPrice += productTotal;

            summary.append(product.getName())
                    .append("\nQuantity - ")
                    .append(product.getQuantity())
                    .append("\nTotal - $")
                    .append(String.format("%.2f", productTotal))
                    .append("\n\n");
        }

        // Remove the trailing \n\n if present
        if (summary.length() >= 2) {
            int length = summary.length();
            if (summary.substring(length - 2).equals("\n\n")) {
                summary.setLength(length - 2);
            }
        }

        StringBuilder total = new StringBuilder();
        total.append("Total - $").append(String.format("%.2f", totalPrice))
                .append("\n\nEstimated Delivery - 3-5 Business Days");

        tvCheckoutSummary.setText(summary.toString());
        tvCheckoutSummary.setGravity(Gravity.START);
        tvCheckoutSummary.setTextSize(16f);

        tvCheckout.setText(total.toString());
        tvCheckout.setGravity(Gravity.START);
        tvCheckout.setTextSize(18f);

        // Set up complete order button with confirmation dialog
        btnCompleteOrder.setOnClickListener(v -> showConfirmationDialog());
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Order")
                .setMessage("Are you sure you want to place this order?")
                .setPositiveButton("Yes", (dialog, which) -> completeOrder())
                .setNegativeButton("No", null)
                .setIcon(R.drawable.ic_launcher)
                .show();
    }

    private void completeOrder() {
        // Retrieve logged-in user's ID from SharedPreferences
        int userId = sharedPreferences.getInt(KEY_USER_ID, -1);

        if (userId == -1) {
            Toast.makeText(this, "User not logged in. Please log in to complete the order.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve user details
        User user = dbHelper.getUserById(userId);
        if (user == null) {
            Toast.makeText(this, "User details not found. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add order to the database
        long orderId = dbHelper.addOrder(userId, totalPrice, "Pending");

        // Add each item in the cart to the order_items table with full product details
        for (Product product : cartList) {
            dbHelper.addOrderItem(orderId, product);
        }

        // Show a Toast for order completion
        Toast toast = Toast.makeText(this, "Order has been successfully placed!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        // Redirect to HomeActivity with an animation
        Intent homeIntent = new Intent(CheckoutActivity.this, HomeActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);
        finish();
    }
}