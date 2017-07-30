package com.wdziemia.githubtimes.ui.organization;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wdziemia.githubtimes.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.subjects.PublishSubject;

public class OrganizationViewAdapter
        extends RecyclerView.Adapter<OrganizationViewAdapter.OrganizationViewHolder> {

    private List<Organization> items;
    private final LayoutInflater inflater;

    private PublishSubject<OrganizationViewHolder> itemClickPublishSubject = PublishSubject.create();

    public OrganizationViewAdapter(Context context) {
        items = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public OrganizationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_item_organization, parent, false);
        return new OrganizationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OrganizationViewHolder holder, int position) {
        holder.item = items.get(position);
        Picasso.with(holder.itemView.getContext())
                .load(holder.item.getAvatarUrl())
                .placeholder(R.color.gray)
                .into(holder.image);
        holder.name.setText(holder.item.getLogin());
        holder.itemView.setOnClickListener(view -> itemClickPublishSubject.onNext(holder));
    }

    @Override
    public void onViewRecycled(OrganizationViewHolder holder) {
        super.onViewRecycled(holder);

        // Clean up any pending image loading, and bitmaps.
        Picasso.with(holder.itemView.getContext())
                .cancelRequest(holder.image);
        holder.image.setImageDrawable(null);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Organization> items) {
        this.items = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    public PublishSubject<OrganizationViewHolder> getItemClickSubject() {
        return itemClickPublishSubject;
    }

    public class OrganizationViewHolder extends RecyclerView.ViewHolder {

        public Organization item;

        @BindView(R.id.organization_image)
        public ImageView image;
        @BindView(R.id.organization_name)
        public TextView name;

        public OrganizationViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
