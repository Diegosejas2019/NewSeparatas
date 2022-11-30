package com.example.mislibros.views;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mislibros.R;
import com.example.mislibros.interfaces.MainContract;
import com.example.mislibros.model.Detalle;
import com.example.mislibros.model.Publicaciones;
import com.example.mislibros.model.ResponseUSER;
import com.example.mislibros.model.User;
import com.example.mislibros.presenter.MainPresenter;
import com.example.mislibros.utils.PublicacionesAdapter;
import com.example.mislibros.MainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MisPublicacionesFragment extends Fragment implements MainContract.View {
    @BindView(R.id.text1_home)
    TextView text1_home;
    @BindView(R.id.text2_home)
    TextView text2_home;
    @BindView(R.id.home_logo)
    ImageView home_logo;
    @BindView(R.id.goToCode)
    Button goToCode;
    @BindView(R.id.pullToRefresh) SwipeRefreshLayout pullToRefresh;
    @BindView(R.id.homeTitleText) TextView homeTitleText;
    RecyclerView recyclerView;
    public MainPresenter mPresenter;
    public Context context;
    Boolean offlineList = false;
    private String mParam1 = "";
    private String mParam2 = "";
    private List<Publicaciones> productList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MainPresenter(this);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("publicacionid");
            mParam2 = getArguments().getString("linkImg");
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mis_publicaciones, container, false);


        context = inflater.getContext();
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            mParam1 = arguments.getString("publicacionid");
            mParam2 = arguments.getString("linkImg");
        }

        ButterKnife.bind(this,view);

        //getting the recyclerview from xml
        if (mParam1 != "" && mParam1 != null)
        {
            Fragment fragment = new DetalleFragment();
            Bundle args = new Bundle();
            args.putString("linkImg", mParam2);
            args.putString("publicacionid", mParam1);

            fragment.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment, "findThisFragment")
                    .addToBackStack(null)
                    .commit();

        }
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        SharedPreferences prefs = context.getSharedPreferences(MainActivity.MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String restoredText = prefs.getString("token", "");
        if (restoredText != "") {

            Integer mIdUser = prefs.getInt("iduser", 0);
            User user = new User();
            user.setIdUser(mIdUser);
            user.setToken(restoredText);

            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    offlineList = prefs.getBoolean("offlineList", false);
                    if (isConnected()){
                        homeTitleText.setText("Mis publicaciones");
                        getBooks(user);
                    } else {
                        homeTitleText.setText("Mis publicaciones (offline)");
                        if (offlineList) {
                            getOfflineBooks();
                        } else {
                            showAlert(editor);
                        }
                    }
                    pullToRefresh.setRefreshing(false);
                }
            });

            if(isConnected()) {
                homeTitleText.setText("Mis publicaciones");
                getBooks(user);
                editor.putBoolean("offlineList", false);
                editor.apply();
            } else {
                    homeTitleText.setText("Mis publicaciones (offline)");
                    offlineList = prefs.getBoolean("offlineList", false);
                    if (offlineList) {
                        getOfflineBooks();
                    } else {
                        showAlert(editor);
                    }
            }
        }

        return view;
    }

    void showAlert(SharedPreferences.Editor editor) {
        Log.d("password", String.valueOf(editor));
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Versión offline");
        alertDialog.setMessage("Parece que no hay conexión con internet. ¿Desea entrar en modo offline? Si oprime que no se cerrará la aplicación.");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sí",
                (dialog, which) -> {
                    editor.putBoolean("offlineList", true);
                    editor.apply();
                    getOfflineBooks();
                    recyclerView.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                (dialog, which) -> {
                    editor.putBoolean("offlineList", false);
                    editor.apply();
                    Objects.requireNonNull(getActivity()).finishAndRemoveTask();
                    System.exit(0);
                    dialog.dismiss();
                });
        alertDialog.show();
    }

    public void mReadJsonData(String params) {
        try {
            FileInputStream fis = context.openFileInput(params);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            String json = sb.toString();
            Gson gson = new Gson();
            Type listPublicaciones = new TypeToken<ArrayList<Publicaciones>>(){}.getType();
            List offlineProducts = gson.fromJson(json, listPublicaciones);
            productList = offlineProducts;
            PublicacionesAdapter adapter = new PublicacionesAdapter(context, productList,1);
            recyclerView.setAdapter(adapter);
            if(productList.isEmpty()) {
                recyclerView.setVisibility(View.VISIBLE);
                goToCode.setVisibility(View.GONE);
                text1_home.setVisibility(View.GONE);
                text2_home.setVisibility(View.GONE);
                home_logo.setVisibility(View.GONE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    void getOfflineBooks() {
        mReadJsonData("BooksJson"); //get offline books
    }

    void getBooks(User user) {
        mPresenter.getBooks(user);
    }

    @Override
    public void onCreatePlayerSuccessful() { }

    @Override
    public void onCreatePlayerFailure(String mensaje) {
//        Toast.makeText(context,mensaje,Toast.LENGTH_LONG).show();
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
    public void onGetBook(List<Publicaciones> publicaciones) {
        PublicacionesAdapter adapter = new PublicacionesAdapter(context, publicaciones,1);
        recyclerView.setAdapter(adapter);

        if(!publicaciones.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            goToCode.setVisibility(View.GONE);
            text1_home.setVisibility(View.GONE);
            text2_home.setVisibility(View.GONE);
            home_logo.setVisibility(View.GONE);
        } else if (publicaciones.isEmpty() && isConnected()){
            recyclerView.setVisibility(View.GONE);
            goToCode.setVisibility(View.VISIBLE);
            text1_home.setVisibility(View.VISIBLE);
            text2_home.setVisibility(View.VISIBLE);
            home_logo.setVisibility(View.VISIBLE);
            goToCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivarLibroFragment nextFrag= new ActivarLibroFragment();

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment, nextFrag, "findThisFragment")
                            .addToBackStack(null)
                            .commit();

                }
            });
        }
    }

    @Override
    public void onGetBookDetail(List<Detalle> detalles) { }

}