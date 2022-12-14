package com.example.mislibros.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.fragment.app.Fragment;

import com.example.mislibros.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DescargarContenidoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DescargarContenidoFragment extends Fragment {
    private Context mCtx;

    @BindView(R.id.downloadSwitch)
    Switch downloadSwitch;

    Boolean permitted;
//    @BindView(R.id.downloadSwitch) Switch downloadSwitch;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DescargarContenidoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DescargarContenidoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DescargarContenidoFragment newInstance(String param1, String param2) {
        DescargarContenidoFragment fragment = new DescargarContenidoFragment();
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

        View view = inflater.inflate(R.layout.fragment_descargar_contenido, container, false);
        mCtx = inflater.getContext();
        ButterKnife.bind(this,view);

        SharedPreferences preferences = mCtx.getSharedPreferences("Configs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        permitted = preferences.getBoolean("downloadPermited", false);

        downloadSwitch.setChecked(permitted);

        downloadSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("downloadPermited", isChecked);
                editor.apply();
            }
        });

        return view;
    }

}