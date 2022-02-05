package com.example.studentsdata.Utility;


import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Main_Interface {

    //Get Subjects

    @POST("student.php")
    Call<DefaultResponse> getId(@Query("studentSerial") String note);


}
