package com.soussidev.kotlin.navigationview.navigation;

/**
 * Created by Soussi on 06/09/2017.
 */

public class NavigationItem {

    private String title;
    private int color;
    private int imageResource;

    public NavigationItem(String title, int color, int imageResource) {
        this.title = title;
        this.color = color;
        this.imageResource = imageResource;
    }
    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
