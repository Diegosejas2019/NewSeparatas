package com.example.mislibros.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mislibros.R;
import com.example.mislibros.interfaces.MainContract;
import com.example.mislibros.model.Detalle;
import com.example.mislibros.model.Publicaciones;
import com.example.mislibros.model.ResponseUSER;
import com.example.mislibros.model.User;
import com.example.mislibros.presenter.MainPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmarContrasenaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmarContrasenaFragment extends Fragment implements MainContract.View {

    @BindView(R.id.progressBar)
    ProgressBar mProgressbar;
    @BindView(R.id.btnContinuar)
    Button mContinuar;
    @BindView(R.id.txtEmail)
    EditText mEmail;
    @BindView(R.id.txtCodigo)
    EditText mCodigo;
    @BindView(R.id.txtNuevaPassword)
    EditText mContraseña;
    @BindView(R.id.txtConfirmarPassword)
    EditText mNuevaContraseña;
    @BindView(R.id.recoverError)
    TextView mRecoverError;

    public MainPresenter mPresenter;
    public Context context;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "email";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ConfirmarContrasenaFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ConfirmarContrasenaFragment newInstance(String param1, String param2) {
        ConfirmarContrasenaFragment fragment = new ConfirmarContrasenaFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirmar_contrasena, container, false);
        context = inflater.getContext();
        ButterKnife.bind(this,view);

        mProgressbar.bringToFront();
        mEmail.setText(mParam1);
        mEmail.setEnabled(false);
        mContraseña.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEmail.setTextColor(Color.rgb(0, 0, 0));
                mContraseña.setTextColor(Color.rgb(0, 0, 0));
                //mErrorMsg.setVisibility(View.INVISIBLE);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        mNuevaContraseña.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEmail.setTextColor(Color.rgb(0, 0, 0));
                mContraseña.setTextColor(Color.rgb(0, 0, 0));
                //mErrorMsg.setVisibility(View.INVISIBLE);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        mContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();

                View focusView = null;
                boolean cancel = false;

                /*if (TextUtils.isEmpty(email)){
                    mEmail.setError("Campo requerido");
                    focusView = mEmail;
                    cancel = true;
                }*/
                if (TextUtils.isEmpty(mCodigo.getText())){
                    mCodigo.setError("Campo requerido");
                    focusView = mCodigo;
                    cancel = true;
                }

                if (!isValidEmail(mEmail.getText())) {
                    mEmail.setError("Email no válido");
                    focusView = mEmail;
                    cancel = true;
                }
                if (TextUtils.isEmpty(mContraseña.getText())){
                    mContraseña.setError("Campo requerido");
                    focusView = mContraseña;
                    cancel = true;
                }
                if (TextUtils.isEmpty(mNuevaContraseña.getText())){
                    mNuevaContraseña.setError("Campo requerido");
                    focusView = mNuevaContraseña;
                    cancel = true;
                }
                else{
                    if (!mContraseña.getText().toString().equals(mNuevaContraseña.getText().toString()))
                    {
                        mNuevaContraseña.setError("Las contraseñas deben coincidir");
                        focusView = mNuevaContraseña;
                        cancel = true;
                    }
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {

                    User user = new User();
                    user.Email = email;
                    user.Codigo = mCodigo.getText().toString();
                    user.Password = mNuevaContraseña.getText().toString();

                    mPresenter.resetPassword(user);
                }

            }
        });

        return view;
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    @Override
    public void onCreatePlayerSuccessful() {
        SuccesChangePasswordFragment nextFrag= new SuccesChangePasswordFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.host_fragment, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCreatePlayerFailure(String mensaje) {

    }

    @Override
    public void onProcessStart() {
        mProgressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProcessEnd() {
        mProgressbar.setVisibility(View.GONE);
    }

    @Override
    public void onUserRead(ResponseUSER user) {

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