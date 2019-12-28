package com.cocotreedebug.android.utils;

import android.text.TextUtils;

import com.cocotreedebug.android.manager.BaseApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * @author linqi
 * @description SecretKey工具类
 */
public class SecretKeyUtils {

    /**
     * 初始化账号密钥
     *
     * @param text
     */
    public static void initAccountSecretKey(String text) {
        DesBase64Tool.setAccountSecretKey(generateAccountKey(text));
    }

    public static String generateAccountKey(String text) {
        String key = "";
        try {
            String deviceId = InitUtils.getIMEI(BaseApplication.getApplication());
            String md5Text = MD5Utils.getMD5String(text + deviceId);
            if (md5Text != null && md5Text.length() >= 24) {
                key = md5Text.substring(md5Text.length() - 24, md5Text.length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return key;
    }

    public static String generateDefaultKey() {
        File defaultKeyFile = getDefaultKeyFile();
        String key = readFromFile(defaultKeyFile);
        if (TextUtils.isEmpty(key)) {
            try {
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
                keyGenerator.init(128);
                SecretKey secretKey = keyGenerator.generateKey();
                key = Base64Tool.encode(secretKey.getEncoded());
                saveToFile(defaultKeyFile, key);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return key;
    }

    public static File getDefaultKeyFile() {
        return new File(BaseApplication.getApplication().getFilesDir(), "default.key");
    }

    public static void saveToFile(File file, String text) {
        FileWriter fw = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file);
            fw.write(text);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String readFromFile(File file) {
        String rtn = "";
        if (file.exists()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                String tmpText = null;
                while ((tmpText = reader.readLine()) != null) {
                    rtn += tmpText;
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                    }
                }
            }
        }
        return rtn;
    }
}
