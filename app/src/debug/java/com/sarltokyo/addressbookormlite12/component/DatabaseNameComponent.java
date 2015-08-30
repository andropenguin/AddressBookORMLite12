package com.sarltokyo.addressbookormlite12.component;

import com.sarltokyo.addressbookormlite12.InjectedBaseActivityTest;
import com.sarltokyo.addressbookormlite12.InjectedInstrumentationTest;
import com.sarltokyo.addressbookormlite12.app.MainActivity;
import com.sarltokyo.addressbookormlite12.app.PersonListFragment;
import com.sarltokyo.addressbookormlite12.app.RegisterFragment;
import com.sarltokyo.addressbookormlite12.module.DebugDatabaseNameModule;
import dagger.Component;

import javax.inject.Singleton;

/**
 * Created by osabe on 15/08/29.
 */
@Singleton
@Component(modules = {DebugDatabaseNameModule.class})
public interface DatabaseNameComponent {

    void inject(MainActivity activity);
    void inject(PersonListFragment personListFragment);
    void inject(RegisterFragment registerFragment);
    void inject(InjectedBaseActivityTest test);
    void inject(InjectedInstrumentationTest instrumentationTest);

    public final static class Initializer {
        public static DatabaseNameComponent init(boolean mockMode) {
            return DaggerDatabaseNameComponent.builder()
                    .debugDatabaseNameModule(new DebugDatabaseNameModule(mockMode))
                    .build();
        }
    }
}
