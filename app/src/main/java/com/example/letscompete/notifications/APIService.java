package com.example.letscompete.notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAn6P_3Vo:APA91bE9vY0milhwlJ4lZOs2JlymEA2bdi3bJEoSP2ARvH91TK1tWmzm4uKA0IuIJ4021ZsJEZd_igkeYE7ONtDLWMNM5jj2XASuL1IalGAoz-8rmFMWE0EALr9SniDranSImPCY4R04"
    })

    @POST("form/send")
    Call<Response> sendNotification(@Body Sender body);
}
