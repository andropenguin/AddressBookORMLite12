package com.sarltokyo.addressbookormlite12.module;

import com.sarltokyo.addressbookormlite12.app.MainActivity;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Created by osabe on 15/08/29.
 */
@Module
public class DebugDatabaseNameModule {
    private final boolean mockMode;

    public DebugDatabaseNameModule(boolean provideMocks) {
        mockMode = provideMocks;
    }

    @Provides
    @Singleton
    String provideDatabaseName() {
        if (mockMode) {
            return "test_" + MainActivity.DATABASE_NAME;
        } else {
            return MainActivity.DATABASE_NAME;
        }
    }
}
