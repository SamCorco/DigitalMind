package com.jemsam.digitalmind;

import com.orm.SugarApp;
import com.orm.SugarContext;


public class Application extends SugarApp {

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }

}
