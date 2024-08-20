package dev.hiruna.doggiedelights.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import dev.hiruna.doggiedelights.R;
import dev.hiruna.doggiedelights.adapters.ProductAdapter;
import dev.hiruna.doggiedelights.database.DatabaseHelper;
import dev.hiruna.doggiedelights.entities.Product;

public class BrowseProductsActivity extends AppCompatActivity implements ProductAdapter.OnAddToCartClickListener {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private DatabaseHelper databaseHelper;
    private EditText etSearch;
    private List<Product> productList;
    private Button btnMoveToCart;
    private Button btnSearch;
    private ImageView btnBack;
    private List<Product> cartList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_products);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        etSearch = findViewById(R.id.etSearch);
        btnMoveToCart = findViewById(R.id.btnMoveToCart);
        btnSearch = findViewById(R.id.btnSearch);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(view -> onBackPressed());

        databaseHelper = new DatabaseHelper(this);
        productList = databaseHelper.getAllProducts();

        productAdapter = new ProductAdapter(productList, this);
        recyclerView.setAdapter(productAdapter);

        btnSearch.setOnClickListener(view -> filterProducts(etSearch.getText().toString()));

        btnMoveToCart.setOnClickListener(view -> {
            Intent intent = new Intent(BrowseProductsActivity.this, CartActivity.class);
            intent.putParcelableArrayListExtra("cart", new ArrayList<>(cartList));
            startActivity(intent);
        });
    }

    @Override
    public void onAddToCartClick(Product product) {
        if (!cartList.contains(product)) {
            product.setQuantity(1);
            cartList.add(product);
            Toast.makeText(this, product.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
        } else {
            int index = cartList.indexOf(product);
            Product cartProduct = cartList.get(index);
            cartProduct.setQuantity(cartProduct.getQuantity() + 1);
            Toast.makeText(this, product.getName() + " quantity updated in cart!", Toast.LENGTH_SHORT).show();
        }
    }

    // Filter products based on search query
    private void filterProducts(String query) {
        List<Product> filteredList = new ArrayList<>();

        if (query.isEmpty()) {
            filteredList.addAll(productList);
        } else {
            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(product);
                }
            }
        }

        if (filteredList.isEmpty() && !query.isEmpty()) {
            productAdapter.updateProductList(productList);
            Toast.makeText(this, "No matching products found. Displaying all products.", Toast.LENGTH_SHORT).show();
        } else {
            productAdapter.updateProductList(filteredList);
        }
    }
}
