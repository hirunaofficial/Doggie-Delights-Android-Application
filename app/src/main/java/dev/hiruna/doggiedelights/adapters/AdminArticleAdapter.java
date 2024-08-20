package dev.hiruna.doggiedelights.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.hiruna.doggiedelights.R;
import dev.hiruna.doggiedelights.database.DatabaseHelper;
import dev.hiruna.doggiedelights.entities.Newsletter;

public class AdminArticleAdapter extends RecyclerView.Adapter<AdminArticleAdapter.ArticleViewHolder> {

    private List<Newsletter> articles;
    private DatabaseHelper dbHelper;
    private Context context;

    public AdminArticleAdapter(List<Newsletter> articles, DatabaseHelper dbHelper) {
        this.articles = articles;
        this.dbHelper = dbHelper;
    }

    public void removeArticleAtPosition(int position) {
        articles.remove(position);
        notifyItemRemoved(position);
    }

    public Newsletter getArticleAtPosition(int position) {
        return articles.get(position);
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_educational_content_admin, parent, false);
        return new ArticleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Newsletter article = articles.get(position);
        holder.title.setText(article.getTitle());
        holder.author.setText("by " + article.getAuthor());

        holder.deleteButton.setOnClickListener(v -> {
            int rowsDeleted = dbHelper.deleteArticle(article.getId());
            if (rowsDeleted > 0) {
                articles.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(context, "Article deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error deleting article", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView title, author;
        Button deleteButton;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvArticleTitle);
            author = itemView.findViewById(R.id.tvArticleAuthor);
            deleteButton = itemView.findViewById(R.id.btnDeleteArticle);
        }
    }
}
