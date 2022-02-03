package com.example.erreparseparatas;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.erreparseparatas.interfaces.MainContract;
import com.example.erreparseparatas.model.Detalle;
import com.example.erreparseparatas.model.Publicaciones;
import com.example.erreparseparatas.model.ResponseUSER;
import com.example.erreparseparatas.views.LoginFragment;

import java.util.List;

import butterknife.BindView;

public class LoginActivity extends AppCompatActivity implements MainContract.View {
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        new Fragment();
        Fragment nextFrag;
        nextFrag = new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.host_fragment, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCreatePlayerSuccessful() { }

    @Override
    public void onCreatePlayerFailure(String mensaje) {
        Toast.makeText(this,mensaje,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProcessStart() { }

    @Override
    public void onProcessEnd() { }

    @Override
    public void onUserRead(ResponseUSER user) { }

    @Override
    public void onUserCreate(ResponseUSER user) { }

    @Override
    public void onGetBook(List<Publicaciones> publicaciones) { }

    @Override
    public void onGetBookDetail(List<Detalle> detalles) { }

}