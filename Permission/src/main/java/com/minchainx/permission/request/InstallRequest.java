package com.minchainx.permission.request;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.minchainx.permission.base.Action;
import com.minchainx.permission.base.Rationale;
import com.minchainx.permission.util.FileProvider;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by jimmy on 2018/8/1.
 * <p>
 * use for install request permission
 */

public abstract class InstallRequest implements PRequest<File> {
    private static final String MIME_TYPE_APK = "application/vnd.android.package-archive";
    private static final int OP_REQUEST_INSTALL_PACKAGES = 66;

    protected Context mContext;

    protected File mFile;
    protected Rationale<File> mRationale;
    protected Action<File> mGranted;
    protected Action<File> mDenied;

    protected InstallRequest(Context context) {
        this.mContext = context;
    }

    /**
     * The apk file.
     *
     * @param file apk file.
     */
    public InstallRequest file(File file) {
        this.mFile = file;
        return this;
    }

    @Override
    public InstallRequest onGranted(Action<File> granted) {
        this.mGranted = granted;
        return this;
    }

    @Override
    public InstallRequest onDenied(Action<File> denied) {
        this.mDenied = denied;
        return this;
    }

    @Override
    public InstallRequest rationale(Rationale<File> rationale) {
        this.mRationale = rationale;
        return this;
    }

    /**
     * Callback acceptance status.
     */
    final void callbackGranted() {
        if (mGranted != null) {
            mGranted.onAction(mFile);
        }
    }

    /**
     * Callback rejected state.
     */
    final void callbackDenied() {
        if (mDenied != null) {
            mDenied.onAction(mFile);
        }
    }

    /**
     * Start the installation.
     */
    final void install() {
        Intent installApkIntent = new Intent();
        installApkIntent.setAction(Intent.ACTION_VIEW);
        installApkIntent.addCategory(Intent.CATEGORY_DEFAULT);
        installApkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            String auth = getFileProviderAuthority(mContext);
            if (!TextUtils.isEmpty(auth)) {
                installApkIntent.setDataAndType(FileProvider.getUriForFile(mContext, auth, mFile), MIME_TYPE_APK);
                installApkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        } else {
            installApkIntent.setDataAndType(Uri.fromFile(mFile), MIME_TYPE_APK);
        }
        if (mContext.getPackageManager().queryIntentActivities(installApkIntent, 0).size() > 0) {
            mContext.startActivity(installApkIntent);
        }
    }

    private String getFileProviderAuthority(Context context) {
        try {
            for (ProviderInfo provider : context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PROVIDERS).providers) {
                if (FileProvider.class.getName().equals(provider.name) && provider.authority.endsWith(".file.path.share")) {
                    return provider.authority;
                }
            }
        } catch (PackageManager.NameNotFoundException ignore) {
        }
        return null;
    }

    /**
     * can request package install
     */
    public final boolean canRequestPackageInstalls() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int targetSdkVersion = mContext.getApplicationInfo().targetSdkVersion;
            if (targetSdkVersion < Build.VERSION_CODES.O) {
                Class<AppOpsManager> clazz = AppOpsManager.class;
                try {
                    Method method = clazz.getDeclaredMethod("checkOpNoThrow", int.class, int.class, String.class);
                    AppOpsManager aom = (AppOpsManager) mContext.getSystemService(Context.APP_OPS_SERVICE);
                    int result = (int) method.invoke(aom, OP_REQUEST_INSTALL_PACKAGES, android.os.Process.myUid(), mContext.getPackageName());
                    return result == AppOpsManager.MODE_ALLOWED;
                } catch (Exception ignored) {
                    // Android P does not allow reflections.
                    return true;
                }
            }
            PackageManager pm = mContext.getPackageManager();
            return pm.canRequestPackageInstalls();
        }
        return true;
    }
}
