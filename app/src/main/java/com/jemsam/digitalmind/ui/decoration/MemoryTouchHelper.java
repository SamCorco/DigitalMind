package com.jemsam.digitalmind.ui.decoration;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.jemsam.digitalmind.ui.adapter.MemoryAdapter;


/**
 * Created by jeremy.toussaint on 25/10/16.
 */

public class MemoryTouchHelper extends ItemTouchHelper.SimpleCallback {
    private MemoryAdapter memoryAdapter;

    public MemoryTouchHelper(MemoryAdapter memoryAdapter){
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.memoryAdapter = memoryAdapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //TODO: Not implemented here
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //Remove item
        memoryAdapter.remove(viewHolder.getAdapterPosition());
    }
}