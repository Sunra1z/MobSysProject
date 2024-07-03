package com.example.projectwork.DataClasses;

public class WasteDisposalItem {
    private int imageResId;
    private String title;

    public WasteDisposalItem(int imageResId, String title) {
        this.imageResId = imageResId;
        this.title = title;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getTitle() {
        return title;
    }
}

