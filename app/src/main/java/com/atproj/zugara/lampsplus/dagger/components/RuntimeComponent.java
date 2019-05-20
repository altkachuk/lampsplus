package com.atproj.zugara.lampsplus.dagger.components;

import com.atproj.zugara.lampsplus.dagger.modules.RuntimeModule;
import com.atproj.zugara.lampsplus.model.singleton.SessionContext;

import dagger.Component;

/**
 * Created by andre on 21-Dec-18.
 */

@Component(modules = {RuntimeModule.class})
public interface RuntimeComponent {

    SessionContext sessionContext();

}
