package com.kantong.android.utils;

import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Java版3DES加密解密，适用于PHP版3DES加密解密(PHP语言开发的MCRYPT_3DES算法、MCRYPT_MODE_ECB模式、
 * PKCS7填充方式)
 *
 * @author G007N
 */
public class DesBase64Tool {

    public static final int TYPE_DEFAULT = 100001;
    public static final int TYPE_ACCOUNT = 100002;

    private static SecretKey sSecretKeyDefault = null; // 默认key对象
    private static SecretKey sSecretKeyAccount = null; // 账号key对象
    private static Cipher sCipher = null; // 私鈅加密对象Cipher

    static {
        try {
            sSecretKeyDefault = new SecretKeySpec(SecretKeyUtils.generateDefaultKey().getBytes(), "DESede"); // 获得密钥
            // 获得一个私鈅加密类Cipher，DESede是算法，ECB是加密模式，PKCS5Padding是填充方式
            sCipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取账号密钥
     *
     * @return
     */
    public static SecretKey getSecretKeyAccount() {
        return sSecretKeyAccount;
    }

    /**
     * 设置账号密钥
     *
     * @param key
     * @return
     */
    public static boolean setAccountSecretKey(String key) {
        if (key != null && key.length() == 24) {
            sSecretKeyAccount = new SecretKeySpec(key.getBytes(), "DESede");
            return true;
        }
        return false;
    }

    /**
     * 加密 针对URL
     *
     * @param message
     * @return
     */
    public static String desEncryptURL(String message) {
        return URLEncoder.encode(desEncrypt(message));
    }

    /**
     * 加密
     *
     * @param message
     * @return
     */
    public static String desEncrypt(String message) {
        return desEncrypt(message, TYPE_DEFAULT);
    }

    /**
     * 加密
     *
     * @param message
     * @param type
     * @return
     */
    public static String desEncrypt(String message, int type) {
        String newResult = "";// 去掉换行符后的加密字符串
        if (message != null && message.length() > 0) {
            String result = ""; // DES加密字符串
            try {
                SecretKey key = null;
                switch (type) {
                    case TYPE_ACCOUNT:
                        key = sSecretKeyAccount;
                        break;
                    case TYPE_DEFAULT:
                    default:
                        key = sSecretKeyDefault;
                        break;
                }
                if (key == null) {
                    return "";
                }
                sCipher.init(Cipher.ENCRYPT_MODE, key); // 设置工作模式为加密模式，给出密钥
                byte[] resultBytes = sCipher.doFinal(message.getBytes("UTF-8")); // 正式执行加密操作
                result = Base64Tool.encode(resultBytes);// 进行BASE64编码
                newResult = filter(result); // 去掉加密串中的换行符
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return newResult;
    }

    /**
     * 解密
     *
     * @param message
     * @return
     */
    public static String desDecrypt(String message) {
        return desDecrypt(message, TYPE_DEFAULT);
    }

    /**
     * 解密
     *
     * @param message
     * @param type
     * @return
     */
    public static String desDecrypt(String message, int type) {
        String result = "";
        if (message != null && message.length() > 0) {
            result = message;
            try {
                SecretKey key = null;
                switch (type) {
                    case TYPE_ACCOUNT:
                        key = sSecretKeyAccount;
                        break;
                    case TYPE_DEFAULT:
                    default:
                        key = sSecretKeyDefault;
                        break;
                }
                if (key == null) {
                    return message;
                }
                byte[] messageBytes = Base64Tool.decode(message); // 进行BASE64编码
                sCipher.init(Cipher.DECRYPT_MODE, key); // 设置工作模式为解密模式，给出密钥
                byte[] resultBytes = sCipher.doFinal(messageBytes);// 正式执行解密操作
                result = new String(resultBytes, "UTF-8");
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 去掉加密字符串换行符
     *
     * @param str
     * @return
     */
    public static String filter(String str) {
        String output = "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            int asc = str.charAt(i);
            if (asc != 10 && asc != 13) {
                sb.append(str.subSequence(i, i + 1));
            }
        }
        output = new String(sb);
        return output;
    }
}