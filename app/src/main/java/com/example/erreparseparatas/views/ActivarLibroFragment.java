package com.example.erreparseparatas.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.erreparseparatas.presenter.MainPresenter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;
import static com.example.erreparseparatas.MainActivity.MY_PREFS_NAME;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActivarLibroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivarLibroFragment extends Fragment implements  MainContract.View{

    @BindView(R.id.txtEncontrarCodigo)
    TextView mEncontrarCodigo;
    @BindView(R.id.btnActivar)
    Button mActivar;
    @BindView(R.id.btnMisPublicaciones)
    Button mbtnMisPublicaciones;
    @BindView(R.id.txtCodigo)
    TextInputEditText mCodigo;
    @BindView(R.id.progressBar)
    ProgressBar mProgressbar;
    public MainPresenter mPresenter;
    public Integer midUser;
    public Context context;
    public String mToken;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ActivarLibroFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActivarLibroFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActivarLibroFragment newInstance(String param1, String param2) {
        ActivarLibroFragment fragment = new ActivarLibroFragment();
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

        View view = inflater.inflate(R.layout.fragment_activar_libro, container, false);
        context = inflater.getContext();
        ButterKnife.bind(this,view);

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        Integer UserId = prefs.getInt("iduser", 0);
        if (UserId != 0) {
            mToken = prefs.getString("token", null);
            midUser = UserId;
        }

        mProgressbar.bringToFront();
        mEncontrarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EncontrarCodigoFragment nextFrag= new EncontrarCodigoFragment();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        mbtnMisPublicaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MisPublicacionesFragment nextFrag= new MisPublicacionesFragment();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        mActivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View focusView = null;
                boolean cancel = false;

                if (TextUtils.isEmpty(mCodigo.getText())){
                    mCodigo.setError("Campo requerido");
                    focusView = mCodigo;
                    cancel = true;
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {
                    String codigo = mCodigo.getText().toString().trim();

                    User user = new User();
                    user.setFullUserName(codigo);
                    user.setIdUser(midUser);
                    user.setToken(mToken);

                    mPresenter.codeBook(user);
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
        FirebaseMessaging.getInstance().subscribeToTopic(mCodigo.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(context,mCodigo.getText().toString().trim(), Toast.LENGTH_LONG).show();
                Toast.makeText(context,"Activación exitosa!", Toast.LENGTH_LONG).show();
            }
        });
        mProgressbar.setVisibility(View.INVISIBLE);
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