package com.example.tuan7_retrofit2.Interface;

import com.example.tuan7_retrofit2.Model.Category;

import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;

public interface APIService {
    @GET("categories.php")
    Call<List<Category>> getCategories();
}
