package com.atproj.zugara.lampsplus.dagger.modules;

import com.atproj.zugara.lampsplus.repository.LampsplusRepository;
import com.atproj.zugara.lampsplus.repository.LocalLampsplusRepository;
import com.atproj.zugara.lampsplus.repository.RepositoryMaker;
import com.atproj.zugara.lampsplus.repository.local.LocalRepositoryMaker;
import com.atproj.zugara.lampsplus.utils.ClientUtil;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    LampsplusRepository lampsplusRepository() {
        RepositoryMaker repositoryMaker = getRepositoryMakerByState(ClientUtil.getState());
        return repositoryMaker.createRepository();
    }

    private static RepositoryMaker getRepositoryMakerByState(String state) {
        if (state.equals("local")) {
            return new LocalRepositoryMaker();
        }

        return null;
    }
}
