package com.apps.newsapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewListAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    ArrayList<News> items;
    private final NewsItemClicked listener;

    public NewListAdapter(ArrayList<News> i, NewsItemClicked newsItemClicked) {
        this.items = i;
        this.listener = newsItemClicked;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_news, parent, false);
        NewsViewHolder viewHolder = new NewsViewHolder(listItem);

        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(items.get(viewHolder.getAdapterPosition()));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News currentItem = items.get(position);
        holder.titleView.setText(currentItem.getTitle());
        holder.descriptionView.setText(currentItem.getDesc());
        holder.number.setText(String.valueOf(position + 1));
        Picasso.get().load(currentItem.getImageUrl()).fit().centerInside().into(holder.newsImage);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

class NewsViewHolder extends RecyclerView.ViewHolder {

    TextView titleView;
    TextView descriptionView;
    ImageView newsImage;
    TextView number;

    public NewsViewHolder(@NonNull View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.title);
        descriptionView = itemView.findViewById(R.id.description);
        newsImage = itemView.findViewById(R.id.newsimage);
        number = itemView.findViewById(R.id.number);
    }
}

interface NewsItemClicked {
    void onItemClicked(News item);

}