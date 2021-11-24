package com.example.erreparseparatas.interfaces;

import com.example.erreparseparatas.model.User;

public interface MainContract {
    interface View{
        void onCreatePlayerSuccessful();
        void onCreatePlayerFailure();
        void onProcessStart();
        void onProcessEnd();
        void onUserRead(User user);
        void onUserCreate(User user);
    }

    interface Presenter{
        void createNewPlayer(User user);
        void readPlayers(User user);
    }

    interface Interactor{
        void performCreatePlayer(User user);
        void performReadPlayers(User user);
    }

    interface onOperationListener{
        void onSuccess();
        void onSuccessCreate(User user);
        void onSuccessRead(User user);
        void onFailure();
        void onStart();
        void onEnd();
    }
}
