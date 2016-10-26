package com.jemsam.digitalmind;

import android.location.Location;
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
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

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
        /* Memory memory = new Memory("AAFirst memory", "AAMy very first memory", new Date(220000000));
        memory.save();*/

        List<Memory> memories = Memory.getAllMemories();

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
            case R.id.action_fav_sort:
                memoryAdapter.setMemories(Memory.getAllFavorites());
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
    public void onBackPressed() {
        super.onBackPressed();
        memoryAdapter.setMemories(Memory.getAllMemories());
    }

    /*@Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onLocationChanged(Location location) {

    }*/
}
