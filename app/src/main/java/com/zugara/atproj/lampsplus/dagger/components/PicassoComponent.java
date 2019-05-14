package com.zugara.atproj.lampsplus.dagger.components;

import com.squareup.picasso.Picasso;
import com.zugara.atproj.lampsplus.dagger.modules.PicassoModule;

import dagger.Component;

/**
 * Created by andre on 07-Dec-18.
 */

@Component(modules = {PicassoModule.class}, dependencies = {ApplicationComponent.class,
        OkHttpClientComponent.class})
public interface PicassoComponent {
    Picasso picasso();
}
