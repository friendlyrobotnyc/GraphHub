package com.wdziemia.githubtimes.ui.repository;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wdziemia.githubtimes.R;
import com.wdziemia.githubtimes.RepoQuery;

import java.text.NumberFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.subjects.PublishSubject;

public class RepositoryViewAdapter
        extends RecyclerView.Adapter<RepositoryViewAdapter.RepositoryViewHolder> {

    private LayoutInflater inflater;
    private RepoQuery.Repositories items;
    private final NumberFormat numFormatter;

    private PublishSubject<RepoQuery.Edge> itemClickPublishSubject = PublishSubject.create();

    public RepositoryViewAdapter(Context context) {
        numFormatter = NumberFormat.getIntegerInstance();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RepositoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_item_repository, parent, false);
        return new RepositoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RepositoryViewHolder holder, int position) {
        holder.item = items.edges().get(position);
        holder.name.setText(holder.item.node().name());
        holder.stars.setText(numFormatter.format(holder.item.node().stargazers().totalCount()));
        holder.itemView.setOnClickListener(view -> itemClickPublishSubject.onNext(holder.item));
    }

    @Override
    public int getItemCount() {
        if(items==null||items.edges()==null){
            return 0;
        }
        return items.edges().size();
    }

    public void setItems(RepoQuery.Repositories items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public PublishSubject<RepoQuery.Edge> getItemClickSubject() {
        return itemClickPublishSubject;
    }

    public class RepositoryViewHolder extends RecyclerView.ViewHolder {

        public RepoQuery.Edge item;

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
