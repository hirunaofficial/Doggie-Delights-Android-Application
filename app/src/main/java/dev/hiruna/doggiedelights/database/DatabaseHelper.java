package dev.hiruna.doggiedelights.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import dev.hiruna.doggiedelights.entities.Order;
import dev.hiruna.doggiedelights.entities.Newsletter;
import dev.hiruna.doggiedelights.entities.Product;
import dev.hiruna.doggiedelights.entities.User;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "doggiedelights.db";
    private static final int DATABASE_VERSION = 9;

    // Table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_ORDERS = "orders";
    private static final String TABLE_ORDER_ITEMS = "order_items";
    private static final String TABLE_NEWSLETTERS = "newsletters";
    private static final String TABLE_USER_DATA = "user_data";

    // Common column names
    private static final String COLUMN_ID = "id";

    // USERS Table - column names
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role";

    // USER_DATA Table - column names
    private static final String COLUMN_PHONE_NUMBER = "phone_number";
    private static final String COLUMN_ADDRESS = "address";

    // PRODUCTS Table - column names
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_IMAGE_URL = "image_url";

    // ORDERS Table - column names
    private static final String COLUMN_ORDER_ID = "order_id";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_TOTAL_PRICE = "total_price";
    private static final String COLUMN_STATUS = "status";

    // ORDER_ITEMS Table - column names
    private static final String COLUMN_PRODUCT_ID = "product_id";
    private static final String COLUMN_QUANTITY = "quantity";

    // NEWSLETTERS Table - column names
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_AUTHOR = "author";

    // Table Create Statements
    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USERS + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USERNAME + " TEXT UNIQUE, "
            + COLUMN_EMAIL + " TEXT UNIQUE, "
            + COLUMN_PASSWORD + " TEXT, "
            + COLUMN_ROLE + " TEXT)";

    private static final String CREATE_TABLE_USER_DATA = "CREATE TABLE "
            + TABLE_USER_DATA + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USER_ID + " INTEGER UNIQUE, "
            + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_EMAIL + " TEXT NOT NULL, "
            + COLUMN_PHONE_NUMBER + " TEXT NOT NULL, "
            + COLUMN_ADDRESS + " TEXT NOT NULL, "
            + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))";

    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE "
            + TABLE_PRODUCTS + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_DESCRIPTION + " TEXT, "
            + COLUMN_PRICE + " REAL, "
            + COLUMN_IMAGE_URL + " TEXT)";

    private static final String CREATE_TABLE_ORDERS = "CREATE TABLE "
            + TABLE_ORDERS + " ("
            + COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USER_ID + " INTEGER, "
            + COLUMN_TOTAL_PRICE + " REAL, "
            + COLUMN_STATUS + " TEXT)";

    private static final String CREATE_TABLE_ORDER_ITEMS = "CREATE TABLE "
            + TABLE_ORDER_ITEMS + " ("
            + COLUMN_ORDER_ID + " INTEGER, "
            + COLUMN_PRODUCT_ID + " INTEGER, "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_DESCRIPTION + " TEXT, "
            + COLUMN_PRICE + " REAL, "
            + COLUMN_IMAGE_URL + " TEXT, "
            + COLUMN_QUANTITY + " INTEGER, "
            + "FOREIGN KEY(" + COLUMN_ORDER_ID + ") REFERENCES " + TABLE_ORDERS + "(" + COLUMN_ORDER_ID + "))";

    private static final String CREATE_TABLE_NEWSLETTERS = "CREATE TABLE "
            + TABLE_NEWSLETTERS + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TITLE + " TEXT, "
            + COLUMN_CONTENT + " TEXT, "
            + COLUMN_AUTHOR + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_ORDERS);
        db.execSQL(CREATE_TABLE_ORDER_ITEMS);
        db.execSQL(CREATE_TABLE_NEWSLETTERS);
        db.execSQL(CREATE_TABLE_USER_DATA);

        insertDefaultUsers(db);
        insertDefaultProducts(db);
        insertDefaultNewsletters(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWSLETTERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_DATA);
        onCreate(db);
    }

    // Insert default users
    private void insertDefaultUsers(SQLiteDatabase db) {
        insertUser(db, "admin", "admin@doggiedelights.com", "admin", "admin");
        insertUser(db, "user", "user@doggiedelights.com", "user", "customer");
    }

    // Insert default products
    private void insertDefaultProducts(SQLiteDatabase db) {
        insertProduct(db, "Blue Buffalo Life Protection Formula Adult Chicken & Brown Rice",
                "Premium dog food made with high-quality chicken and brown rice.",
                55.99,
                "https://telegra.ph/file/d6d5f5dab1870f7a4a3b6.png");

        insertProduct(db, "Purina Pro Plan Savor Adult Shredded Blend Chicken & Rice",
                "A mix of crunchy kibble and tender shredded pieces.",
                52.99,
                "https://telegra.ph/file/dc41cd5b8bba4f3ab8301.png");

        insertProduct(db, "Royal Canin Size Health Nutrition Small Adult Formula",
                "Specially formulated for small breeds, providing balanced nutrition.",
                49.99,
                "https://telegra.ph/file/61c00d30899b759639ead.png");

        insertProduct(db, "Hill's Science Diet Adult Large Breed Chicken & Barley Recipe",
                "Supports joint health and mobility for large breeds.",
                64.99,
                "https://telegra.ph/file/ad9f186a33c413cd0331f.png");

        insertProduct(db, "Nutro Ultra Adult Dry Dog Food",
                "A blend of high-quality protein sources and whole grains for optimal nutrition.",
                69.99,
                "https://telegra.ph/file/b404bfc25ee678137baf4.png");

        insertProduct(db, "Merrick Grain-Free Texas Beef & Sweet Potato Recipe",
                "Grain-free dog food made with Texas beef and sweet potatoes.",
                79.99,
                "https://telegra.ph/file/ad25025c5f4c27faa58bc.png");
    }

    // Insert default newsletters
    private void insertDefaultNewsletters(SQLiteDatabase db) {
        insertNewsletter(db, "The Importance of Protein in Your Dog's Diet",
                "Protein is essential for building and repairing tissues in dogs. It supports muscle growth, immune function, and overall health. Ensure your dog’s diet includes high-quality protein sources to keep them strong and healthy.",
                "Dr. John Doe");
        insertNewsletter(db, "Why Fats are Essential for Dogs",
                "Fats provide a concentrated source of energy and are crucial for maintaining healthy skin and a shiny coat. They also aid in the absorption of fat-soluble vitamins and support overall cellular function.",
                "Dr. Jane Smith");
        insertNewsletter(db, "Understanding Carbohydrates in Dog Nutrition",
                "Carbohydrates offer a quick source of energy and help with digestion. Including carbohydrates in your dog’s diet can contribute to sustained energy levels and overall well-being.",
                "Dr. Emily Johnson");
        insertNewsletter(db, "Vitamins and Minerals: Keys to a Healthy Dog",
                "Vitamins and minerals play a vital role in various bodily functions, including immune response, bone health, and metabolism. A balanced diet with the right nutrients helps keep your dog healthy and vibrant.",
                "Dr. Michael Brown");
        insertNewsletter(db, "The Role of Water in Dog Nutrition",
                "Water is fundamental for life and supports every bodily function. It aids in digestion, regulates body temperature, and keeps cells hydrated. Always ensure your dog has access to fresh, clean water.",
                "Dr. Sarah Lee");
    }

    // Insert a user into the database
    private void insertUser(SQLiteDatabase db, String username, String email, String password, String role) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE, role);
        db.insert(TABLE_USERS, null, values);
    }

    // Insert a product into the database
    private void insertProduct(SQLiteDatabase db, String name, String description, double price, String imageUrl) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_IMAGE_URL, imageUrl);
        db.insert(TABLE_PRODUCTS, null, values);
    }

    // Insert a newsletter into the database
    private void insertNewsletter(SQLiteDatabase db, String title, String content, String author) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_CONTENT, content);
        values.put(COLUMN_AUTHOR, author);
        db.insert(TABLE_NEWSLETTERS, null, values);
    }

    // Add a user
    public long addUser(String username, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE, role);
        return db.insert(TABLE_USERS, null, values);
    }

    // Add a product
    public long addProduct(String name, String description, double price, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_IMAGE_URL, imageUrl);
        return db.insert(TABLE_PRODUCTS, null, values);
    }

    // Add an order
    public long addOrder(int userId, double totalPrice, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_TOTAL_PRICE, totalPrice);
        values.put(COLUMN_STATUS, status);
        return db.insert(TABLE_ORDERS, null, values);
    }

    // Add an order item
    public void addOrderItem(long orderId, Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_ID, orderId);
        values.put(COLUMN_PRODUCT_ID, product.getId());
        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_DESCRIPTION, product.getDescription());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_IMAGE_URL, product.getImageUrl());
        values.put(COLUMN_QUANTITY, product.getQuantity());
        db.insert(TABLE_ORDER_ITEMS, null, values);
    }

    // Add a newsletter
    public long addNewsletter(String title, String content, String author) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_CONTENT, content);
        values.put(COLUMN_AUTHOR, author);
        return db.insert(TABLE_NEWSLETTERS, null, values);
    }

    // Get all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)));
                user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE)));
                users.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }

    // Get all products
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                product.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
                product.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL)));
                products.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return products;
    }

    // Get all orders
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_ORDERS;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int orderId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID));
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
                double totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_PRICE));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS));

                // Fetch order items
                List<Product> products = getOrderItemsByOrderId(orderId);

                User user = getUserById(userId);
                if (user != null) {
                    orders.add(new Order(orderId, user, products, status));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orders;
    }

    // Get order items by order ID
    private List<Product> getOrderItemsByOrderId(int orderId) {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_ORDER_ITEMS + " WHERE " + COLUMN_ORDER_ID + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(orderId)});

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)));
                product.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
                product.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL)));
                product.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)));
                products.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return products;
    }

    // Get order items by User ID
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_ORDERS + " WHERE " + COLUMN_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int orderId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID));
                double totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_PRICE));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS));

                // Fetch order items
                List<Product> products = getOrderItemsByOrderId(orderId);

                User user = getUserById(userId);
                if (user != null) {
                    orders.add(new Order(orderId, user, products, status));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orders;
    }


    // Get all newsletters
    public List<Newsletter> getAllNewsletters() {
        List<Newsletter> newsletters = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NEWSLETTERS;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Newsletter newsletter = new Newsletter();
                newsletter.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                newsletter.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                newsletter.setContent(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)));
                newsletter.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR)));
                newsletters.add(newsletter);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return newsletters;
    }

    // Get a user by ID
    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId)});
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE)));
            cursor.close();
            return user;
        }
        return null;
    }

    // Get a user by username or email and password
    public User getUserByUsernameOrEmail(String usernameOrEmail, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_USERS
                + " WHERE (" + COLUMN_USERNAME + " = ? OR " + COLUMN_EMAIL + " = ?) AND "
                + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{usernameOrEmail, usernameOrEmail, password});
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE)));
            cursor.close();
            return user;
        }
        return null;
    }

    // Get a user by username
    public User getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{username});
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE)));
            cursor.close();
            return user;
        }
        return null;
    }

    // Get a user by email
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{email});
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE)));
            cursor.close();
            return user;
        }
        return null;
    }

    public int deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_USERS, COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
    }

    public int deleteArticle(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NEWSLETTERS, COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
    }

    public void updateOrderStatus(int orderId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, newStatus);
        db.update(TABLE_ORDERS, values, COLUMN_ORDER_ID + "=?", new String[]{String.valueOf(orderId)});
    }

    public boolean updateUserProfile(int userId, String name, String email, String phoneNumber, String address) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PHONE_NUMBER, phoneNumber);
        values.put(COLUMN_ADDRESS, address);

        // Check if the profile exists
        Cursor cursor = db.query(TABLE_USER_DATA, null, COLUMN_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            // Profile exists, update it
            long result = db.update(TABLE_USER_DATA, values, COLUMN_USER_ID + "=?", new String[]{String.valueOf(userId)});
            cursor.close();
            return result != -1;
        } else {
            // Profile doesn't exist, insert new record
            long result = db.insert(TABLE_USER_DATA, null, values);
            return result != -1;
        }
    }

    public Cursor getUserProfile(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USER_DATA, null, COLUMN_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
    }

}