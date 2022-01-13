package com.example.erreparseparatas.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erreparseparatas.R;
import com.example.erreparseparatas.interfaces.MainContract;
import com.example.erreparseparatas.model.Detalle;
import com.example.erreparseparatas.model.Publicaciones;
import com.example.erreparseparatas.model.ResponseUSER;
import com.example.erreparseparatas.model.User;
import com.example.erreparseparatas.presenter.MainPresenter;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;
import static com.example.erreparseparatas.MainActivity.MY_PREFS_NAME;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrarP3Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrarP3Fragment extends Fragment implements  MainContract.View{
    public MainPresenter mPresenter;
    @BindView(R.id.btnRegister)
    Button mRegister;
    @BindView(R.id.txtContraseña)
    TextInputEditText mContraseña;
    @BindView(R.id.txtVerificarContraseña) TextInputEditText mReContraseña;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.registerUserError)
    TextView mRegisterError;
    public Context context;
    private static final String ARG_PARAM1 = "email";
    private static final String ARG_PARAM2 = "nombre";
    private static final String ARG_PARAM3 = "telefono";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    public RegistrarP3Fragment() {
        // Required empty public constructor
    }


    public static RegistrarP3Fragment newInstance(String param1, String param2) {
        RegistrarP3Fragment fragment = new RegistrarP3Fragment();
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
            mParam3 = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registrar_p3, container, false);
        context = inflater.getContext();
        ButterKnife.bind(this,view);



        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View focusView = null;
                boolean cancel = false;
                mRegisterError.setVisibility(View.INVISIBLE);

                if (TextUtils.isEmpty(mReContraseña.getText())){
                    mReContraseña.setError("Campo requerido");
                    focusView = mReContraseña;
                    cancel = true;
                }

                if (TextUtils.isEmpty(mContraseña.getText())){
                    mContraseña.setError("Campo requerido");
                    focusView = mContraseña;
                    cancel = true;
                }
                else{
                    if (!mContraseña.getText().toString().equals(mReContraseña.getText().toString()))
                    {
                        mReContraseña.setError("Las contraseñas deben coincidir");
                        focusView = mReContraseña;
                        cancel = true;
                    }
                }
                if (cancel) {
                    focusView.requestFocus();
                } else {
                    String contraseña = mContraseña.getText().toString().trim();

                    User user = new User();
                    user.UserName = mParam2;
                    user.FullUserName = mParam2;
                    user.Email = mParam1;
                    user.Telefono = mParam3;
                    user.Password = contraseña;

                    mPresenter.createNewPlayer(user);
                }
            }
        });

        mContraseña.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRegisterError.setVisibility(View.INVISIBLE);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        mReContraseña.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRegisterError.setVisibility(View.INVISIBLE);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        return view;
    }

    @Override
    public void onCreatePlayerSuccessful() {
        RegistrarPFFragment nextFrag= new RegistrarPFFragment();
        Bundle bundle=new Bundle();
        nextFrag.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.host_fragment, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCreatePlayerFailure(String mensaje) {
        Toast.makeText(context,mensaje,Toast.LENGTH_LONG).show();
        mRegisterError.setText(mensaje);
        mRegisterError.setVisibility(View.VISIBLE);
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

    }

    @Override
    public void onUserCreate(ResponseUSER user) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("password", mContraseña.getText().toString());
        editor.putString("email", mParam1);
        editor.putString("token", user.token);
        editor.apply();

        RegistrarPFFragment nextFrag= new RegistrarPFFragment();
        Bundle bundle=new Bundle();
        bundle.putString("token", user.token);
        nextFrag.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.host_fragment, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onGetBook(List<Publicaciones> publicaciones) {

    }
    @Override
    public void onGetBookDetail(List<Detalle> detalles) {

    }
}