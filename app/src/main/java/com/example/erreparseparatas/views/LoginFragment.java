package com.example.erreparseparatas.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erreparseparatas.MainActivity;
import com.example.erreparseparatas.R;
import com.example.erreparseparatas.interfaces.MainContract;
import com.example.erreparseparatas.model.Detalle;
import com.example.erreparseparatas.model.Publicaciones;
import com.example.erreparseparatas.model.ResponseUSER;
import com.example.erreparseparatas.model.User;
import com.example.erreparseparatas.model.UserData;
import com.example.erreparseparatas.presenter.MainPresenter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.example.erreparseparatas.MainActivity.MY_PREFS_NAME;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements  MainContract.View{

    public MainPresenter mPresenter;
    @BindView(R.id.btnRegister)  Button mRegistrar;
    @BindView(R.id.btnContinuar) Button mContinuar;
    @BindView(R.id.txtRecuperar) TextView mRecuperar;
    @BindView(R.id.txtNroSuscriptor) EditText mEmail;
    @BindView(R.id.txtContraseña) EditText mContraseña;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    public Context context;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MainPresenter(this);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        context = inflater.getContext();
        ButterKnife.bind(this,view);

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("token", "");
        if (restoredText != "") {

            mEmail.setText(prefs.getString("email", ""));
            mContraseña.setText(prefs.getString("password", ""));
            Log.println(Log.INFO,"mEmail",mEmail.getText().toString());
            Log.println(Log.INFO,"mPassword",mContraseña.getText().toString());
            User user = new User();
            user.setEmail(mEmail.getText().toString());
            user.setPassword(mContraseña.getText().toString());

            if(isConnected())
            {
                mPresenter.readPlayers(user);
            }
            else{
                Intent intent = new Intent(context, MainActivity.class);

                Log.println(Log.INFO,"Opcion","Log");
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }
        mProgressBar.bringToFront();
        mContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mContraseña.getText().toString().trim();

                View focusView = null;
                boolean cancel = false;

                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Campo requerido");
                    focusView = mEmail;
                    cancel = true;
                }

                if (TextUtils.isEmpty(password)){
                    mContraseña.setError("Campo requerido");
                    focusView = mContraseña;
                    cancel = true;
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {

                    User user = new User();
                    user.Email = email;
                    user.Password = password;

                    if(isConnected())
                    {
                        mPresenter.readPlayers(user);
                    }
                    else{
                        Intent intent = new Intent(context, MainActivity.class);

                        Log.println(Log.INFO,"Opcion","Log");
                        startActivity(intent);
                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }
            }
        });

        mRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrarFragment nextFrag= new RegistrarFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.host_fragment, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        mRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecuperarPasswordFragment nextFrag= new RecuperarPasswordFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.host_fragment, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onCreatePlayerSuccessful() {

    }

    @Override
    public void onCreatePlayerFailure(String mensaje) {
        Toast.makeText(context,mensaje,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProcessStart() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProcessEnd() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onUserRead(ResponseUSER user) {
        UserData user1 = user.getUser();
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("token", user.token);

        editor.putInt("iduser", user1.getId());

        editor.putString("email", mEmail.getText().toString());
        editor.putString("password", mContraseña.getText().toString());
        editor.apply();

        /*MisPublicacionesFragment nextFrag= new MisPublicacionesFragment();
        Bundle bundle=new Bundle();
        bundle.putString("email", mEmail.getText().toString());
        bundle.putString("password", mContraseña.getText().toString());
        nextFrag.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.host_fragment, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit();*/
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }

    @Override
    public void onUserCreate(ResponseUSER user) {

    }

    @Override
    public void onGetBook(List<Publicaciones> publicaciones) {

    }
    @Override
    public void onGetBookDetail(List<Detalle> detalles) {

    }
}