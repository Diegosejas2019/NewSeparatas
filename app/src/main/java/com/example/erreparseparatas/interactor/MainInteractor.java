package com.example.erreparseparatas.interactor;

import android.widget.Toast;

import com.example.erreparseparatas.interfaces.APIService;
import com.example.erreparseparatas.interfaces.MainContract;
import com.example.erreparseparatas.model.Detalle;
import com.example.erreparseparatas.model.Publicaciones;
import com.example.erreparseparatas.model.ResponseUSER;
import com.example.erreparseparatas.model.User;
import com.example.erreparseparatas.utils.ApiUtils;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
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
    public void performCodeBook(User user) {
        mListner.onStart();
        mAPIService.activarcodigo(user.getToken(), user.getFullUserName(),user.getIdUser()
        ).enqueue(new Callback<ResponseUSER>() {
            @Override
            public void onResponse(Call<ResponseUSER> call, Response<ResponseUSER> response) {

                if(response.isSuccessful()) {
                    mListner.onSuccessCreate(response.body());
                    mListner.onEnd();
                }
                else  {
                    try {
                        mListner.onFailure(response.errorBody().string());
                        mListner.onEnd();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseUSER> call, Throwable t) {
                mListner.onSuccess();
                mListner.onEnd();
            }
        });
    }

    @Override
    public void performGetCode(User user) {
        mListner.onStart();
        mAPIService.obtenercodigos(user.getToken(), user.getIdUser()
        ).enqueue(new Callback<List<Publicaciones>>() {
            @Override
            public void onResponse(Call<List<Publicaciones>> call, Response<List<Publicaciones>> response) {

                if(response.isSuccessful()) {
                    mListner.onSuccessGetBook(response.body());
                    mListner.onEnd();
                }
                else {
                    try {
                        mListner.onFailure(response.errorBody().string());
                        mListner.onEnd();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Publicaciones>> call, Throwable t) {
                mListner.onSuccess();
                mListner.onEnd();
            }
        });
    }

    @Override
    public void performGetCodeDetail(User user) {
        mListner.onStart();
        mAPIService.obtenerdetalle(user.getToken(), user.getIdUser()
        ).enqueue(new Callback<List<Detalle>>() {
            @Override
            public void onResponse(Call<List<Detalle>> call, Response<List<Detalle>> response) {

                if(response.isSuccessful()) {
                    mListner.onSuccessGetBookDetail(response.body());
                    mListner.onEnd();
                }
                else {
                    try {
                        mListner.onFailure(response.errorBody().string());
                        mListner.onEnd();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Detalle>> call, Throwable t) {
                mListner.onSuccess();
                mListner.onEnd();
            }
        });
    }

    @Override
    public void performCreatePlayer(User user) {
        mListner.onStart();
        mAPIService.registeruser(user.getUserName(),
                user.getFullUserName(),
                user.getEmail(),
                user.getTelefono(),
                user.getPassword(),
                user.getPassword()
        ).enqueue(new Callback<ResponseUSER>() {
            @Override
            public void onResponse(Call<ResponseUSER> call, Response<ResponseUSER> response) {

                if(response.isSuccessful()) {
                    mListner.onSuccessCreate(response.body());
                    mListner.onEnd();
                }
                else {
                    try {
                        mListner.onFailure(response.errorBody().string());
                        mListner.onEnd();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseUSER> call, Throwable t) {
                mListner.onFailure(t.getMessage());
                mListner.onEnd();
            }
        });
    }

    @Override
    public void performReadPlayers(User user) {
        mListner.onStart();
        mAPIService.authenticateUser(user.getEmail(), user.Password).enqueue(new Callback<ResponseUSER>() {
            @Override
            public void onResponse(Call<ResponseUSER> call, Response<ResponseUSER> response) {

                if(response.isSuccessful()) {
                    mListner.onSuccessRead(response.body());
                    mListner.onEnd();
                }
                else{
                    try {
                        mListner.onFailure(response.errorBody().string());
                        mListner.onEnd();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseUSER> call, Throwable t) {
                mListner.onFailure(t.getMessage());
                mListner.onEnd();
            }
        });
    }

    @Override
    public void performRecoveryPlayers(User user) {
        mListner.onStart();
        mAPIService.recoveryUser(user.getEmail()).enqueue(new Callback<ResponseUSER>() {
            @Override
            public void onResponse(Call<ResponseUSER> call, Response<ResponseUSER> response) {

                if(response.isSuccessful()) {
                    mListner.onSuccess();
                    mListner.onEnd();
                }
                else{
                    try {
                        mListner.onFailure(response.errorBody().string());
                        mListner.onEnd();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseUSER> call, Throwable t) {
                mListner.onFailure(t.getMessage());
                mListner.onEnd();
            }
        });
    }

}


