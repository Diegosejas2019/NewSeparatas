package com.example.mislibros.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mislibros.LoginActivity;
import com.example.mislibros.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SuccesChangePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuccesChangePasswordFragment extends Fragment {

    @BindView(R.id.btnRegister)
    Button mContinuar;

    public static SuccesChangePasswordFragment newInstance(String param1, String param2) {
        SuccesChangePasswordFragment fragment = new SuccesChangePasswordFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_succes_change_password, container, false);

        ButterKnife.bind(this,view);


        mContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(inflater.getContext(), LoginActivity.class));

            }
        });

        return view;
    }
}