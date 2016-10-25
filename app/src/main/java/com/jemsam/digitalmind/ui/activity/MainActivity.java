package com.jemsam.digitalmind.ui.activity;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.jemsam.digitalmind.model.Memory;

import com.jemsam.digitalmind.model.User;
import com.jemsam.digitalmind.ui.adapter.MemoryAdapter;
import com.jemsam.digitalmind.ui.decoration.MemoryTouchHelper;
import com.jemsam.digitalmind.R;
import com.jemsam.digitalmind.model.Tag;
import com.jemsam.digitalmind.model.TagMemory;
import com.jemsam.digitalmind.ui.fragment.MemoryFragment;
import com.jemsam.digitalmind.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//*****************************//


public class MainActivity extends AppCompatActivity implements MemoryAdapter.MemoryClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean isDateDescSorting;
    private MemoryAdapter memoryAdapter;
    private RecyclerView recyclerView;
    private boolean isAlphaDescSorting;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText edtSeach;
    private FloatingActionButton addNote;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* Memory memory = new Memory("AAFirst memory", "AAMy very first memory", new Date(220000000));
        memory.save();*/
        user = User.getUser(Utils.getPrefLogin(this), Utils.getPrefPassword(this));

        List<Memory> memories = Memory.getAllMemories(user);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        memoryAdapter = new MemoryAdapter(this);
        memoryAdapter.setMemories(memories);
        recyclerView.setAdapter(memoryAdapter);

        ItemTouchHelper.Callback callback = new MemoryTouchHelper(memoryAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        addNote = (FloatingActionButton) findViewById(R.id.newNote);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNote.setVisibility(View.GONE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment memoryFragment = new MemoryFragment();
                Memory memory = new Memory(new Date(), user);
                memory.save();
                ((MemoryFragment)memoryFragment).setMemoryModel(memory);
                ft.addToBackStack(MemoryFragment.TAG);
                ft.replace(R.id.container, memoryFragment);
                ft.commit();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                handleMenuSearch();
                break;
            case R.id.action_date_sort:
                isDateDescSorting = !isDateDescSorting;
                memoryAdapter.setMemories(Memory.sortByDate(isDateDescSorting, user));
                break;
            case R.id.action_alpha_sort:
                isAlphaDescSorting = !isAlphaDescSorting;
                memoryAdapter.setMemories(Memory.sortByTitle(isAlphaDescSorting, user));
                break;
            case R.id.action_fav_sort:
                memoryAdapter.setMemories(Memory.getAllFavorites(user));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void handleMenuSearch(){
        ActionBar action = getSupportActionBar();
        if(isSearchOpened){
            action.setDisplayShowCustomEnabled(false);
            action.setDisplayShowTitleEnabled(true);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);
            mSearchAction.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_search_white_24px));

            memoryAdapter.setMemories(Memory.getAllMemories(user));

            isSearchOpened = false;
        } else {
            action.setDisplayShowCustomEnabled(true);
            action.setCustomView(R.layout.search_bar);
            action.setDisplayShowTitleEnabled(false);
            edtSeach = (EditText)action.getCustomView().findViewById(R.id.edtSearch);
            edtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        doSearch();
                        return true;
                    }
                    return false;
                }
            });

            edtSeach.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);
            mSearchAction.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_cancel_black_24dp));

            isSearchOpened = true;
        }
    }

    private void doSearch() {
        List<Memory> memories = new ArrayList<>();
        Tag tag = Tag.getTagByWord(edtSeach.getText().toString(), user);
        if(tag != null){
            List<TagMemory> tagMemories = TagMemory.getAllTagMemories();
            for (TagMemory tagMemory: tagMemories){
                if (tagMemory.getTagId().equals(tag.getId()))
                {
                    Memory memory = Memory.getMemory(tagMemory.getMemoryId());
                    if (memory != null){
                        memories.add(memory);
                    }
                }
            }

        }
        memoryAdapter.setMemories(memories);
        /*Toast.makeText(this, "Heheh", Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public void memoryClicked(Memory memory) {
        addNote.setVisibility(View.GONE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment memoryFragment = new MemoryFragment();
        ((MemoryFragment)memoryFragment).setMemoryModel(memory);
        ft.addToBackStack(MemoryFragment.TAG);
        ft.replace(R.id.container, memoryFragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        addNote.setVisibility(View.VISIBLE);
        if(isSearchOpened) {
            handleMenuSearch();
            return;
        }
        super.onBackPressed();
        memoryAdapter.setMemories(Memory.getAllMemories(user));
    }
}
