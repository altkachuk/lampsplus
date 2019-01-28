package com.zugara.atproj.lampsplus.presenters;

import android.content.Context;

import com.zugara.atproj.lampsplus.dagger.App;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by andre on 27-Jan-19.
 */

public class BasePresenter {

    protected Context context;

    public BasePresenter(Context context) {
        App.get(context).inject(this);
        this.context = context;
    }
}
