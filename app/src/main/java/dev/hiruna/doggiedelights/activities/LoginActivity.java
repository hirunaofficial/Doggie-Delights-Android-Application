package dev.hiruna.doggiedelights.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import dev.hiruna.doggiedelights.R;
import dev.hiruna.doggiedelights.database.DatabaseHelper;
import dev.hiruna.doggiedelights.entities.User;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsernameOrEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegisterLink;
    private DatabaseHelper db;
    private SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "user_pref";
    private static final String KEY_USER_ID = "user_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        etUsernameOrEmail = findViewById(R.id.etUsernameOrEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterLink = findViewById(R.id.tvRegisterLink);

        // Initialize DatabaseHelper and SharedPreferences
        db = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        // Set up login button click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        // Set up register link click listener
        tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRegister();
            }
        });
    }

    // Method to handle login logic
    private void handleLogin() {
        String usernameOrEmail = etUsernameOrEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (usernameOrEmail.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username/email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Authenticate user
        User user = db.getUserByUsernameOrEmail(usernameOrEmail, password);
        if (user != null) {
            // Save user ID in SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(KEY_USER_ID, user.getId());
            editor.apply();

            String role = user.getRole();
            if (role != null) {
                switch (role) {
                    case "admin":
                        navigateToAdminPanel();
                        break;
                    case "customer":
                        navigateToHome();
                        break;
                    default:
                        Toast.makeText(this, "Unknown user role", Toast.LENGTH_SHORT).show();
                        break;
                }
            } else {
                Toast.makeText(this, "User role is not assigned", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Invalid credentials, please try again", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to navigate to the RegisterActivity
    private void navigateToRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    // Method to navigate to the AdminActivity
    private void navigateToAdminPanel() {
        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
        startActivity(intent);
        finish(); // Close LoginActivity so user cannot navigate back to it
    }

    // Method to navigate to the HomeActivity
    private void navigateToHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish(); // Close LoginActivity so user cannot navigate back to it
    }
}