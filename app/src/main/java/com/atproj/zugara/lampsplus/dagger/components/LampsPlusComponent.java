package com.atproj.zugara.lampsplus.dagger.components;

import com.atproj.zugara.lampsplus.presenters.CanvasPresenter;
import com.atproj.zugara.lampsplus.presenters.CreateSessionPresenter;
import com.atproj.zugara.lampsplus.presenters.LampsCanvasPresenter;
import com.atproj.zugara.lampsplus.presenters.LampsPresenter;
import com.atproj.zugara.lampsplus.ui.activities.MainActivity;
import com.atproj.zugara.lampsplus.ui.activities.SplashActivity;
import com.atproj.zugara.lampsplus.ui.fragments.ActionsFragment;
import com.atproj.zugara.lampsplus.ui.fragments.CanvasFragment;
import com.atproj.zugara.lampsplus.ui.fragments.CreateSessionFragment;
import com.atproj.zugara.lampsplus.ui.fragments.InvoiceFragment;
import com.atproj.zugara.lampsplus.ui.fragments.LampsFragment;
import com.atproj.zugara.lampsplus.ui.viewholders.FileViewHolder;
import com.atproj.zugara.lampsplus.dagger.scope.RuntimeScope;
import com.atproj.zugara.lampsplus.presenters.ActionsPresenter;
import com.atproj.zugara.lampsplus.presenters.InvoicePresenter;
import com.atproj.zugara.lampsplus.ui.fragments.LampsCanvasFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by andre on 07-Dec-18.
 */

@RuntimeScope
@Component(dependencies = {
        ApplicationComponent.class,
        PicassoComponent.class,
        RuntimeComponent.class,
        RepositoryComponent.class
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
