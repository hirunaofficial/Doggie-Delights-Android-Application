package dev.hiruna.doggiedelights.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import dev.hiruna.doggiedelights.R;
import dev.hiruna.doggiedelights.database.DatabaseHelper;

public class HomeActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private DatabaseHelper db;
    private static final String SHARED_PREF_NAME = "user_pref";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize SharedPreferences and DatabaseHelper
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        db = new DatabaseHelper(this);

        // Retrieve the user ID from SharedPreferences
        userId = sharedPreferences.getInt(KEY_USER_ID, -1);

        // Get the TextView for the greeting
        TextView tvGreeting = findViewById(R.id.tvGreeting);

        // Retrieve the username from SharedPreferences
        String username = sharedPreferences.getString(KEY_USERNAME, "User");

        // Generate the greeting based on the time of day
        String greeting = getGreetingMessage() + ", " + username + "!";
        tvGreeting.setText(greeting);

        // Set up navigation for buttons
        Button btnBrowseProducts = findViewById(R.id.btnBrowseProducts);
        Button btnDogNews = findViewById(R.id.btnDogNews);
        Button btnOrderDetails = findViewById(R.id.btnOrderDetails);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnUpdateProfile = findViewById(R.id.btnUpdateProfile);

        btnBrowseProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isProfileComplete()) {
                    navigateTo(BrowseProductsActivity.class);
                } else {
                    promptUpdateProfile();
                }
            }
        });

        btnDogNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isProfileComplete()) {
                    navigateTo(EducationalContentActivity.class);
                } else {
                    promptUpdateProfile();
                }
            }
        });

        btnOrderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isProfileComplete()) {
                    navigateTo(OrderDetailsActivity.class);
                } else {
                    promptUpdateProfile();
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogout();
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUpdateProfile(v);
            }
        });
    }

    private String getGreetingMessage() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (hour >= 5 && hour < 12) {
            return "Good morning";
        } else if (hour >= 12 && hour < 17) {
            return "Good afternoon";
        } else {
            return "Good evening";
        }
    }

    private void navigateTo(Class<?> activityClass) {
        Intent intent = new Intent(HomeActivity.this, activityClass);
        startActivity(intent);
    }

    private void handleLogout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // Method to handle the Update Profile button click
    public void openUpdateProfile(View view) {
        Intent intent = new Intent(HomeActivity.this, UpdateProfileActivity.class);
        startActivity(intent);
    }

    // Method to check if the user's profile is complete
    private boolean isProfileComplete() {
        // Return false if the userId is invalid
        if (userId == -1) {
            return false;
        }

        // Query the user profile from the database
        Cursor cursor = db.getUserProfile(userId);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name")).trim();
                    String email = cursor.getString(cursor.getColumnIndexOrThrow("email")).trim();
                    String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phone_number")).trim();
                    String address = cursor.getString(cursor.getColumnIndexOrThrow("address")).trim();

                    return !name.isEmpty() && !email.isEmpty() && !phoneNumber.isEmpty() && !address.isEmpty();
                } else {
                    return false;
                }
            } finally {
                cursor.close();
            }
        } else {
            return false;
        }
    }

    // Method to prompt the user to update their profile
    private void promptUpdateProfile() {
        Toast.makeText(this, "Please update your profile before proceeding.", Toast.LENGTH_LONG).show();
        openUpdateProfile(null); // Navigate to UpdateProfileActivity
    }
}