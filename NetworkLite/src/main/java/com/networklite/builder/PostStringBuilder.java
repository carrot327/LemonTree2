package com.networklite.builder;

import com.networklite.request.PostStringRequest;
import com.networklite.request.RequestCall;

import okhttp3.MediaType;

/**
 * @author linqi
 * @description
 */
public class PostStringBuilder extends OkHttpRequestBuilder<PostStringBuilder> {

    private String content;
    private MediaType mediaType;

    public PostStringBuilder content(String content) {
        this.content = content;
        return this;
    }

    public PostStringBuilder mediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public RequestCall build() {
        return new PostStringRequest(url, params, headers, content, mediaType, tag, id).build();
    }
}
