package com.minchainx.permission.setting;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;


import com.minchainx.permission.factory.RuntimeLRequestFactory;
import com.minchainx.permission.factory.RuntimeMRequestFactory;
import com.minchainx.permission.factory.RuntimeRequestFactory;
import com.minchainx.permission.request.RuntimeRequest;
import com.minchainx.permission.util.Permission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jimmy on 2018/8/1.
 * <p>
 * runtime permission
 */

public class Runtime {

    private static RuntimeRequestFactory FACTORY;
    private Context mContext;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FACTORY = new RuntimeMRequestFactory();
        } else {
            FACTORY = new RuntimeLRequestFactory();
        }
    }

    public Runtime(Context context) {
        this.mContext = context;
    }

    /**
     * One or more permissions.
     */
    public RuntimeRequest permission(String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {  //deal with permission group
            Set<String> permissionSet = new HashSet<>();
            for (String permission : permissions) {
                String[] group = whichGroup(permission);
                permissionSet.addAll(Arrays.asList(group));
            }
            permissions = permissionSet.toArray(new String[]{});
        } else {
            checkPermissions(permissions);
        }
        return FACTORY.create(mContext).permission(permissions);
    }

    /**
     * check permission contains group
     */
    public String[] whichGroup(String permission) {
        List<String[]> groups = Permission.getAllGroups();
        for (String[] group : groups) {
            for (String p : group) {
                if (p.equals(permission))
                    return group;
            }
        }
        return null;
    }

    /**
     * Check if the permissions are valid and each permission has been registered in manifest.xml.
     * This method will throw a exception if permissions are invalid or there is any permission
     * which is not registered in manifest.xml.
     *
     * @param permissions permissions which will be checked.
     */

    public void checkPermissions(String... permissions) {
        List<String> manifestPermissions = getManifestPermissions(mContext);
        if (permissions == null || permissions.length == 0) {
            throw new IllegalArgumentException("Please enter at least one permission.");
        }
        for (String p : permissions) {
            if (!manifestPermissions.contains(p)) {
                throw new IllegalStateException(String.format("The permission %1$s is not registered in manifest.xml", p));
            }
        }
    }

    /**
     * Get a list of permissions in the manifest.
     */
    private List<String> getManifestPermissions(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] requestedPermissions = pi.requestedPermissions;
            if (requestedPermissions == null || requestedPermissions.length == 0) {
                throw new IllegalStateException("You did not register any permissions in the manifest.xml.");
            }
            return Collections.unmodifiableList(Arrays.asList(requestedPermissions));
        } catch (PackageManager.NameNotFoundException e) {
            throw new AssertionError("Package name cannot be found.");
        }
    }

}
