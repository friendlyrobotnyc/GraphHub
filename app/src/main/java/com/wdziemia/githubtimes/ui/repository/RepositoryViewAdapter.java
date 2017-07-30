package com.wdziemia.githubtimes.ui.repository;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wdziemia.githubtimes.R;
import com.wdziemia.githubtimes.util.GithubLanguageColorUtil;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.subjects.PublishSubject;

public class RepositoryViewAdapter
        extends RecyclerView.Adapter<RepositoryViewAdapter.RepositoryViewHolder> {

    private final LayoutInflater inflater;
    private List<Repository> items;
    private final NumberFormat numFormatter;

    private PublishSubject<Repository> itemClickPublishSubject = PublishSubject.create();

    public RepositoryViewAdapter(Context context) {
        numFormatter = NumberFormat.getIntegerInstance();
        items = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RepositoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_item_repository, parent, false);
        return new RepositoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RepositoryViewHolder holder, int position) {
        holder.item = items.get(position);
        holder.name.setText(holder.item.getName());
        holder.updated.setText(DateUtils.getRelativeTimeSpanString(
                holder.item.getPushedAt().getTime(),
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE));
        holder.desc.setText(holder.item.getDescription());

        // Tint language drawable
        if (!TextUtils.isEmpty(holder.item.getLanguage())) {
            holder.language.setVisibility(View.VISIBLE);
            holder.language.setText(holder.item.getLanguage());

            int tintColor = GithubLanguageColorUtil.getInstance().getColor(holder.item.getLanguage());
            Drawable[] languageDrawables = holder.language.getCompoundDrawables();
            Drawable toTintDrawable = languageDrawables[0].mutate();
            Drawable wrappedDrawable = DrawableCompat.wrap(toTintDrawable);
            DrawableCompat.setTint(wrappedDrawable, tintColor);

            holder.language.setCompoundDrawablesWithIntrinsicBounds(wrappedDrawable,
                    languageDrawables[1],
                    languageDrawables[2],
                    languageDrawables[3]);
        } else {
            holder.language.setVisibility(View.GONE);
        }

        holder.stars.setText(numFormatter.format(holder.item.getStargazersCount()));
        holder.forks.setText(numFormatter.format(holder.item.getForksCount()));
        holder.itemView.setOnClickListener(view -> itemClickPublishSubject.onNext(holder.item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Repository> items) {
        this.items = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    public PublishSubject<Repository> getItemClickSubject() {
        return itemClickPublishSubject;
    }

    public class RepositoryViewHolder extends RecyclerView.ViewHolder {

        public Repository item;

        @BindView(R.id.repository_name)
        TextView name;
        @BindView(R.id.repository_updated)
        TextView updated;
        @BindView(R.id.repository_desc)
        TextView desc;
        @BindView(R.id.repository_language)
        TextView language;
        @BindView(R.id.repository_stars)
        TextView stars;
        @BindView(R.id.repository_forks)
        TextView forks;

        public RepositoryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
