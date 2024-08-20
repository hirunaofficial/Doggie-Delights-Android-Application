package dev.hiruna.doggiedelights.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import dev.hiruna.doggiedelights.R;
import dev.hiruna.doggiedelights.entities.Order;
import dev.hiruna.doggiedelights.entities.Product;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.tvOrderId.setText("Order ID: #" + order.getId());
        holder.tvOrderStatus.setText("Status: " + order.getStatus());

        // Build a string of product names
        StringBuilder products = new StringBuilder();
        double total = 0.0;
        for (Product product : order.getProducts()) {
            if (products.length() > 0) {
                products.append(", ");
            }
            products.append(product.getName());
            total += product.getPrice();
        }
        holder.tvOrderProducts.setText("Products: " + products.toString());
        holder.tvOrderTotal.setText("Total: $" + String.format("%.2f", total));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderStatus, tvOrderProducts, tvOrderTotal;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvOrderProducts = itemView.findViewById(R.id.tvOrderProducts);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
        }
    }
}
