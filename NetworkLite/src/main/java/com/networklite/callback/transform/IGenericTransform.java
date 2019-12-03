package com.networklite.callback.transform;

/**
 * @author linqi
 * @description
 */
public interface IGenericTransform {

    <T> T transform(String responseText, Class<T> clazz);
}
