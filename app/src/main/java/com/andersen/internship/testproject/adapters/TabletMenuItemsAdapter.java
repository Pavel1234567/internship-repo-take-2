package com.andersen.internship.testproject.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.andersen.internship.testproject.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabletMenuItemsAdapter extends RecyclerView.Adapter<TabletMenuItemsAdapter.ItemViewHolder> {

    private List<Integer> menuItems;
    Listener listener;

    private int lastSelectedPosition = -1;

    public void setLastSelectedPosition(int lastSelectedPosition) {
        this.lastSelectedPosition = lastSelectedPosition;
        notifyDataSetChanged();
    }

    public int getLastSelectedPosition() {
        return lastSelectedPosition;
    }

    public TabletMenuItemsAdapter() {
        this.menuItems = new ArrayList<Integer>(){{
            add(R.drawable.ic_menu_camera);
            add(R.drawable.ic_menu_gallery);
            add(R.drawable.ic_menu_slideshow);
            add(R.drawable.ic_menu_tools);
            add(R.drawable.ic_arrow_downward_black_24dp);
        }};
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.recycler_view_item, viewGroup, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {

        int item = menuItems.get(i);

        RadioButton radioButton = itemViewHolder.radioButton;

        radioButton.setButtonDrawable(item);
        radioButton.setChecked(lastSelectedPosition == i);
        radioButton.setTag(i);
        radioButton.setOnClickListener(v -> {
                    lastSelectedPosition = (int) v.getTag();
                    notifyDataSetChanged();
                    if (listener != null)
                        listener.onClick(i);
                });
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.radio_button)
        RadioButton radioButton;

        public ItemViewHolder(View view) {

            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface Listener{
        void onClick(int position);
    }
}