package com.atproj.zugara.lampsplus.dagger;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.atproj.zugara.lampsplus.BuildConfig;
import com.atproj.zugara.lampsplus.dagger.components.ApplicationComponent;
import com.atproj.zugara.lampsplus.dagger.components.DaggerRepositoryComponent;
import com.atproj.zugara.lampsplus.dagger.components.LampsPlusComponent;
import com.atproj.zugara.lampsplus.dagger.components.OkHttpClientComponent;
import com.atproj.zugara.lampsplus.dagger.components.PicassoComponent;
import com.atproj.zugara.lampsplus.dagger.components.RepositoryComponent;
import com.atproj.zugara.lampsplus.dagger.components.RuntimeComponent;
import com.atproj.zugara.lampsplus.dagger.modules.ApplicationModule;
import com.atproj.zugara.lampsplus.dagger.modules.OkHttpClientModule;
import com.atproj.zugara.lampsplus.dagger.modules.PicassoModule;
import com.atproj.zugara.lampsplus.dagger.modules.RepositoryModule;
import com.atproj.zugara.lampsplus.dagger.modules.RuntimeModule;
import com.atproj.zugara.lampsplus.utils.ClientUtil;
import com.atproj.zugara.lampsplus.dagger.components.DaggerApplicationComponent;
import com.atproj.zugara.lampsplus.dagger.components.DaggerLampsPlusComponent;
import com.atproj.zugara.lampsplus.dagger.components.DaggerOkHttpClientComponent;
import com.atproj.zugara.lampsplus.dagger.components.DaggerPicassoComponent;
import com.atproj.zugara.lampsplus.dagger.components.DaggerRuntimeComponent;

import java.lang.reflect.Method;

/**
 * Created by andre on 07-Dec-18.
 */

public class App extends Application {

    private static final String TAG = "App";

    ApplicationComponent applicationComponent;
    OkHttpClientComponent okHttpClientComponent;
    PicassoComponent picassoComponent;
    RuntimeComponent runtimeComponent;
    RepositoryComponent repositoryComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        ClientUtil.setClientUrl(BuildConfig.BUILD_TYPE, BuildConfig.HOST);
        ClientUtil.setState(BuildConfig.BUILD_TYPE, BuildConfig.STATE);

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

        runtimeComponent = DaggerRuntimeComponent.builder()
                .runtimeModule(new RuntimeModule())
                .build();

        repositoryComponent = DaggerRepositoryComponent.builder()
                .repositoryModule(new RepositoryModule())
                .build();
    }

    public void inject(Object object) {
        LampsPlusComponent lampsPlusComponent = DaggerLampsPlusComponent.builder()
                .applicationComponent(applicationComponent)
                .picassoComponent(picassoComponent)
                .runtimeComponent(runtimeComponent)
                .repositoryComponent(repositoryComponent)
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
