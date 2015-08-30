package com.sarltokyo.addressbookormlite12;

import android.app.Application;
import com.sarltokyo.addressbookormlite12.component.DatabaseNameComponent;

/**
 * Created by osabe on 15/08/29.
 */
public class App extends Application {

    private static App sInstance;
    private DatabaseNameComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        component = DatabaseNameComponent.Initializer.init(false);
    }

    public static App getInstance() {
        return sInstance;
    }

    public DatabaseNameComponent component() {
        return component;
    }

    public void setMockMode(boolean useMock) {
        component = DatabaseNameComponent.Initializer.init(useMock);
    }
}

