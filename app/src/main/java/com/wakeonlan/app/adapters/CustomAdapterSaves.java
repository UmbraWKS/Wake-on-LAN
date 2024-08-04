package com.wakeonlan.app.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import com.wakeonlan.app.Files;
import com.wakeonlan.app.R;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CustomAdapterSaves extends RecyclerView.Adapter<CustomHolderSaves> {

    private Context context;
    private List<AdapterSaveContent> objects;
    private OnItemClickListener onItemClickListener;

    public CustomAdapterSaves(Context context, List<AdapterSaveContent> objects, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.objects = objects;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CustomHolderSaves onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomHolderSaves(LayoutInflater.from(context).inflate(R.layout.custom_item_list_saves, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomHolderSaves holder, @SuppressLint("RecyclerView") int position) {
        holder.mac.setText(objects.get(position).getMac());
        holder.name.setText(objects.get(position).getName());

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(holder.itemView, position);
            }
        });
    }


    // removing an item from the adapter
    public void removeItem(int position) {
        objects.remove(position);
        notifyItemRemoved(position);
        // using a thread to perform file operations
        Thread thread  = new Thread(() ->
                Files.removeItemAtLocation(position, context)
        );
        thread.start();
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    // interface for when an item is clicked in the recyclerView
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
