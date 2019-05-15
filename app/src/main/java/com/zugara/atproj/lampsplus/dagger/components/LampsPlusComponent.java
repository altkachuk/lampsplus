package com.zugara.atproj.lampsplus.dagger.components;

import com.zugara.atproj.lampsplus.dagger.scope.RuntimeScope;
import com.zugara.atproj.lampsplus.presenters.ActionsPresenter;
import com.zugara.atproj.lampsplus.presenters.CanvasPresenter;
import com.zugara.atproj.lampsplus.presenters.CreateSessionPresenter;
import com.zugara.atproj.lampsplus.presenters.InvoicePresenter;
import com.zugara.atproj.lampsplus.presenters.LampsCanvasPresenter;
import com.zugara.atproj.lampsplus.presenters.LampsPresenter;
import com.zugara.atproj.lampsplus.ui.activities.MainActivity;
import com.zugara.atproj.lampsplus.ui.activities.SplashActivity;
import com.zugara.atproj.lampsplus.ui.fragments.ActionsFragment;
import com.zugara.atproj.lampsplus.ui.fragments.CanvasFragment;
import com.zugara.atproj.lampsplus.ui.fragments.CreateSessionFragment;
import com.zugara.atproj.lampsplus.ui.fragments.InvoiceFragment;
import com.zugara.atproj.lampsplus.ui.fragments.LampsCanvasFragment;
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
        RuntimeComponent.class
    })

@Singleton
public interface LampsPlusComponent {
    // activities
    void inject(SplashActivity splashActivity);

    void inject(MainActivity mainActivity);

    // fragments
    void inject(CreateSessionFragment createSessionFragment);

    void inject(LampsCanvasFragment lampsCanvasFragment);

    void inject(CanvasFragment canvasFragment);

    void inject(ActionsFragment actionsFragment);

    void inject(LampsFragment lampsFragment);

    void inject(InvoiceFragment invoiceFragment);

    // presenters
    void inject(CreateSessionPresenter createSessionPresenter);

    void inject(LampsPresenter lampsPresenter);

    void inject(LampsCanvasPresenter lampsCanvasPresenter);

    void inject(CanvasPresenter canvasPresenter);

    void inject(ActionsPresenter actionsPresenter);

    void inject(InvoicePresenter invoicePresenter);

    // viewholders
    void inject(FileViewHolder fileViewHolder);
}
