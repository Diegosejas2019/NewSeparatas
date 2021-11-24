package com.example.erreparseparatas.interfaces;

import com.example.erreparseparatas.model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {

    @POST("/api/login/authenticateuser")
    @FormUrlEncoded
    Call<User> authenticateUser(@Field("UserName") String userName,
                                @Field("Password") String password,
                                @Field("idUser") Integer idUser,
                                @Field("Email") String email
    );

    @POST("/api/login/registrarusuario")
    @FormUrlEncoded
    Call<User> registeruser(@Field("Nombre") String nombre,
                            @Field("Apellido") String apellido,
                            @Field("Email") String email,
                            @Field("Telefono") String telefono,
                            @Field("Password") String password,
                            @Field("UrlFoto") String urlfoto,
                            @Field("TipoRed") String tipored
    );

    @POST("/api/login/activarcodigo")
    @FormUrlEncoded
    Call<User> activarcodigo(@Field("IdUser") int iduser,
                             @Field("Codigo") String codigo
    );

}

