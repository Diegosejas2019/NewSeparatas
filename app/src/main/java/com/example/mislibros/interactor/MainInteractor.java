package com.example.mislibros.interactor;

import android.content.Context;

import com.example.mislibros.interfaces.APIService;
import com.example.mislibros.interfaces.MainContract;
import com.example.mislibros.model.Detalle;
import com.example.mislibros.model.Publicaciones;
import com.example.mislibros.model.ResponseUSER;
import com.example.mislibros.model.User;
import com.example.mislibros.utils.ApiUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainInteractor implements MainContract.Interactor{
    private APIService mAPIService;
    private MainContract.onOperationListener mListner;
    private Context context;
    public MainInteractor(MainContract.onOperationListener mListner) {
        this.mListner = mListner;
        mAPIService = ApiUtils.getAPIService();
    }

    private List<Detalle> detalleList;
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
    public void performGetCodeDetail(User user, Context context) {
        this.context = context;
        mListner.onStart();
        mAPIService.obtenerdetalle(user.getToken(), user.getIdUser()
        ).enqueue(new Callback<List<Detalle>>() {
            @Override
            public void onResponse(Call<List<Detalle>> call, Response<List<Detalle>> response) {

                if(response.isSuccessful()) {
                    mListner.onSuccessGetBookDetail(response.body());
                    mListner.onEnd();
                    String filename = "bookdetail" + user.getIdUser();
                    Gson gson = new Gson();
                    String s = gson.toJson(response.body());
                    FileOutputStream outputStream;
                    try {
                        outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
                        outputStream.write(s.getBytes());
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                String filename = "bookdetail" + user.getIdUser();
                try {
                    if (context.openFileInput(filename) != null) {
                        try {
                            FileInputStream fis = context.openFileInput(filename);
                            InputStreamReader isr = new InputStreamReader(fis);
                            BufferedReader bufferedReader = new BufferedReader(isr);
                            StringBuilder sb = new StringBuilder();
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                sb.append(line);
                            }
                            String json = sb.toString();
                            Gson gson = new Gson();
                            Type listDetalle = new TypeToken<ArrayList<Detalle>>() {
                            }.getType();
                            detalleList = gson.<List>fromJson(json, listDetalle);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mListner.onSuccessGetBookDetail(detalleList);
                    } else {
                        mListner.onSuccess();
                    }
                    mListner.onEnd();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

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
        mAPIService.recoveryUser(user.getEmail()
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
}


