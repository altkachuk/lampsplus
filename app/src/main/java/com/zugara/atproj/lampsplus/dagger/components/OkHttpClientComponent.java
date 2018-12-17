package com.zugara.atproj.lampsplus.dagger.components;

import com.zugara.atproj.lampsplus.dagger.modules.OkHttpClientModule;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * Created by andre on 07-Dec-18.
 */

@Component(modules = {OkHttpClientModule.class}, dependencies = {ApplicationComponent.class})
public interface OkHttpClientComponent {
    OkHttpClient okHttpClient();
}
