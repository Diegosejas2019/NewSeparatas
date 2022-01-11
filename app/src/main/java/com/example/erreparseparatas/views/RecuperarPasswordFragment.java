package com.example.erreparseparatas.views;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.erreparseparatas.R;
import com.example.erreparseparatas.interfaces.MainContract;
import com.example.erreparseparatas.model.Detalle;
import com.example.erreparseparatas.model.Publicaciones;
import com.example.erreparseparatas.model.ResponseUSER;
import com.example.erreparseparatas.model.User;
import com.example.erreparseparatas.presenter.MainPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecuperarPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecuperarPasswordFragment extends Fragment implements  MainContract.View{

    @BindView(R.id.progressBar)
    ProgressBar mProgressbar;
    @BindView(R.id.btnContinuar)
    Button mContinuar;
    @BindView(R.id.txtEmail)  EditText mEmail;
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

                if (cancel) {
                    focusView.requestFocus();
                } else {

                    User user = new User();
                    user.Email = email;

                    mPresenter.recoveryPlayers(user);
                }
            }
        });


        return view;
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
        mProgressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProcessEnd() {
        mProgressbar.setVisibility(View.INVISIBLE);
        Toast.makeText(context,"Solicitud de cambio de contraseña exitosa! Se ha enviado un mail a su correo electronico.",Toast.LENGTH_LONG).show();
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