package com.wdziemia.githubtimes.ui.organization;

import com.wdziemia.githubtimes.mvp.MvpView;

import java.util.List;

interface OrganizationView extends MvpView {
    void showOrganizations(List<Organization> items);

    void showProgress();

    void showError(Throwable e);
}
