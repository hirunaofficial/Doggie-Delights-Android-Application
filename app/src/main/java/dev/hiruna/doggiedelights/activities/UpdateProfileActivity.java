package dev.hiruna.doggiedelights.activities;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import dev.hiruna.doggiedelights.R;
import dev.hiruna.doggiedelights.database.DatabaseHelper;

public class UpdateProfileActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "user_pref";
    private EditText etName, etEmail, etPhoneNumber, etAddress;
    private DatabaseHelper db;
    private int userId;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        btnBack = findViewById(R.id.btnBack);

        // Set up back button click listener
        btnBack.setOnClickListener(view -> onBackPressed());

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        // Initialize DatabaseHelper
        db = new DatabaseHelper(this);

        // Initialize EditText fields
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etAddress = findViewById(R.id.etAddress);

        // Load user data (if available) to pre-fill the form
        loadUserData();
    }

    private void loadUserData() {
        // Fetch user ID from SharedPreferences
        userId = sharedPreferences.getInt("user_id", -1);

        if (userId != -1) {
            // Fetch user data from user_data table in the database and set to EditText fields
            Cursor cursor = db.getUserProfile(userId);
            if (cursor.moveToFirst()) {
                etName.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                etEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                etPhoneNumber.setText(cursor.getString(cursor.getColumnIndexOrThrow("phone_number")));
                etAddress.setText(cursor.getString(cursor.getColumnIndexOrThrow("address")));
            }
            cursor.close();
        }
    }

    public void saveProfile(View view) {
        // Collect data from EditText fields
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (validateInput(name, email, phoneNumber, address)) {
            // Save updated profile details to the user_data table in the database
            boolean result = db.updateUserProfile(userId, name, email, phoneNumber, address);
            if (result) {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInput(String name, String email, String phoneNumber, String address) {
        if (name.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!phoneNumber.matches("^\\+?[0-9]{10,13}$")) {
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (name.length() < 3) {
            Toast.makeText(this, "Name must be at least 3 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (address.length() < 5) {
            Toast.makeText(this, "Address must be at least 5 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
