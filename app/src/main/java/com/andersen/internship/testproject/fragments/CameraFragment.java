package com.andersen.internship.testproject.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andersen.internship.testproject.R;
import com.andersen.internship.testproject.SharedPreferencesManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CameraFragment extends AbstractFragment {

    @BindView(R.id.editText)
    EditText editText;

    @BindView(R.id.save)
    Button button;

    public CameraFragment() {
        setIdTitle(R.string.camera);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_camera, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button.setOnClickListener(v ->{
            String text = editText.getText().toString();
            SharedPreferencesManager.getInstance(getActivity()).writeText(text);
            Toast.makeText(getContext(), "Text saved", Toast.LENGTH_SHORT).show();
        });
    }
}