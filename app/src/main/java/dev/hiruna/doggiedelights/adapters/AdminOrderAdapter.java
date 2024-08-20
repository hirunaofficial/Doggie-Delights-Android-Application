package dev.hiruna.doggiedelights.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.hiruna.doggiedelights.R;
import dev.hiruna.doggiedelights.database.DatabaseHelper;
import dev.hiruna.doggiedelights.entities.Order;
import dev.hiruna.doggiedelights.entities.Product;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private Context context;
    private DatabaseHelper dbHelper;

    public AdminOrderAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_admin, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        // Set basic order details
        holder.tvOrderId.setText("Order ID: " + order.getId());
        holder.tvOrderStatus.setText("Status: " + order.getStatus());
        holder.tvOrderUser.setText("User: " + order.getUser().getUsername());
        holder.tvOrderDate.setText("Date: " + order.getOrderDate());
        holder.tvOrderAddress.setText("Address: " + order.getDeliveryAddress());

        // Calculate and display total price
        double totalPrice = 0.0;
        StringBuilder productDetails = new StringBuilder();
        for (Product product : order.getProducts()) {
            totalPrice += product.getPrice() * product.getQuantity();
            productDetails.append(product.getName())
                    .append(" x ")
                    .append(product.getQuantity())
                    .append("\n");
        }
        holder.tvOrderTotal.setText("Total: $" + String.format("%.2f", totalPrice));
        holder.tvProductDetails.setText(productDetails.toString());

        // Handle status change
        holder.spinnerOrderStatus.setSelection(getSpinnerPosition(holder.spinnerOrderStatus, order.getStatus()));
        holder.btnChangeStatus.setOnClickListener(v -> {
            String newStatus = holder.spinnerOrderStatus.getSelectedItem().toString();
            dbHelper.updateOrderStatus(order.getId(), newStatus);
            order.setStatus(newStatus);
            notifyItemChanged(position);
            Toast.makeText(context, "Order status updated", Toast.LENGTH_SHORT).show();
        });
    }

    // Helper method to set the spinner to the correct position
    private int getSpinnerPosition(Spinner spinner, String status) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(status)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderTotal, tvOrderStatus, tvOrderUser, tvOrderDate, tvOrderAddress, tvProductDetails;
        Spinner spinnerOrderStatus;
        Button btnChangeStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvOrderUser = itemView.findViewById(R.id.tvOrderUser);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderAddress = itemView.findViewById(R.id.tvOrderAddress);
            tvProductDetails = itemView.findViewById(R.id.tvProductDetails);
            spinnerOrderStatus = itemView.findViewById(R.id.spinnerOrderStatus);
            btnChangeStatus = itemView.findViewById(R.id.btnChangeStatus);
        }
    }
}