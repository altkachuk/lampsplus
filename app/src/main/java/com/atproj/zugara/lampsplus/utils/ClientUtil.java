package com.atproj.zugara.lampsplus.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by andre on 07-Dec-18.
 */

public class ClientUtil {

    private static String clientUrl;
    private static String state;

    public static String getClientUrl() {
        return clientUrl != null ? clientUrl : "";
    }

    public static void setClientUrl(String buildConfig, String clientUrlMap) {
        Map<String, String> configMap = getConfigMap(clientUrlMap);
        ClientUtil.clientUrl = configMap.get(buildConfig);
    }

    public static String getState() {
        return state;
    }

    public static void setState(String buildConfig, String stateMap) {
        Map<String, String> configMap = getConfigMap(stateMap);
        ClientUtil.state = configMap.get(buildConfig);
    }

    private static Map<String, String> getConfigMap(String configString) {
        Type mapType = new TypeToken<Map<String, String>>(){}.getType();
        return new Gson().fromJson(configString, mapType);
    }
}
