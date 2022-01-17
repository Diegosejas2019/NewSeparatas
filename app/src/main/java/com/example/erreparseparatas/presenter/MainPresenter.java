package com.example.erreparseparatas.presenter;

import com.example.erreparseparatas.interactor.MainInteractor;
import com.example.erreparseparatas.interfaces.MainContract;
import com.example.erreparseparatas.model.Detalle;
import com.example.erreparseparatas.model.Publicaciones;
import com.example.erreparseparatas.model.ResponseUSER;
import com.example.erreparseparatas.model.User;

import java.util.List;

public class MainPresenter implements MainContract.Presenter, MainContract.onOperationListener{

    private MainContract.View mView;
    private MainInteractor mInteractor;

    public MainPresenter(MainContract.View mView) {
        this.mView = mView;
        mInteractor = new MainInteractor(this);
    }

    @Override
    public void createNewPlayer(User user) {
        mInteractor.performCreatePlayer(user);
    }

    @Override
    public void readPlayers(User user) {
        mInteractor.performReadPlayers(user);
    }

    @Override
    public void recoveryPlayers(User user) {
        mInteractor.performRecoveryPlayers(user);
    }

    @Override
    public void codeBook(User user) {
        mInteractor.performCodeBook(user);
    }

    @Override
    public void getBooks(User user) {
        mInteractor.performGetCode(user);
    }

    @Override
    public void getBooksDetails(User user) {
        mInteractor.performGetCodeDetail(user);
    }


    @Override
    public void onSuccess() {
        mView.onCreatePlayerSuccessful();
    }

    @Override
    public void onSuccessCreate(ResponseUSER user) {
        mView.onUserCreate(user);
    }

    @Override
    public void onSuccessRead(ResponseUSER user) {
        mView.onUserRead(user);
    }

    @Override
    public void onSuccessGetBook(List<Publicaciones> publicaciones) {
        mView.onGetBook(publicaciones);
    }

    @Override
    public void onSuccessGetBookDetail(List<Detalle> detalles) {
        mView.onGetBookDetail(detalles);
    }

    @Override
    public void onFailure(String mensaje) {
        mView.onCreatePlayerFailure(mensaje);
    }

    @Override
    public void onStart() {
        mView.onProcessStart();
    }

    @Override
    public void onEnd() {
        mView.onProcessEnd();
    }
}

