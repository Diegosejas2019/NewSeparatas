package com.example.erreparseparatas.views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.erreparseparatas.R;

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
    @BindView(R.id.txtTelefono) EditText mTelefono;

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

                if (TextUtils.isEmpty(mTelefono.getText())){
                    mTelefono.setError("Campo requerido");
                    focusView = mTelefono;
                    cancel = true;
                }

                if (TextUtils.isEmpty(mNombre.getText())){
                    mNombre.setError("Campo requerido");
                    focusView = mNombre;
                    cancel = true;
                }

                if (TextUtils.isEmpty(mEmail.getText())){
                    mEmail.setError("Campo requerido");
                    focusView = mEmail;
                    cancel = true;
                }
                else {
                    if (!isValidEmail(mEmail.getText())){
                        mEmail.setError("Email no válido");
                        focusView = mEmail;
                        cancel = true;
                    }
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {

                    String email = mEmail.getText().toString().trim();
                    String nombre = mNombre.getText().toString().trim();
                    String telefono = mTelefono.getText().toString().trim();

                    RegistrarP3Fragment nextFrag= new RegistrarP3Fragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("email", email);
                    bundle.putString("nombre", nombre);
                    bundle.putString("telefono", telefono);
                    nextFrag.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.host_fragment, nextFrag, "findThisFragment")
                            .addToBackStack(null)
                            .commit();
                }
            }
        });


        return view;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}