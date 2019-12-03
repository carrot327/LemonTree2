package com.update.updatesdk;

/**
 * Created by karl on 2017/10/31.
 */

public interface UpdateListener {
    void onUpdateStatuesReturned(int updateStatus, UpdateResBean updateInfo);
}
