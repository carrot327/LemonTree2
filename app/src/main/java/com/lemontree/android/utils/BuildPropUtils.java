package com.lemontree.android.utils;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author evanyu
 * @date 17/11/24
 */

public class BuildPropUtils {

    private static Properties sProperties;

    static {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(Environment.getRootDirectory(), "build.prop"));
            sProperties = new Properties();
            sProperties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Properties getProperties() {
        return sProperties;
    }

    public static String getProperty(String key) {
        return getProperty(key, null);
    }

    public static String getProperty(String key, String defVal) {
        String value = defVal;
        if (sProperties != null && !TextUtils.isEmpty(key)) {
            value = sProperties.getProperty(key, defVal);
        }
        return value;
    }

}
