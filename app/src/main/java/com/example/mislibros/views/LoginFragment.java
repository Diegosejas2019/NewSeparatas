package com.example.mislibros.views;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mislibros.MainActivity;
import com.example.mislibros.R;
import com.example.mislibros.interfaces.MainContract;
import com.example.mislibros.model.Detalle;
import com.example.mislibros.model.Publicaciones;
import com.example.mislibros.model.ResponseUSER;
import com.example.mislibros.model.User;
import com.example.mislibros.model.UserData;
import com.example.mislibros.presenter.MainPresenter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements MainContract.View {

    public MainPresenter mPresenter;
    @BindView(R.id.btnRegister)
    Button mRegistrar;
    @BindView(R.id.btnContinuar)
    Button mContinuar;
    @BindView(R.id.txtRecuperar)
    TextView mRecuperar;
    @BindView(R.id.txtNroSuscriptor)
    EditText mEmail;
    @BindView(R.id.txtContraseña)
    EditText mContraseña;
    @BindView(R.id.loginError)
    TextView mErrorMsg;
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
        ButterKnife.bind(this, view);
        SharedPreferences prefs = context.getSharedPreferences(MainActivity.MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("token", "");
        if (restoredText != "") {
            mEmail.setText(prefs.getString("email", ""));
            mContraseña.setText(prefs.getString("password", ""));
            Log.println(Log.INFO, "mEmail", mEmail.getText().toString());
            Log.println(Log.INFO, "mPassword", mContraseña.getText().toString());
            User user = new User();
            user.setEmail(mEmail.getText().toString());
            user.setPassword(mContraseña.getText().toString());
            if (isConnected()) {
                mPresenter.readPlayers(user);
            } else {
                Intent intent = new Intent(context, MainActivity.class);

                Log.println(Log.INFO, "Opcion", "Log");
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }
        mProgressBar.bringToFront();
        mContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmail.setTextColor(Color.rgb(0, 0, 0));
                mContraseña.setTextColor(Color.rgb(0, 0, 0));
                mErrorMsg.setVisibility(View.INVISIBLE);
                String email = mEmail.getText().toString().trim();
                String password = mContraseña.getText().toString().trim();

                View focusView = null;
                boolean cancel = false;

                if (TextUtils.isEmpty(email)) {
                    mErrorMsg.setVisibility(View.VISIBLE);
                    mErrorMsg.setText("Debe ingresar un Email y Contraseña");
                    focusView = mEmail;
                    cancel = true;
                }

                if (TextUtils.isEmpty(password)) {
                    mErrorMsg.setVisibility(View.VISIBLE);
                    mErrorMsg.setText("Debe ingresar un Email y Contraseña");
                    focusView = mContraseña;
                    cancel = true;
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {

                    User user = new User();
                    user.Email = email;
                    user.Password = password;

                    if (isConnected()) {
                        mPresenter.readPlayers(user);
                    } else {
                        Intent intent = new Intent(context, MainActivity.class);

                        Log.println(Log.INFO, "Opcion", "Log");
                        startActivity(intent);
                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }
            }
        });

        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEmail.setTextColor(Color.rgb(0, 0, 0));
                mContraseña.setTextColor(Color.rgb(0, 0, 0));
                mErrorMsg.setVisibility(View.INVISIBLE);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        mContraseña.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEmail.setTextColor(Color.rgb(0, 0, 0));
                mContraseña.setTextColor(Color.rgb(0, 0, 0));
                mErrorMsg.setVisibility(View.INVISIBLE);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        mRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrarFragment nextFrag = new RegistrarFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.host_fragment, nextFrag, "findThisFragment")
                        .addToBackStack("login")
                        .commit();
            }
        });

        mRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecuperarPasswordFragment nextFrag = new RecuperarPasswordFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.host_fragment, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }


    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.

                } else {
                    showAlertDialog();
                }

            });
    public void makePermissionRequest() {
        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
    }
    public void checkPermissionRequest() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            // continue running app

        } else {
            showAlertDialog();

            //makePermissionRequest();
        }
    }
    public void showAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Para poder recibir las notificaciones de la aplicación debe otorgar el permiso en el menu de ajustes.");
                alertDialogBuilder.setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                //makePermissionRequest();
                                ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.POST_NOTIFICATIONS}, 1);
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
  /*  private void checkPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            //permission not granted

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.POST_NOTIFICATIONS}, 1);
            } else {
                //request the permission
                ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    ACCESS_COARSE_LOCATION_CODE)
        }
    }*/

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onCreatePlayerSuccessful() { }

    @Override
    public void onCreatePlayerFailure(String mensaje) {
        mEmail.setTextColor(Color.rgb(255, 0, 0));
        mContraseña.setTextColor(Color.rgb(255, 0, 0));
        mErrorMsg.setVisibility(View.VISIBLE);
        mErrorMsg.setText(mensaje.toUpperCase());
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
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(MainActivity.MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("token", user.token);

        editor.putInt("iduser", user1.getId());

        editor.putString("email", mEmail.getText().toString());
        editor.putString("password", mContraseña.getText().toString());
        editor.apply();

        User userAux = new User();

        userAux.setToken(user.token);
        userAux.setIdUser(user1.getId());
        userAux.setEmail(mEmail.getText().toString());
        userAux.setPassword(mContraseña.getText().toString());

        mPresenter.getBooks(userAux);

       /* if (Build.VERSION.SDK_INT >= 32) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.POST_NOTIFICATIONS},101);
            }
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{POST_NOTIFICATIONS}, 1);
            }
        }*/

        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }

    @Override
    public void onUserCreate(ResponseUSER user) { }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onGetBook(List<Publicaciones> publicaciones) {
        mCreateAndSaveFile("BooksJson", publicaciones);

        publicaciones.forEach(publicacion -> {
            FirebaseMessaging.getInstance().subscribeToTopic(String.valueOf(publicacion.getCode())).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) { Log.d("asd","SUCCESS"); }
                    });
            }
        );
    }

    @Override
    public void onGetBookDetail(List<Detalle> detalles) { }

    public void mCreateAndSaveFile(String params, List<Publicaciones> publicaciones) {
        String filename = params;
        Gson gson = new Gson();
        String s = gson.toJson(publicaciones);
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}