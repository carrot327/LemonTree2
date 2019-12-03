package com.networklite.callback;

import com.networklite.callback.transform.IGenericTransform;
import com.networklite.callback.transform.JsonGenericTransform;

import java.lang.reflect.ParameterizedType;

import okhttp3.Response;

/**
 * @author linqi
 * @description
 */
public abstract class GenericCallback<T> extends Callback<T> {

    IGenericTransform mGenericTransform;

    public GenericCallback() {
        this(new JsonGenericTransform());
    }

    public GenericCallback(IGenericTransform transform) {
        if (transform == null) {
            throw new RuntimeException("IGenericTransform can not be null!");
        }
        this.mGenericTransform = transform;
    }

    @Override
    public T parseResponse(Response response, int id) throws Exception {
        String responseText = response.body().string();
        Class<T> targetClazz =
                (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0];
        if (targetClazz == String.class) {
            return (T) responseText;
        }
        return mGenericTransform.transform(responseText, targetClazz);
    }
}
