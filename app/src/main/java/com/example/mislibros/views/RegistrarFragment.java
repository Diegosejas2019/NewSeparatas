package com.example.mislibros.views;

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
import android.widget.TextView;

import com.example.mislibros.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrarFragment extends Fragment {

    @BindView(R.id.btnContinuar)
    Button mContinuar;
    @BindView(R.id.txtEmail)
    EditText mEmail;
    @BindView(R.id.txtNombre) EditText mNombre;
    /*@BindView(R.id.txtTelefono) EditText mTelefono;*/
    @BindView(R.id.errorMsg)
    TextView mErrorMsg;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public RegistrarFragment() {
        // Required empty public constructor
    }

    public static RegistrarFragment newInstance(String param1, String param2) {
        RegistrarFragment fragment = new RegistrarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_registrar, container, false);

        ButterKnife.bind(this,view);

        mContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View focusView = null;
                boolean cancel = false;

                /*if (TextUtils.isEmpty(mTelefono.getText())){
                    mErrorMsg.setVisibility(View.VISIBLE);
                    mErrorMsg.setText("Debe ingresar un Email, Contraseña y Teléfono");
                    focusView = mTelefono;
                    cancel = true;
                }*/

                if (TextUtils.isEmpty(mNombre.getText())){
                    mErrorMsg.setVisibility(View.VISIBLE);
                    mErrorMsg.setText("Debe ingresar un Email, Contraseña y Teléfono");
                    focusView = mNombre;
                    cancel = true;
                }

                if (TextUtils.isEmpty(mEmail.getText())){
                    mErrorMsg.setVisibility(View.VISIBLE);
                    mErrorMsg.setText("Debe ingresar un Email, Contraseña y Teléfono");
                    focusView = mEmail;
                    cancel = true;
                }
                else {
                    if (!isValidEmail(mEmail.getText())){
                        mErrorMsg.setVisibility(View.VISIBLE);
                        mErrorMsg.setText("Email no válido");
                        focusView = mEmail;
                        cancel = true;
                    }
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {

                    String email = mEmail.getText().toString().trim();
                    String nombre = mNombre.getText().toString().trim();
                    /*String telefono = mTelefono.getText().toString().trim();*/

                    RegistrarP3Fragment nextFrag= new RegistrarP3Fragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("email", email);
                    bundle.putString("nombre", nombre);
                    bundle.putString("telefono", "99999");
                    nextFrag.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.host_fragment, nextFrag, "findThisFragment")
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mErrorMsg.setVisibility(View.INVISIBLE);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        mNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mErrorMsg.setVisibility(View.INVISIBLE);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        /*mTelefono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mErrorMsg.setVisibility(View.INVISIBLE);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });*/

        return view;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}