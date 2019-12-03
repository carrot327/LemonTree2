package com.lemontree.android.setting;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;

/**
 * Created by WZTENG on 2017/10/23 0023.
 */

public class ConfigLoad {
    boolean mExternalStorageAvailable = false;//外部存储是否可用
    boolean mExternalStorageWriteable = false;//可写
    boolean mExternalStorageReadable = false;//可读
    Context context;

    public static String soPath = "";

    public static final String externalSOFile = "libIDCARDDLL.so";//外部so文件名称
    public static final String internalSOFile = "libocrengine.so";//内部so文件名称


    public ConfigLoad(Context context) {
        this.context = context;
    }

    /**
     * 检查内部是否已经存在
     *
     * @return
     */
    public boolean checkInternalSOState(String fileName) {
        String dir = context.getFilesDir().getAbsolutePath();
        dir = dir + File.separator + fileName;
        if ("libocrengine.so".equals(fileName)) {
            //必须使用getFilesDir()获取路径
            soPath = context.getFilesDir().getAbsolutePath() + File.separator + "libocrengine.so";
        }
        File distFile = new File(dir);
        boolean b = distFile.exists();
        return b;
    }

    public String findLibrary1(Context context, String libName) {
        String result = null;
        ClassLoader classLoader = (context.getClassLoader());
        if (classLoader != null) {
            try {
                Method findLibraryMethod = classLoader.getClass().getMethod("findLibrary", new Class<?>[]{String.class});
                if (findLibraryMethod != null) {
                    Object objPath = findLibraryMethod.invoke(classLoader, new Object[]{libName});
                    if (objPath != null && objPath instanceof String) {
                        result = (String) objPath;
                    }
                }
            } catch (NoSuchMethodException e) {
                Log.e("findLibrary1", e.toString());
            } catch (IllegalAccessException e) {
                Log.e("findLibrary1", e.toString());
            } catch (IllegalArgumentException e) {
                Log.e("findLibrary1", e.toString());
            } catch (InvocationTargetException e) {
                Log.e("findLibrary1", e.toString());
            } catch (Exception e) {
                Log.e("findLibrary1", e.toString());
            }
        }

        return result;
    }

    /**
     * 复制apk原so文件
     *
     * @return
     */
    public boolean copyApkDistFile2PrivatePath() {
        File distFile = new File(findLibrary1(context, "IDCARDDLL"));
        //先检查外部存储状态
        checkExternal();
        if (mExternalStorageWriteable) {
            String dir = context.getFilesDir().getAbsolutePath();
            dir = dir + File.separator + "libIDCARDDLLcopy.so";
            File files = new File(dir);
            //复制文件
            boolean isOk = fileChannelCopy(distFile, files);
            return isOk;
        }
        return false;
    }


    /**
     * 复制文件管理器选中的文件到私有目录
     *
     * @param copyPath
     * @return
     */
    public boolean copySO2PrivatePath(String copyPath) {
        String dir = context.getFilesDir().getAbsolutePath();
        dir = dir + File.separator + "libocrengine.so";
        File distFile = new File(dir);
        //先检查外部存储状态
        checkExternal();
        if (mExternalStorageWriteable) {
            File file = new File(copyPath);
            if (!file.exists()) {
                return false;
            }
            //复制文件
            boolean isOk = fileChannelCopy(file, distFile);
            return isOk;
        }
        return false;
    }

    /**
     * 复制so到apk目录，但是没有权限
     * @return
     */
    public boolean copySO2ApkPath() {
        File distFile = new File(findLibrary1(context, "IDCARDDLL"));
        //先检查外部存储状态
        checkExternal();
        if (mExternalStorageWriteable) {
            String dir = context.getFilesDir().getAbsolutePath();
            dir = dir + File.separator + "libocrengine.so";
            File file = new File(dir);
            if (!file.exists()) {
                return false;
            }
            //复制文件
            boolean isOk = fileChannelCopy(file, distFile);
            return isOk;
        }
        return false;
    }

    /**
     * 使用文件通道的方式复制文件
     *
     * @param source 源文件
     * @param dest   新文件
     */
    public boolean fileChannelCopy(File source, File dest) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(source);
            fo = new FileOutputStream(dest);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
//            Log.d("wzt", "so复制完成");
            return true;
        } catch (IOException e) {
//            Log.d("wzt", "so复制异常");
            e.printStackTrace();
            return false;
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 检查外部存储的状态
     */
    public void checkExternal() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // 可读写
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = true;
            mExternalStorageReadable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // 只读
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
            mExternalStorageReadable = true;
        } else {
            // 错误
            mExternalStorageAvailable = false;
            mExternalStorageWriteable = false;
            mExternalStorageReadable = false;
        }
    }
}
