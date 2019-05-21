package com.atproj.zugara.lampsplus.repository;

public interface BaseResponse<Response> {
    void onComplete(Response response);
    void onError(Exception e);
}
