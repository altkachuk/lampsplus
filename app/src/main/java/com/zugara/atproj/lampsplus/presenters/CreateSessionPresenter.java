package com.zugara.atproj.lampsplus.presenters;

import com.zugara.atproj.lampsplus.model.singleton.SessionContext;
import com.zugara.atproj.lampsplus.utils.StringUtil;
import com.zugara.atproj.lampsplus.views.CreateSessionView;

/**
 * Created by andre on 15-Dec-18.
 */

public class CreateSessionPresenter {

    private CreateSessionView view;
    private SessionContext sessionContext;

    public CreateSessionPresenter(CreateSessionView view, SessionContext sessionContext) {
        this.view = view;
        this.sessionContext = sessionContext;
    }

    public void create(String name) {
        if (!StringUtil.isSessionNameLegal(name)) {
            view.showIllegalMessage();
            return;
        }

        String sessionName = name.replace(" ", "_");
        sessionContext.setSessionName(sessionName);

        view.close();
    }
}
