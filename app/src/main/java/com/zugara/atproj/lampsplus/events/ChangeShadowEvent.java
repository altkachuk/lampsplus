package com.zugara.atproj.lampsplus.events;

/**
 * Created by andre on 27-Jan-19.
 */

public class ChangeShadowEvent {

    private float shadow;

    public ChangeShadowEvent(float shadow) {
        this.shadow = shadow;
    }

    public float getShadow() {
        return shadow;
    }
}
