package com.example.erreparseparatas.presenter;

import com.example.erreparseparatas.interactor.MainInteractor;
import com.example.erreparseparatas.interfaces.MainContract;
import com.example.erreparseparatas.model.User;

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
    public void onSuccess() {

    }

    @Override
    public void onSuccessCreate(User user) {
        mView.onUserCreate(user);
    }

    @Override
    public void onSuccessRead(User user) {
        mView.onUserRead(user);
    }

    @Override
    public void onFailure() {
        mView.onCreatePlayerFailure();
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

