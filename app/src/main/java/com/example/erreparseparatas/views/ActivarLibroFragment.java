package com.example.erreparseparatas.views;

import static android.content.Context.MODE_PRIVATE;
import static com.example.erreparseparatas.MainActivity.MY_PREFS_NAME;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

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
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.bookActivateError)
    TextView mBookError;
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

    User user = new User();

    private List<Publicaciones> publicacionesList = new ArrayList<>();
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

        mCodigo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBookError.setVisibility(View.INVISIBLE);
                mCodigo.setTextColor(Color .rgb(0,0,0));
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

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
    public void onCreatePlayerSuccessful() { }

    @Override
    public void onCreatePlayerFailure(String mensaje) {
        mBookError.setVisibility(View.VISIBLE);
        mBookError.setText(mensaje);
        mBookError.setTextColor(Color .rgb(255,0,0));
        mCodigo.setTextColor(Color .rgb(255,0,0));
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
            }
        });
        mProgressbar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onUserRead(ResponseUSER user) { }

    @Override
    public void onUserCreate(ResponseUSER user) {
        mPresenter.getBooks(this.user);
        SuccessActivateBookFragment nextFrag= new SuccessActivateBookFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onGetBook(List<Publicaciones> publicaciones) {
        publicacionesList = publicaciones;

        //Download booklist.json for offline mode
        mCreateAndSaveFile("BooksJson", publicaciones);
        for (int i = 0; i < publicacionesList.size(); i++) {
            downloadBooksImages(publicacionesList.get(i).getTitle(), publicacionesList.get(i).getImageUrl());
        }
    }

    @Override
    public void onGetBookDetail(List<Detalle> detalles) { }

    private void downloadBooksImages(String filename, String downloadUrlOfImage){
        try{
            DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(downloadUrlOfImage);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType("image/jpeg")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + filename + ".jpg");
            dm.enqueue(request);
        }catch (Exception e){
            Log.d("BOOK",e.getMessage());
        }
    }

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