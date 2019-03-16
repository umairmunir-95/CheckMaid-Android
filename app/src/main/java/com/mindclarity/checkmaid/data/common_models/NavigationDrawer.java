package com.mindclarity.checkmaid.data.common_models;

public class NavigationDrawer  {

    String iconName = "";
    int iconImg = -1; // menu icon resource id
    boolean isEnabled = false;
    public boolean isEnabled() {
        return isEnabled;
    }
    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
    public String getIconName() {
        return iconName;
    }
    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
    public int getIconImg() {
        return iconImg;
    }
    public void setIconImg(int iconImg) {
        this.iconImg = iconImg;
    }
}

