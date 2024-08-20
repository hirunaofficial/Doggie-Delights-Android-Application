package dev.hiruna.doggiedelights.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.hiruna.doggiedelights.R;
import dev.hiruna.doggiedelights.adapters.AdminUserAdapter;
import dev.hiruna.doggiedelights.database.DatabaseHelper;
import dev.hiruna.doggiedelights.entities.User;

public class ManageUsersActivity extends AppCompatActivity {

    private RecyclerView rvUsers;
    private AdminUserAdapter adminUserAdapter;
    private DatabaseHelper dbHelper;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        dbHelper = new DatabaseHelper(this);

        // Get all users from the database
        userList = dbHelper.getAllUsers();

        // Set up RecyclerView
        rvUsers = findViewById(R.id.rvUsers);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        adminUserAdapter = new AdminUserAdapter(userList, this);
        rvUsers.setAdapter(adminUserAdapter);
    }
}