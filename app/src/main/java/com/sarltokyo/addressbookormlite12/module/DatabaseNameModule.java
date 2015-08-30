package com.sarltokyo.addressbookormlite12.module;

import com.sarltokyo.addressbookormlite12.app.MainActivity;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Created by osabe on 15/08/29.
 */
@Module
public class DatabaseNameModule {

    DatabaseNameModule() {
    }

    @Provides
    @Singleton
    String provideDatabaseName() {
        return MainActivity.DATABASE_NAME;
    }
}

