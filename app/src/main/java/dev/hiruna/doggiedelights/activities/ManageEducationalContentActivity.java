package dev.hiruna.doggiedelights.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.hiruna.doggiedelights.R;
import dev.hiruna.doggiedelights.adapters.AdminArticleAdapter;
import dev.hiruna.doggiedelights.database.DatabaseHelper;
import dev.hiruna.doggiedelights.entities.Newsletter;

public class ManageEducationalContentActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private AdminArticleAdapter articleAdapter;
    private RecyclerView rvArticles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_educational_content);

        dbHelper = new DatabaseHelper(this);
        rvArticles = findViewById(R.id.rvArticles);
        rvArticles.setLayoutManager(new LinearLayoutManager(this));

        // Load articles from the database
        List<Newsletter> articles = dbHelper.getAllNewsletters(); // Implement this method in DatabaseHelper
        articleAdapter = new AdminArticleAdapter(articles, dbHelper);
        rvArticles.setAdapter(articleAdapter);
    }

    // Method to handle the Remove Article button click
    public void removeArticle(View view) {
        int position = rvArticles.getChildAdapterPosition(rvArticles.findChildViewUnder(view.getX(), view.getY()));
        if (position != RecyclerView.NO_POSITION) {
            Newsletter article = articleAdapter.getArticleAtPosition(position); // Implement this method in AdminArticleAdapter
            dbHelper.deleteArticle(article.getId());
            articleAdapter.removeArticleAtPosition(position); // Implement this method in AdminArticleAdapter
            Toast.makeText(this, "Article removed successfully", Toast.LENGTH_SHORT).show();
        }
    }
}