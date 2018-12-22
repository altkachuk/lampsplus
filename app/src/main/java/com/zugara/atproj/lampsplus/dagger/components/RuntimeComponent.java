package com.zugara.atproj.lampsplus.dagger.components;

import com.zugara.atproj.lampsplus.dagger.modules.RuntimeModule;
import com.zugara.atproj.lampsplus.model.singleton.SessionContext;

import dagger.Component;

/**
 * Created by andre on 21-Dec-18.
 */

@Component(modules = {RuntimeModule.class})
public interface RuntimeComponent {

    SessionContext sessionContext();

}
