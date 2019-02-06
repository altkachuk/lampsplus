package com.zugara.atproj.lampsplus.presenters;

import android.content.Context;

import com.zugara.atproj.lampsplus.model.singleton.SessionContext;
import com.zugara.atproj.lampsplus.views.CreateSessionView;

import javax.inject.Inject;

/**
 * Created by andre on 15-Dec-18.
 */

public class CreateSessionPresenter extends BasePresenter {

    @Inject
    SessionContext sessionContext;

    private CreateSessionView view;

    public CreateSessionPresenter(Context context, CreateSessionView view) {
        super(context);
        this.view = view;
    }

    public void create(String name) {
        if (!isSessionNameLegal(name)) {
            view.showIllegalMessage();
            return;
        }

        String sessionName = name.replace(" ", "_");
        sessionContext.setSessionName(sessionName);

        view.close();
    }

    private boolean isSessionNameLegal(String name) {
        if (name.length() < 1) return false;

        String reservedChars = "?:\"*|/\\<>";
        for (int i = 0;  i < name.length(); i++) {
            String ch = name.substring(i, i+1);
            if (reservedChars.indexOf(ch) > -1)
                return false;
        }
        return true;
    }
}
