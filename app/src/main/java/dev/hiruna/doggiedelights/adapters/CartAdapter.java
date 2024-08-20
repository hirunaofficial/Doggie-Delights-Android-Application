package dev.hiruna.doggiedelights.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import dev.hiruna.doggiedelights.R;
import dev.hiruna.doggiedelights.entities.Product;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Product> cartList;
    private Context context;
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    // Constructor
    public CartAdapter(List<Product> cartList) {
        this.cartList = cartList;
    }

    @Override
    public int getItemViewType(int position) {
        if (cartList.isEmpty()) {
            return VIEW_TYPE_EMPTY;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == VIEW_TYPE_EMPTY) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_cart_empty, parent, false);
            return new EmptyViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
            return new CartViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CartViewHolder) {
            CartViewHolder cartHolder = (CartViewHolder) holder;
            Product product = cartList.get(position);

            cartHolder.tvProductName.setText(product.getName());
            cartHolder.tvProductDescription.setText(product.getDescription());
            cartHolder.tvProductPrice.setText(String.format("Price - $%.2f", product.getPrice()));
            cartHolder.tvQuantity.setText(String.format("Qty - %d", product.getQuantity()));

            cartHolder.btnChangeQuantity.setOnClickListener(v -> showChangeQuantityDialog(cartHolder, product, position));

            cartHolder.btnDeleteItem.setOnClickListener(v -> {
                // Handle delete item logic
                cartList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cartList.size());
                Toast.makeText(context, "Item successfully deleted", Toast.LENGTH_SHORT).show();

                if (cartList.isEmpty()) {
                    notifyDataSetChanged(); // Refresh the list to show the empty message
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cartList.isEmpty() ? 1 : cartList.size();
    }

    private void showChangeQuantityDialog(CartViewHolder holder, Product product, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_change_quantity, null);
        builder.setView(dialogView);

        EditText etNewQuantity = dialogView.findViewById(R.id.etNewQuantity);
        Button btnUpdate = dialogView.findViewById(R.id.btnUpdate);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        AlertDialog dialog = builder.create();

        btnUpdate.setOnClickListener(v -> {
            String newQuantityStr = etNewQuantity.getText().toString().trim();
            if (!newQuantityStr.isEmpty()) {
                int newQuantity = Integer.parseInt(newQuantityStr);
                product.setQuantity(newQuantity);  // Update the product's quantity
                notifyItemChanged(position);  // Refresh the RecyclerView item
                dialog.dismiss();
                Toast.makeText(context, "Quantity updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Please enter a valid quantity", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    // ViewHolder class for cart items
    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName;
        TextView tvProductDescription;
        TextView tvProductPrice;
        TextView tvQuantity;
        Button btnChangeQuantity;
        Button btnDeleteItem;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductDescription = itemView.findViewById(R.id.tvProductDescription);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnChangeQuantity = itemView.findViewById(R.id.btnChangeQuantity);
            btnDeleteItem = itemView.findViewById(R.id.btnDeleteItem);
        }
    }

    // ViewHolder class for empty cart message
    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmptyMessage;

        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmptyMessage = itemView.findViewById(R.id.tvEmptyMessage);
        }
    }
}