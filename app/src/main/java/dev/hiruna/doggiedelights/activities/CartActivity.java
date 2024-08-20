package dev.hiruna.doggiedelights.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dev.hiruna.doggiedelights.R;
import dev.hiruna.doggiedelights.adapters.CartAdapter;
import dev.hiruna.doggiedelights.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<Product> cartList;
    private Button btnCheckout;
    private TextView tvCartTitle;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewCart);
        tvCartTitle = findViewById(R.id.tvCartTitle);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnBack = findViewById(R.id.btnBack);

        // Set up back button click listener
        btnBack.setOnClickListener(view -> onBackPressed());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get cart list from intent
        Intent intent = getIntent();
        cartList = intent.getParcelableArrayListExtra("cart");

        // Initialize cartList if it's null
        if (cartList == null) {
            cartList = new ArrayList<>();
        }

        // Set up adapter
        cartAdapter = new CartAdapter(cartList);
        recyclerView.setAdapter(cartAdapter);

        // Calculate total price
        double totalPrice = cartList.stream().mapToDouble(product -> product.getPrice() * product.getQuantity()).sum();

        // Set up checkout button
        btnCheckout.setOnClickListener(v -> {
            // Handle checkout action
            Toast.makeText(CartActivity.this, "Proceeding to checkout", Toast.LENGTH_SHORT).show();
            // Start CheckoutActivity with cart details
            Intent checkoutIntent = new Intent(CartActivity.this, CheckoutActivity.class);
            checkoutIntent.putParcelableArrayListExtra("cart", new ArrayList<>(cartList));
            checkoutIntent.putExtra("totalPrice", totalPrice);
            startActivity(checkoutIntent);
        });
    }
}