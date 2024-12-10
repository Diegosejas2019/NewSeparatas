package com.example.mislibros.views;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.example.mislibros.MainActivity.MY_PREFS_NAME;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mislibros.R;
import com.example.mislibros.interfaces.MainContract;
import com.example.mislibros.model.Detalle;
import com.example.mislibros.model.Publicaciones;
import com.example.mislibros.model.ResponseUSER;
import com.example.mislibros.model.User;
import com.example.mislibros.presenter.MainPresenter;
import com.example.mislibros.utils.DetallesAdapter;
import com.github.barteksc.pdfviewer.PDFView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class DetalleFragment extends Fragment implements  MainContract.View{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "linkImg";
    private static final String ARG_PARAM2 = "publicacionid";

    @BindView(R.id.imageView)
    ImageView mImage;
    @BindView(R.id.compartir)
    ImageView mImageShare;
    @BindView(R.id.emptyDetailText)
    TextView emptyDetail;
    @BindView(R.id.pdfView)
    PDFView pdfView;
    @BindView(R.id.webPdfView)
    WebView webView;
    Boolean hasShowedUpOfflineAlert = false;
    public MainPresenter mPresenter;
    public Context context;
    RecyclerView recyclerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2 = "";

    public DetalleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MainPresenter(this);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                MisPublicacionesFragment nextFrag= new MisPublicacionesFragment();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detalle, container, false);
        context = inflater.getContext();
        ButterKnife.bind(this,view);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences prefs2 = context.getSharedPreferences("Configs", MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs2.edit();
        hasShowedUpOfflineAlert = prefs2.getBoolean("hasShowedUp", false);

        if (!hasShowedUpOfflineAlert && isConnected()) {
            editor.putBoolean("hasShowedUp", true);
            editor.apply();
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Offline");
            alertDialog.setMessage("Si desea utilizar la versión offline, desde el Menú, ingrese a “Descarga contenido” y habilite la descarga para disfrutar de todo el contenido sin necesidad de estar conectado.");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar",
                    (dialog, which) -> {
                        recyclerView.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    });
            alertDialog.show();
        }

        String restoredText = prefs.getString("token", "");
        if (restoredText != "") {
            if (mParam2 != null) {
                User user = new User();
                user.setIdUser(Integer.valueOf(mParam2));
                user.setToken(restoredText);

                mPresenter.getBooksDetails(user, context);
            }
        }
        return view;
    }

    @Override
    public void onCreatePlayerSuccessful() { }

    @Override
    public void onCreatePlayerFailure(String mensaje) {
        Toast.makeText(context,mensaje,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProcessStart() { }

    @Override
    public void onProcessEnd() { }

    @Override
    public void onUserRead(ResponseUSER user) { }

    @Override
    public void onUserCreate(ResponseUSER user) { }

    @Override
    public void onGetBook(List<Publicaciones> publicaciones) { }

    @Override
    public void onGetBookDetail(List<Detalle> detalles) {
        if (detalles.isEmpty()) {
            Log.d("detalle", "vacio");
            emptyDetail.setVisibility(View.VISIBLE);
        } else {
            emptyDetail.setVisibility(View.GONE);
            Picasso.with(getLayoutInflater().getContext())
                    .load(mParam1)
                    .into(mImage);
        }

        DetallesAdapter adapter = new DetallesAdapter(context, detalles, webView, pdfView,getActivity(),mPresenter);
        recyclerView.setAdapter(adapter);
        mImageShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter.visibilidad == 0)
                {
                    adapter.visibilidad = 8;
                }
                else{
                    adapter.visibilidad = 0;
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}