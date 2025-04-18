package com.example.tuan9_viewflipper.config;
import com.example.tuan9_viewflipper.model.ImageSlider;
import com.example.tuan9_viewflipper.model.MessageModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {
    @FormUrlEncoded
    @POST("newimagesmanager.php")
    Call<MessageModel> loadImageSlider(@Field("position") int position);
}


