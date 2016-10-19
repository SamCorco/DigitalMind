package com.jemsam.digitalmind;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam on 12/10/16.
 */



public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.ViewHolder> {

    private MemoryClickListener listener;

    interface MemoryClickListener {
        void memoryClicked(Memory memory);
        void favoriteClicked(Memory memory);
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
        holder.title.setText(memories.get(position).getTitle());



        if (memories.get(position).getTitle() != null){
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.memoryClicked(memories.get(position));
                }
            });
            holder.date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.memoryClicked(memories.get(position));
                }
            });
        }

        holder.date.setText(memories.get(position).getDate().toString());


        if(!(memories.get(position).getIsFavorite()))
        {
            holder.isFavorite.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
        else
        {
            holder.isFavorite.setImageResource(R.drawable.ic_star_black_24dp);
        }

        holder.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.memoryClicked(memories.get(position));
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
        public TextView title;
        public TextView date;
        public ImageButton isFavorite;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            date = (TextView) v.findViewById(R.id.date);
            isFavorite = (ImageButton) v.findViewById(R.id.isFavorite);
        }



    }

}
