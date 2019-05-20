package com.atproj.zugara.lampsplus.dagger.modules;

import com.atproj.zugara.lampsplus.model.singleton.SessionContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by andre on 21-Dec-18.
 */

@Module
public class RuntimeModule {

    private final SessionContext sessionContext;

    public RuntimeModule() {
        sessionContext = new SessionContext();
    }

    @Provides
    SessionContext sessionContext() {
        return sessionContext;
    }
}
