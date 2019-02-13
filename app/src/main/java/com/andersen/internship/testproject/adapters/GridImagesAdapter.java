package com.andersen.internship.testproject.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andersen.internship.testproject.R;
import com.andersen.internship.testproject.data.Child;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GridImagesAdapter extends RecyclerView.Adapter<GridImagesAdapter.ViewHolder>{


    private List<Child> list = new ArrayList<>();
    public void setList(List<Child> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CardView view = (CardView) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.post_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Child item = list.get(i);
        ImageView imageView = viewHolder.imageView;
        Picasso
                .get()
                .load(item.getData().getThumbnail())
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.image)
        ImageView imageView;

        public ViewHolder(@NonNull CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);
        }
    }
}
