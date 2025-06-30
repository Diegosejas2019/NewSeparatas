package com.example.mislibros.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
 * Use the {@link RecuperarPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecuperarPasswordFragment extends Fragment implements MainContract.View {

    @BindView(R.id.progressBar)
    ProgressBar mProgressbar;
    @BindView(R.id.btnContinuar)
    Button mContinuar;
    @BindView(R.id.txtEmail)  EditText mEmail;
    @BindView(R.id.recoverError)
    TextView mRecoverError;
    public MainPresenter mPresenter;
    public Context context;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecuperarPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecuperarPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecuperarPasswordFragment newInstance(String param1, String param2) {
        RecuperarPasswordFragment fragment = new RecuperarPasswordFragment();
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

        View view = inflater.inflate(R.layout.fragment_recuperar_password, container, false);
        context = inflater.getContext();
        ButterKnife.bind(this,view);
        mProgressbar.bringToFront();

        mContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();

                View focusView = null;
                boolean cancel = false;

                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Campo requerido");
                    focusView = mEmail;
                    cancel = true;
                }

                if (!isValidEmail(mEmail.getText())) {
                    mEmail.setError("Email no válido");
                    focusView = mEmail;
                    cancel = true;
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {

                    User user = new User();
                    user.Email = email;

                    mPresenter.recoveryPlayers(user);
                }
            }
        });

        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRecoverError.setVisibility(View.INVISIBLE);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    @Override
    public void onCreatePlayerSuccessful() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Se ha enviado un código a tu email para cambiar la contraseña.")
                .setTitle("Código enviado!");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Bundle bundle=new Bundle();
                bundle.putString("email", mEmail.getText().toString());

                ConfirmarContrasenaFragment nextFrag = new ConfirmarContrasenaFragment();
                nextFrag.setArguments(bundle);
                getActivity().getSupportFragmentManager()

                        .beginTransaction()
                        //.setCustomAnimations(R.anim.slide_out)
                        .replace(R.id.host_fragment, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onCreatePlayerFailure(String mensaje) {
        mRecoverError.setVisibility(View.VISIBLE);
        mRecoverError.setTextColor(Color .rgb(255,0,0));
        mRecoverError.setText(mensaje);
    }

    @Override
    public void onProcessStart() {
        mProgressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProcessEnd() { mProgressbar.setVisibility(View.GONE); }

    @Override
    public void onUserRead(ResponseUSER user) { }

    @Override
    public void onUserCreate(ResponseUSER user) { }

    @Override
    public void onGetBook(List<Publicaciones> publicaciones) { }

    @Override
    public void onGetBookDetail(List<Detalle> detalles) { }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}