package com.zugara.atproj.lampsplus.dagger.components;

import android.app.Application;
import android.content.Context;

import com.zugara.atproj.lampsplus.dagger.modules.ApplicationModule;

import dagger.Component;

/**
 * Created by andre on 07-Dec-18.
 */

@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    Application application();
    Context context();
}
