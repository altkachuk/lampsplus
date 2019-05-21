package com.atproj.zugara.lampsplus.dagger.components;

import com.atproj.zugara.lampsplus.dagger.modules.RepositoryModule;
import com.atproj.zugara.lampsplus.repository.LampsplusRepository;

import dagger.Component;

@Component(modules = {RepositoryModule.class})
public interface RepositoryComponent {

    LampsplusRepository lampsplusRepository();

}
