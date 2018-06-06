package com.ttl.project.thetalklist.retrofit;


import com.ttl.project.thetalklist.model.TutorInformationModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface ApiInterface {
    @FormUrlEncoded
    @POST("tutor_info_new")
    Call<TutorInformationModel> getInformation(@Field("tutor_id") String tutor_id);
}
