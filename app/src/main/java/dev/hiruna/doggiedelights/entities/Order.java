package dev.hiruna.doggiedelights.entities;

import java.util.List;

public class Order {
    private int id;
    private User user;
    private List<Product> products;
    private String status; // "pending", "completed", "canceled"
    private String orderDate; // New attribute to store the date of the order
    private String deliveryAddress; // New attribute to store the delivery address

    public Order(int id, User user, List<Product> products, String status, String orderDate, String deliveryAddress) {
        this.id = id;
        this.user = user;
        this.products = products;
        this.status = status;
        this.orderDate = orderDate;
        this.deliveryAddress = deliveryAddress;
    }

    public Order(int id, User user, List<Product> products, String status) {
        this.id = id;
        this.user = user;
        this.products = products;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public double getTotalPrice() {
        double totalPrice = 0.0;
        for (Product product : products) {
            totalPrice += product.getPrice() * product.getQuantity();
        }
        return totalPrice;
    }
}