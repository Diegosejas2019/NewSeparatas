package com.example.erreparseparatas.interfaces;

import com.example.erreparseparatas.model.Detalle;
import com.example.erreparseparatas.model.Publicaciones;
import com.example.erreparseparatas.model.ResponseUSER;
import com.example.erreparseparatas.model.User;

import java.util.List;

public interface MainContract {
    interface View{
        void onCreatePlayerSuccessful();
        void onCreatePlayerFailure(String mensaje);
        void onProcessStart();
        void onProcessEnd();
        void onUserRead(ResponseUSER user);
        void onUserCreate(ResponseUSER user);
        void onGetBook(List<Publicaciones> publicaciones);
        void onGetBookDetail(List<Detalle> detalles);
    }

    interface Presenter{
        void createNewPlayer(User user);
        void readPlayers(User user);
        void recoveryPlayers(User user);
        void codeBook(User user);
        void getBooks(User user);
        void getBooksDetails(User user);
    }

    interface Interactor{
        void performGetCode(User user);
        void performGetCodeDetail(User user);
        void performCodeBook(User user);
        void performCreatePlayer(User user);
        void performReadPlayers(User user);
        void performRecoveryPlayers(User user);
    }

    interface onOperationListener{
        void onSuccess();
        void onSuccessCreate(ResponseUSER user);
        void onSuccessRead(ResponseUSER user);
        void onSuccessGetBook(List<Publicaciones> publicaciones);
        void onSuccessGetBookDetail(List<Detalle> detalles);
        void onFailure(String mensaje);
        void onStart();
        void onEnd();
    }
}
