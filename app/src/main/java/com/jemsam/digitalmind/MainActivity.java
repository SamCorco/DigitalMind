package com.jemsam.digitalmind;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Memory memory = new Memory("First memory", "My very first memory", new Date(200000000));
        memory.save();

        memory = new Memory("Second memory", "My very second memory", new Date(200100001));
        memory.save();

        memory = new Memory("Third memory", "My very third memory", new Date(200900001));
        memory.save();

        List<Memory> memories = Memory.getAllMemories();

        for (Memory memoryItem: memories){
            Log.d(TAG, "memoryItem: " + memoryItem.getTitle());
        }
    }
}
