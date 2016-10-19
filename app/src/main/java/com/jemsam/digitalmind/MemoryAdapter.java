package com.jemsam.digitalmind;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam on 12/10/16.
 */



public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.ViewHolder> {


    List<Memory> memories = new ArrayList<>();



    public MemoryAdapter(List<Memory> pMemories) {
        this.memories = pMemories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_memory_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(memories.get(position).getTitle());
        holder.date.setText(memories.get(position).getDate().toString());



        if(!(memories.get(position).getIsFavorite()))
        {
            holder.isFavorite.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
        else
        {
            holder.isFavorite.setImageResource(R.drawable.ic_star_black_24dp);
        }
    }

    @Override
    public int getItemCount() {
        return memories.size();
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




/*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.element_message, null);
        TextView txtView = (TextView) convertView.findViewById(R.id.message);
        txtView.setText(messages.get(position).getMessage());
        txtView = (TextView) convertView.findViewById(R.id.login);
        txtView.setText(messages.get(position).getUser());

        if(messages.get(position).getUser().equals("Corto")){
            txtView.setBackgroundColor(Color.WHITE);
        }

        return convertView;
    }
    */
}
