package com.example.customadapter.models;

public class MonHoc {
    private String name;
    private String descr;
    private int image;

    public MonHoc(String name, String descr, int image) {
        this.name = name;
        this.descr = descr;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getDescr() {
        return descr;
    }

    public int getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
}
