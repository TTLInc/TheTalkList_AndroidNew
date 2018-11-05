package com.ttl.project.thetalklist.retrofit;


import com.ttl.project.thetalklist.model.AllSearchDataModel;
import com.ttl.project.thetalklist.model.CountyModel;
import com.ttl.project.thetalklist.model.DeviceIdModel;
import com.ttl.project.thetalklist.model.SearchTutorsModel;
import com.ttl.project.thetalklist.model.SearchViewModel;
import com.ttl.project.thetalklist.model.SubjectModel;
import com.ttl.project.thetalklist.model.TutorInformationModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface ApiInterface {
    //https://www.thetalklist.com/api/tutor_info_new
    @FormUrlEncoded
    @POST("tutor_info_new")
    Call<TutorInformationModel> getInformation(@Field("tutor_id") String tutor_id);

    //https://www.thetalklist.com/api/desired_tutor_search
    @FormUrlEncoded
    @POST("desired_tutor_search")
    Call<SearchViewModel> getSearchItem(@Field("keyword") String keyword);

    //https://www.thetalklist.com/api/tutorsearch_new
    @FormUrlEncoded
    @POST("tutorsearch_new")
    Call<SearchTutorsModel> searchTutors(@Field("keyword") String keyword);

    //https://www.thetalklist.com/api/tutor_search_filter
    @FormUrlEncoded
    @POST("tutor_search_filter")
    Call<SearchTutorsModel> searchTutorsGenderANDPrice(@Field("id") String id, @Field("keyword") String keyword, @Field("gender") String gender, @Field("price") String price);

    //https://www.thetalklist.com/api/countries
    @POST("countries")
    Call<CountyModel> getCountryList();

    //https://www.thetalklist.com/api/desired_tutor_search_new
    @POST("desired_tutor_search_new")
    Call<AllSearchDataModel> getAllSearchData();

    //https://www.thetalklist.com/api/subject
    @POST("subject")
    Call<SubjectModel> getSubjectList();

    //https://www.thetalklist.com/api/update_device_id
    @FormUrlEncoded
    @POST("update_device_id")
    Call<DeviceIdModel> deviceIdCall(@Field("user_id") String user_id, @Field("device_id") String device_id);
}
