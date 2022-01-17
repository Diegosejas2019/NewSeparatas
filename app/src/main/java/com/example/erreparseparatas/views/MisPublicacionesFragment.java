package com.example.erreparseparatas.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.erreparseparatas.utils.PublicacionesAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.example.erreparseparatas.MainActivity.MY_PREFS_NAME;

public class MisPublicacionesFragment extends Fragment implements  MainContract.View{

    @BindView(R.id.empty_view)
    TextView emptyView;
    @BindView(R.id.goToCode)
    Button goToCode;

    private List<Publicaciones> publicacionesList = new ArrayList<>();

    //the recyclerview
    RecyclerView recyclerView;
    public MainPresenter mPresenter;
    public Context context;
    private String mParam1 = "";
    private String mParam2 = "";
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
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_mis_publicaciones, container, false);
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

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("token", "");
        if (restoredText != "") {

            Integer mIdUser = prefs.getInt("iduser", 0);
            User user = new User();
            user.setIdUser(mIdUser);
            user.setToken(restoredText);

            if(isConnected())
            {
                mPresenter.getBooks(user);

            }
        }


        /*//initializing the productlist
        publicacionesList = new ArrayList<>();


        //adding some items to our list
        publicacionesList.add(
                new Publicaciones(
                        1,
                        "Publicacion 1","Descripcion 1","https://tiendaonline.errepar.com/3672-home_default/resoluciones-tecnicas-comentadas.jpg","https://www.errepar.com/resources/solicitudpaginas/IN%20I%20y%20II%20-%20999%20-%20Act.%20662%20OK.pdf","a","b","v"));

        publicacionesList.add(
                new Publicaciones(
                        1,
                        "Publicacion 2","Descripcion 2","https://tiendaonline.errepar.com/3600-home_default/codigo-procesal-civil-y-comercial-de-la-nacioncomenty-anot2.jpg","","a","b","v"));

        publicacionesList.add(
                new Publicaciones(
                        1,
                        "Publicacion 3","Descripcion 3","https://tiendaonline.errepar.com/2871-home_default/balances-guia-practica-para-su-presentacion.jpg","","a","b","v"));

        publicacionesList.add(
                new Publicaciones(
                        1,
                        "Publicacion 4","Descripcion 4","https://tiendaonline.errepar.com/3112-home_default/regimen-de-infortunios-laborales.jpg","","a","b","v"));

        publicacionesList.add(
                new Publicaciones(
                        1,
                        "Publicacion 5","Descripcion 5","https://tiendaonline.errepar.com/3635-home_default/impuesto-a-las-ganancias-impuestos-explicados-y-comentados.jpg","","a","b","v"));

        publicacionesList.add(
                new Publicaciones(
                        1,
                        "Publicacion 6","Descripcion 6","https://tiendaonline.errepar.com/3655-home_default/jubilaciones-y-pensiones.jpg","","a","b","v"));

        publicacionesList.add(
                new Publicaciones(
                        1,
                        "Publicacion 7","Descripcion 7","https://tiendaonline.errepar.com/3642-home_default/preventa-monotributo-2021.jpg","","a","b","v"));

        publicacionesList.add(
                new Publicaciones(
                        1,
                        "Publicacion 8","Descripcion 8","https://tiendaonline.errepar.com/3692-home_default/desarrollo-de-organizaciones-sostenibles-en-contextos-turbulentos.jpg","","a","b","v"));

        publicacionesList.add(
                new Publicaciones(
                        1,
                        "Publicacion 9","Descripcion 9","https://tiendaonline.errepar.com/3690-home_default/el-impuesto-sobre-los-ingresos-brutos-en-la-provincia-de-santa-fe.jpg","","a","b","v"));


        //creating recyclerview adapter
        PublicacionesAdapter adapter = new PublicacionesAdapter(inflater.getContext(), publicacionesList,1);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);*/
        return view;
    }

    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    }

    @Override
    public void onProcessEnd() {

    }

    @Override
    public void onUserRead(ResponseUSER user) {

    }

    @Override
    public void onUserCreate(ResponseUSER user) {

    }

    @Override
    public void onGetBook(List<Publicaciones> publicaciones) {
        PublicacionesAdapter adapter = new PublicacionesAdapter(context, publicaciones,1);
        recyclerView.setAdapter(adapter);

        if(!publicaciones.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            goToCode.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            goToCode.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.VISIBLE);
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
    public void onGetBookDetail(List<Detalle> detalles) {

    }
}