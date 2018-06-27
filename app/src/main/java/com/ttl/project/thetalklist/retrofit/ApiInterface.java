package com.ttl.project.thetalklist.retrofit;


import com.ttl.project.thetalklist.model.CountyModel;
import com.ttl.project.thetalklist.model.FilterTutorsModel;
import com.ttl.project.thetalklist.model.SearchTutorsModel;
import com.ttl.project.thetalklist.model.SearchViewModel;
import com.ttl.project.thetalklist.model.SubjectModel;
import com.ttl.project.thetalklist.model.TutorInformationModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface ApiInterface {
    @FormUrlEncoded
    @POST("tutor_info_new")
    Call<TutorInformationModel> getInformation(@Field("tutor_id") String tutor_id);

    @FormUrlEncoded
    @POST("desired_tutor_search")
    Call<SearchViewModel> getSearchItem(@Field("keyword") String keyword);

    @FormUrlEncoded
    @POST("tutorsearch_new")
    Call<SearchTutorsModel> searchTutors(@Field("keyword") String keyword);


    @POST("countries")
    Call<CountyModel> getCountryList();

    @POST("subject")
    Call<SubjectModel> getSubjectList();
}
