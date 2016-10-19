package com.jemsam.digitalmind;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Date;
import java.util.List;

//*****************************//


public class MainActivity extends AppCompatActivity implements MemoryAdapter.MemoryClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean isDateDescSorting;
    private MemoryAdapter memoryAdapter;
    private RecyclerView recyclerView;
    private boolean isAlphaDescSorting;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



/*
        Memory memory = new Memory("First memory", "My very first memory", new Date(200000000));

/*        Memory memory = new Memory("AAFirst memory", "AAMy very first memory", new Date(220000000));

        memory.save();

        memory = new Memory("CCSecond memory", "CCMy very second memory", new Date(221100001));
        memory.save();


        memory = new Memory("Third memory", "My very third memory", new Date(200900001));
        memory.save();
*/

      //  memory = new Memory("BBThird memory", "DDMy very third memory", new Date(222900001));
      //  memory.save();


        List<Memory> memories = Memory.getAllMemories();

        //List<Memory> memories = Memory.sortByDate(FALSE);   //or TRUE   --sort by date

        //List<Memory> memories = Memory.sortByTitle();  --sort by title


        // key word search (with title)
        //String keyWord = "First";
        //List<Memory> memories = Memory.searchMemory(keyWord);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        memoryAdapter = new MemoryAdapter(this);
        memoryAdapter.setMemories(memories);
        recyclerView.setAdapter(memoryAdapter);

        Button newNote = (Button) findViewById(R.id.newNote);
        newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment memoryFragment = new MemoryFragment();
                Memory memory = new Memory(new Date());
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_date_sort:
                isDateDescSorting = !isDateDescSorting;
                memoryAdapter.setMemories(Memory.sortByDate(isDateDescSorting));
                break;
            case R.id.action_alpha_sort:
                isAlphaDescSorting = !isAlphaDescSorting;
                memoryAdapter.setMemories(Memory.sortByTitle(isAlphaDescSorting));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void memoryClicked(Memory memory) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment memoryFragment = new MemoryFragment();

        ((MemoryFragment)memoryFragment).setMemoryModel(memory);
        ft.addToBackStack(MemoryFragment.TAG);
        ft.replace(R.id.container, memoryFragment);
        ft.commit();
    }

    @Override
    public void favoriteClicked(Memory memory) {
        memory.setIsfav(memory.getIsfav);
        Memory.update(memory);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        memoryAdapter.setMemories(Memory.getAllMemories());
    }
}
