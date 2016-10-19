package com.jemsam.digitalmind;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by jeremy.toussaint on 19/10/16.
 */

public class MemoryFragment extends Fragment {

    public static final String TAG = MemoryFragment.class.getSimpleName();
    private Memory memoryModel;
    private EditText titleEd;
    private EditText descriptionEd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memory_detail, container, false);

        titleEd = (EditText) view.findViewById(R.id.title);
        titleEd.setText(memoryModel.getTitle());

        descriptionEd = (EditText) view.findViewById(R.id.description);
        descriptionEd.setText(memoryModel.getDescription());

        return view;
    }

    public void setMemoryModel(Memory memoryModel) {
        this.memoryModel = memoryModel;
    }

    @Override
    public void onDestroy() {
        memoryModel.setTitle(titleEd.getText().toString());
        memoryModel.setDescription(descriptionEd.getText().toString());
        Memory.update(memoryModel);
        super.onDestroy();
    }
}
