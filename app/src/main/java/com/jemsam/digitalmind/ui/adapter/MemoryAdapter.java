package com.jemsam.digitalmind.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jemsam.digitalmind.model.Memory;
import com.jemsam.digitalmind.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam on 12/10/16.
 */



public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.ViewHolder> {

    private MemoryClickListener listener;

    public interface MemoryClickListener {
        void memoryClicked(Memory memory);
    }


    List<Memory> memories = new ArrayList<>();
    private Context context;

    public MemoryAdapter(MemoryClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_memory_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Memory currentMemory = memories.get(position);
        holder.title.setText(currentMemory.getTitle());

        holder.rootCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.memoryClicked(currentMemory);
            }
        });

        holder.date.setText(currentMemory.getDate().toString());

        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               currentMemory.setIsFavorite(!(currentMemory.getIsFavorite()));
                Memory.update(currentMemory);
                notifyDataSetChanged();
            }
        });


        if(!(currentMemory.getIsFavorite())){
            holder.favoriteButton.setImageResource(R.drawable.ic_star_border_black_24dp);
        } else {
            holder.favoriteButton.setImageResource(R.drawable.ic_star_black_24dp);
        }

        holder.date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.memoryClicked(currentMemory);
                }
            });


    }

    @Override
    public int getItemCount() {
        return memories.size();
    }

    public void setMemories(List<Memory> memories) {
        this.memories = memories;
        notifyDataSetChanged();
    }

    /**
     *
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView rootCardview;
        public TextView title;
        public TextView date;
        public ImageButton favoriteButton;

        public ViewHolder(View v) {
            super(v);
            rootCardview = (CardView) v.findViewById(R.id.cardview);
            title = (TextView) v.findViewById(R.id.title);
            date = (TextView) v.findViewById(R.id.date);
            favoriteButton = (ImageButton) v.findViewById(R.id.isFavorite);
        }



    }

    public void remove(int position) {
        Memory.delete(memories.get(position));
        memories.remove(position);
        notifyItemRemoved(position);
    }

}
