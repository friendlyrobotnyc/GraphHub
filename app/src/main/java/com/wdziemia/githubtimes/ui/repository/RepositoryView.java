package com.wdziemia.githubtimes.ui.repository;

import com.wdziemia.githubtimes.R;
import com.wdziemia.githubtimes.RepoQuery;
import com.wdziemia.githubtimes.mvp.MvpView;

import java.util.List;

interface RepositoryView extends MvpView {
    void showRepositories(RepoQuery.Repositories items);

    void showProgress();

    void showError(Throwable e);
}
