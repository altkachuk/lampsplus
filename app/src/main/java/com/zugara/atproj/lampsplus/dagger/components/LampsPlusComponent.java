package com.zugara.atproj.lampsplus.dagger.components;

import com.zugara.atproj.lampsplus.dagger.scope.RuntimeScope;
import com.zugara.atproj.lampsplus.ui.activities.MainActivity;
import com.zugara.atproj.lampsplus.ui.activities.SplashActivity;
import com.zugara.atproj.lampsplus.ui.fragments.CanvasFragment;
import com.zugara.atproj.lampsplus.ui.fragments.LampsFragment;
import com.zugara.atproj.lampsplus.ui.viewholders.FileViewHolder;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by andre on 07-Dec-18.
 */

@RuntimeScope
@Component(dependencies = {
        ApplicationComponent.class,
        PicassoComponent.class,
        FileManagerComponent.class
})

@Singleton
public interface LampsPlusComponent {
    // activities
    void inject(SplashActivity splashActivity);
    void inject(MainActivity mainActivity);

    // fragments
    void inject(CanvasFragment canvasFragment);
    void inject(LampsFragment lampsFragment);

    // viewholders
    void inject(FileViewHolder fileViewHolder);
}
