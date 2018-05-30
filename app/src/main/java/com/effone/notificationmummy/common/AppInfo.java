package com.effone.notificationmummy.common;

import android.graphics.drawable.Drawable;

import com.effone.notificationmummy.model.ListData;

/**
 * Created by sumanth.peddinti on 5/24/2018.
 */

public class AppInfo extends ListData{

    String appname = "";
    String pname = "";
     int count;
     boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }
}
