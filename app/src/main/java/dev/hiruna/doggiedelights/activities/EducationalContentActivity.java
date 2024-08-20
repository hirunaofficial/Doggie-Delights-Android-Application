package dev.hiruna.doggiedelights.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import dev.hiruna.doggiedelights.R;
import dev.hiruna.doggiedelights.adapters.NewsletterAdapter;
import dev.hiruna.doggiedelights.database.DatabaseHelper;
import dev.hiruna.doggiedelights.entities.Newsletter;

public class EducationalContentActivity extends AppCompatActivity {

    private RecyclerView rvDogNews;
    private NewsletterAdapter newsletterAdapter;
    private List<Newsletter> newsletterList;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educational_content);

        btnBack = findViewById(R.id.btnBack);

        // Set up back button click listener
        btnBack.setOnClickListener(view -> onBackPressed());

        // Initialize RecyclerView and other UI elements
        rvDogNews = findViewById(R.id.rvDogNews);
        rvDogNews.setLayoutManager(new LinearLayoutManager(this));

        // Load newsletters from database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        newsletterList = dbHelper.getAllNewsletters();

        // Set up the adapter
        newsletterAdapter = new NewsletterAdapter(newsletterList);
        rvDogNews.setAdapter(newsletterAdapter);
    }
}
