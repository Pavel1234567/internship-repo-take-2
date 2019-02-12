package com.andersen.internship.testproject.fragments;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andersen.internship.testproject.R;
import com.andersen.internship.testproject.SharedPreferencesManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryFragment extends AbstractFragment {


    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.load)
    Button button;

    public GalleryFragment() {
        setIdTitle(R.string.gallery);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_gallery, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button.setOnClickListener(v ->{

            String s = SharedPreferencesManager.getInstance(getActivity()).readText();
            textView.setText(s);
            Toast.makeText(getContext(), "Text loaded", Toast.LENGTH_SHORT).show();
        });
    }
}