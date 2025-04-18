package com.example.tuan9_viewflipper.model;

import java.io.Serializable;

public class ImageSlider implements Serializable {
    private int id;
    private int position;
    private String avatar;

    public int getId() { return id; }
    public int getPosition() { return position; }
    public String getAvatar() { return avatar; }
}

