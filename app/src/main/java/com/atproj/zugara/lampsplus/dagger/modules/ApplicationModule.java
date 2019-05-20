package com.atproj.zugara.lampsplus.dagger.modules;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by andre on 07-Dec-18.
 */

@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    Application application() {
        return this.application;
    }

    @Provides
    Context context() {
        return this.application.getApplicationContext();
    }
}
