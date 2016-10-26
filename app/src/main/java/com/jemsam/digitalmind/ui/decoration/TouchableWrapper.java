package com.jemsam.digitalmind.ui.decoration;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.jemsam.digitalmind.ui.activity.MainActivity;
import com.jemsam.digitalmind.ui.fragment.CustomSupportMapFragment;
import com.jemsam.digitalmind.ui.fragment.MemoryFragment;

/**
 * Created by jeremy.toussaint on 26/10/16.
 */

public class TouchableWrapper extends FrameLayout {

    private final Context context;
    private CustomSupportMapFragment.OnTouchListener touchListener;

    public TouchableWrapper(Context context) {
        super(context);
        this.context = context;
    }

    public void setTouchListener(CustomSupportMapFragment.OnTouchListener touchListener) {
        this.touchListener = touchListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                touchListener.onTouch(true);
                break;

            case MotionEvent.ACTION_UP:
                touchListener.onTouch(false);
                break;
        }
        return super.dispatchTouchEvent(event);
    }
}