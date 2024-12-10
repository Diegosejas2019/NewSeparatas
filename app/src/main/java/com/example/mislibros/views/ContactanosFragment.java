package com.example.mislibros.views;

import static android.content.Context.MODE_PRIVATE;
import static com.example.mislibros.MainActivity.MY_PREFS_NAME;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mislibros.R;
import com.example.mislibros.interfaces.MainContract;
import com.example.mislibros.model.Detalle;
import com.example.mislibros.model.LogEvento;
import com.example.mislibros.model.Publicaciones;
import com.example.mislibros.model.ResponseUSER;
import com.example.mislibros.presenter.MainPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactanosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactanosFragment extends Fragment implements  MainContract.View{

    @BindView(R.id.imageView3)
    ImageView fbImage;
    @BindView(R.id.imageView4) ImageView twImage;
    @BindView(R.id.imageView5) ImageView lkImage;
    @BindView(R.id.imageView6) ImageView igImage;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Integer midUser = 0;
    public MainPresenter mPresenter;
    public ContactanosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactanosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactanosFragment newInstance(String param1, String param2) {
        ContactanosFragment fragment = new ContactanosFragment();
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

        View view = inflater.inflate(R.layout.fragment_contactanos, container, false);
        ButterKnife.bind(this,view);
        Context context = inflater.getContext();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        Integer UserId = prefs.getInt("iduser", 0);
        if (UserId != 0) {
            midUser = UserId;
        }
        fbImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFacebookIntent(getContext());


            }
        });

        twImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openTwiterIntent(getContext());

            }
        });

        igImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openInstagramIntent(getContext());

            }
        });

        lkImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openLinkedinIntent(getContext());

            }
        });


        return view;
    }

    public void openFacebookIntent(Context context) {

        Intent intent;

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/Errepar"));
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Errepar"));
        }

        context.startActivity(intent);
    }

    public static void openTwiterIntent(Context context) {

        Intent intent;

        try {
            context.getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=errepar"));
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/errepar"));
        }

        context.startActivity(intent);
    }

    public void openInstagramIntent(Context context) {

        Uri uri = Uri.parse("http://instagram.com/_u/errepar_editorial");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        intent.setPackage("com.instagram.android");

        try {
            //if (isAppInstalled("com.instagram.android"))
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/errepar_editorial")));
        }
    }

    public void openLinkedinIntent(Context context) {

        Uri uri = Uri.parse("https://www.linkedin.com/company/errepar");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        intent.setPackage("com.linkedin.android");

        try {
            //if (isAppInstalled("com.instagram.android"))
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.linkedin.com/company/errepar")));
        }
    }

    @Override
    public void onCreatePlayerSuccessful() {

    }

    @Override
    public void onCreatePlayerFailure(String mensaje) {

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

    }
}