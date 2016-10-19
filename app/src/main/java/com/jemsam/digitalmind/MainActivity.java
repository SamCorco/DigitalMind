package com.jemsam.digitalmind;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//*****************************//
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


/*
        Memory memory = new Memory("First memory", "My very first memory", new Date(200000000));
        memory.save();

        memory = new Memory("Second memory", "My very second memory", new Date(200100001));
        memory.save();

        memory = new Memory("Third memory", "My very third memory", new Date(200900001));
        memory.save();
*/
        List<Memory> memories = Memory.getAllMemories();

        //List<Memory> memories = Memory.sortByDate(FALSE);   //or TRUE   --sort by date

        //List<Memory> memories = Memory.sortByTitle();  --sort by title

        // key word search (with title)
        //String keyWord = "First";
        //List<Memory> memories = Memory.searchMemory(keyWord);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        MemoryAdapter memoryAdapter = new MemoryAdapter(memories);
        recyclerView.setAdapter(memoryAdapter);

    }
}
