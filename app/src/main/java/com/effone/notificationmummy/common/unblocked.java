package com.effone.notificationmummy.common;

import com.effone.notificationmummy.model.ListData;

/**
 * Created by sumanth.peddinti on 5/25/2018.
 */

public class unblocked extends ListData{
    private String appName;
    private String packName;
    private boolean blocked;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
