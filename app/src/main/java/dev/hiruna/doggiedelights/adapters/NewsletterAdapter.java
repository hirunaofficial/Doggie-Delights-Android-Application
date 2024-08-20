package dev.hiruna.doggiedelights.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import dev.hiruna.doggiedelights.R;
import dev.hiruna.doggiedelights.entities.Newsletter;

public class NewsletterAdapter extends RecyclerView.Adapter<NewsletterAdapter.NewsletterViewHolder> {

    private List<Newsletter> newsletterList;

    public NewsletterAdapter(List<Newsletter> newsletterList) {
        this.newsletterList = newsletterList;
    }

    @NonNull
    @Override
    public NewsletterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_educational_content, parent, false);
        return new NewsletterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsletterViewHolder holder, int position) {
        Newsletter newsletter = newsletterList.get(position);
        holder.tvArticleTitle.setText(newsletter.getTitle());
        holder.tvArticleAuthor.setText(String.format("By %s", newsletter.getAuthor())); // Set author
        holder.tvArticleContent.setText(newsletter.getContent());
    }

    @Override
    public int getItemCount() {
        return newsletterList.size();
    }

    static class NewsletterViewHolder extends RecyclerView.ViewHolder {
        TextView tvArticleTitle, tvArticleAuthor, tvArticleContent;

        public NewsletterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvArticleTitle = itemView.findViewById(R.id.tvArticleTitle);
            tvArticleAuthor = itemView.findViewById(R.id.tvArticleAuthor);
            tvArticleContent = itemView.findViewById(R.id.tvArticleContent);
        }
    }
}
