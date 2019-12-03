package com.networklite.callback.transform;

import com.networklite.util.GsonUtils;

/**
 * @author linqi
 * @description
 */
public class JsonGenericTransform implements IGenericTransform {

    @Override
    public <T> T transform(String responseText, Class<T> clazz) {
        return GsonUtils.getGsonInstance().fromJson(responseText, clazz);
    }
}
