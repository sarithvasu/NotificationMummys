package com.effone.notificationmummy.model;

import android.widget.ListView;

/**
 * Created by sumanth.peddinti on 5/25/2018.
 */

public class ApplicationInform extends ListData {

    private String appName;
    private String packageName;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
