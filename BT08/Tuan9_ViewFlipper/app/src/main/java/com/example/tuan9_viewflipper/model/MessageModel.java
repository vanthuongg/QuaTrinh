package com.example.tuan9_viewflipper.model;

import java.util.List;

public class MessageModel {
    private boolean success;
    private String message;
    private List<ImageSlider> result;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<ImageSlider> getResult() { return result; }
}
