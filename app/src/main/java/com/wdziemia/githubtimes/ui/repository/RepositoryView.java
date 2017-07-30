package com.wdziemia.githubtimes.ui.repository;

import com.wdziemia.githubtimes.mvp.MvpView;

import java.util.List;

interface RepositoryView extends MvpView {
    void showRepositories(List<Repository> items);

    void showProgress();

    void showError(Throwable e);
}
