package com.ttl.project.thetalklist;

/**
 * Created by Saubhagyam on 10/06/2017.
 */

public class TabBackStack {

    String ClassName;
    int tabPosition;

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public int getTabPosition() {
        return tabPosition;
    }

    public void setTabPosition(int tabPosition) {
        this.tabPosition = tabPosition;
    }

    private static final TabBackStack ourInstance = new TabBackStack();

    public static TabBackStack getInstance() {
        return ourInstance;
    }

    private TabBackStack() {
    }
}
