package com.andersen.internship.testproject.mvp.reddit;

import com.andersen.internship.testproject.data.Child;
import com.andersen.internship.testproject.data.Post;

import java.util.List;

public interface View {
    void showContent(List<Child> list);
    void showProgress();
    void hideProgress();
    void showError(String cause);

    void setProgress(int value);
}
