package com.example.erreparseparatas.views;
import androidx.activity.OnBackPressedCallback;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;



import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erreparseparatas.R;
import com.example.erreparseparatas.interfaces.MainContract;
import com.example.erreparseparatas.model.Detalle;
import com.example.erreparseparatas.model.Publicaciones;
import com.example.erreparseparatas.model.ResponseUSER;
import com.example.erreparseparatas.model.User;
import com.example.erreparseparatas.presenter.MainPresenter;
import com.example.erreparseparatas.utils.DetallesAdapter;
import com.example.erreparseparatas.utils.PublicacionesAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.example.erreparseparatas.MainActivity.MY_PREFS_NAME;

public class DetalleFragment extends Fragment implements  MainContract.View{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "linkImg";
    private static final String ARG_PARAM2 = "publicacionid";
    @BindView(R.id.imageView) ImageView mImage;
    RecyclerView recyclerView;
    public MainPresenter mPresenter;
    public Context context;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2 = "";
    private String mParam3;
    private String mParam4;
    private String mParam5;
    private String mParam6;

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

        Picasso.with(inflater.getContext())
                .load(mParam1)
                .resize(850, 1000)
                .into(mImage);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("token", "");
        if (restoredText != "") {
            if (mParam2 != null){
            User user = new User();
            user.setIdUser(Integer.valueOf(mParam2));
            user.setToken(restoredText);

            if(isConnected())
            {
                mPresenter.getBooksDetails(user);
            }
            }
        }
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

    }

    @Override
    public void onGetBookDetail(List<Detalle> detalles) {
        DetallesAdapter adapter = new DetallesAdapter(context, detalles,1);
        recyclerView.setAdapter(adapter);
    }
}