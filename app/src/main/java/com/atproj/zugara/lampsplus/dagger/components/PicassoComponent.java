package com.atproj.zugara.lampsplus.dagger.components;

import com.atproj.zugara.lampsplus.dagger.modules.PicassoModule;
import com.squareup.picasso.Picasso;

import dagger.Component;

/**
 * Created by andre on 07-Dec-18.
 */

@Component(modules = {PicassoModule.class}, dependencies = {ApplicationComponent.class,
        OkHttpClientComponent.class})
public interface PicassoComponent {
    Picasso picasso();
}
