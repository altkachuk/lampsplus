package com.zugara.atproj.lampsplus.model.singleton;

/**
 * Created by andre on 21-Dec-18.
 */

public class SessionContext {

    private String sessionName;
    private String lastImagePath;

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String value) {
        sessionName = value;
    }

    public String getLastImagePath() {
        return lastImagePath;
    }

    public void setLastImagePath(String value) {
        lastImagePath = value;
    }
}
