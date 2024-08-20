package dev.hiruna.doggiedelights.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dev.hiruna.doggiedelights.R;
import dev.hiruna.doggiedelights.database.DatabaseHelper;
import dev.hiruna.doggiedelights.entities.Newsletter;
import dev.hiruna.doggiedelights.adapters.AdminArticleAdapter;

public class AdminActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "user_pref";
    private AdminArticleAdapter articleAdapter;
    private RecyclerView rvArticles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
    }

    // Method to handle the Add User button click
    public void addUser(View view) {
        showAddUserDialog();
    }

    // Method to handle the Add Article button click
    public void addArticle(View view) {
        showAddArticleDialog();
    }

    // Method to handle the Delete Article button click
    public void deleteArticle(View view) {
        Intent intent = new Intent(this, ManageEducationalContentActivity.class);
        startActivity(intent);
    }

    // Method to handle the Manage Orders button click
    public void manageOrders(View view) {
        Intent intent = new Intent(this, ManageOrdersActivity.class);
        startActivity(intent);
    }

    // Method to handle the Delete User button click
    public void deleteUser(View view) {
        Intent intent = new Intent(this, ManageUsersActivity.class);
        startActivity(intent);
    }

    // Method to handle the Logout button click
    public void logout(View view) {
        handleLogout();
    }

    // Dialog to add a new user
    private void showAddUserDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_user, null);

        final EditText etUsername = dialogView.findViewById(R.id.etUsername);
        final EditText etEmail = dialogView.findViewById(R.id.etEmail);
        final EditText etPassword = dialogView.findViewById(R.id.etPassword);
        final Spinner spinnerUserType = dialogView.findViewById(R.id.spinnerUserType);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New User");
        builder.setView(dialogView);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String username = etUsername.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String userType = spinnerUserType.getSelectedItem().toString().toLowerCase();

                if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    long userId = dbHelper.addUser(username, email, password, userType);
                    if (userId != -1) {
                        Toast.makeText(AdminActivity.this, "User added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AdminActivity.this, "Error adding user", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);

        builder.create().show();
    }

    // Dialog to add a new article
    private void showAddArticleDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_article, null);

        final EditText etTitle = dialogView.findViewById(R.id.etArticleTitle);
        final EditText etContent = dialogView.findViewById(R.id.etArticleContent);
        final EditText etAuthor = dialogView.findViewById(R.id.etArticleAuthor);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Article");
        builder.setView(dialogView);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = etTitle.getText().toString().trim();
                String content = etContent.getText().toString().trim();
                String author = etAuthor.getText().toString().trim();

                if (!title.isEmpty() && !content.isEmpty() && !author.isEmpty()) {
                    long articleId = dbHelper.addNewsletter(title, content, author);
                    if (articleId != -1) {
                        Toast.makeText(AdminActivity.this, "Article added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AdminActivity.this, "Error adding article", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);

        builder.create().show();
    }

    private void handleLogout() {
        // Clear SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Navigate to LoginActivity
        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}