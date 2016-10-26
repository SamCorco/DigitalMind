package com.jemsam.digitalmind.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;
import com.jemsam.digitalmind.ui.decoration.TouchableWrapper;

/**
 * Created by jeremy.toussaint on 26/10/16.
 */

public class CustomSupportMapFragment extends SupportMapFragment {
    public View mOriginalContentView;
    public TouchableWrapper mTouchView;

    public interface OnTouchListener {
         void onTouch(boolean b);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        mOriginalContentView = super.onCreateView(inflater, parent, savedInstanceState);
        mTouchView = new TouchableWrapper(getContext());
        mTouchView.addView(mOriginalContentView);
        return mTouchView;
    }

    public void setTouchListener(OnTouchListener touchListener) {
          mTouchView.setTouchListener(touchListener);
    }

    @Override
    public View getView() {
        return mOriginalContentView;
    }
}