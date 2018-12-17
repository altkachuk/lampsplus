package com.zugara.atproj.lampsplus.dagger.modules;

import android.app.Application;
import android.content.Context;

import org.riversun.okhttp3.OkHttp3CookieHelper;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by andre on 07-Dec-18.
 */

@Module
public class OkHttpClientModule {

    String baseUrl;

    public OkHttpClientModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Provides
    Cache provideHttpCache(Application application) {
        int cachSize = 10 * 1024 * 1024;
        Cache cache = new Cache(application.getCacheDir(), cachSize);
        return cache;
    }

    @Provides
    CookieJar provideCookie() {
        OkHttp3CookieHelper cookieHelper = new OkHttp3CookieHelper();
        //cookieHelper.setCookie(baseUrl, "cookie_name", "cookie_value");
        return cookieHelper.cookieJar();
    }

    @Provides
    Interceptor provideInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @Provides
    OkHttpClient provideOkHttpClient(Context context, CookieJar cookie, Cache cache, Interceptor interceptor) {
        OkHttpClient.Builder client = new OkHttpClient.Builder().cookieJar(cookie);
        client.cache(cache);
        client.addInterceptor(interceptor);
        client.followRedirects(false);
        return client.build();
    }
}
