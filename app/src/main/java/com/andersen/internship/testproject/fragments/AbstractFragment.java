package com.andersen.internship.testproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import butterknife.Unbinder;

public abstract class AbstractFragment extends Fragment {

    Unbinder unbinder;
    private int idTitle;


    public void setIdTitle(int idTitle) {
        this.idTitle = idTitle;
    }

    void setTitleText(int res){
        getActivity().setTitle(getResources().getString(res));
    }

    public AbstractFragment() {
        setIdTitle(0);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitleText(idTitle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null){
            unbinder.unbind();
        }
    }

}
