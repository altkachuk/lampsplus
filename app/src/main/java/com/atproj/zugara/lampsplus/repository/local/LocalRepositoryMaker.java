package com.atproj.zugara.lampsplus.repository.local;

import com.atproj.zugara.lampsplus.repository.LampsplusRepository;
import com.atproj.zugara.lampsplus.repository.LocalLampsplusRepository;
import com.atproj.zugara.lampsplus.repository.RepositoryMaker;
import com.atproj.zugara.lampsplus.utils.FileUtils;

public class LocalRepositoryMaker implements RepositoryMaker {

    @Override
    public LampsplusRepository createRepository() {
        LocalDataParser parser = new LocalDataParser();
        parser.setFileExceptions(".xls", "effects", "ies_light");
        parser.parse(FileUtils.getAppDirectory());

        return new LocalLampsplusRepository(parser.getAllItems());
    }
}
