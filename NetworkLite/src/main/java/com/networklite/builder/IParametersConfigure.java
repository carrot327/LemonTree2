package com.networklite.builder;

import java.util.Map;

/**
 * @author linqi
 * @description
 */
public interface IParametersConfigure {

    OkHttpRequestBuilder setParams(Map<String, String> params);

    OkHttpRequestBuilder addParam(String key, String value);
}
