package com.ttl.project.thetalklist;

/**
 * Created by Saubhagyam on 03/06/2017.
 */

//Navigation drawer item bean class

public class DrawerModel {
    public int icon;
    public String name;
    public boolean isSelected = false;

    // Constructor.
    public DrawerModel(int icon, String name) {

        this.icon = icon;
        this.name = name;
    }

    public DrawerModel( int icon, String name, boolean isSelected ) {
        this.icon = icon;
        this.name = name;
        this.isSelected = isSelected;
    }
}
