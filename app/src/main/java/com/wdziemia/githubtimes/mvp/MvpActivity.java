package com.wdziemia.githubtimes.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Activity with support for an MVP architecture. This classes uses a simple static cache to
 * retain data via configuration changes ({@link PresenterCache})
 */
public abstract class MvpActivity<T extends MvpPresenter> extends AppCompatActivity implements MvpView {

    private static final String PRESENTER_KEY = "PRESENTER_KEY";

    private String presenterId;
    T presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            presenterId = savedInstanceState.getString(PRESENTER_KEY);
            presenter = (T) PresenterCache.getInstance().getPresenterById(presenterId);
        }

        if (presenter == null) {
            presenter = createPresenter();
            presenterId = PresenterCache.getInstance().cachePresenter(presenter);
        }

    }

    protected abstract T createPresenter();

    public T getPresenter() {
        return presenter;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(PRESENTER_KEY, presenterId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            presenter.destroy();
            PresenterCache.getInstance().removePresenterWithId(presenterId);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attach(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.detach();
    }
}
