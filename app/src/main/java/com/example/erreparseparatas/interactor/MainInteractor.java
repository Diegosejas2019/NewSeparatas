package com.example.erreparseparatas.interactor;

import com.example.erreparseparatas.interfaces.APIService;
import com.example.erreparseparatas.interfaces.MainContract;
import com.example.erreparseparatas.model.User;
import com.example.erreparseparatas.utils.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainInteractor implements MainContract.Interactor{
    private APIService mAPIService;
    private MainContract.onOperationListener mListner;


    public MainInteractor(MainContract.onOperationListener mListner) {
        this.mListner = mListner;
        mAPIService = ApiUtils.getAPIService();
    }

    @Override
    public void performCreatePlayer(User user) {
        mListner.onStart();
        mAPIService.registeruser(user.getUserName(),
                user.getFullUserName(),
                user.getEmail(),
                user.getTelefono(),
                user.getPassword(),
                user.getUrlFoto(),
                user.getTipoRed()
        ).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(response.isSuccessful()) {
                    mListner.onSuccessCreate(response.body());
                    mListner.onEnd();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mListner.onFailure();
                mListner.onEnd();
            }
        });
    }

    @Override
    public void performReadPlayers(User user) {
        mListner.onStart();
        mAPIService.authenticateUser(user.UserName, user.Password,user.IdUser,user.Email).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(response.isSuccessful()) {
                    mListner.onSuccessRead(response.body());
                    mListner.onEnd();
                }
                else{
                    mListner.onFailure();
                    mListner.onEnd();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mListner.onFailure();
                mListner.onEnd();
            }
        });
    }

}


