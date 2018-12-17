package com.zugara.atproj.lampsplus.dagger;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.zugara.atproj.lampsplus.BuildConfig;
import com.zugara.atproj.lampsplus.dagger.components.ApplicationComponent;
import com.zugara.atproj.lampsplus.dagger.components.DaggerApplicationComponent;
import com.zugara.atproj.lampsplus.dagger.components.DaggerFileManagerComponent;
import com.zugara.atproj.lampsplus.dagger.components.DaggerLampsPlusComponent;
import com.zugara.atproj.lampsplus.dagger.components.DaggerOkHttpClientComponent;
import com.zugara.atproj.lampsplus.dagger.components.DaggerPicassoComponent;
import com.zugara.atproj.lampsplus.dagger.components.FileManagerComponent;
import com.zugara.atproj.lampsplus.dagger.components.LampsPlusComponent;
import com.zugara.atproj.lampsplus.dagger.components.OkHttpClientComponent;
import com.zugara.atproj.lampsplus.dagger.components.PicassoComponent;
import com.zugara.atproj.lampsplus.dagger.modules.ApplicationModule;
import com.zugara.atproj.lampsplus.dagger.modules.FileManagerModule;
import com.zugara.atproj.lampsplus.dagger.modules.OkHttpClientModule;
import com.zugara.atproj.lampsplus.dagger.modules.PicassoModule;
import com.zugara.atproj.lampsplus.utils.ClientUtil;

import java.lang.reflect.Method;

/**
 * Created by andre on 07-Dec-18.
 */

public class App extends Application {

    private final String TAG = "App";

    ApplicationComponent applicationComponent;
    OkHttpClientComponent okHttpClientComponent;
    PicassoComponent picassoComponent;
    FileManagerComponent fileManagerComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        ClientUtil.setClientUrl(BuildConfig.BUILD_TYPE, BuildConfig.HOST);

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        okHttpClientComponent = DaggerOkHttpClientComponent.builder()
                .okHttpClientModule(new OkHttpClientModule(ClientUtil.getClientUrl()))
                .applicationComponent(applicationComponent)
                .build();

        picassoComponent = DaggerPicassoComponent.builder()
                .picassoModule(new PicassoModule())
                .applicationComponent(applicationComponent)
                .okHttpClientComponent(okHttpClientComponent)
                .build();

        fileManagerComponent = DaggerFileManagerComponent.builder()
                .fileManagerModule(new FileManagerModule())
                .build();
    }

    public void inject(Object object) {
        LampsPlusComponent lampsPlusComponent = DaggerLampsPlusComponent.builder()
                .applicationComponent(applicationComponent)
                .picassoComponent(picassoComponent)
                .fileManagerComponent(fileManagerComponent)
                .build();

        try {
            Method method = lampsPlusComponent.getClass().getMethod("inject", object.getClass());
            method.invoke(lampsPlusComponent, object);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }
}
