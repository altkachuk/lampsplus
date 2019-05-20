package com.atproj.zugara.lampsplus.presenters;

import android.content.Context;

import com.atproj.zugara.lampsplus.dagger.App;

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
